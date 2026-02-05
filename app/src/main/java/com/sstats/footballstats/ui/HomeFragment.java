package com.sstats.footballstats.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sstats.footballstats.MatchDetailsActivity;
import com.sstats.footballstats.R;
import com.sstats.footballstats.api.ApiClient;
import com.sstats.footballstats.api.SstatsApi;
import com.sstats.footballstats.model.ApiResponse;
import com.sstats.footballstats.model.Match;
import com.sstats.footballstats.util.DateUtils;
import com.sstats.footballstats.util.TimeZoneUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private MatchAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView emptyView;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_matches);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MatchAdapter(match -> {
            Intent intent = new Intent(requireContext(), MatchDetailsActivity.class);
            intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_ID, match.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        emptyView = view.findViewById(R.id.text_empty);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this::loadMatches);

        swipeRefresh.setRefreshing(true);
        loadMatches();
    }

    private void loadMatches() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, 2);

        String from = DateUtils.formatForApi(now);
        String to = DateUtils.formatForApi(calendar.getTime());

        SstatsApi api = ApiClient.getClient().create(SstatsApi.class);
        Call<ApiResponse<List<Match>>> call = api.getMatches(
                null,
                from,
                to,
                null,
                true,
                null,
                1,
                1000,
                TimeZoneUtils.getTimeZoneOffsetHours()
        );

        call.enqueue(new Callback<ApiResponse<List<Match>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Match>>> call,
                                   @NonNull Response<ApiResponse<List<Match>>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Match> matches = response.body().data;
                    adapter.setItems(matches);
                    updateEmpty(matches == null || matches.isEmpty());
                } else {
                    updateEmpty(true);
                    Toast.makeText(requireContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Match>>> call, @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                updateEmpty(true);
                Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmpty(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}

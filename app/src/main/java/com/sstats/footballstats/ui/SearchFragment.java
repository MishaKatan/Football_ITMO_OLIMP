package com.sstats.footballstats.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sstats.footballstats.MatchDetailsActivity;
import com.sstats.footballstats.R;
import com.sstats.footballstats.api.ApiClient;
import com.sstats.footballstats.api.SstatsApi;
import com.sstats.footballstats.model.ApiResponse;
import com.sstats.footballstats.model.League;
import com.sstats.footballstats.model.Match;
import com.sstats.footballstats.util.DateUtils;
import com.sstats.footballstats.util.TimeZoneUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private Spinner leagueSpinner;
    private RadioGroup rangeGroup;
    private MatchAdapter adapter;
    private ProgressBar progress;
    private TextView emptyView;

    private final List<League> leagues = new ArrayList<>();

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        leagueSpinner = view.findViewById(R.id.spinner_league);
        rangeGroup = view.findViewById(R.id.radio_range);
        progress = view.findViewById(R.id.progress);
        emptyView = view.findViewById(R.id.text_empty);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_matches);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MatchAdapter(match -> {
            Intent intent = new Intent(requireContext(), MatchDetailsActivity.class);
            intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_ID, match.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        Button showButton = view.findViewById(R.id.button_show);
        Button archiveButton = view.findViewById(R.id.button_archive);

        showButton.setOnClickListener(v -> loadUpcoming());
        archiveButton.setOnClickListener(v -> loadArchive());

        loadLeagues();
    }

    private void loadLeagues() {
        progress.setVisibility(View.VISIBLE);
        SstatsApi api = ApiClient.getClient().create(SstatsApi.class);
        api.getLeagues(null).enqueue(new Callback<ApiResponse<List<League>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<League>>> call,
                                   @NonNull Response<ApiResponse<List<League>>> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    leagues.clear();
                    leagues.addAll(response.body().data);
                    Collections.sort(leagues, Comparator.comparing(l -> l.name != null ? l.name : ""));
                    List<String> items = new ArrayList<>();
                    items.add(getString(R.string.select_league));
                    for (League league : leagues) {
                        String country = league.country != null ? league.country.name : "";
                        if (!country.isEmpty()) {
                            items.add(league.name + " (" + country + ")");
                        } else {
                            items.add(league.name);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(), android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    leagueSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<League>>> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUpcoming() {
        League league = getSelectedLeague();
        if (league == null) {
            Toast.makeText(requireContext(), R.string.error_select_league, Toast.LENGTH_SHORT).show();
            return;
        }
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, getRangeDays());

        String from = DateUtils.formatForApi(now);
        String to = DateUtils.formatForApi(calendar.getTime());

        requestMatches(league.id, from, to, true, false);
    }

    private void loadArchive() {
        League league = getSelectedLeague();
        if (league == null) {
            Toast.makeText(requireContext(), R.string.error_select_league, Toast.LENGTH_SHORT).show();
            return;
        }
        requestMatches(league.id, null, null, false, true);
    }

    private void requestMatches(String leagueId, String from, String to, boolean upcoming, boolean ended) {
        progress.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);

        SstatsApi api = ApiClient.getClient().create(SstatsApi.class);
        Call<ApiResponse<List<Match>>> call = api.getMatches(
                leagueId,
                from,
                to,
                null,
                upcoming ? true : null,
                ended ? true : null,
                upcoming ? 1 : -1,
                ended ? 200 : 1000,
                TimeZoneUtils.getTimeZoneOffsetHours()
        );

        call.enqueue(new Callback<ApiResponse<List<Match>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Match>>> call,
                                   @NonNull Response<ApiResponse<List<Match>>> response) {
                progress.setVisibility(View.GONE);
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
                progress.setVisibility(View.GONE);
                updateEmpty(true);
                Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRangeDays() {
        int checked = rangeGroup.getCheckedRadioButtonId();
        if (checked == R.id.radio_week) {
            return 7;
        }
        if (checked == R.id.radio_month) {
            return 30;
        }
        return 1;
    }

    private League getSelectedLeague() {
        int index = leagueSpinner.getSelectedItemPosition();
        if (index <= 0 || index > leagues.size()) {
            return null;
        }
        return leagues.get(index - 1);
    }

    private void updateEmpty(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}

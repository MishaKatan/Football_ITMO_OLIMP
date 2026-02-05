package com.sstats.footballstats;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.sstats.footballstats.api.ApiClient;
import com.sstats.footballstats.api.SstatsApi;
import com.sstats.footballstats.model.ApiResponse;
import com.sstats.footballstats.model.GameDetails;
import com.sstats.footballstats.model.GameDetailsResponse;
import com.sstats.footballstats.util.DateUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MATCH_ID = "extra_match_id";

    private TextView textLeague;
    private TextView textTitle;
    private TextView textDate;
    private TextView textCountry;
    private TextView textStatus;
    private TextView textMatchId;
    private TextView textApi1;
    private TextView textApi2;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.details_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        textLeague = findViewById(R.id.text_league);
        textTitle = findViewById(R.id.text_title);
        textDate = findViewById(R.id.text_date);
        textCountry = findViewById(R.id.text_country);
        textStatus = findViewById(R.id.text_status);
        textMatchId = findViewById(R.id.text_match_id);
        textApi1 = findViewById(R.id.text_api_1);
        textApi2 = findViewById(R.id.text_api_2);
        textError = findViewById(R.id.text_error);

        String matchId = getIntent().getStringExtra(EXTRA_MATCH_ID);
        if (matchId == null || matchId.isEmpty()) {
            textError.setText(R.string.error_missing_match_id);
            textError.setVisibility(android.view.View.VISIBLE);
            return;
        }

        loadDetails(matchId);
    }

    private void loadDetails(String matchId) {
        SstatsApi api = ApiClient.getClient().create(SstatsApi.class);
        api.getGameDetails(matchId).enqueue(new Callback<ApiResponse<GameDetailsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<GameDetailsResponse>> call,
                                   @NonNull Response<ApiResponse<GameDetailsResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    GameDetailsResponse data = response.body().data;
                    bindGame(data.game, matchId);
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<GameDetailsResponse>> call, @NonNull Throwable t) {
                showError();
            }
        });
    }

    private void bindGame(GameDetails game, String fallbackId) {
        if (game == null) {
            showError();
            return;
        }
        String leagueName = game.season != null && game.season.league != null
                ? game.season.league.name : "";
        textLeague.setText(leagueName);

        String home = game.homeTeam != null ? game.homeTeam.name : "";
        String away = game.awayTeam != null ? game.awayTeam.name : "";
        textTitle.setText(home + " vs " + away);

        textDate.setText(DateUtils.formatDateTime(game.date));

        String country = game.season != null && game.season.league != null && game.season.league.country != null
                ? game.season.league.country.name : "";
        textCountry.setText(country);

        String status = game.statusName != null ? game.statusName : String.valueOf(game.status);
        textStatus.setText(status);

        String idText = game.id != null ? String.valueOf(game.id) : fallbackId;
        textMatchId.setText(idText);

        String apiId = game.id != null ? String.valueOf(game.id) : fallbackId;
        textApi1.setText("https://api.sstats.net/games/" + apiId);
        textApi2.setText("https://api.sstats.net/games/glicko/" + apiId);
    }

    private void showError() {
        textError.setText(R.string.error_loading);
        textError.setVisibility(android.view.View.VISIBLE);
        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show();
    }
}

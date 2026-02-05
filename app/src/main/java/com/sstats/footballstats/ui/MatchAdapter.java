package com.sstats.footballstats.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sstats.footballstats.R;
import com.sstats.footballstats.model.League;
import com.sstats.footballstats.model.Match;
import com.sstats.footballstats.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    public interface OnMatchClickListener {
        void onMatchClick(Match match);
    }

    private final List<Match> items = new ArrayList<>();
    private final OnMatchClickListener listener;

    public MatchAdapter(OnMatchClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Match> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = items.get(position);
        holder.bind(match, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        private final TextView teams;
        private final TextView dateTime;
        private final TextView leagueCountry;

        MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            teams = itemView.findViewById(R.id.text_teams);
            dateTime = itemView.findViewById(R.id.text_date_time);
            leagueCountry = itemView.findViewById(R.id.text_league_country);
        }

        void bind(final Match match, final OnMatchClickListener listener) {
            String home = match.homeTeam != null ? match.homeTeam.name : "";
            String away = match.awayTeam != null ? match.awayTeam.name : "";
            teams.setText(home + " vs " + away);
            dateTime.setText(DateUtils.formatDateTime(match.date));

            League league = match.season != null ? match.season.league : null;
            String leagueName = league != null ? league.name : "";
            String countryName = league != null && league.country != null ? league.country.name : "";
            if (!countryName.isEmpty()) {
                leagueCountry.setText(leagueName + " • " + countryName);
            } else {
                leagueCountry.setText(leagueName);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMatchClick(match);
                }
            });
        }
    }
}

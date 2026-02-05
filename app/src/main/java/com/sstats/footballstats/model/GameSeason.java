package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class GameSeason {
    @SerializedName("uid")
    public String uid;

    @SerializedName("year")
    public Integer year;

    @SerializedName("league")
    public GameLeague league;
}

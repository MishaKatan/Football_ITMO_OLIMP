package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class Match {
    @SerializedName("id")
    public String id;

    @SerializedName("date")
    public String date;

    @SerializedName("status")
    public Integer status;

    @SerializedName("season")
    public Season season;

    @SerializedName("homeTeam")
    public Team homeTeam;

    @SerializedName("awayTeam")
    public Team awayTeam;

    @SerializedName("homeResult")
    public Integer homeResult;

    @SerializedName("awayResult")
    public Integer awayResult;
}

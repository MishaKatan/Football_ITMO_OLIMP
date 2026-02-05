package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class GameDetails {
    @SerializedName("id")
    public Integer id;

    @SerializedName("flashId")
    public String flashId;

    @SerializedName("date")
    public String date;

    @SerializedName("status")
    public Integer status;

    @SerializedName("statusName")
    public String statusName;

    @SerializedName("homeTeam")
    public GameTeam homeTeam;

    @SerializedName("awayTeam")
    public GameTeam awayTeam;

    @SerializedName("season")
    public GameSeason season;
}

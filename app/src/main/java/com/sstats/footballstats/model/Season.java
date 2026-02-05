package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class Season {
    @SerializedName("uid")
    public String uid;

    @SerializedName("id")
    public String id;

    @SerializedName("years")
    public String years;

    @SerializedName("league")
    public League league;
}

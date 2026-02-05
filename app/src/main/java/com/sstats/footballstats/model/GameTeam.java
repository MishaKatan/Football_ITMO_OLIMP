package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class GameTeam {
    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("flashId")
    public String flashId;
}

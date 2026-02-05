package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class GameLeague {
    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("country")
    public GameCountry country;

    @SerializedName("flashScoreId")
    public String flashScoreId;
}

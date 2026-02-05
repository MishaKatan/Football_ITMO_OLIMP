package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class League {
    @SerializedName("uid")
    public String uid;

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("country")
    public Country country;
}

package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("abbr2")
    public String abbr2;

    @SerializedName("abbr3")
    public String abbr3;
}

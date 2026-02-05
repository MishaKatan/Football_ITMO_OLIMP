package com.sstats.footballstats.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("status")
    public String status;

    @SerializedName("count")
    public Integer count;

    @SerializedName("data")
    public T data;

    @SerializedName("message")
    public String message;

    @SerializedName("offset")
    public Integer offset;

    @SerializedName("TotalCount")
    public Integer totalCount;

    @SerializedName("traceId")
    public String traceId;
}

package com.sstats.footballstats.api;

import com.sstats.footballstats.model.ApiResponse;
import com.sstats.footballstats.model.GameDetailsResponse;
import com.sstats.footballstats.model.League;
import com.sstats.footballstats.model.Match;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SstatsApi {
    @GET("ls/list")
    Call<ApiResponse<List<Match>>> getMatches(
            @Query("LeagueId") String leagueId,
            @Query("From") String from,
            @Query("To") String to,
            @Query("Date") String date,
            @Query("Upcoming") Boolean upcoming,
            @Query("Ended") Boolean ended,
            @Query("Order") Integer order,
            @Query("Limit") Integer limit,
            @Query("TimeZone") Integer timeZone
    );

    @GET("ls/leagues")
    Call<ApiResponse<List<League>>> getLeagues(
            @Query("name") String name
    );

    @GET("games/{id}")
    Call<ApiResponse<GameDetailsResponse>> getGameDetails(
            @Path("id") String id
    );
}

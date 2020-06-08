package com.example.cermati;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceApi {

    @GET("/search/users")
    Call <ApiResponse> getGithubUsers(@Query("q") String query, @Query("page") int pageNo);
}

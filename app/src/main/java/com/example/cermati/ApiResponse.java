package com.example.cermati;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("items")
    List<GithubUsers> githubUsers;

    public List<GithubUsers> getGithubUsers() {
        return githubUsers;
    }
}

class GithubUsers {
    @SerializedName("login")
    private String loginId;

    @SerializedName("avatar_url")
    private String avatar;

    public String getLoginId() {
        return loginId;
    }

    public String getAvatar() {
        return avatar;
    }
}

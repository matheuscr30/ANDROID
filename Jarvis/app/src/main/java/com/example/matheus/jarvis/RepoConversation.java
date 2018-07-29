package com.example.matheus.jarvis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by matheus on 17/10/17.
 */

public class RepoConversation{
    @SerializedName("response")
    private String response;

    @SerializedName("language")
    private String language;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

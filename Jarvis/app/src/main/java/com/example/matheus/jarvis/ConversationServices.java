package com.example.matheus.jarvis;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by matheus on 17/10/17.
 */

public interface ConversationServices {
    @FormUrlEncoded
    @POST("/api/conversation")
    Call<RepoConversation> postMessage(@Field("response") String response);
}

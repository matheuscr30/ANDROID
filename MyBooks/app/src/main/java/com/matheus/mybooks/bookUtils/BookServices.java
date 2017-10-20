package com.matheus.mybooks.bookUtils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by matheus on 19/10/17.
 */

public interface BookServices {
    @GET("volumes")
    Call<RepoBook> listBook(@Query("q") String isbn);
}

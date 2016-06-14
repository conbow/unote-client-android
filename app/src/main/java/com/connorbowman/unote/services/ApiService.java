package com.connorbowman.unote.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.connorbowman.unote.BuildConfig;
import com.connorbowman.unote.activities.LoginActivity;
import com.connorbowman.unote.models.Account;
import com.connorbowman.unote.models.Note;
import com.connorbowman.unote.models.Session;
import com.connorbowman.unote.utils.SessionUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ApiService {

    public static final double VERSION = 1.0;
    public static final String URL = "https://unoteservernodejs-connorbowman.rhcloud.com/";
    //public static final String URL_BASE = URL + VERSION + "/";
    public static final String URL_BASE = URL;

    private static Api sApi;

    public ApiService(final Context context) {
        // Create new HTTP Client builder
        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();

        // Intercept all requests to add bearer token
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {

                /**
                 * Request
                 * */
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();

                // Add session token
                String sessionToken = SessionUtil.read(context).getToken();
                if (sessionToken != null) {
                    requestBuilder.header("Authorization", "Bearer " + sessionToken);
                }

                /**
                 * Response
                 */
                Response response = chain.proceed(requestBuilder.build());

                // Clear session token if we ever get a 401 error code
                // FIXME This could probably be handled better
                if (response.code() == 401) {
                    SessionUtil.destroy(context);
                }

                // Add 5 second delay in all network calls for debugging
                if (SessionUtil.isDebugging(context)) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {

                    }
                }

                // Print raw json to console for debugging or just return response
                if (BuildConfig.DEBUG) {
                    String rawJson = response.body().string();
                    Log.d(BuildConfig.APPLICATION_ID, String.format("Network response: %s", rawJson));
                    return response.newBuilder().body(ResponseBody.create(response.body().contentType(), rawJson)).build();
                } else {
                    return response;
                }
            }
        });

        // Create API service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
        sApi = retrofit.create(Api.class);
    }

    public static Api getInstance() {
        return sApi;
    }

    public interface Api {

        // DEBUG
        @GET("{url}")
        Call<Note> debug(@Path("url") String url);

        // Auth
        @FormUrlEncoded
        @POST("sessions")
        Call<Session> sessionPost(@Field("email") String email, @Field("password") String password);

        @FormUrlEncoded
        @POST("auth/forgot")
        Call<Account> accountForgotPost(@Field("email") String email);

        @FormUrlEncoded
        @POST("auth/verify")
        Call<Account> authVerifyPost(@Field("code") String code);

        // Account
        @POST("accounts")
        Call<Account> accountPost(@Body Account account);

        @GET("{id}") //accounts/{id}
        Call<Account> accountGet(@Path("id") String id);

        @PUT("accounts/{id}")
        Call<Account> accountPut(@Path("id") String id);

        @DELETE("accounts/{id}")
        Call<Account> accountDelete(@Path("id") String id);

        // Notes
        @GET("notes")
        Call<List<Note>> notesGet();

        // Note
        @POST("notes")
        Call<Note> notePost(@Body Note note);

        @GET("notes/{id}")
        Call<Note> noteGet(@Path("id") String id);

        @PUT("notes/{id}")
        Call<Note> notePut(@Path("id") String id, @Body Note note);

        @DELETE("notes/{id}")
        Call<Note> noteDelete(@Path("id") String id);
    }
}

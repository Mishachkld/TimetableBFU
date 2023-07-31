package com.example.timetablebfu.GoogleSheetAPI.retrofit;


import static com.example.timetablebfu.GoogleSheetAPI.APIConfig.SPREADSHEET_ID;

import com.example.timetablebfu.GoogleSheetAPI.APIConfig;
import com.example.timetablebfu.GoogleSheetAPI.KeyAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("v4/spreadsheets/" + SPREADSHEET_ID + "/values/" + APIConfig.SPREADSHEET_RANGE + "key=" + KeyAPI.GOOGLE_API_KEY)
    Call<DataResponse> getData();


    @GET("v4/spreadsheets/" + SPREADSHEET_ID + "/values/" + "{list}" + "?" + "{dimension}" + "&key=" + KeyAPI.GOOGLE_API_KEY)
    Call<DataResponse> getData(@Path(value = "list") String list, @Path("dimension") String dimension);

    @PUT("v4/spreadsheets/" + SPREADSHEET_ID + "/values/" + APIConfig.SPREADSHEET_RANGE + "?valueInputOption=" + "{data}" + "&key=" + KeyAPI.GOOGLE_API_KEY)
    void sendData(@Path("data") String[] data);
}

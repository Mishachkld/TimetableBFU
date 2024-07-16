package com.example.timetablebfu.GoogleSheetAPI.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponse {
    @SerializedName("range")
    public String range;
    @SerializedName("majorDimension")
    public String majorDimension;
    @SerializedName("values")
    public List<List<String>> values;
}

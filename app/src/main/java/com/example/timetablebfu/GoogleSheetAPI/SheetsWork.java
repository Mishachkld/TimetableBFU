package com.example.timetablebfu.GoogleSheetAPI;

import static com.example.timetablebfu.Constants.Constants.PREF_ACCOUNT_NAME;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.tasks.TasksScopes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SheetsWork {

    private static final String APPLICATION_NAME = "BFU Timetable";

    public static List<String> days;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private Sheets sheetService;
    private String range;
    private String spreadsheetId;
    private static InputStream in;
    private List<List<Object>> values;

    private ArrayList<ScheduleList> res = new ArrayList<ScheduleList>();
    private APIService service;
    private Call<DataResponse> call;
    private Retrofit retrofit;

    private List<String> date = new ArrayList<>();
    private List<String> homework = new ArrayList<>();
    private List<String> lessons = new ArrayList<>();

    public SheetsWork(String range, String spreadsheetId, InputStream in) {
        this.range = range;
        this.spreadsheetId = spreadsheetId;
        SheetsWork.in = in;

    }

    public SheetsWork(Context context) throws GeneralSecurityException, IOException {
        sheetService = getSheetsService(authorize(context));
    }

    private void defaultSettings() {
        retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(APIService.class);
        date = new ArrayList<>();
        homework = new ArrayList<>();
        lessons = new ArrayList<>();
    }

    private static GoogleAccountCredential authorize(Context context)  {
     GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(context, Collections.singleton(TasksScopes.TASKS));
        SharedPreferences settings = credential.getContext().getSharedPreferences(null, Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        return credential;
    }

    private Sheets getSheetsService(GoogleAccountCredential credential) throws GeneralSecurityException, IOException {
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private void workWithSheets(GoogleAccountCredential credential) throws GeneralSecurityException, IOException {
        sheetService = getSheetsService(credential);
        ValueRange response = sheetService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        values = response.getValues();

    }

    public List<String> getDate() {
        return date;
    }

    public List<String> getHomework() {
        return homework;
    }

    public List<String> getLessons() {
        return lessons;
    }

    public List<List<Object>> getValues() throws IOException {
        if (values == null || values.isEmpty()) {
            throw new IOException("Not found Data");
        }
        return values;
    }
}

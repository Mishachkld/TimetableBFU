package com.example.timetablebfu.GoogleSheetAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.renderscript.ScriptGroup;
import android.view.View;

import com.example.timetablebfu.Components.ScheduleList;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.APIService;
import com.example.timetablebfu.GoogleSheetAPI.retrofit.DataResponse;
import com.example.timetablebfu.MainActivity;
import com.example.timetablebfu.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public SheetsWork(String range, String spreadsheetId, InputStream in) throws GeneralSecurityException, IOException {
        this.range = range;
        this.spreadsheetId = spreadsheetId;
        SheetsWork.in = in;

    }

    public SheetsWork() {
        defaultSettings();
    }

    private void defaultSettings() {
        retrofit = new Retrofit.Builder().baseUrl(APIConfig.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(APIService.class);
        date = new ArrayList<>();
        homework = new ArrayList<>();
        lessons = new ArrayList<>();
    }

    public ArrayList<ScheduleList> getDataJson(View view) {
        ArrayList<ScheduleList> result = new ArrayList<>();
        call = service.getData();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().values.size(); i++) {
                        int counter = 0;
                        String dataLessons = "";
                        for (int j = 0; j < response.body().values.get(i).size(); j++) {
                            String item = response.body().values.get(i).get(j);
                            switch (i) {
                                case 0:
                                    if (!item.equals(""))
                                        date.add(item);
                                    break;
                                case 1:
                                    dataLessons += item + "\n";
                                    counter++;
                                    if (counter == 5) {
                                        homework.add(dataLessons);
                                        counter = 0;
                                        dataLessons = "";
                                    }
                                    break;
                                case 2:
                                    dataLessons += item + "\n";
                                    counter++;
                                    if (counter == 5) {
                                        lessons.add(dataLessons);
                                        counter = 0;
                                        dataLessons = "";
                                    }
                                    break;
                                default:
                                    lessons.add(item);
                                    break;
                            }
                        }
                    }
                    for (int i = 0; i < lessons.size(); i++) {
                        result.add(new ScheduleList(i, date.get(i), lessons.get(i)));
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Snackbar.make(view, "Errrorrr....", Snackbar.LENGTH_LONG).show();
            }
        });
        return result;
    }

    private static void sortData() {


    }

    /*private static Credential authorize() throws IOException, GeneralSecurityException {
     *//*Credential credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(TasksScopes.TASKS));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));*//*
        ;///(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));
        List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), clientSecrets, scopes).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline").build();

        return credential;
    }*/

    private Sheets getSheetsService(Credential credential) throws GeneralSecurityException, IOException {
        Sheets service =
                new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        return service;
    }

    void workWithSheets(Credential credential) throws GeneralSecurityException, IOException {
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

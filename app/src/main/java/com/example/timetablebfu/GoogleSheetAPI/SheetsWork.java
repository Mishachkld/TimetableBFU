package com.example.timetablebfu.GoogleSheetAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.renderscript.ScriptGroup;

import com.example.timetablebfu.MainActivity;
import com.example.timetablebfu.R;
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
import java.util.Collections;
import java.util.List;

public class SheetsWork {

    private static final String APPLICATION_NAME = "BFU Timetable";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private Sheets sheetService;
    private String range;
    private String spreadsheetId;
    private static InputStream in;
    private List<List<Object>> values;

    public SheetsWork(String range, String spreadsheetId, InputStream in) throws GeneralSecurityException, IOException {
        this.range = range;
        this.spreadsheetId = spreadsheetId;
        SheetsWork.in = in;
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

    public List<List<Object>> getValues() throws IOException {
        if (values == null || values.isEmpty()) {
            throw new IOException("Not found Data");
        }
        return values;
    }
}

package com.example.timetablebfu.GoogleSheetAPI;

public class APIConfig {
    public static final String SPREADSHEET_ID = "1NSg2_LawFds5S0jphSLsFu4KZ4D8yNLB9lkr02Yr7YM";
    //public static final String SPREADSHEET_ID = "1HcOGiN85l90onQZH1WqdSThJo6Sq3d0Qsc0SUvqGET8"; послединий вариант
   // public static final String SPREADSHEET_ID = "1cApPmSiYSQ9r5B32sssWYPvcEYT8GZurpZMUFaVqhwQ";
    public static final String SPREADSHEET_RANGE = "2курс!A2:C990?majorDimension=COLUMNS&";
    //public static final String SPREADSHEET_RANGE = "Лист1!A2:C990?majorDimension=COLUMNS&"; последний вариант
    public static final String SPREADSHEET_LIST_ID = "2курс!A2:C990";
    //public static final String SPREADSHEET_LIST_ID = "Лист1!A2:C990";
    public static final String SPREADSHEET_MAJOR_DIMENSION = "majorDimension=COLUMNS";//"";
    public static final String URL = "https://sheets.googleapis.com";
}

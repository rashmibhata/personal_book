package com.example.abc;

import android.provider.BaseColumns;

public class DiaryContract {
    private DiaryContract(){

    }
    public final static class DiaryEntry implements BaseColumns{
        public static final String TABLE_NAME = "diary";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}

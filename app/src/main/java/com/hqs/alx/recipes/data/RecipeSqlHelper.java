package com.hqs.alx.recipes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alex on 04/11/2017.
 */

public class RecipeSqlHelper extends SQLiteOpenHelper {

    //the name of the database file
    public static final String DATABASE_NAME = "recipes.db";
    //database version - should be incremneted on every change to the database
    public static final int DATABSE_VERSION = 1;

    public RecipeSqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + RecipeContract.TABLE_NAME + " ("
                + RecipeContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecipeContract.COLUMN_RECIPE_NAME + " TEXT NOT NULL, "
                + RecipeContract.COLUMN_PUBLISHER_NAME + " TEXT, "
                + RecipeContract.COLUMN_IMAGE_URL + " TEXT, "
                + RecipeContract.COLUMN_RECIPE_URL + " TEXT NOT NULL, "
                + RecipeContract.COLUMN_SOCIAL_RANK + " INTEGER NOT NULL, "
                + RecipeContract.COLUMN_PUBLISHER_SITE + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

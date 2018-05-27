package com.hqs.alx.recipes.data;

/**
 * Created by Alex on 04/11/2017.
 */

public class RecipeContract {

    private RecipeContract (){
    }

    public final static String TABLE_NAME = "recipes";

    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_RECIPE_NAME = "Recipe_Name";
    public final static String COLUMN_RECIPE_URL = "Recipe_URL";
    public final static String COLUMN_PUBLISHER_NAME = "Publisher";
    public final static String COLUMN_SOCIAL_RANK = "Social_Rank";
    public final static String COLUMN_IMAGE_URL = "Image_URL";
    public final static String COLUMN_PUBLISHER_SITE = "Publisher_Site";

    public final static int IS_FAVORITE = 1;
    public final static int NOT_FAVORITE = 0;


}

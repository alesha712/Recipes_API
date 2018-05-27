package com.hqs.alx.recipes;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hqs.alx.recipes.data.RecipeContract;
import com.hqs.alx.recipes.data.RecipeSqlHelper;

import java.util.ArrayList;

import static com.hqs.alx.recipes.MainActivity.recipesFromAPI;

/**
 * Created by Alex on 01/11/2017.
 */

public class RecipeListAdapter extends ArrayAdapter<RecipeList> {

    View listItemView;
    RecipeList currentRecipeList;
    RecipeSqlHelper sqlHelper;
    SQLiteDatabase db;

    public RecipeListAdapter (Activity context, ArrayList<RecipeList> newList) {
        super(context, 0, newList);
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        // Check if the existing view is being reused, otherwise inflate the view
        listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        currentRecipeList = getItem(position);
        listItemView.setBackgroundResource(position % 2 == 0 ? R.color.favoritesWhiteItem : R.color.favoritsGreyItem);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView recipeHeader = (TextView) listItemView.findViewById(R.id.listRecipeHeaderTV);
        recipeHeader.setText(currentRecipeList.getRecipeHeader());

        TextView recipePublisherName = (TextView) listItemView.findViewById(R.id.nameFromAPITV);
        recipePublisherName.setText(currentRecipeList.getPublisherName());

        Button saveBtn = (Button) listItemView.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sqlHelper = new RecipeSqlHelper(getContext());
                String currentRecipeName = recipesFromAPI.get(position).getRecipeHeader();
                if(!CheckIfValueExistsInDataBase(currentRecipeName)){

                    ContentValues values = new ContentValues();
                    values.put(RecipeContract.COLUMN_RECIPE_NAME, recipesFromAPI.get(position).getRecipeHeader());
                    values.put(RecipeContract.COLUMN_RECIPE_URL, recipesFromAPI.get(position).getRecipeURL());
                    values.put(RecipeContract.COLUMN_PUBLISHER_NAME, recipesFromAPI.get(position).getPublisherName());
                    values.put(RecipeContract.COLUMN_SOCIAL_RANK, recipesFromAPI.get(position).getRank());
                    values.put(RecipeContract.COLUMN_IMAGE_URL, recipesFromAPI.get(position).getImageURL());
                    values.put(RecipeContract.COLUMN_PUBLISHER_SITE, recipesFromAPI.get(position).getPublisherSite());

                    long newRowId = sqlHelper.getWritableDatabase().insert(RecipeContract.TABLE_NAME, null, values);

                    if(newRowId == -1){
                        Toast.makeText(getContext(), "Error saving recipe", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Recipe Saved Successfully ", Toast.LENGTH_SHORT).show();
                    }
                }else{
                      Toast.makeText(getContext(), "Recipe Already exists in favorites!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }

    private boolean CheckIfValueExistsInDataBase(String name) {

        sqlHelper = new RecipeSqlHelper(getContext());
        db = sqlHelper.getReadableDatabase();
        String [] columns = {RecipeContract.COLUMN_RECIPE_NAME};
        String selection = RecipeContract.COLUMN_RECIPE_NAME + "=?";
        String [] selectionArgs = {name};

        Cursor cursor = db.query(RecipeContract.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int result = cursor.getCount();
        if (result > 0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

}

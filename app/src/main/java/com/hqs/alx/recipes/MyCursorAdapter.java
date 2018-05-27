package com.hqs.alx.recipes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqs.alx.recipes.data.RecipeContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Alex on 04/11/2017.
 */

public class MyCursorAdapter extends CursorAdapter {

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    //just inflate the view (convert xml to live view)
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View singleView = LayoutInflater.from(context).inflate(R.layout.favorites_list_item, null);
        singleView.setBackgroundResource(cursor.getPosition() % 2 == 0 ? R.color.favoritesWhiteItem : R.color.favoritsGreyItem);

        return singleView;
    }

    //here we bind the data from the cursor to the view
    //no need to call cursor.movetonext()- already initialized for each line
    //view- is the view we inflated earlier
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView recipeTitle = (TextView) view.findViewById(R.id.favoritesSingleRecipeHeaderTV);
        TextView publisherName = (TextView) view.findViewById(R.id.favoritesPublisherNameTV);
        TextView showRecipe = (TextView) view.findViewById(R.id.favoritesShowRecipeTV);
        TextView recipeRank = (TextView) view.findViewById(R.id.favoritesRankTV);
        ImageView recipeImage = (ImageView) view.findViewById(R.id.favoritesRecipeIV);

        recipeTitle.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_NAME)));
        publisherName.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_PUBLISHER_NAME)));
        showRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_URL)));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        recipeRank.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_SOCIAL_RANK)));
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_IMAGE_URL))).into(recipeImage);
    }
}

package com.hqs.alx.recipes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hqs.alx.recipes.data.RecipeContract;
import com.hqs.alx.recipes.data.RecipeSqlHelper;

public class FavoritesActivity extends AppCompatActivity {

    RecipeSqlHelper sqlHelper;
    Cursor recipeCursor;
    MyCursorAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        sqlHelper = new RecipeSqlHelper(FavoritesActivity.this);

        recipeCursor = sqlHelper.getReadableDatabase().query(RecipeContract.TABLE_NAME, null, null, null, null, null, null);
        adapter = new MyCursorAdapter(this, recipeCursor);

        ListView favoritesListView = (ListView) findViewById(R.id.favoritesLV);
        favoritesListView.setAdapter(adapter);

        registerForContextMenu(favoritesListView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.backToMainItem:
                finish();
                return true;
            case R.id.deleteAllItem:
                deleteAllTableData();
                Cursor updatedCursor = sqlHelper.getReadableDatabase().query(RecipeContract.TABLE_NAME, null, null, null, null,null, null);
                adapter.swapCursor(updatedCursor);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllTableData() {

        db = sqlHelper.getReadableDatabase();
        int numOfDeleted = db.delete(RecipeContract.TABLE_NAME, null, null);

        Toast.makeText(this, "You Deleted " + numOfDeleted + " Items", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.on_click_menu_favorites, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.openRecipe:
                String url = recipeCursor.getString(recipeCursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_URL));
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.shareRecipe:
                //Nothing For now
                break;
            case R.id.deleteRecipe:
                int position = info.position;
                Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();

                String idToDeleteString = recipeCursor.getString(recipeCursor.getColumnIndex(RecipeContract.COLUMN_ID));
                db = sqlHelper.getWritableDatabase();
                String [] delete = {idToDeleteString};

                Toast.makeText(this,recipeCursor.getString(recipeCursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_NAME))
                        + "\nDeleted", Toast.LENGTH_SHORT).show();

                int count = db.delete(RecipeContract.TABLE_NAME, RecipeContract.COLUMN_ID + "=?" , delete);

                Cursor updatedCursor = sqlHelper.getReadableDatabase().query(RecipeContract.TABLE_NAME, null, null, null, null,null, null);
                adapter.swapCursor(updatedCursor);
                break;
        }

        return true;
    }
}

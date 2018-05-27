package com.hqs.alx.recipes;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView listMessage;
    EditText foodFromUser;
    ListView recipesLV;
    RecipeListAdapter adapter;
    static ArrayList<RecipeList> recipesFromAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodFromUser = (EditText) findViewById(R.id.searcValueET);
        recipesLV = (ListView) findViewById(R.id.recipeLV);
        listMessage = (TextView) findViewById(R.id.listHeaderMessageTV);

        (findViewById(R.id.searchBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userFood = (foodFromUser.getText().toString().trim());
                MyLongTaskHelper longTaskHelper = new MyLongTaskHelper();
                longTaskHelper.execute(userFood);
                listMessage.setText(getString(R.string.listHeaderMessage) + " " + userFood);
            }
        });

        recipesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Uri uri = Uri.parse(recipesFromAPI.get(i).getRecipeURL());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.favoritesItem){
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyLongTaskHelper extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            String allLines = "";
            String foodToSearch = params[0];

            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            try {
                url = new URL("http://food2fork.com/api/search?key=7f41caf0e3f37f662318034bdbdb727e&q=" + foodToSearch);
                is = url.openStream();  // throws an IOException
                br = new BufferedReader(new InputStreamReader(is));

                while ((line = br.readLine()) != null) {

                    allLines = allLines + line;
                }
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException ioe) {
                    // nothing to see here
                }
            }
            return allLines;
        }

        @Override
        protected void onPostExecute(String jsonText) {
            recipesFromAPI = new ArrayList<RecipeList>();

            try {
                JSONObject mainObject = new JSONObject(jsonText);
                JSONArray array = mainObject.getJSONArray("recipes");
                int arrayLength = array.length();

                for(int i = 0 ; i < arrayLength ; i++){
                    JSONObject result = array.getJSONObject(i);
                    String recipeTitleFromJson = result.getString("title").trim();
                    String recipeURL = result.getString("f2f_url").trim();
                    String publisherName = result.getString("publisher");
                    double socialRank = result.getDouble("social_rank");
                    String imageURL = result.getString("image_url");
                    String publisherOwnSite = result.getString("publisher_url");

                    recipesFromAPI.add(new RecipeList(recipeTitleFromJson, recipeURL, publisherName, socialRank, imageURL, publisherOwnSite));
                }

                listMessage.setVisibility(View.VISIBLE);

                adapter = new RecipeListAdapter(MainActivity.this, recipesFromAPI);
                recipesLV.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    }
}

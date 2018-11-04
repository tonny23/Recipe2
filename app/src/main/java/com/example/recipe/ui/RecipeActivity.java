package com.example.recipe.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.recipe.R;
import com.example.recipe.data.api.ApiClient;
import com.example.recipe.data.api.ApiService;
import com.example.recipe.data.model.Recipe;
import com.example.recipe.data.model.RecipeList;
import com.example.recipe.data.model.RecipeMain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = "RecipeActivity";
//    private ApiService client;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        getRecipes();
    }

    /**
     * Retrieves the top 3 rated recipes and creates the viewpages
     */
    public void getRecipes() {
        // Create a very simple REST adapter which points the API endpoint.
        ApiService client = ApiClient.getClient().create(ApiService.class);
        // Fetch a list of the Github repositories.
        Call<RecipeList> call = client.getRecipes();
        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
                Log.d(TAG, "onResponse: get call");
                // The network call was a success and we got a response
                if (response.isSuccessful() && response != null && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getCount());
                    Log.d(TAG, "onResponse: " + response.raw().toString());
                    setRecipes(response.body().getRecipes());
                } else {
                    Log.e(TAG, "onResponse: not successful" );
                }
            }
            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) {
                // the network call was a failure
                Toast.makeText(getApplicationContext(), "Error, check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setRecipes(List<Recipe> recipeList) {
        recipes = recipeList;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), recipes);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Recipe> recipes;

        public SectionsPagerAdapter(FragmentManager fm, List<Recipe> recipes) {
            super(fm);
            this.recipes = recipes;
        }

        @Override
        public Fragment getItem(int position) {
            Recipe recipe = recipes.get(position);
            return RecipeFragment.newInstance(recipe.getTitle(), recipe.getRecipeId(), recipe.getImageUrl());
        }

        @Override
        public int getCount() {
            return recipes.size();
        }
    }
}

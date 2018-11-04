package com.example.recipe.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipe.R;
import com.example.recipe.data.api.ApiClient;
import com.example.recipe.data.api.ApiService;
import com.example.recipe.data.model.Recipe;
import com.example.recipe.data.model.RecipeMain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {
    public static final String ARG_RECIPE_NAME = "arg_recipe_name";
    public static final String ARG_RECIPE_ID = "arg_recipe_id";
    private static final String ARG_RECIPE_IMAGE = "arg_recipe_image";
    private TextView descriptionTextView;

    public static RecipeFragment newInstance(String recipeName, String recipeId, String recipeImageUrl) {
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_NAME, recipeName);
        args.putString(ARG_RECIPE_ID, recipeId);
        args.putString(ARG_RECIPE_IMAGE, recipeImageUrl);
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recipeView = inflater.inflate(R.layout.fragment_recipe, container, false);
        TextView nameTextView = recipeView.findViewById(R.id.recipe_name);
        descriptionTextView = recipeView.findViewById(R.id.recipe_instruction);
        ImageView recipeImage = recipeView.findViewById(R.id.recipe_image);

        nameTextView.setText(getArguments().getString(ARG_RECIPE_NAME));
        getRecipe();
        Glide.with(this).load(getArguments().getString(ARG_RECIPE_IMAGE)).into(recipeImage);
        return recipeView;
    }

    /**
     * Retrieves the recipe based on the ID for the ingredients
     */
    public void getRecipe() {
        // Create a very simple REST adapter which points the API endpoint.
        ApiService client = ApiClient.getClient().create(ApiService.class);
        // Fetch a list of the Github repositories.
        Call<RecipeMain> call = client.getRecipe(getArguments().getString(ARG_RECIPE_ID));
        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<RecipeMain>() {
            @Override
            public void onResponse(Call<RecipeMain> call, Response<RecipeMain> response) {
                Log.d(TAG, "onResponse: get call");
                // The network call was a success and we got a response
                if (response.isSuccessful() && response != null && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.raw().toString());
                    setRecipe(response.body().getRecipe());
                } else {
                    Log.e(TAG, "onResponse: not successful" );
                }
            }
            @Override
            public void onFailure(Call<RecipeMain> call, Throwable t) {
                // the network call was a failure
                Toast.makeText(getContext(), "Error, check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Set the ingredient list in the textview
     * @param recipe
     */
    private void setRecipe(Recipe recipe) {
        String ingredients = "Ingredients: \n";
        for (String ingredient : recipe.getIngredients()) {
            ingredients += "- " + ingredient + "\n";
        }

        descriptionTextView.setText(ingredients);
    }
}

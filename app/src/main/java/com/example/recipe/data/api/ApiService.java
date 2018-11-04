package com.example.recipe.data.api;

import com.example.recipe.data.model.RecipeMain;
import com.example.recipe.data.model.RecipeList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/search?key=9a7c0496e3624235ed108f6b328d2423&sort=r&count=3")
    Call<RecipeList> getRecipes();

    @GET("api/get?key=9a7c0496e3624235ed108f6b328d2423")
    Call<RecipeMain> getRecipe(@Query("rId") String recipeId);
}

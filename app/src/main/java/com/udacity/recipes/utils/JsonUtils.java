package com.udacity.recipes.utils;

import com.udacity.recipes.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {


    final static String MOVIE_LISTINGS_RESULTS = "results";
    final static String MOVIE_LISTINGS_PAGE = "page";
    final static String MOVIE_LISTINGS_TOTAL_RESULTS = "total_results";
    final static String MOVIE_LISTINGS_TOTAL_PAGES = "total_pages";

    final static String MOVIE_POSTER_PATH = "poster_path";
    final static String MOVIE_ADULT = "adult";
    final static String MOVIE_OVERVIEW = "overview";
    final static String MOVIE_RELEASE_DATE = "release_date";
    final static String MOVIE_GENRE_IDS = "genre_ids";
    final static String MOVIE_ID = "id";
    final static String MOVIE_ORIGINAL_TITLE = "original_title";
    final static String MOVIE_ORIGINAL_LANGUAGE = "original_language";
    final static String MOVIE_TITLE = "title";
    final static String MOVIE_BACKDROP_PATH = "backdrop_path";
    final static String MOVIE_POPULARITY = "popularity";
    final static String MOVIE_VOTE_COUNT = "vote_count";
    final static String MOVIE_VIDEO = "video";
    final static String MOVIE_VOTE_AVERAGE = "vote_average";

    final static String MOVIE_VIDEO_DETAIL_id = "id";
    final static String MOVIE_VIDEO_results = "results";
    final static String MOVIE_VIDEO_id = "id";
    final static String MOVIE_VIDEO_iso_6391 = "iso_639_1";
    final static String MOVIE_VIDEO_iso_3166_1 = "iso_3166_1";
    final static String MOVIE_VIDEO_key = "key";
    final static String MOVIE_VIDEO_name = "name";
    final static String MOVIE_VIDEO_site = "site";
    final static String MOVIE_VIDEO_size = "size";
    final static String MOVIE_VIDEO_type = "type";

    final static String MOVIE_REVIEW_DETAIL_id = "id";
    final static String MOVIE_REVIEW_total_pages = "total_pages";
    final static String MOVIE_REVIEW_total_results = "total_results";
    final static String MOVIE_REVIEW_results = "results";
    final static String MOVIE_REVIEW_id = "id";
    final static String MOVIE_REVIEW_author = "author";
    final static String MOVIE_REVIEW_content = "content";
    final static String MOVIE_REVIEW_url = "url";

    public static List<Recipe> parseRecipesJson(String json) {
        JSONArray recipeJsonArray = null;
        List<Recipe> recipesList = null;
        try {
            recipesList = new ArrayList<Recipe>();
            recipeJsonArray = new JSONArray(json);
            JSONObject recipeJSONObject;
            //recipes = getRecipes(recipeJsonArray);
            for (int i = 0; i < recipeJsonArray.length(); i++) {
                recipeJSONObject = recipeJsonArray.getJSONObject(i);
                recipesList.add(parseRecipeJson(recipeJSONObject));
            }

        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return recipesList;
    }

    private static Recipe parseRecipeJson(JSONObject jsonObj) throws JSONException {
        Recipe recipeDetails = null;
        try {
            recipeDetails = new Recipe();
            recipeDetails.setId(jsonObj.getInt("id"));
            recipeDetails.setName(jsonObj.getString("name"));
            recipeDetails.setIngredients(getRecipeIngredientsListProperty(jsonObj, "ingredients"));
            recipeDetails.setRecipeSteps(getRecipeStepsListProperty(jsonObj, "steps"));
        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return recipeDetails;

    }

    public static Recipe.Ingredient parseRecipeIngredientJson(String json) {
        JSONObject recipeIngredientJson = null;
        Recipe.Ingredient recipeIngredientDetails = null;
        try {
            recipeIngredientJson = new JSONObject(json);
            recipeIngredientDetails = new Recipe.Ingredient();
            recipeIngredientDetails.setIngredientDescription(recipeIngredientJson.getString("ingredient"));
            recipeIngredientDetails.setMeasure(recipeIngredientJson.getString("measure"));
            recipeIngredientDetails.setQuantity(recipeIngredientJson.getInt("quantity"));
        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return recipeIngredientDetails;
    }

    private static List<Recipe.Ingredient> getRecipeIngredientsListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<Recipe.Ingredient> objectPropertyList = new ArrayList<Recipe.Ingredient>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectPropertyList.add(parseRecipeIngredientJson(propertyArray.getString(i)));
        }
        return objectPropertyList;
    }

    public static Recipe.RecipeStep parseRecipeStepJson(String json) {
        JSONObject recipeStepJson = null;
        Recipe.RecipeStep recipeStepDetails = null;
        try {
            recipeStepJson = new JSONObject(json);
            recipeStepDetails = new Recipe.RecipeStep();
            recipeStepDetails.setDescription(recipeStepJson.getString("description"));
            recipeStepDetails.setId(recipeStepJson.getInt("id"));
            recipeStepDetails.setShortDescription(recipeStepJson.getString("shortDescription"));
            recipeStepDetails.setThumbnailURL(recipeStepJson.getString("thumbnailURL"));
            recipeStepDetails.setVideoUrl(recipeStepJson.getString("videoURL"));
        } catch(JSONException jsonEx){
            jsonEx.printStackTrace();
        }
        return recipeStepDetails;
    }

    private static List<Recipe.RecipeStep> getRecipeStepsListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<Recipe.RecipeStep> objectPropertyList = new ArrayList<Recipe.RecipeStep>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectPropertyList.add(parseRecipeStepJson(propertyArray.getString(i)));
        }
        return objectPropertyList;
    }

    private static List<String> getStringListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<String> objectProperty = new ArrayList<String>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectProperty.add(propertyArray.getString(i));
        }
        return objectProperty;
    }

    private static List<Integer> getIntegerListProperty(JSONObject jsonObj, String propertyName) throws JSONException {
        JSONArray propertyArray = jsonObj.getJSONArray(propertyName);
        List<Integer> objectProperty = new ArrayList<Integer>();

        for (int i = 0; i < propertyArray.length(); i++) {
            objectProperty.add(propertyArray.getInt(i));
        }
        return objectProperty;
    }

}
package com.udacity.recipes.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//TODO: Implement Parcelable
public class Recipe implements Serializable {

    String TAG = Recipe.class.getSimpleName();
    private int id;

    private String name;
    private List<Ingredient> ingredients;
    private List<RecipeStep> recipeSteps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getIngredientsString() {
        String ingredientsString = "";
        for(int i=0; i<ingredients.size(); i++)
            ingredientsString += (i+1)+". "+ingredients.get(i) + "\n";
        Log.d(TAG, "ingredientsString: "+ingredientsString);
        return ingredientsString;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public RecipeStep getFirstRecipeStep(){
        if(recipeSteps==null) return null;
        return recipeSteps.get(0);
    }

    public RecipeStep getSelectedRecipeStep(int selectedRecipeStepIndex){
        if(recipeSteps==null) return null;
        return recipeSteps.get(selectedRecipeStepIndex);
    }

    public ArrayList<RecipeStep> getRecipeStepsArrayList() {
        ArrayList<Recipe.RecipeStep> recipeStepsArrayList = new ArrayList<Recipe.RecipeStep>();
        for(Recipe.RecipeStep recipeStep: getRecipeSteps())
            recipeStepsArrayList.add(recipeStep);
        return recipeStepsArrayList;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    //TODO: Implement Parcelable
    public static class Ingredient implements Serializable{
        private int quantity;
        private String measure;
        private String ingredientDescription;

        public String toString(){
            return getIngredientDescription()+": Quantity: "+getQuantity()+", Measure: "+getMeasure();
        }
        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredientDescription() {
            return ingredientDescription;
        }

        public void setIngredientDescription(String ingredientDescription) {
            this.ingredientDescription = ingredientDescription;
        }
    }

    //TODO: Implement Parcelable
    public static class RecipeStep implements Parcelable, Serializable {
        private int id;
        private String shortDescription;
        private String description;
        private String videoUrl;
        private String thumbnailURL;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoUrl);
            dest.writeString(thumbnailURL);
        }

        public RecipeStep(){

        }

        public RecipeStep(Parcel in){
            this.id = in.readInt();
            this.shortDescription = in.readString();
            this.description = in.readString();
            this.videoUrl = in.readString();
            this.thumbnailURL = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        // This is to de-serialize the object
        public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>(){
            public RecipeStep createFromParcel(Parcel in) {
                return new RecipeStep(in);
            }
            public RecipeStep[] newArray(int size) {
                return new RecipeStep[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }

        public void setThumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
        }

        public String getRecipeStepMedia(){
            if(getVideoUrl()!=null) return getVideoUrl();
            else return getThumbnailURL();
        }
    }
}
package com.udacity.recipes.utils;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.udacity.recipes.R;
import com.udacity.recipes.model.Recipe;
import com.udacity.recipes.ui.RecipeStepDetailFragment;

public class FragmentUtils {

    public static String INTENT_EXTRA_SERIALIZE_DATA = "serialize_data";
    public static String INTENT_EXTRA_SELECTED_RECIPE_INDEX = "selectedRecipeIndex";

    public static RecipeStepDetailFragment setUpRecipeStepDetailFragment(FragmentManager fragmentManager, Recipe.RecipeStep selectedRecipeStep){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(selectedRecipeStep);
        ft.replace(R.id.recipe_step_details_fragment, fragment);
        ft.commit();
        return fragment;
    }
}

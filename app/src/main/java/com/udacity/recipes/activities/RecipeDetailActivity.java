package com.udacity.recipes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.udacity.recipes.R;
import com.udacity.recipes.adapters.RecipeStepsRVAdapter;
import com.udacity.recipes.model.Recipe;
import com.udacity.recipes.ui.RecipeDetailFragment;
import com.udacity.recipes.ui.RecipeStepDetailFragment;
import com.udacity.recipes.utils.FragmentUtils;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepsRVAdapter.RecipeStepsAdapterOnClickHandler, RecipeStepDetailFragment.OnRecipeStepDumbListener  {

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean twoPane = false;

    private Recipe mSelectedRecipe;

    public Recipe getSelectedRecipe() {
        return mSelectedRecipe;
    }

    public Recipe.RecipeStep getSelectedRecipeStep(int selectedRecipeStepIndex) {
        if(mSelectedRecipe==null) return null;
        return mSelectedRecipe.getSelectedRecipeStep(selectedRecipeStepIndex);
    }

    public void setSelectedRecipe(Recipe selectedRecipe) {
        this.mSelectedRecipe = selectedRecipe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On Create Called");
        setContentView(R.layout.activity_recipe_ingr_steps_list);

        Intent intent = getIntent();

        if (savedInstanceState != null && savedInstanceState.getSerializable(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA)!=null){
            mSelectedRecipe = (Recipe) savedInstanceState.getSerializable(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA);
            Log.d(TAG, "in detailactivity from savedinstancestate mSelectedRecipeObj: "+ mSelectedRecipe);
            savedInstanceState.clear();
        } else if (intent != null) {
            mSelectedRecipe = (Recipe) getIntent().getSerializableExtra(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA);
            Log.d(TAG, "in detailactivity from intent mSelectedRecipeObj: "+ mSelectedRecipe);
        } else {
            Log.d(TAG, "in detailactivity both savesinstancestate and intent are null closeOnError");
            closeOnError();
        }

        if (mSelectedRecipe == null) {
            // Recipe data unavailable
            closeOnError();
            return;
        }

        setUpRecipeDetailFragment(getSelectedRecipe());
        //TODO: If tablet, we will also set up the second pane here... the step detail fragment.
        // Initially it will be for step index 0 and then user can select any other step and the second panel will be updated accordingly
        if(findViewById(R.id.recipe_step_details_fragment) !=null){
            FragmentUtils.setUpRecipeStepDetailFragment(getSupportFragmentManager(), getSelectedRecipe().getFirstRecipeStep());
            twoPane = true;
        }
    }

    private void setUpRecipeDetailFragment(Recipe selectedRecipe){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(selectedRecipe);
        ft.replace(R.id.recipe_detail_fragment_frame, fragment);
        ft.commit();

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRecipeStepClick(int selectedRecipeStepIndex){
        //Toast.makeText(getContext(), "Recipe step clicked, selectedRecipeIndex:"+selectedRecipeIndex, Toast.LENGTH_SHORT).show();
        if(twoPane){
            FragmentUtils.setUpRecipeStepDetailFragment(getSupportFragmentManager(), getSelectedRecipe().getSelectedRecipeStep(selectedRecipeStepIndex));
        } else {
            Context context = this;
            Class destinationClass = RecipeStepDetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            Log.d(TAG, "in onclick of recipe detail fragment onRecipeStepClick(int selectedRecipeIndex): " + selectedRecipeStepIndex);
            Recipe.RecipeStep selectedRecipeStep = getSelectedRecipeStep(selectedRecipeStepIndex);
            Log.d(TAG, "in onclick of recipedetailfragment selectedRecipeStep: " + selectedRecipeStep);
            intentToStartDetailActivity.putExtra(FragmentUtils.INTENT_EXTRA_SELECTED_RECIPE_INDEX, selectedRecipeStepIndex);
            intentToStartDetailActivity.putParcelableArrayListExtra(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA, getSelectedRecipe().getRecipeStepsArrayList());
            startActivity(intentToStartDetailActivity);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        savedInstanceState.putSerializable(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA, mSelectedRecipe);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
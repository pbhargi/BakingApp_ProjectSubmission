package com.udacity.recipes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.udacity.recipes.R;
import com.udacity.recipes.model.Recipe;
import com.udacity.recipes.ui.RecipeStepDetailFragment;
import com.udacity.recipes.utils.FragmentUtils;

import java.util.ArrayList;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailFragment.OnRecipeStepDumbListener {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    private ArrayList<Recipe.RecipeStep> mRecipeSteps;
    private Recipe.RecipeStep mSelectedRecipeStep;
    private int mSelectedRecipeStepIndex;
    public int getmSelectedRecipeStepIndex() {
        return mSelectedRecipeStepIndex;
    }

    public void setmSelectedRecipeStepIndex(int mSelectedRecipeStepIndex) {
        this.mSelectedRecipeStepIndex = mSelectedRecipeStepIndex;
    }

    public ArrayList<Recipe.RecipeStep> getRecipeSteps() {
        return mRecipeSteps;
    }

    public Recipe.RecipeStep getSelectedRecipeStep(int selectedRecipeStep) {
        if(mRecipeSteps==null) return null;
        return mRecipeSteps.get(selectedRecipeStep);
    }

    Button mButtonPrevious;
    Button mButtonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On Create Called");
        setContentView(R.layout.activity_recipe_step_details);
        mButtonPrevious = findViewById(R.id.previous_button);
        mButtonNext = findViewById(R.id.next_button);

        mButtonPrevious.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "in detailactivity from intent previous button clicked, mSelectedRecipeStepIndex: "+mSelectedRecipeStepIndex);
                int selectedRecipeStep = mSelectedRecipeStepIndex -1;
                if(selectedRecipeStep<0 || selectedRecipeStep>=mRecipeSteps.size()){
                    //Do nothing
                } else{
                    mSelectedRecipeStepIndex = selectedRecipeStep;
                    mSelectedRecipeStep = getSelectedRecipeStep(selectedRecipeStep);
                    Log.d(TAG, "in detailactivity from intent previous button clicked, selectedRecipeStep: "+mSelectedRecipeStep);
                    FragmentUtils.setUpRecipeStepDetailFragment(getSupportFragmentManager(), mSelectedRecipeStep);
                }
                Log.d(TAG, "in detailactivity from intent previous button clicked, selectedRecipeStep: "+mSelectedRecipeStep);
            }
        });

        mButtonNext.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "in detailactivity from intent next button clicked, mSelectedRecipeStepIndex: " + mSelectedRecipeStepIndex);
                int selectedRecipeStep = mSelectedRecipeStepIndex + 1;
                if (selectedRecipeStep < 0 || selectedRecipeStep >= mRecipeSteps.size()) {
                    //Do nothing
                } else {
                    mSelectedRecipeStepIndex = selectedRecipeStep;
                    mSelectedRecipeStep = getSelectedRecipeStep(selectedRecipeStep);
                    Log.d(TAG, "in detailactivity from intent next button clicked, selectedRecipeStep: " + mSelectedRecipeStep);
                    FragmentUtils.setUpRecipeStepDetailFragment(getSupportFragmentManager(), mSelectedRecipeStep);
                }
                Log.d(TAG, "in detailactivity from intent next button clicked, selectedRecipeStep: " + mSelectedRecipeStep);
            }
        });

        Intent intent = getIntent();

        if (savedInstanceState != null && savedInstanceState.getSerializable(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA)!=null){
            mSelectedRecipeStepIndex = savedInstanceState.getInt(FragmentUtils.INTENT_EXTRA_SELECTED_RECIPE_INDEX, 0);
            mRecipeSteps = savedInstanceState.getParcelableArrayList(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA);
            if(mRecipeSteps!=null)
                mSelectedRecipeStep = mRecipeSteps.get(mSelectedRecipeStepIndex);

            Log.d(TAG, "in detailactivity from savedinstancestate mSelectedRecipeStepObj: "+ mSelectedRecipeStep);
            savedInstanceState.clear();
        } else if (intent != null) {
            mSelectedRecipeStepIndex = intent.getIntExtra(FragmentUtils.INTENT_EXTRA_SELECTED_RECIPE_INDEX, 0);
            mRecipeSteps = intent.getParcelableArrayListExtra(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA);
            Log.d(TAG, "in detailactivity from intent mSelectedRecipeStepIndex: "+ mSelectedRecipeStepIndex +", mSelectedRecipeStepObj: "+ mSelectedRecipeStep);
            if(mRecipeSteps!=null)
                mSelectedRecipeStep = mRecipeSteps.get(mSelectedRecipeStepIndex);

            FragmentUtils.setUpRecipeStepDetailFragment(getSupportFragmentManager(), mSelectedRecipeStep);
        } else {
            Log.d(TAG, "in detailactivity both savesinstancestate and intent are null closeOnError");
            closeOnError();
        }

        Log.d(TAG, "in detailactivity from intent mSelectedRecipeStep: "+mSelectedRecipeStep);
        if (mSelectedRecipeStep == null) {
            // Recipe data unavailable
            closeOnError();
            return;
        }

    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        savedInstanceState.putInt(FragmentUtils.INTENT_EXTRA_SELECTED_RECIPE_INDEX, mSelectedRecipeStepIndex);
        savedInstanceState.putParcelableArrayList(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA, mRecipeSteps);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
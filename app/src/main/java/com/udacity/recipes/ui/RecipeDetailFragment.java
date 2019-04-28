package com.udacity.recipes.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.recipes.R;
import com.udacity.recipes.adapters.RecipeStepsRVAdapter;
import com.udacity.recipes.model.Recipe;

public class RecipeDetailFragment extends Fragment {

    String TAG = RecipeDetailFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    // COMPLETED (35) Add a private ForecastAdapter variable called mForecastAdapter
    private RecipeStepsRVAdapter mRecipeStepsAdapter;

    private Recipe mSelectedRecipe;

    public Recipe getSelectedRecipe() {
        return mSelectedRecipe;
    }

    public void setSelectedRecipe(Recipe selectedRecipe) {
        Log.d(TAG, "in setSelectedRecipe of RecipeDetailFragment: "+selectedRecipe);
        this.mSelectedRecipe = selectedRecipe;
    }

    private TextView mRecipeName;
    private TextView mRecipeIngredients;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    RecipeStepsRVAdapter.RecipeStepsAdapterOnClickHandler mCallback;

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (RecipeStepsRVAdapter.RecipeStepsAdapterOnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RecipeDetailFragment.OnFragmentClickListener");
        }
    }


    // Mandatory empty constructor
    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(Recipe selectedRecipe) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedRecipe", selectedRecipe);
        recipeDetailFragment.setArguments(args);
        Log.d(RecipeDetailFragment.class.getSimpleName(), "In newInstance of RecipeDetailFragment. Recipe received from args: "+selectedRecipe);
        return recipeDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        mSelectedRecipe = (Recipe)getArguments().getSerializable("selectedRecipe");
        Log.d(TAG, "In oncreate of RecipeDetailFragment. Recipe received from args: "+mSelectedRecipe);
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Intent i = getActivity().getIntent();
        //mSelectedRecipe = (Recipe) i.getSerializableExtra("selectedRecipe");

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mRecipeName = (TextView) rootView.findViewById(R.id.tv_recipe_name);
        mRecipeName.setText(mSelectedRecipe.getName());
        mRecipeIngredients = (TextView) rootView.findViewById(R.id.tv_recipe_ingredients);
        mRecipeIngredients.setText(mSelectedRecipe.getIngredientsString());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_recipe_steps);

        // COMPLETED (43) set mForecastAdapter equal to a new ForecastAdapter
        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mRecipeStepsAdapter = new RecipeStepsRVAdapter(mCallback, mSelectedRecipe.getRecipeSteps());

        // COMPLETED (44) Use mRecyclerView.setAdapter and pass in mForecastAdapter
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mRecipeStepsAdapter);
        mRecyclerView.setFocusable(false);
        // COMPLETED (38) Create layoutManager, a LinearLayoutManager with VERTICAL orientation and shouldReverseLayout == false
        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        // COMPLETED (42) Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        //mRecyclerView.setHasFixedSize(true);


        // Return the root view
        return rootView;
    }


}

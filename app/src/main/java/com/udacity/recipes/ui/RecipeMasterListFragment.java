package com.udacity.recipes.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.recipes.R;
import com.udacity.recipes.activities.RecipeDetailActivity;
import com.udacity.recipes.adapters.RecipesAdapter;
import com.udacity.recipes.model.Recipe;
import com.udacity.recipes.utils.FragmentUtils;
import com.udacity.recipes.utils.JsonUtils;
import com.udacity.recipes.utils.NetworkUtils;
import com.udacity.recipes.widget.BakingAppWidgetService;

import java.net.URL;
import java.util.List;

public class RecipeMasterListFragment extends Fragment
        implements RecipesAdapter.RecipesAdapterOnClickHandler {

    String TAG = RecipeMasterListFragment.class.getName();
    private RecyclerView mRecyclerView;
    // COMPLETED (35) Add a private ForecastAdapter variable called mForecastAdapter
    private RecipesAdapter mRecipesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnMasterFragmentClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnMasterFragmentClickListener{};

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnMasterFragmentClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RecipeMasterListFragment.OnFragmentClickListener");
        }
    }


    // Mandatory empty constructor
    public RecipeMasterListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_recipe_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_master_recipe_list);
        mErrorMessageDisplay = (TextView) rootView.findViewById(R.id.tv_error_message_display);

        // COMPLETED (38) Create layoutManager, a LinearLayoutManager with VERTICAL orientation and shouldReverseLayout == false
        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        final int columns = getResources().getInteger(R.integer.gridlayout_columns);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), columns));

        // COMPLETED (42) Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        // COMPLETED (43) set mForecastAdapter equal to a new ForecastAdapter
        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mRecipesAdapter = new RecipesAdapter(this);

        // COMPLETED (44) Use mRecyclerView.setAdapter and pass in mForecastAdapter
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mRecipesAdapter);

        /*mRecyclerView.addOnItemTouchListener()
                new OnCardClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

        loadRecipesData();

        // Return the root view
        return rootView;
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param selectedRecipeIndex The movie that was clicked
     */
    @Override
    public void onRecipeClick(int selectedRecipeIndex) {
        Log.d(TAG, "In on click of RecipeMasterListFragment - a recipe card was clicked! " + "selectedRecipeIndex:"+selectedRecipeIndex);
        Context context = getContext();
        Class destinationClass = RecipeDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        Log.d(TAG, "in onclick of recipe master fragment intentToStartDetailActivity: "+intentToStartDetailActivity);
        Log.d(TAG, "in onclick of mainactivity selectedRecipeIndex: "+selectedRecipeIndex);
        Recipe selectedRecipeObj = mRecipesAdapter.getRecipe(selectedRecipeIndex);
        BakingAppWidgetService.startActionUpdateRecipe(context, selectedRecipeObj);
        Log.d(TAG, "in onclick of mainactivity selectedRecipeObj: "+ selectedRecipeObj);
        intentToStartDetailActivity.putExtra(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA, selectedRecipeObj);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadRecipesData() {
        showRecipesDataView();
        new FetchRecipesTask().execute();
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showRecipesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchRecipesTask extends AsyncTask<String, Void, List<Recipe>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Recipe> doInBackground(String... params) {

            URL recipesRequestUrl = NetworkUtils.getRecipesUrl();

            try {
                String jsonRecipesResponse = NetworkUtils.getResponseFromHttpUrl(recipesRequestUrl);

                return JsonUtils.parseRecipesJson(jsonRecipesResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Recipe> recipeList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipeList != null) {
                mRecipesAdapter.setRecipesList(recipeList);
            } else {
                showErrorMessage();
            }
        }
    }

}

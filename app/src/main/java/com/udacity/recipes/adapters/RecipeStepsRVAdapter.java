package com.udacity.recipes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.recipes.R;
import com.udacity.recipes.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsRVAdapter extends RecyclerView.Adapter<RecipeStepsRVAdapter.RecipeStepsAdapterViewHolder> {

    String TAG = RecipeStepsRVAdapter.class.getSimpleName();
    private List<Recipe.RecipeStep> mRecipeStepsList;
    public Recipe.RecipeStep getRecipeStep(int selectedRecipeStepIndex) {
        if(mRecipeStepsList==null) return null;
        return mRecipeStepsList.get(selectedRecipeStepIndex);
    }
    public void setRecipesList(List<Recipe.RecipeStep> recipeStepsList) {
        this.mRecipeStepsList = recipeStepsList;
        notifyDataSetChanged();
    }

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final RecipeStepsAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeStepsAdapterOnClickHandler {
        void onRecipeStepClick(int selectedRecipeIndex);
    }

    /**
     * Creates a RecipesAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public RecipeStepsRVAdapter(RecipeStepsAdapterOnClickHandler clickHandler, List<Recipe.RecipeStep> recipeSteps) {
        mClickHandler = clickHandler;
        setRecipesList(recipeSteps);
    }

    /**
     * Cache of the children views for a recipe list item.
     */
    public class RecipeStepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mRecipeStepDescTextView;

        public TextView getmRecipeStepDescTextView() {
            return mRecipeStepDescTextView;
        }

        private Context mContext;

        public RecipeStepsAdapterViewHolder(View view) {
            super(view);
            mRecipeStepDescTextView = view.findViewById(R.id.recipe_step);
            //mMovieTitle = (TextView) view.findViewById(R.id.tv_original_title_list);
            //mMovieOverview = (TextView) view.findViewById(R.id.tv_release_date_list);
            mContext = view.getContext();
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Log.d(TAG, "onClick in RecipeStepsRVAdapter adapterposition: "+adapterPosition);
            mClickHandler.onRecipeStepClick(adapterPosition);
        }

        public Context getContext() {
            return mContext;
        }

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecipeStepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_steps_list_item;
        Log.d(TAG, "RecipeStepsAdapterViewHolder  in RecipeStepsRVAdapter, layoutIdForListItem: "+layoutIdForListItem);
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeStepsAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param  recipeStepsAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipeStepsAdapterViewHolder recipeStepsAdapterViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder in RecipeStepsRVAdapter, position: "+position);
        if(mRecipeStepsList!=null) {
            Recipe.RecipeStep selectedRecipeStep = getRecipeStep(position);
            Log.d(TAG, "onBindViewHolder in RecipeStepsRVAdapter, selectedRecipeStep.getShortDescription(): "+selectedRecipeStep.getShortDescription());
            String positionToPrint = position==0?"":((position)+". ");
            recipeStepsAdapterViewHolder.getmRecipeStepDescTextView().setText(positionToPrint+selectedRecipeStep.getShortDescription());
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        int count = 0;
        if (null == mRecipeStepsList)
            count = 0;
        else
            count = mRecipeStepsList.size();
        Log.d(TAG, "getItemCount in RecipeStepsRVAdapter: "+count);
        return count;
    }

}

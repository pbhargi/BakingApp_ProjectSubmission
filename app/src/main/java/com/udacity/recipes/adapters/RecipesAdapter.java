package com.udacity.recipes.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.recipes.R;
import com.udacity.recipes.model.Recipe;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private List<Recipe> mRecipesList;
    public List<Recipe> getRecipesList() {
        return mRecipesList;
    }
    public Recipe getRecipe(int selectedRecipeIndex) {
        if(mRecipesList==null) return null;
        return mRecipesList.get(selectedRecipeIndex);
    }
    public void setRecipesList(List<Recipe> mRecipesList) {
        this.mRecipesList = mRecipesList;
        notifyDataSetChanged();
    }

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final RecipesAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipesAdapterOnClickHandler {
        void onRecipeClick(int selectedRecipeIndex);
    }

    /**
     * Creates a RecipesAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public RecipesAdapter(RecipesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a recipe list item.
     */
    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CardView mRecipeCardView;

        private final TextView mRecipeNameTextView;

        public CardView getRecipeCardView() {
            return mRecipeCardView;
        }

        public TextView getRecipeNameTextView() {
            return mRecipeNameTextView;
        }

        private Context mContext;

        public RecipesAdapterViewHolder(View view) {
            super(view);
            mRecipeCardView = (CardView) view.findViewById(R.id.recipe_card_view);
            mRecipeNameTextView = mRecipeCardView.findViewById(R.id.recipe_name);
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

            mClickHandler.onRecipeClick(adapterPosition);
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
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipesAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param  recipesAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder recipesAdapterViewHolder, int position) {
        if(getRecipesList()!=null) {
            Recipe selectedRecipe = getRecipesList().get(position);
            recipesAdapterViewHolder.getRecipeNameTextView().setText(selectedRecipe.getName());
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
        if (null == getRecipesList()) return 0;
        return getRecipesList().size();
    }

}

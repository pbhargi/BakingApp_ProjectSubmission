package com.udacity.recipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.recipes.R;
import com.udacity.recipes.activities.RecipeDetailActivity;
import com.udacity.recipes.model.Recipe;
import com.udacity.recipes.utils.FragmentUtils;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    public static Recipe mSelectedRecipe;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe selectedRecipe) {

        mSelectedRecipe = selectedRecipe;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(FragmentUtils.INTENT_EXTRA_SERIALIZE_DATA, selectedRecipe);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(mSelectedRecipe!=null) {
            views.setTextViewText(R.id.bakingapp_widget_recipe_name, selectedRecipe.getName());
            views.setTextViewText(R.id.bakingapp_widget_recipe_ingredients, selectedRecipe.getIngredientsString());
        }

        views.setOnClickPendingIntent(R.id.bakingapp_widget_linear_layout, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mSelectedRecipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


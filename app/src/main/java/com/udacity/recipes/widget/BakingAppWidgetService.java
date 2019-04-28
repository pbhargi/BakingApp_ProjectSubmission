package com.udacity.recipes.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.udacity.recipes.model.Recipe;

public class BakingAppWidgetService extends IntentService {

    private static Recipe mSelectedRecipe;
    private static String ACTION_UPDATE_WIDGET_SELECTED_RECIPE = "UPDATE_WIDGET_SELECTED_RECIPE";

    public BakingAppWidgetService() {
        super(BakingAppWidgetService.class.getSimpleName());
    }

    public static void startActionUpdateRecipe(Context context, Recipe selectedRecipe) {
        mSelectedRecipe = selectedRecipe;
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_SELECTED_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET_SELECTED_RECIPE.equals(action)) {
                for (int appWidgetId : appWidgetIds) {
                    BakingAppWidget.updateAppWidget(this, appWidgetManager, appWidgetId, mSelectedRecipe);
                }
            }
        }

    }

}

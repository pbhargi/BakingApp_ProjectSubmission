package com.udacity.recipes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.recipes.R;
import com.udacity.recipes.adapters.RecipesAdapter;
import com.udacity.recipes.ui.RecipeMasterListFragment;

public class MainActivity extends AppCompatActivity implements RecipeMasterListFragment.OnMasterFragmentClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On Create Called");
        setContentView(R.layout.activity_main);
    }

}

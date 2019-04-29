package com.udacity.recipes.ui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.recipes.R;
import com.udacity.recipes.model.Recipe;
import com.udacity.recipes.utils.NetworkUtils;

public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    @Override
    public void onShuffleModeEnabledChanged(boolean boo){

    }

    public String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private Recipe.RecipeStep mSelectedRecipeStep;

    public Recipe.RecipeStep getSelectedRecipeStep() {
        return mSelectedRecipeStep;
    }

    public void setSelectedRecipeStep(Recipe.RecipeStep selectedRecipeStep) {
        Log.d(TAG, "in setSelectedRecipeStep of RecipeStepDetailFragment: "+selectedRecipeStep);
        this.mSelectedRecipeStep = selectedRecipeStep;
    }

    private CardView mRecipeStepMediaCardView;
    //private TextView mRecipeStepMediaTextView;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;


    private CardView mRecipeStepInstructionsCardView;
    private TextView mRecipeStepInstructionsTextView;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnRecipeStepDumbListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnRecipeStepDumbListener{

    };

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnRecipeStepDumbListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RecipeStepDetailFragment.OnRecipeStepDumbListener");
        }
    }


    // Mandatory empty constructor
    public RecipeStepDetailFragment() {
    }

    public static RecipeStepDetailFragment newInstance(Recipe.RecipeStep selectedRecipeStep) {
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("selectedRecipeStep", selectedRecipeStep);
        recipeStepDetailFragment.setArguments(args);
        Log.d(RecipeStepDetailFragment.class.getSimpleName(), "In newInstance of RecipeStepDetailFragment. Recipe Step received from args: "+selectedRecipeStep);
        return recipeStepDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        mSelectedRecipeStep = (Recipe.RecipeStep) getArguments().getParcelable("selectedRecipeStep");
        Log.d(TAG, "In oncreate of RecipeDetailFragment. Recipe received from args: "+mSelectedRecipeStep);
        if (savedInstanceState != null) {
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
            Log.d(TAG, "onCreate of StepDetailFragemnt after reading savedInstanceState, startAutoPlay: "+startAutoPlay+", startWindow: "+startWindow+", startPosition: "+startPosition);
        } else {
            clearStartPosition();
        }
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps_details, container, false);
        mRecipeStepMediaCardView = (CardView) rootView.findViewById(R.id.recipe_step_media_card_view);

        //mRecipeStepMediaTextView = (TextView) rootView.findViewById(R.id.tv_recipe_step_media);
        mRecipeStepInstructionsCardView= (CardView) rootView.findViewById(R.id.recipe_step_instructions_card_view);
        mRecipeStepInstructionsTextView = (TextView) rootView.findViewById(R.id.tv_recipe_step_instructions);

        //mRecipeStepMediaTextView.setText(mSelectedRecipeStep.getRecipeStepMedia());
        mRecipeStepInstructionsTextView.setText(mSelectedRecipeStep.getDescription());

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        if ((null==mSelectedRecipeStep.getRecipeStepMedia()) || "".equals(mSelectedRecipeStep.getRecipeStepMedia())) {
            mPlayerView.setVisibility(View.GONE);
            //Toast.makeText(getContext(), getString(R.string.recipe_step_media_not_found_error), Toast.LENGTH_SHORT).show();
            return rootView;
        }

        // Initialize the player view.
        mPlayerView.setVisibility(View.VISIBLE);
        // Load the question mark as the background image until the user answers the question.
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
        // Initialize the Media Session.

        initializeMediaSession();

        // Initialize the player.
        initializePlayer();

        if(mExoPlayer!=null) {
            mExoPlayer.setPlayWhenReady(startAutoPlay);
            Log.d(TAG, "onCreateView of StepDetailFragment, mExoPlayer startAutoPlay: " + startAutoPlay);
            boolean haveStartPosition = startWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                mExoPlayer.seekTo(startWindow, startPosition);
                Log.d(TAG, "onCreateView of StepDetailFragment, mExoPlayer moved to startPosition: " + startPosition);
            }
        }
        // Return the root view
        return rootView;
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                break;
            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                break;
            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                break;
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            Uri recipeStepMediaUri = NetworkUtils.buildUri(mSelectedRecipeStep.getRecipeStepMedia());
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(recipeStepMediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            Log.d(TAG, "initializePlayer, startAutoPlay: "+startAutoPlay+", startWindow: "+startWindow+", startPosition: "+startPosition);
            mExoPlayer.setPlayWhenReady(startAutoPlay);
            boolean haveStartPosition = startWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                mExoPlayer.seekTo(startWindow, startPosition);
                Log.d(TAG, "initializePlayer, mExoPlayer moved to startPosition: "+startPosition);
            }
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        //mNotificationManager.cancelAll();
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    // ExoPlayer Event Listeners
    @Override
    public void onSeekProcessed() {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters paras) {
    }

    @Override
    public void onPositionDiscontinuity(int i){

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }

        //mMediaSession.setPlaybackState(mStateBuilder.build());
        //showNotification(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }



    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }


    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    private static final String KEY_WINDOW = "window";

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            startAutoPlay = mExoPlayer.getPlayWhenReady();
            startWindow = mExoPlayer.getCurrentWindowIndex();
            startPosition = Math.max(0, mExoPlayer.getCurrentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startPosition = C.TIME_UNSET;
        startWindow = C.INDEX_UNSET;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "beginning of onSaveInstanceState of StepDetailFragemnt, startAutoPlay: "+startAutoPlay+", startWindow: "+startWindow+", startPosition: "+startPosition);
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
        Log.d(TAG, "end of onSaveInstanceState of StepDetailFragemnt, startAutoPlay: "+startAutoPlay+", startWindow: "+startWindow+", startPosition: "+startPosition);
    }


}

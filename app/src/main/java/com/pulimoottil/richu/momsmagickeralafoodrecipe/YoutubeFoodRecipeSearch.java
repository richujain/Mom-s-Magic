package com.pulimoottil.richu.momsmagickeralafoodrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YoutubeFoodRecipeSearch extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    String YOUTUBE_VIDEO_CODE,title,ingredients;
    TextView textViewTitle,textViewIngredients;
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_youtube_food_recipe_search);





        textViewTitle = (TextView)findViewById(R.id.textViewTitle);
        textViewIngredients = (TextView)findViewById(R.id.textViewIngredients);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);
        Intent intent = getIntent();
        //Toast.makeText(this, ""+intent.getIntExtra(), Toast.LENGTH_SHORT).show();
        YOUTUBE_VIDEO_CODE = intent.getStringExtra("YOUTUBE_VIDEO_CODE");
        title = intent.getStringExtra("title");
        ingredients = intent.getStringExtra("ingredients");
        textViewTitle.setText(title);
        textViewIngredients.setText(ingredients);
        mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
        scrollToBottom();


    }
    private void scrollToBottom(){
        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            //zString YOUTUBE_VIDEO_CODE = "_oEA18Y8gM0";
            youTubePlayer.loadVideo(YOUTUBE_VIDEO_CODE);
            //youTubePlayer.setFullscreen(true);

            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }


}

package com.bee.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bee.player.base.ShareUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = "PlayActivity";
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private MediaItem mediaItem;
    /**
     * pause 记录当前position
     */
    private long mWhenPauseTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mediaItem = getIntent().getParcelableExtra("media");
        if (mediaItem == null) {
            finish();
            return;
        }
        Object pauseTime = getLastCustomNonConfigurationInstance();
        if (pauseTime != null) {
            mWhenPauseTime = (long) pauseTime;
        }
        initializePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWhenPauseTime = player.getCurrentPosition();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mWhenPauseTime;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            // Bind the player to the view.
            playerView = findViewById(R.id.player_view);
            playerView.setPlayer(player);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "com.bee.player"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(ShareUtils.getUri(this, mediaItem.path));
            // Prepare the player with the source.
            player.prepare(videoSource);
            player.seekTo(mWhenPauseTime);
            player.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

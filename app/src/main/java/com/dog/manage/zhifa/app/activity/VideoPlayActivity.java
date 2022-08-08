package com.dog.manage.zhifa.app.activity;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SeekBar;

import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityVideoPlayBinding;

import java.util.Formatter;
import java.util.Locale;

public class VideoPlayActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private ActivityVideoPlayBinding binding;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_video_play);

        binding.videoView.setOnPreparedListener(this);
        binding.videoView.setOnCompletionListener(this);

        String uri = "android.resource://" + getPackageName() + "/" + (getIntent().getIntExtra("type", 0) == 0 ? R.raw.video : R.raw.picture);
        binding.videoView.setVideoURI(Uri.parse(uri));
//        binding.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        binding.videoView.start();

        SeekBar seekBar = (SeekBar) binding.mediaProgress;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                long duration = binding.videoView.getDuration();
                long newposition = (duration * progress) / 1000L;
                binding.videoView.seekTo((int) newposition);
                binding.curMediaTime.setText(stringForTime((int) newposition));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(SHOW_PROGRESS);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setProgress();
                updatePausePlay();
                handler.sendEmptyMessage(SHOW_PROGRESS);
            }
        });
        binding.mediaProgress.setMax(1000);

        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseVideo();
            }
        });

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.videoView.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
            handler.sendEmptyMessage(SHOW_PROGRESS);
        }
    }

    private void updatePausePlay() {
        if (binding.videoView.isPlaying()) {
            binding.play.setImageResource(R.drawable.ic_media_play);
        } else {
            binding.play.setImageResource(R.drawable.ic_media_pause);
        }
    }

    public void onClickBack(View view) {
        finish();
    }

    private static final int SHOW_PROGRESS = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESS:
                    if (binding.videoView.isPlaying()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setProgress();
                                if (binding.videoView.isPlaying()) {
                                    sendEmptyMessage(SHOW_PROGRESS);
                                }
                            }
                        });
                    }
                    break;
            }

        }
    };

    public int setProgress() {
        int position = binding.videoView.getCurrentPosition();
        int duration = binding.videoView.getDuration();

        if (duration > 0) {
            long pos = 1000L * position / duration;
            binding.mediaProgress.setProgress((int) pos);
        }
        int percent = binding.videoView.getBufferPercentage();
        binding.mediaProgress.setSecondaryProgress(percent * 10);

        binding.mediaTime.setText(stringForTime(duration));
        binding.curMediaTime.setText(stringForTime(position));

        return position;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void pauseVideo() {
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
            handler.sendEmptyMessage(SHOW_PROGRESS);
        } else {
            binding.videoView.start();
            handler.sendEmptyMessage(SHOW_PROGRESS);
        }
        updatePausePlay();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        updatePausePlay();
        this.handler.removeCallbacksAndMessages(this);
        binding.mediaProgress.setProgress(0);
        binding.mediaProgress.setSecondaryProgress(0);
        binding.curMediaTime.setText(stringForTime(0));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    binding.videoView.setBackgroundColor(Color.TRANSPARENT);
                    handler.sendEmptyMessage(SHOW_PROGRESS);
                    updatePausePlay();
                }
                return true;
            }
        });
    }
}
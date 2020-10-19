package com.example.genshincd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private static final boolean USE_TEXTURE_VIEW = true;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String ASSET_FILENAME = "bbb.m4v";

    private VLCVideoLayout mVideoLayout = null;
    private TextureView textureLayer = null;
    private Button screenshotButton = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        final ArrayList<String> args = new ArrayList<>();
        args.add("--network-caching=0");
        args.add("--clock-jitter=0");
        args.add("--clock-synchro=0");
        args.add("--live-caching=0");
        args.add("--drop-late-frames");
        args.add("--skip-frames");
        args.add("--sout-x264-tune=zerolatency");
        args.add("--avcodec-hw=");

        mLibVLC = new LibVLC(this, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);

        mVideoLayout = findViewById(R.id.video_layout);
        screenshotButton = findViewById(R.id.screenshotButton);

        screenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File sd = Environment.getExternalStorageDirectory();
                File dest = new File(sd, "filefile" + System.currentTimeMillis() + ".jpg");

                if(textureLayer == null){

                    ViewGroup viewGroup = (ViewGroup) mVideoLayout.getChildAt(0);
                    textureLayer = (TextureView) viewGroup.getChildAt(2);

                }

                Bitmap image = textureLayer.getBitmap();

                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    image.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);

        final Media media = new Media(mLibVLC, Uri.parse("udp://@192.168.1.70:1234?pkt_size=1316"));
        media.addOption(":network-caching=150");
        media.addOption(":clock-jitter=0");
        media.addOption(":clock-synchro=0");
        mMediaPlayer.setMedia(media);

        media.release();

        mMediaPlayer.play();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }
}
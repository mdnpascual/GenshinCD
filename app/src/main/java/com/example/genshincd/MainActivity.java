package com.example.genshincd;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private static final boolean USE_TEXTURE_VIEW = true;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String ASSET_FILENAME = "bbb.m4v";

    private VLCVideoLayout mVideoLayout = null;
    private TextureView textureLayer = null;
//    private Button screenshotButton = null;

    private ImageView circleOne = null;
    private ImageView circleTwo = null;
    private ImageView circleThree = null;
    private ImageView circleFour = null;
    private GradientDrawable shapeOne = null;
    private GradientDrawable shapeTwo = null;
    private GradientDrawable shapeThree = null;
    private GradientDrawable shapeFour = null;
    Random rnd = new Random();

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

        initLayout();
        looper();

    }

    public void initLayout(){
        circleOne = findViewById(R.id.circleFirst);
        shapeOne = initShapes(shapeOne);
        circleOne.setBackground(shapeOne);

        circleTwo = findViewById(R.id.circleSecond);
        shapeTwo = initShapes(shapeTwo);
        circleTwo.setBackground(shapeTwo);

        circleThree = findViewById(R.id.circleThird);
        shapeThree = initShapes(shapeThree);
        circleThree.setBackground(shapeThree);

        circleFour = findViewById(R.id.circleFourth);
        shapeFour = initShapes(shapeFour);
        circleFour.setBackground(shapeFour);
    }

    public GradientDrawable initShapes(GradientDrawable shape){
        shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setUseLevel(false);
        shape.setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        shape.setStroke(12, Color.BLUE);
        shape.setSize(160,60);
        return shape;
    }

    public void looper() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(textureLayer == null){

                                ViewGroup viewGroup = (ViewGroup) mVideoLayout.getChildAt(0);
                                textureLayer = (TextureView) viewGroup.getChildAt(2);

                            }

                            Bitmap image = textureLayer.getBitmap();
                            int width = image.getWidth();
                            int height = image.getHeight();

                            shapeOne.setColor(Color.argb(255, Color.red(image.getPixel(10,10)), Color.blue(image.getPixel(10,10)), Color.green(image.getPixel(10,10))));
                            shapeTwo.setColor(Color.argb(255, Color.red(image.getPixel(width/4,height/4)), Color.green(image.getPixel(width/4,height/4)), Color.blue(image.getPixel(width/4,height/4))));
                            shapeThree.setColor(Color.argb(255, Color.red(image.getPixel(width/2,height/2)), Color.green(image.getPixel(width/2,height/2)), Color.blue(image.getPixel(width/2,height/2))));
                            shapeFour.setColor(Color.argb(255, Color.red(image.getPixel(3*width/4,3*height/4)), Color.green(image.getPixel(3*width/4,3*height/4)), Color.blue(image.getPixel(3*width/4,3*height/4))));
                        }
                    });
                }
            }
        }).start();
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
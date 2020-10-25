package com.example.genshincd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {
    private static final boolean USE_TEXTURE_VIEW = true;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String ASSET_FILENAME = "bbb.m4v";

    private VLCVideoLayout mVideoLayout = null;
    private TextureView textureLayer = null;
//    private Button screenshotButton = null;

    private ViewGroup circleLayoutParent = null;
    private ImageView circleFirst = null;
    private ImageView circleSecond = null;
    private ImageView circleThird = null;
    private ImageView circleFourth = null;
    private GradientDrawable shapeOne = null;
    private GradientDrawable shapeTwo = null;
    private GradientDrawable shapeThree = null;
    private GradientDrawable shapeFour = null;
    Random rnd = new Random();

    private RelativeLayout configureSettingsParent = null;
    private Button saveButton = null;

    private SurfaceView surfaceView = null;

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
        surfaceView = findViewById(R.id.surface_view);
        surfaceView.setOnTouchListener(this);
        surfaceView.setOnDragListener(this);
        configureSettingsParent = (RelativeLayout) findViewById(R.id.configureSettingsParent);
        saveButton = (Button) findViewById(R.id.save_button);

        initLayout();
        initOnClicks();
        looper();

    }

    public void initLayout(){
        circleLayoutParent = (ViewGroup) findViewById(R.id.circleLayoutParent);

        circleFirst = findViewById(R.id.circleFirst);
        shapeOne = initShapes(shapeOne);
        circleFirst.setBackground(shapeOne);

        circleSecond = findViewById(R.id.circleSecond);
        shapeTwo = initShapes(shapeTwo);
        circleSecond.setBackground(shapeTwo);

        circleThird = findViewById(R.id.circleThird);
        shapeThree = initShapes(shapeThree);
        circleThird.setBackground(shapeThree);

        circleFourth = findViewById(R.id.circleFourth);
        shapeFour = initShapes(shapeFour);
        circleFourth.setBackground(shapeFour);
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

    public void initOnClicks(){
        circleFirst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("first");

            }
        });
        circleSecond .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("second");

            }
        });
        circleThird.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("third");

            }
        });
        circleFourth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("fourth");

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                configureSettingsParent.setVisibility(View.GONE);
                circleLayoutParent.setVisibility(View.VISIBLE);
                System.out.println("saveButton");

            }
        });
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("touching");
        System.out.println(event.getAction());
        System.out.println(event.getX());
        System.out.println(event.getY());
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("moving");
        System.out.println(event.getAction());
        System.out.println(event.getX());
        System.out.println(event.getY());
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        System.out.println("dragging");
        System.out.println(event.getAction());
        System.out.println(event.getX());
        System.out.println(event.getY());
        return false;
    }
}
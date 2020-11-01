package com.example.genshincd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
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

    private int activeObject = -1;
    private RelativeLayout configureSettingsParent = null;
    private ViewGroup crosshairLayoutParent = null;
    private ImageView crosshair = null;
    private ViewGroup squareLayoutParent = null;
    private ImageView square = null;
    private Button saveButton = null;

    private SurfaceView surfaceView = null;
    private float pointerX, pointerY = 0;

    // SCALE
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    // VLC
    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;

    // SETTINGS
    private int activeSquare = -1;
    private Character C_first = null;
    private Character C_second = null;
    private Character C_third = null;
    private Character C_fourth = null;

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

        C_first = new Character(this, "first");
        C_second = new Character(this, "second");
        C_third = new Character(this, "third");
        C_fourth = new Character(this, "fourth");

        initLayout();
        initOnClicks();
        looper();

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

    }

    public void initLayout(){
        circleLayoutParent = (ViewGroup) findViewById(R.id.circleLayoutParent);
        crosshairLayoutParent = (ViewGroup) findViewById(R.id.crosshairLayoutParent);
        squareLayoutParent = (ViewGroup) findViewById(R.id.squareLayoutParent);

        configureSettingsParent = (RelativeLayout) findViewById(R.id.configureSettingsParent);
        saveButton = (Button) findViewById(R.id.save_button);
        crosshair = (ImageView) findViewById(R.id.crosshair);
        square = (ImageView) findViewById(R.id.square);

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

        // FIRST CIRCLE
        circleFirst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                crosshair.setX(C_first.crossHairX);
                crosshair.setY(C_first.crossHairY);
                square.setX(C_first.squareX);
                square.setY(C_first.squareY);
                square.setScaleX(C_first.squareScale);
                square.setScaleY(C_first.squareScale);

                activeSquare = 1;
                crosshair.setVisibility(View.VISIBLE);
                square.setVisibility(View.VISIBLE);
                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("first");

            }
        });

        // SECOND CIRCLE
        circleSecond .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                crosshair.setX(C_second.crossHairX);
                crosshair.setY(C_second.crossHairY);
                square.setX(C_second.squareX);
                square.setY(C_second.squareY);
                square.setScaleX(C_second.squareScale);
                square.setScaleY(C_second.squareScale);

                activeSquare = 2;
                crosshair.setVisibility(View.VISIBLE);
                square.setVisibility(View.VISIBLE);
                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("second");

            }
        });

        // THIRD CIRCLE
        circleThird.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                crosshair.setX(C_third.squareX);
                crosshair.setY(C_third.squareY);
                square.setX(C_third.squareX);
                square.setY(C_third.squareY);
                square.setScaleX(C_third.squareScale);
                square.setScaleY(C_third.squareScale);

                activeSquare = 3;
                crosshair.setVisibility(View.VISIBLE);
                square.setVisibility(View.VISIBLE);
                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("third");

            }
        });

        // FOURTH CIRCLE
        circleFourth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                crosshair.setX(C_fourth.squareX);
                crosshair.setY(C_fourth.squareY);
                square.setX(C_fourth.squareX);
                square.setY(C_fourth.squareY);
                square.setScaleX(C_fourth.squareScale);
                square.setScaleY(C_fourth.squareScale);

                activeSquare = 4;
                crosshair.setVisibility(View.VISIBLE);
                square.setVisibility(View.VISIBLE);
                configureSettingsParent.setVisibility(View.VISIBLE);
                circleLayoutParent.setVisibility(View.GONE);
                System.out.println("fourth");

            }
        });

        // SAVE BUTTON
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Character charToChange = null;
                switch (activeSquare){
                    case 1:
                        charToChange = C_first;
                        break;
                    case 2:
                        charToChange = C_second;
                        break;
                    case 3:
                        charToChange = C_third;
                        break;
                    case 4:
                        charToChange = C_fourth;
                        break;
                }

                charToChange.crossHairX = (int)crosshair.getX();
                charToChange.crossHairY = (int)crosshair.getY();
                charToChange.squareX = (int)square.getX();
                charToChange.squareY = (int)square.getY();
                charToChange.squareScale = square.getScaleX();
                charToChange.save();

                configureSettingsParent.setVisibility(View.GONE);
                circleLayoutParent.setVisibility(View.VISIBLE);
                System.out.println("saveButton");
                activeSquare = 0;

            }
        });

        // CROSSHAIR BUTTON
        crosshairLayoutParent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                activeObject = 1;
                System.out.println("Crosshair Button");

            }
        });

        // SQUARE BUTTON
        squareLayoutParent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                activeObject = 2;
                System.out.println("Square Button");

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
                            if(image != null){
                                int width = image.getWidth();
                                int height = image.getHeight();

                                shapeOne.setColor(Color.argb(255, Color.red(image.getPixel(10,10)), Color.blue(image.getPixel(10,10)), Color.green(image.getPixel(10,10))));
                                shapeTwo.setColor(Color.argb(255, Color.red(image.getPixel(width/4,height/4)), Color.green(image.getPixel(width/4,height/4)), Color.blue(image.getPixel(width/4,height/4))));
                                shapeThree.setColor(Color.argb(255, Color.red(image.getPixel(width/2,height/2)), Color.green(image.getPixel(width/2,height/2)), Color.blue(image.getPixel(width/2,height/2))));
                                shapeFour.setColor(Color.argb(255, Color.red(image.getPixel(3*width/4,3*height/4)), Color.green(image.getPixel(3*width/4,3*height/4)), Color.blue(image.getPixel(3*width/4,3*height/4))));
                            }
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
    public boolean onTouchEvent(MotionEvent event) {

        View objectToMove = null;

        // SELECT ACTIVE OBJECT
        switch(activeObject){
            case 1:
                objectToMove = crosshair;
                break;
            case 2:
                objectToMove = square;
                break;
        }

        if(objectToMove != null){

            int action = event.getActionMasked();
            int pointer = event.getActionIndex();

            mScaleDetector.onTouchEvent(event);
            objectToMove.setScaleX(mScaleFactor);
            objectToMove.setScaleY(mScaleFactor);

            switch (action){
                case MotionEvent.ACTION_DOWN:
                    pointerX = event.getX(pointer);
                    pointerY = event.getY(pointer);
                    break;
                case MotionEvent.ACTION_MOVE:

                    float newPointerX = event.getX(pointer);
                    float newPointerY = event.getY(pointer);

                    objectToMove.setX(objectToMove.getX() + (newPointerX - pointerX));
                    objectToMove.setY(objectToMove.getY() + (newPointerY - pointerY));

                    pointerX = newPointerX;
                    pointerY = newPointerY;
                    break;
            }

        }

        return false;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

//            invalidate();
            return true;
        }
    }

}
package com.example.ec327_final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.ec327_final_project.R;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    //adding variables
    int screenX;
    int countHit;
    boolean flag;
    private boolean isGameOver;

    public static final int WIDTH = 2080;
    public static final int HEIGHT = 640;


    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    long start = System.currentTimeMillis();

    private background bg;

    //Adding enemies object array
    private Enemy[] enemies;

    //Adding 5 enemies to display on screen at one time
    private int enemyCount = 5;

    private int life = 3;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);

        surfaceHolder = getHolder();
        paint = new Paint();

        //creating a background that will be able to move
        bg = new background(BitmapFactory.decodeResource(getResources(), R.drawable.level_bg));
        bg.setVector(-5);

        //initializing an array of enemy objects
        enemies = new Enemy[enemyCount];
        for(int i=0; i<enemyCount; i++){
            enemies[i] = new Enemy(context, screenX, screenY);
        }

        this.screenX = screenX;
        countHit = 0;
        isGameOver = false;

    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update();

        for(int i=0; i<enemyCount; i++){
            if(enemies[i].getX() == screenX){
                flag = true;
            }

            bg.update();

            enemies[i].update(player.getSpeed());

            //recognizing the collisions between player and enemies to check if life decrements
            if(Rect.intersects(player.getDetectCollision(),enemies[i].getDetectCollision())){
                enemies[i].setX(-200);
                countHit++;
                life--;
                if(countHit==3){
                    playing = false;
                    isGameOver = true;
                }
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            if(canvas != null) {
                bg.draw(canvas);
            }

            paint.setTextSize(75);
            paint.setColor(Color.WHITE);
            canvas.drawText("TIME: " + (int)(System.currentTimeMillis()-start)/1000, 1400, 225 + paint.ascent(),paint);
            canvas.drawText("LIVES: " + life, 100, 225 + paint.ascent(),paint);

            //drawing the player to the screen
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            //drawing the enemies
            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint
                );
            }

            //if isGameOver is true, the game ends and calls the GameOver activity
            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                Intent myIntent = new Intent(getContext().getApplicationContext(), GameOver.class);
                getContext().startActivity(myIntent);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopMoving();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setMoving();
                break;
        }
        return true;
    }

}

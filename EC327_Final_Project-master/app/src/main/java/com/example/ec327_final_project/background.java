package com.example.ec327_final_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class background {
    private Bitmap image;
    private int x,y,dx;

    public background(Bitmap res) {
        image = res;
    }

    public void update(){
        x += dx;
        if (x<-GameView.WIDTH){
            x=0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);
        if(x<0){
            canvas.drawBitmap(image,x+GameView.WIDTH,y,null);
        }
    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}

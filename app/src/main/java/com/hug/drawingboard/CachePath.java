package com.hug.drawingboard;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by HStan on 2017/6/19.
 */

public class CachePath {
    private Path path;
    private Paint paint;
    private Canvas canvas;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void draw(){
        if (canvas != null && paint != null && path != null){
            canvas.drawPath(path,paint);
        }
    }
}

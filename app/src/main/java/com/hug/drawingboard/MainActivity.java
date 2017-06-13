package com.hug.drawingboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageButton mPen,mEraser,mColorPicker;
    private DrawingView  mDrawingView;
    private ColorPicker colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPen = (ImageButton) findViewById(R.id.pen);
        mEraser = (ImageButton) findViewById(R.id.eraser);
        mColorPicker= (ImageButton) findViewById(R.id.color_picker);
        mDrawingView = (DrawingView) findViewById(R.id.drawingview);
        mPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setMode(DrawingView.MODE_PEN);
            }
        });
        mEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setMode(DrawingView.MODE_ERASER);
            }
        });
        mColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorPicker == null){
                    colorPicker = new ColorPicker(MainActivity.this);
                    colorPicker.setColorSelectedListener(new ColorPicker.ColorSelectedListener() {
                        @Override
                        public void onColorChoose(int color) {
                            mDrawingView.setDrawColor(color);
                        }
                    });
                    colorPicker.show();
                }
            }
        });
    }
}

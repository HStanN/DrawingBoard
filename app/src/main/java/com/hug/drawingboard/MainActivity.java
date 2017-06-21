package com.hug.drawingboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView mPen,mEraser,mColorPicker,mUndo,mRedo;
    private DrawingView  mDrawingView;
    private ColorPicker colorPicker;
    private SeekBar mStrokeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPen = (ImageView) findViewById(R.id.pen);
        mEraser = (ImageView) findViewById(R.id.eraser);
        mColorPicker= (ImageView) findViewById(R.id.color_picker);
        mDrawingView = (DrawingView) findViewById(R.id.drawingview);
        mStrokeBar = (SeekBar) findViewById(R.id.stroke);
        mUndo = (ImageView) findViewById(R.id.undo);
        mRedo = (ImageView) findViewById(R.id.redo);
        mStrokeBar.setVisibility(View.VISIBLE);
        mPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStrokeBar.setVisibility(View.VISIBLE);
                mDrawingView.setMode(DrawingView.MODE_PEN);
            }
        });
        mEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStrokeBar.setVisibility(View.GONE);
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
        mStrokeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDrawingView.setStroke((float)progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.undo();
            }
        });
        mRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.redo();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mDrawingView.clear();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}

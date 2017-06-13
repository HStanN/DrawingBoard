package com.hug.drawingboard;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by HStan on 2017/6/13.
 */

public class ColorPicker {
    private Context context;
    private  AlertDialog.Builder builder;
    private AlertDialog dialog;
    private SeekBar r,g,b;
    private View show;
    private int iR,iG,iB;
    private ColorSelectedListener colorSelectedListener;

    public interface ColorSelectedListener{
        void onColorChoose(int color);
    }

    public ColorPicker(Context context){
        this.context =context;
        iR = 255;
        iG = 255;
        iB = 255;
    }

    public void setColorSelectedListener(ColorSelectedListener colorSelectedListener){
        this.colorSelectedListener = colorSelectedListener;
    }

    public void show(){
        builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.color_picker_view);
        builder.setTitle("");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (colorSelectedListener != null){
                    colorSelectedListener.onColorChoose(Color.rgb(iR,iG,iB));
                }
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        ProcessDialog processDialog = new ProcessDialog();
        processDialog.processDialog(dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        r = (SeekBar)dialog.findViewById(R.id.r_seek);
        g = (SeekBar)dialog.findViewById(R.id.g_seek);
        b = (SeekBar)dialog.findViewById(R.id.b_seek);
        show = dialog.findViewById(R.id.color_shower);
        r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iR = progress;
                show.setBackgroundColor(Color.rgb(iR,iG,iB));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iG = progress;
                show.setBackgroundColor(Color.rgb(iR,iG,iB));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iB = progress;
                show.setBackgroundColor(Color.rgb(iR,iG,iB));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void dismiss(){
        if (dialog != null){
            dialog.dismiss();
        }
    }
}

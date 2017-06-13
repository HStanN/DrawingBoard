package com.hug.drawingboard;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 *@title        ProcessDialog
 *@descript		处理dialog类
 */
public class ProcessDialog {
    public void processDialog(AlertDialog dialog){
        if(dialog == null)
            return;
        try
        {
            Field field  =  dialog.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);
            //获得mAlert变量的值
            Object obj  =  field.get(dialog);
            field  =  obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);
            //修改mHandler变量的值，使用新的ButtonHandler类
            field.set(obj, new ButtonHandler(dialog));
        }
        catch  (Exception e)
        {
        }
    }

    /***
     *
     *@title        ButtonHandler
     *@descript		dialog的Button消息处理类
     */
    class  ButtonHandler  extends Handler
    {

        private WeakReference< DialogInterface > mDialog;

        public  ButtonHandler(DialogInterface dialog)
        {
            mDialog  =   new  WeakReference < DialogInterface > (dialog);
        }

        @Override
        public   void  handleMessage(Message msg)
        {
            switch  (msg.what)
            {
                case  DialogInterface.BUTTON_POSITIVE:
                case  DialogInterface.BUTTON_NEGATIVE:
                case  DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog
                            .get(), msg.what);
                    break ;
            }
        }
    }
}

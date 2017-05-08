package com.dong.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by msi on 2016/5/17.
 */
public class DrawView extends View {
    private int view_width = 0;	//屏幕的宽度
    private int view_height = 0;	//屏幕的高度
    private float preX;	//起始点的x坐标值
    private float preY;//起始点的y坐标值
    private Path path;	//路径
    public Paint paint = null;	//画笔
    Bitmap cacheBitmap = null;// 定义一个内存中的图片，该图片将作为缓冲区
    Canvas cacheCanvas = null;// 定义cacheBitmap上的Canvas对象

    public DrawView(Context context, AttributeSet set) {
        super(context, set);
        view_width = context.getResources().getDisplayMetrics().widthPixels; // 获取屏幕的宽度
        view_height = context.getResources().getDisplayMetrics().heightPixels; // 获取屏幕的高度
        System.out.println(view_width + "*" + view_height);
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(view_width, view_height,
                Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();//路径
        cacheCanvas.setBitmap(cacheBitmap);// 在cacheCanvas上绘制cacheBitmap
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED); // 设置默认的画笔颜色
        // 设置画笔风格
        paint.setStyle(Paint.Style.STROKE);	//设置填充方式为描边
        paint.setStrokeJoin(Paint.Join.ROUND);		//设置笔刷的图形样式
        paint.setStrokeCap(Paint.Cap.ROUND);	//设置画笔转弯处的连接风格
        paint.setStrokeWidth(10); // 设置默认笔触的宽度为1像素
        paint.setAntiAlias(true); // 使用抗锯齿功能
        paint.setDither(true); // 使用抖动效果
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFFFFFF);	//设置背景颜色
        Paint bmpPaint = new Paint();	//采用默认设置创建一个画笔
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint); //绘制cacheBitmap
        canvas.drawPath(path, paint);	//绘制路径
        canvas.save(Canvas.ALL_SAVE_FLAG);	//保存canvas的状态
        canvas.restore();	//恢复canvas之前保存的状态，防止保存后对canvas执行的操作对后续的绘制有影响
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取触摸事件的发生位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://用户开始触摸
                path.moveTo(x, y); // 将绘图的起始点移到（x,y）坐标点的位置
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE://用户正在使用绘画
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx >= 5 || dy >= 5) { // 判断是否在允许的范围内
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);//根据参数绘制轨迹
                    preX = x;
                    preY = y;
                }
                break;
            case MotionEvent.ACTION_UP://用户抬起手指
                cacheCanvas.drawPath(path, paint); //绘制路径
                path.reset();//清空路径
                break;
        }
        invalidate();
        return true;		// 返回true表明处理方法已经处理该事件
    }


    public void clear() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setStrokeWidth(50);	//设置笔触的宽度
    }

    public void save() {
        try {
            Date date = new Date();
            String time = date.toString().substring(17, 19);
            saveBitmap(time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 保存绘制好的位图
    public void saveBitmap(String fileName) throws IOException {

        File file = new File("/sdcard/Pictures/" + fileName + ".jpg");	//创建文件对象
        file.createNewFile();	//创建一个新文件
        FileOutputStream fileOS = new FileOutputStream(file);	//创建一个文件输出流对象
        cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);	//将绘图内容压缩为PNG格式输出到输出流对象中
        fileOS.flush();	//将缓冲区中的数据全部写出到输出流中
        fileOS.close();	//关闭文件输出流对象

    }

}

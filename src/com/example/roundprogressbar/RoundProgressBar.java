package com.example.roundprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.example.circlepregress.R;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class RoundProgressBar extends View {

    public List<PieModel> pieModels;
    float totalCount = 0f;
    int start = 0;

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;


    /**
     * 圆环的宽度
     */
    private float roundWidth;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);

        mTypedArray.recycle();

        pieModels = new ArrayList<>();
        pieModels.add(new PieModel("#00ff00", 23));
        pieModels.add(new PieModel("#0000ff", 53));
        pieModels.add(new PieModel("#FECD44", 83));
        pieModels.add(new PieModel("#DE8CF9", 41));
        pieModels.add(new PieModel("#34C3BF", 53));
        pieModels.add(new PieModel("#05C76E", 23));

        if (pieModels != null) {
            for (int x = 0; x < pieModels.size(); x++) {
                totalCount += pieModels.get(x).getCount();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        start = 0;
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿

        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
        paint.setStyle(Paint.Style.STROKE);

        for (int x = 0; x < pieModels.size(); x++) {
            paint.setColor(Color.parseColor(pieModels.get(x).getColor()));  //设置进度的颜色
            float b = (pieModels.get(x).getCount() / totalCount) * 100 * 3.6f;
            Log.i("zzzzz", "b  = " + b + "   start = " + start + "    start + b = " + (start + b));
            if (x == pieModels.size() - 1) {
                canvas.drawArc(oval, start, 360 - start, false, paint);  //根据进度画圆弧
            } else {
                canvas.drawArc(oval, start, b, false, paint);  //根据进度画圆弧
            }
            start += b;
        }
    }

    public List<PieModel> getPieModels() {
        return pieModels;
    }

    public void setPieModels(List<PieModel> pieModels) {
        this.pieModels = pieModels;
        totalCount = 0;
        invalidate();
    }

    public class PieModel {

        public PieModel(String color, int count) {
            this.color = color;
            this.count = count;
        }

        String color = "";  //颜色
        int count = 0;   //数量

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}

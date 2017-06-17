package com.example.roundprogressbar;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.example.circlepregress.R;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TubiaoView extends View {

    public List<AddAssetStatItem> datas;

    private Paint linePaint;  //线的配置
    private Paint textPaint;  //文本的配置
    private Paint xuXianPaint;  //虚线的配置
    private Paint backgroundPaint;//背景的配置

    private String topText = "金额（万元）";
    private String bottomRigText = "月份";

    private int topTextHeight = 0;
    private int maxAmount = 0;
    private int bottomRigTextWidth = 0;

    private int rowLineCount = 4;
    private int bottomHeight = 0;

    private int leftTextMaxWidth = 0;//左边字体最大的宽度

    public int[] amountPos = new int[rowLineCount + 1];

    public TubiaoView(Context context) {
        this(context, null);
    }

    public TubiaoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TubiaoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

//        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
//                R.styleable.RoundProgressBar);
//        //获取自定义属性和默认值
//        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
//        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);

//        mTypedArray.recycle();

        datas = new ArrayList<>();
        datas.add(new AddAssetStatItem(25, 1, 24));
        datas.add(new AddAssetStatItem(45, 2, 24));
        datas.add(new AddAssetStatItem(65, 3, 24));
        datas.add(new AddAssetStatItem(23, 4, 24));
        datas.add(new AddAssetStatItem(37, 5, 24));
        datas.add(new AddAssetStatItem(29, 6, 24));
        datas.add(new AddAssetStatItem(19, 7, 24));
        datas.add(new AddAssetStatItem(22, 8, 24));
        datas.add(new AddAssetStatItem(56, 9, 24));
        datas.add(new AddAssetStatItem(25, 10, 24));
        datas.add(new AddAssetStatItem(13, 11, 24));
        datas.add(new AddAssetStatItem(96, 12, 24));

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        linePaint = new Paint();
        textPaint = new Paint();
        xuXianPaint = new Paint();
        backgroundPaint = new Paint();

        linePaint.setColor(Color.RED); //设置圆环的颜色
        linePaint.setStyle(Paint.Style.FILL); //设置空心
        linePaint.setStrokeWidth(1); //设置圆环的宽度
        linePaint.setAntiAlias(true);  //消除锯齿

        textPaint.setColor(Color.BLACK); //设置圆环的颜色
        textPaint.setStyle(Paint.Style.FILL); //设置空心
        textPaint.setStrokeWidth(1); //设置圆环的宽度
        textPaint.setAntiAlias(true);  //消除锯齿
        textPaint.setTextSize(DisplayUtil.spToPixels(getContext(), 14));

        xuXianPaint.setColor(Color.BLACK); //设置圆环的颜色
        xuXianPaint.setStyle(Paint.Style.STROKE); //设置空心
        xuXianPaint.setAntiAlias(true);  //消除锯齿
        xuXianPaint.setStrokeWidth(2);
        xuXianPaint.setPathEffect(new DashPathEffect(new float[]{4, 4, 4, 4}, 1));

        backgroundPaint.setColor(getContext().getResources().getColor(R.color.white)); //设置圆环的颜色
        backgroundPaint.setStyle(Paint.Style.FILL); //设置空心
        backgroundPaint.setAntiAlias(true);  //消除锯齿
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Rect bounds = new Rect();
        textPaint.getTextBounds(topText, 0, topText.length(), bounds);
        topTextHeight = bounds.height();  //计算顶部文字高度
        maxAmount = 0;
        if (datas != null) {
            for (int x = 0; x < datas.size(); x++) {
                if (datas.get(x).amount > maxAmount) {
                    maxAmount = datas.get(x).amount;
                }
            }
            int itemh = getRowItemHeight(maxAmount);
            for (int x = rowLineCount; x >= 0; x--) {
                String str = (rowLineCount - x) * itemh + "";
                leftTextMaxWidth = getTextWidth(textPaint, str);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), backgroundPaint);
        /**
         * 绘制顶部文字
         */
        canvas.drawText(topText, 0, getBaseLine(topText, textPaint), textPaint);
        /**
         * 绘制底部文字
         */

        Rect bottomTextRec = new Rect();
        textPaint.getTextBounds(bottomRigText, 0, bottomRigText.length(), bottomTextRec);
        canvas.drawText(bottomRigText,
                getMeasuredWidth() - getTextWidth(textPaint, bottomRigText),
                getMeasuredHeight() - bottomTextRec.height() + Math.abs(bottomTextRec.top), textPaint);
//
//        Log.i("zzzzzz", "bounds top = " + bounds.top);
//        Log.i("zzzzzz", "bounds left = " + bounds.left);
//        Log.i("zzzzzz", "bounds right = " + bounds.right);
//        Log.i("zzzzzz", "bounds bottom = " + bounds.bottom);

        /***
         * 绘制左边每行虚线
         */
        int t = topTextHeight + topTextHeight / 2;
        int b = getMeasuredHeight() - topTextHeight - topTextHeight / 2;
        int itemHeight = (b - t) / rowLineCount;
        for (int x = 0; x < rowLineCount + 1; x++) {

            Path path = new Path();
            int left = 0;
            int top = x * itemHeight + t;
            int right = getMeasuredWidth();
            int bottom = x * itemHeight + t;

            path.moveTo(left, top);
            path.lineTo(right, bottom);
            canvas.drawPath(path, xuXianPaint);
            amountPos[x] = top;
        }

        canvas.drawRect(0, topTextHeight, leftTextMaxWidth, getMeasuredHeight(), backgroundPaint);
        /***
         * 绘制左边每行金额
         */

        int itemh = getRowItemHeight(maxAmount);

        for (int x = rowLineCount; x >= 0; x--) {
            int p = amountPos[x];

            String str = (rowLineCount - x) * itemh + "";
            Rect bounds = new Rect();
            textPaint.getTextBounds(str, 0, str.length(), bounds);
            canvas.drawText(str, (leftTextMaxWidth - getTextWidth(textPaint, str)) / 2, p + (bounds.height() / 2) - bounds.bottom, textPaint);
        }

        /**
         * 绘制底部1-12月份
         */
    }

    public int getRowItemHeight(int account) {
        int gewei = account % 10;
        int temp = 10 - gewei;
        if (temp != 0) {
            account += temp;
        }
        int itemh = account / (rowLineCount);
        int g = itemh % 10;
        int a = 10 - g;
        if (a != 0) {
            itemh += a;
        }
        return itemh;
    }

    private int getBaseLine(String str, Paint p) {
        Rect bounds = new Rect();
        p.getTextBounds(str, 0, str.length(), bounds);
        int baseline = Math.abs(bounds.top);
        return baseline;
    }

    private int getBaseLine(int height, Paint p) {
        Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
        int baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        return baseline;
    }


    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public static class AddAssetStatItem {
        private int amount;  //总额
        private int month;  //月份
        private int num;  //数量


        public AddAssetStatItem(int amount, int month, int num) {
            this.amount = amount;
            this.month = month;
            this.num = num;
        }


        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }


}

package com.example.roundprogressbar;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TubiaoView extends View {

    public List<AddAssetStatItem> datas;

    private Paint textPaint;  //文本的配置
    private Paint xuXianPaint;  //虚线的配置
    private Paint brokenLinePaint; //折线的配置

    private String topText = "金额（万元）";
    private String bottomRigText = "月份";

    private int topTextHeight = 0;

    private int arrayMaxAmount = 0;  //数组中最大的金额值
    private int drawMaxAmount = 0;  //界面中绘制的最大值

    private int bottomRigTextWidth = 0;

    private int rowLineCount = 4;
    private int bottomHeight = 0;

    private int leftTextMaxWidth = 0;//左边字体最大的宽度

    private int radius;//折线图圆的半径

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
        datas.add(new AddAssetStatItem(0, 1, 24));
        datas.add(new AddAssetStatItem(45, 2, 24));
        datas.add(new AddAssetStatItem(0, 3, 24));
        datas.add(new AddAssetStatItem(0, 4, 24));
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

        radius = DisplayUtil.dipToPixels(getContext(), 4);
        textPaint = new Paint();
        xuXianPaint = new Paint();
        brokenLinePaint = new Paint();

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

        brokenLinePaint.setColor(Color.BLUE); //设置圆环的颜色
        brokenLinePaint.setStyle(Paint.Style.FILL); //设置空心
        brokenLinePaint.setStrokeWidth(2); //设置圆环的宽度
        brokenLinePaint.setAntiAlias(true);  //消除锯齿
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Rect bounds = new Rect();
        textPaint.getTextBounds(topText, 0, topText.length(), bounds);
        topTextHeight = bounds.height();  //计算顶部文字高度
        arrayMaxAmount = 0;
        if (datas != null) {
            for (int x = 0; x < datas.size(); x++) {
                if (datas.get(x).amount > arrayMaxAmount) {
                    arrayMaxAmount = datas.get(x).amount;
                }
            }
            int itemh = getRowItemHeight(arrayMaxAmount);
            for (int x = rowLineCount; x >= 0; x--) {
                String str = (rowLineCount - x) * itemh + "";
                leftTextMaxWidth = getTextWidth(textPaint, str);
            }
        }

        bottomRigTextWidth = getTextWidth(textPaint, bottomRigText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 绘制顶部文字
         */

        Rect topBounds = new Rect();
        textPaint.getTextBounds(topText, 0, topText.length(), topBounds);
        int topBaseline = Math.abs(topBounds.top);

        canvas.drawText(topText, 0, topBaseline, textPaint);
        /**
         * 绘制底部文字
         */

        Rect bottomTextRec = new Rect();
        textPaint.getTextBounds(bottomRigText, 0, bottomRigText.length(), bottomTextRec);
        canvas.drawText(bottomRigText,
                getMeasuredWidth() - getTextWidth(textPaint, bottomRigText),
                getMeasuredHeight() - bottomTextRec.bottom, textPaint);

        /***
         * 绘制左边每行虚线
         */
        int topDottedLine = topTextHeight + topTextHeight / 2;  //顶部虚线位置

        int bottomDottedLine = getMeasuredHeight() - topDottedLine;  //底部虚线位置
        int itemHeight = (bottomDottedLine - topDottedLine) / rowLineCount;
        for (int x = 0; x < rowLineCount + 1; x++) {

            Path path = new Path();
            int left = leftTextMaxWidth;
            int top = x * itemHeight + topDottedLine;
            int right = getMeasuredWidth();
            int bottom = x * itemHeight + topDottedLine;

            path.moveTo(left, top);
            path.lineTo(right, bottom);
            canvas.drawPath(path, xuXianPaint);
            amountPos[x] = top;
        }

        /***
         * 绘制左边每行金额
         */

        int itemh = getRowItemHeight(arrayMaxAmount);

        drawMaxAmount = rowLineCount * itemh;
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

        if (datas != null && datas.size() > 0) {
            int num = datas.size();
            float distance = getMeasuredWidth() - bottomRigTextWidth - leftTextMaxWidth;//计算出底部12月份的总宽度
            float temp = distance / num;  //计算出每个宽度

            float saveX = 0, saveY = 0; //绘制线 保存上一个点的坐标
            for (int x = 0; x < datas.size(); x++) {
                float leftX = leftTextMaxWidth + (temp / 2) + (temp * x);  //每行月份字体的中心线位置

                /**
                 * 绘制月份
                 */
                String moth = String.valueOf(datas.get(x).month);
                int mothW = getTextWidth(textPaint, moth);
                Rect mothBound = new Rect();
                textPaint.getTextBounds(moth, 0, moth.length(), mothBound);
                canvas.drawText(moth, leftX - mothW / 2, getMeasuredHeight() - mothBound.bottom, textPaint);

                /***
                 * 绘制折线  ######################################################
                 */

                /**
                 * 绘制点
                 */
                float currentAmount = datas.get(x).amount;
                float topY = (bottomDottedLine - topDottedLine) - (currentAmount / drawMaxAmount * (bottomDottedLine - topDottedLine));

                canvas.drawCircle(leftX, topDottedLine + topY, radius, brokenLinePaint);

                /**
                 * 绘制线
                 */
                if (x > 0) {
                    canvas.drawLine(saveX, saveY, leftX, topDottedLine + topY, brokenLinePaint);
                }
                saveX = leftX;
                saveY = topDottedLine + topY;

                /***
                 * 绘制折线  ######################################################
                 */
            }
        }

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

package com.xulaoyao.ezninegridimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xulaoyao.ezninegridimage.widget.EzNineGridImageViewComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.初始化 EzNineGridImageView 的图片加载器 ImageLoaderListener
 * 2.在自己的 Adapter 中初始化 EzNineGridImageView 的适配器
 * <code>
 * holder.ezNineGrid.setAdapter(new EzNineGridImageViewAdapter(context, imageInfoList));
 * </code>
 * EzNineGridImageView
 * Created by renwoxing on 2017/12/18.
 */
public class EzNineGridImageView extends ViewGroup {


    private static ImageLoaderListener mImageLoaderListener;        //全局的图片加载器(必须设置,否者不显示图片)

    private int singleImageSize = 250;              // 单张图片时的最大大小,单位dp
    private float singleImageRatio = 1.0f;          // 单张图片的宽高比(宽/高)
    private int maxImageSize = 9;                   // 最大显示的图片数
    private int gridSpacing = 3;                    // 宫格间距，单位dp


    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度

    private List<ImageView> imageViews;
    private List<EzImage> mImageInfo;
    private EzNineGridImageViewAdapter mAdapter;

    public EzNineGridImageView(Context context) {
        this(context, null);
    }

    public EzNineGridImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EzNineGridImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        gridSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gridSpacing, dm);
        singleImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, singleImageSize, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EzNineGridImageView);
        gridSpacing = (int) a.getDimension(R.styleable.EzNineGridImageView_ez_ngiv_gridSpacing, gridSpacing);
        singleImageSize = a.getDimensionPixelSize(R.styleable.EzNineGridImageView_ez_ngiv_singleImageSize, singleImageSize);
        singleImageRatio = a.getFloat(R.styleable.EzNineGridImageView_ez_ngiv_singleImageRatio, singleImageRatio);
        maxImageSize = a.getInt(R.styleable.EzNineGridImageView_ez_ngiv_maxSize, maxImageSize);

        a.recycle();

        imageViews = new ArrayList<>();
    }

    /**
     * view 大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (mImageInfo != null && mImageInfo.size() > 0) {
            if (mImageInfo.size() == 1) {   //单张的时候 长宽
                EzImage ezImage = mImageInfo.get(0);
                gridWidth = singleImageSize > totalWidth ? totalWidth : singleImageSize;
                if (ezImage.getImageViewHeight() > ezImage.getImageViewWidth()) {
                    gridHeight = ezImage.getImageViewHeight();
                    if (ezImage.getImageViewWidth() > singleImageSize) {
                        float ratio = singleImageSize * 1.0f / ezImage.getImageViewWidth();
                        gridWidth = (int) (ezImage.getImageViewWidth() * ratio);
                        gridHeight = (int) (ezImage.getImageViewHeight() * ratio);
                    } else {
                        gridWidth = ezImage.getImageViewWidth();
                        gridHeight = ezImage.getImageViewHeight();
                    }
                } else {
                    gridHeight = (int) (gridWidth / singleImageRatio);
                    //矫正图片显示区域大小，不允许超过最大显示范围
                    if (gridHeight > singleImageSize) {
                        float ratio = singleImageSize * 1.0f / gridHeight;
                        gridWidth = (int) (gridWidth * ratio);
                        gridHeight = singleImageSize;
                    }
                }
            } else {
//                gridWidth = gridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
                //这里无论是几张图片，宽高都按总宽度的 1/3
                gridWidth = gridHeight = (totalWidth - gridSpacing * 2) / 3;
            }
            width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
            height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 定位
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mImageInfo == null) return;
        int childrenCount = mImageInfo.size();
        for (int i = 0; i < childrenCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);

            int rowNum = i / columnCount;
            int columnNum = i % columnCount;
            int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
            int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;
            childrenView.layout(left, top, right, bottom);

            /**
             * 调用处部图片加载器
             */
            if (mImageLoaderListener != null) {
                mImageLoaderListener.onShowImage(getContext(), childrenView, mImageInfo.get(i).thumbnailUrl);
            }
        }
    }

    /**
     * 设置适配器
     */
    public void setAdapter(@NonNull EzNineGridImageViewAdapter adapter) {
        mAdapter = adapter;
        List<EzImage> imageInfo = adapter.getImageInfoList();

        if (imageInfo == null || imageInfo.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        int imageCount = imageInfo.size();
        if (maxImageSize > 0 && imageCount > maxImageSize) {
            imageInfo = imageInfo.subList(0, maxImageSize);
            imageCount = imageInfo.size();   //再次获取图片数量
        }

        //默认是3列显示，行数根据图片的数量决定
        //rowCount = imageCount / 3 + (imageCount % 3 == 0 ? 0 : 1);
        //columnCount = 3;
        //grid模式下，显示4张使用2X2模式
//        if (mode == MODE_GRID) {
//            if (imageCount == 4) {
//                rowCount = 2;
//                columnCount = 2;
//            }
//        }
        initChildrenLayout(imageCount);


        //保证View的复用，避免重复创建
        if (mImageInfo == null) {
            for (int i = 0; i < imageCount; i++) {
                ImageView iv = getImageView(i);
                if (iv == null) return;
                addView(iv, generateDefaultLayoutParams());
            }
        } else {
            int oldViewCount = mImageInfo.size();
            int newViewCount = imageCount;
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = oldViewCount; i < newViewCount; i++) {
                    ImageView iv = getImageView(i);
                    if (iv == null) return;
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        //修改最后一个条目，决定是否显示更多
        if (adapter.getImageInfoList().size() > maxImageSize) {
            View child = getChildAt(maxImageSize - 1);
            if (child instanceof EzNineGridImageViewComponent) {
                EzNineGridImageViewComponent imageView = (EzNineGridImageViewComponent) child;
                imageView.setMoreNum(adapter.getImageInfoList().size() - maxImageSize);
            }
        }
        mImageInfo = imageInfo;
        requestLayout();
    }

    /**
     * 获得 ImageView 保证了 ImageView 的重用
     */
    private ImageView getImageView(final int position) {
        ImageView imageView;
        if (position < imageViews.size()) {
            imageView = imageViews.get(position);
        } else {
            imageView = mAdapter.createImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.onImageItemClick(getContext(), EzNineGridImageView.this, position, mAdapter.getImageInfoList());
                }
            });
            imageViews.add(imageView);
        }
        return imageView;
    }

    /**
     * 设置宫格间距
     */
    public void setGridSpacing(int spacing) {
        gridSpacing = spacing;
    }

    /**
     * 设置只有一张图片时的宽
     */
    public void setSingleImageSize(int maxImageSize) {
        singleImageSize = maxImageSize;
    }

    /**
     * 设置只有一张图片时的宽高比
     */
    public void setSingleImageRatio(float ratio) {
        singleImageRatio = ratio;
    }

    /**
     * 设置最大图片数
     */
    public void setMaxSize(int maxSize) {
        maxImageSize = maxSize;
    }

    public int getMaxSize() {
        return maxImageSize;
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num        row        column
     * 1           1        1
     * 2           1        2
     * 3           1        3
     * 4           2        2
     * 5           2        3
     * 6           2        3
     * 7           3        3
     * 8           3        3
     * 9           3        3
     *
     * @param num
     */
    private void initChildrenLayout(int num) {
        rowCount = num / 3 + (num % 3 == 0 ? 0 : 1);
        if (num <= 3) {
            columnCount = num;
        } else if (num == 4) {
            columnCount = 2;
        } else {
            columnCount = 3;
        }
    }


    public static void setImageLoaderListener(ImageLoaderListener imageLoader) {
        mImageLoaderListener = imageLoader;
    }

    public static ImageLoaderListener getImageLoaderListener() {
        return mImageLoaderListener;
    }

    /**
     * 自定义图片显示接口
     */
    public interface ImageLoaderListener {
        /**
         * 需要子类实现该方法，以确定如何加载和显示图片
         *
         * @param context   上下文
         * @param imageView 需要展示图片的ImageView
         * @param url       图片地址
         */
        void onShowImage(Context context, ImageView imageView, String url);

    }


}

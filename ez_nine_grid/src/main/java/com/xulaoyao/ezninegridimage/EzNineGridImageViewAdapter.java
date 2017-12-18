package com.xulaoyao.ezninegridimage;

import android.content.Context;
import android.widget.ImageView;

import com.xulaoyao.ezninegridimage.widget.EzNineGridImageViewComponent;

import java.io.Serializable;
import java.util.List;

/**
 * EzNineGridImageViewAdapter
 * Created by renwoxing on 2017/12/18.
 */
public class EzNineGridImageViewAdapter implements Serializable {

    protected Context mContext;
    private List<EzImage> imageInfo;

    public EzNineGridImageViewAdapter(Context context, List<EzImage> imageInfo) {
        this.mContext = context;
        this.imageInfo = imageInfo;
    }

    /**
     * 如果要实现图片点击的逻辑，重写此方法即可
     *
     * @param context      上下文
     * @param nineGridView 九宫格控件
     * @param index        当前点击图片的的索引
     * @param imageInfo    图片地址的数据集合
     */
    protected void onImageItemClick(Context context, EzNineGridImageView nineGridView, int index, List<EzImage> imageInfo) {
    }

    /**
     * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
     * 如果需要自定义图片展示效果，重写此方法即可
     *
     * @param context 上下文
     * @return 生成的 ImageView
     */
    protected ImageView generateImageView(Context context) {
        EzNineGridImageViewComponent imageView = new EzNineGridImageViewComponent(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.ic_default_color);
        return imageView;
    }

    public List<EzImage> getImageInfo() {
        return imageInfo;
    }

    public void setImageInfoList(List<EzImage> imageInfo) {
        this.imageInfo = imageInfo;
    }
}

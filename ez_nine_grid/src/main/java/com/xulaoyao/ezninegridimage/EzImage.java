package com.xulaoyao.ezninegridimage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片实体对象
 * EzImage
 * Created by renwoxing on 2017/12/18.
 */
public class EzImage implements Parcelable {

    /**
     * 缩略图
     */
    public String thumbnailUrl;
    /**
     * 原图
     */
    public String imageUrl;
    public int imageViewHeight;
    public int imageViewWidth;
    public int imageViewX;
    public int imageViewY;

    public EzImage() {
    }


    public EzImage(String imageUrl) {
        this.imageUrl = imageUrl;
        this.thumbnailUrl = imageUrl;
    }

    public EzImage(String imageUrl, int imageViewHeight, int imageViewWidth) {
        this.imageUrl = imageUrl;
        this.thumbnailUrl = imageUrl;
        this.imageViewHeight = imageViewHeight;
        this.imageViewWidth = imageViewWidth;
    }

    protected EzImage(Parcel in) {
        thumbnailUrl = in.readString();
        imageUrl = in.readString();
        imageViewHeight = in.readInt();
        imageViewWidth = in.readInt();
        imageViewX = in.readInt();
        imageViewY = in.readInt();
    }

    public static final Creator<EzImage> CREATOR = new Creator<EzImage>() {
        @Override
        public EzImage createFromParcel(Parcel in) {
            return new EzImage(in);
        }

        @Override
        public EzImage[] newArray(int size) {
            return new EzImage[size];
        }
    };

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageViewHeight() {
        return imageViewHeight;
    }

    public void setImageViewHeight(int imageViewHeight) {
        this.imageViewHeight = imageViewHeight;
    }

    public int getImageViewWidth() {
        return imageViewWidth;
    }

    public void setImageViewWidth(int imageViewWidth) {
        this.imageViewWidth = imageViewWidth;
    }

    public int getImageViewX() {
        return imageViewX;
    }

    public void setImageViewX(int imageViewX) {
        this.imageViewX = imageViewX;
    }

    public int getImageViewY() {
        return imageViewY;
    }

    public void setImageViewY(int imageViewY) {
        this.imageViewY = imageViewY;
    }

    @Override
    public String toString() {
        return "EzImage{" +
                "thumbnailUrl='" + thumbnailUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageViewHeight=" + imageViewHeight +
                ", imageViewWidth=" + imageViewWidth +
                ", imageViewX=" + imageViewX +
                ", imageViewY=" + imageViewY +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thumbnailUrl);
        parcel.writeString(imageUrl);
        parcel.writeInt(imageViewHeight);
        parcel.writeInt(imageViewWidth);
        parcel.writeInt(imageViewX);
        parcel.writeInt(imageViewY);
    }
}

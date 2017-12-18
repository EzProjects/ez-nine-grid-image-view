package com.xulaoyao.ezninegridimageview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xulaoyao.ezninegridimage.EzImage;
import com.xulaoyao.ezninegridimage.EzNineGridImageView;
import com.xulaoyao.ezninegridimage.EzNineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    // init image data
    private String[][] images = new String[][]{{"https://cdn.pixabay.com/photo/2016/06/21/19/57/sommerfest-1471809_960_720.jpg", "720", "960"}
            , {"https://cdn.pixabay.com/photo/2017/12/03/17/23/fantasy-2995326_960_720.jpg", "720", "960"}
            , {"http://7xi8d6.com1.z0.glb.clouddn.com/20171212083612_WvLcTr_Screenshot.jpeg", "840", "1000"}
            , {"https://cdn.pixabay.com/photo/2017/12/12/14/20/ballet-3014754_960_720.jpg", "640", "640"}
            , {"https://cdn.pixabay.com/photo/2012/04/24/13/22/giraffe-40035_960_720.png", "720", "360"}
            , {"https://cdn.pixabay.com/photo/2017/12/11/22/42/peacock-feathers-3013486_960_720.jpg", "640", "640"}
            , {"https://cdn.pixabay.com/photo/2017/06/04/21/20/peacock-2372211_960_720.png", "640", "640"}
            , {"https://cdn.pixabay.com/photo/2017/03/12/13/41/beaded-2137080_960_720.jpg", "640", "640"}
            , {"http://img2.imgtn.bdimg.com/it/u=3347259689,1828160575&fm=21&gp=0.jpg", "640", "640"}
            , {"http://img1.imgtn.bdimg.com/it/u=3607821315,1190508392&fm=21&gp=0.jpg", "640", "640"}
            , {"http://img4.imgtn.bdimg.com/it/u=2495945657,2561148855&fm=21&gp=0.jpg", "640", "640"}
            , {"https://cdn.pixabay.com/photo/2012/04/24/13/22/giraffe-40035_960_720.png", "800", "650"}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //1. set ImageLoader
        EzNineGridImageView.setImageLoaderListener(new GlideImageLoader());


        RecyclerView rvEzNineGridView = findViewById(R.id.rv_ez_demo_nine_image_view_list);
        rvEzNineGridView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<List<EzImage>> imagesList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            ArrayList<EzImage> itemList = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                itemList.add(new EzImage(images[j][0], Integer.valueOf(images[j][1]), Integer.valueOf(images[j][2])));
            }
            imagesList.add(itemList);
        }
        rvEzNineGridView.setAdapter(new EzNineGridViewDemoAdapter(this, imagesList));
    }


    public class EzNineGridViewDemoAdapter extends RecyclerView.Adapter<EzNineGridViewDemoAdapter.ViewHolder> {

        private List<List<EzImage>> datas;
        private Context mContext;

        public EzNineGridViewDemoAdapter(Context context, List<List<EzImage>> datas) {
            this.datas = datas;
            this.mContext = context;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nine_grid_image_view_demo_list_item, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.mTextView.setText(String.valueOf(position));
            //2. set EzNineGridImageViewAdapter
            viewHolder.ezNineGridImageView.setAdapter(new EzNineGridImageViewAdapter(mContext, datas.get(position)));
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public EzNineGridImageView ezNineGridImageView;

            public ViewHolder(View view) {
                super(view);
                mTextView = (TextView) view.findViewById(R.id.tv_content);
                ezNineGridImageView = view.findViewById(R.id.ez_nineGrid);
            }
        }

    }


    // init ImageLoaderListener
    private class GlideImageLoader implements EzNineGridImageView.ImageLoaderListener {

        @Override
        public void onShowImage(Context context, ImageView imageView, String url) {
            RequestOptions ro = new RequestOptions();
            ro.placeholder(R.drawable.ic_default_color);
            ro.error(R.drawable.ic_default_color);
            ro.diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    .load(url)
                    .apply(ro)
                    .into(imageView);
        }

    }
}

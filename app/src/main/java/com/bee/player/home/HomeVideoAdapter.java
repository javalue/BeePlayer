package com.bee.player.home;

import android.widget.ImageView;
import android.widget.TextView;

import com.bee.player.MediaItem;
import com.bee.player.R;
import com.bee.player.base.NetImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class HomeVideoAdapter extends BaseQuickAdapter<MediaItem, BaseViewHolder> {

    public HomeVideoAdapter(List<MediaItem> data) {
        super(R.layout.home_video_item_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaItem item) {

        ImageView imageView = helper.getView(R.id.im_video_thumb);
        NetImageLoader.load(imageView.getContext(), item.path, imageView);

        helper.setText(R.id.tv_video_title, item.title);

    }
}

package com.bee.player.home;

import android.widget.ImageView;

import com.bee.player.play.MediaItem;
import com.bee.player.R;
import com.bee.player.base.NetImageLoader;
import com.bee.player.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.List;

public class HomeVideoAdapter extends BaseQuickAdapter<MediaItem, BaseViewHolder> {

    public HomeVideoAdapter(List<MediaItem> data) {
        super(R.layout.home_video_item_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaItem item) {

        ImageView imageView = helper.getView(R.id.im_video_thumb);
        NetImageLoader.load(imageView.getContext(), item.path, imageView);

        helper.setText(R.id.tv_video_title, item.displayName);

        helper.setText(R.id.tv_video_duration, TimeUtils.toDuration(item.duration));

        File file = new File(item.path);
        if (file.exists() && file.isFile()) {
            helper.setText(R.id.tv_video_size, String .format("%.2f", file.length() * 1.0f / 1024 / 1024) + "MB");
        }

    }
}

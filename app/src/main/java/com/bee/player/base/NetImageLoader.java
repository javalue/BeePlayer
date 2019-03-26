package com.bee.player.base;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class NetImageLoader {

    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}

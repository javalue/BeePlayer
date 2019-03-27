package com.bee.player.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

public class ShareUtils {

    public static Uri getUri(Context context, String path) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getVideoContentUri(context, new File(path));
        } else {
            fileUri = Uri.fromFile(new File(path));
        }
        return fileUri;
    }

    private static Uri getVideoContentUri(Context context, File file) {
        String filePath = file.getAbsolutePath();
        Cursor cursor =
                context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA +
                                "=?",
                        new String[]{filePath}, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Uri baseUri = Uri.parse("content://media/external/video/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }
            cursor.close();
        }

        //如果使用fileProvider获取失败，则使用此方法
        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , values);
        }

        if (uri == null) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        }
        return uri;
    }

}

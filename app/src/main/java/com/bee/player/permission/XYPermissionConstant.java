package com.bee.player.permission;

import android.Manifest;

public class XYPermissionConstant {
    public static final int RC_EXTERNAL_STRORAGE_PERM = 123;

    public static final String[] EXTERNAL_STRORAGE = new String[]{Manifest.permission
            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
}

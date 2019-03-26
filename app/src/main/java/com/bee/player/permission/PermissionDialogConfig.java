package com.bee.player.permission;

import android.os.Bundle;

public class PermissionDialogConfig {

    String[] perms;
    int requestCode;
    String tag;
    int from;

    public PermissionDialogConfig(String[] perms, int requestCode, String tag, int from) {
        this.perms = perms;
        this.requestCode = requestCode;
        this.tag = tag;
        this.from = from;
    }

    public PermissionDialogConfig(Bundle arguments) {
        this.perms = arguments.getStringArray(KEY_PERMISSIONS);
        this.requestCode = arguments.getInt(KEY_REQUEST_CODE);
        this.tag = arguments.getString(KEY_REQUEST_TAG);
        this.from = arguments.getInt(KEY_FROM);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_PERMISSIONS, perms);
        bundle.putInt(KEY_REQUEST_CODE, requestCode);
        bundle.putString(KEY_REQUEST_TAG, tag);
        bundle.putInt(KEY_FROM, from);
        return bundle;
    }

    private String KEY_PERMISSIONS = "key_permissions";
    private String KEY_REQUEST_CODE = "key_request_code";
    private String KEY_REQUEST_TAG = "key_request_tag";
    private String KEY_FROM = "key_from";
}

package com.bee.player.permission;

import java.io.Serializable;


/**
 * 重新创建文件夹，重新赋值静态变量等等
 *
 * @author tong.xu
 */


public class PermissionChangeEvent implements Serializable {
    /**
     * 权限的requestCode（RC）
     */
    public int type;

    public static PermissionChangeEvent newUpdateInstance(int type) {
        return new PermissionChangeEvent(type);
    }

    private PermissionChangeEvent(int type) {
        this.type = type;
    }
}

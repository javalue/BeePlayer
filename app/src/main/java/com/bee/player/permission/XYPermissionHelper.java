package com.bee.player.permission;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;


import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class XYPermissionHelper {

    public static class Builder {
        FragmentActivity context;
        String mPos;
        String[] mPermissions;
        int mReqCode;

        public Builder(FragmentActivity context) {
            this.context = context;
        }

        public boolean checkPermissions(String rationale, OnPermissionResultListener listener) {
            if (hasPermission(mPermissions)) {
                return true;
            } else {
                if (!PermissionFragmentProxy.getInstance().isAdded()) {
                    context.getSupportFragmentManager().beginTransaction()
                            .add(PermissionFragmentProxy.getInstance(), "PermissionFragmentProxy")
                            .commitNowAllowingStateLoss();
                }
                PermissionFragmentProxy.getInstance().setResultListener(listener);
                PermissionFragmentProxy.getInstance().setPerms(mReqCode, mPermissions);
                PermissionFragmentProxy.getInstance().setPosition(mPos);
                PermissionFragmentProxy.getInstance().check(rationale);
                return false;
            }
        }

        public Builder setPermissions(int requestCode, @NonNull String[] permissions) {
            mReqCode = requestCode;
            mPermissions = permissions;
            return this;
        }

        /**
         * @param position 约等于from
         * @return
         */
        public Builder setPlaceTag(String position) {
            this.mPos = position;
            return this;
        }

        private boolean hasPermission(String[] permissions) {
            return EasyPermissions.hasPermissions(context, permissions);
        }
    }

    public static void onDestroy(){
        PermissionFragmentProxy.getInstance().clear();
    }


    public static class PermissionFragmentProxy extends Fragment implements EasyPermissions
            .PermissionCallbacks {

        OnPermissionResultListener onPermissionResultListener;
        String[] mPermissions;
        String position;
        int requestCode;

        static PermissionFragmentProxy INSTANCE = null;

        public static synchronized PermissionFragmentProxy getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new PermissionFragmentProxy();
            }
            return PermissionFragmentProxy.INSTANCE;
        }

        public void clear() {
            if (INSTANCE != null) {
                INSTANCE.onPermissionResultListener = null;
                INSTANCE = null;
            }
        }

        public void check(String rationale) {
            if (!TextUtils.isEmpty(rationale)) {
                EasyPermissions.requestPermissions(this, rationale, requestCode, mPermissions);
            } else {
                requestPermissions(mPermissions, requestCode);
            }
        }

        public void setResultListener(OnPermissionResultListener resultListener) {
            this.onPermissionResultListener = resultListener;
        }

        public void setPosition(String pos) {
            position = pos;
        }

        public void setPerms(int requestCode, String[] perms) {
            this.requestCode = requestCode;
            mPermissions = perms;
        }

        @Override
        public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
            if (mPermissions == null || mPermissions.length <= 0) {
                return;
            }
            /**
             * 请求多个权限，有允许，有拒绝，granted和denied都会有回调，所以要判断是否所有的权限都允许了，才回调成功
             */
            if (EasyPermissions.hasPermissions(getContext(), mPermissions)) {
                if (onPermissionResultListener != null) {
                    onPermissionResultListener.onPermissionsGranted(requestCode, perms);
                }
            }

            for (String permission : mPermissions) {
                /**
                 * 读写权限去重一个
                 */
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    continue;
                }
                /**
                 * 获取读操作权限时，统计用户是否是通过WhatsApp分享安装的APP
                 */
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //重新创建文件夹，重新赋值静态变量等等
//                    EventBusUtil.getGlobalBus().post(PermissionChangeEvent.newUpdateInstance(XYPermissionConstant.RC_EXTERNAL_STRORAGE_PERM));
                }
            }
        }

        @Override
        public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
            if (mPermissions == null || mPermissions.length <= 0) {
                return;
            }

            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                if (onPermissionResultListener != null) {
                    onPermissionResultListener.onNeverAsk();
                    return;
                }
            }

            if (onPermissionResultListener != null) {
                onPermissionResultListener.onPermissionsDenied(requestCode, perms);
            }
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            // EasyPermissions handles the request result.
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                    this);
        }
    }

    public interface OnPermissionResultListener {
        void onPermissionsGranted(int requestCode, @NonNull List<String> perms);

        void onPermissionsDenied(int requestCode, @NonNull List<String> perms);

        void onNeverAsk();
    }
}

package com.bee.player.permission;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.bee.player.R;
import com.bee.player.base.BaseFragment;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class XYPermissionProxyFragment extends BaseFragment {

    private OnRationalListener mOnRationalListener;
    private PermissionDialogConfig mConfig;

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_permission_proxy;
    }

    public static XYPermissionProxyFragment newInstance(PermissionDialogConfig config,
                                                        OnRationalListener listener) {
        XYPermissionProxyFragment fragment = new XYPermissionProxyFragment();
        fragment.setArguments(config.toBundle());
        fragment.setOnRationalListener(listener);
        return fragment;
    }

    private void setOnRationalListener(OnRationalListener listener) {
        mOnRationalListener = listener;
    }

    @Override
    protected void afterInject() {
        mConfig = new PermissionDialogConfig(getArguments());
        showRequsetDialog();
    }

    /**
     * 第一个请求权限的弹窗
     */
    private void showRequsetDialog() {
        XYPermissionRationaleDialog dialog =
                XYPermissionRationaleDialog.newInstance(new XYPermissionRationaleDialog.OnDialogListener() {
                    @Override
                    public void onAllow() {
                        requestPerms(mConfig);
                    }

                    @Override
                    public void onDenied() {
                        if (mOnRationalListener != null) {
                            mOnRationalListener.onPermissionsDenied(mConfig.requestCode, Arrays
                                    .asList(mConfig.perms));
                        }
                    }
                });
        try {
            dialog.showAllowingStateLoss(getChildFragmentManager(), mConfig.tag);
        }catch (IllegalStateException e){
            dialog.showAllowingStateLoss(getFragmentManager(), mConfig.tag);
        }
    }

    private void requestPerms(PermissionDialogConfig config) {
        new XYPermissionHelper.Builder(getActivity()).setPlaceTag(config.tag)
                .setPermissions(config.requestCode, config.perms).checkPermissions("",
                new XYPermissionHelper.OnPermissionResultListener() {
                    @Override
                    public void onPermissionsGranted(int requestCode, @NonNull
                            List<String> perms) {
                        if (mOnRationalListener != null) {
                            mOnRationalListener.onPermissionsGranted(requestCode, perms);
                        }
                        XYPermissionHelper.onDestroy();
                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, @NonNull
                            List<String> perms) {
                        if (mOnRationalListener != null) {
                            mOnRationalListener.onPermissionsDenied(requestCode, perms);
                        }
                        XYPermissionHelper.onDestroy();
                    }

                    @Override
                    public void onNeverAsk() {
                        showSettingDialog(mConfig);
                        XYPermissionHelper.onDestroy();
                    }
                });
    }

    /**
     * 第二个弹窗，不再提示的情况下，弹出setting Dialog
     */
    private void showSettingDialog(PermissionDialogConfig config) {
        XYPermissionSettingRationaleDialog dialog = XYPermissionSettingRationaleDialog.newInstance
                (config, mOnRationalListener);
        try {
            dialog.showAllowingStateLoss(getChildFragmentManager(), config.tag);
        }catch (IllegalStateException e){
            dialog.showAllowingStateLoss(getFragmentManager(), config.tag);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mConfig != null && requestCode == mConfig.requestCode) {
            if (EasyPermissions.hasPermissions(getContext(), mConfig.perms)) {
                if (mOnRationalListener != null) {
                    mOnRationalListener.onPermissionsGranted(mConfig.requestCode, Arrays
                            .asList(mConfig.perms));
                }
            } else {
                if (mOnRationalListener != null) {
                    mOnRationalListener.onPermissionsDenied(mConfig.requestCode, Arrays
                            .asList(mConfig.perms));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOnRationalListener = null;
    }

    public interface OnRationalListener {

        void onPermissionsGranted(int requestCode, @NonNull List<String> perms);

        void onPermissionsDenied(int requestCode, @NonNull List<String> perms);
    }
}

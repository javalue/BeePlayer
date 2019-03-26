package com.bee.player.permission;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;


import com.bee.player.R;

import java.util.Arrays;

public class XYPermissionSettingRationaleDialog extends DialogFragment {

    private boolean mStateSaved = false;
    private XYPermissionProxyFragment.OnRationalListener mOnRationalListener;
    private PermissionDialogConfig mConfig;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mConfig = new PermissionDialogConfig(getArguments());
        View dialogview = getDialogView();

        return new AlertDialog.Builder(getActivity()).setCancelable(false).setView(dialogview)
                .setPositiveButton(R.string.str_setting, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSettingPage(mConfig.requestCode);
                    }
                }).setNegativeButton(R.string.str_refuse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnRationalListener != null) {
                            mOnRationalListener.onPermissionsDenied(mConfig.requestCode, Arrays
                                    .asList(mConfig.perms));
                        }
                    }
                }).create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mOnRationalListener != null) {
            mOnRationalListener.onPermissionsDenied(mConfig.requestCode, Arrays.asList(mConfig
                    .perms));
        }
        super.onCancel(dialog);
    }

    private void startSettingPage(int requestCode) {
        getParentFragment().startActivityForResult(new Intent(Settings
                .ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package",
                getActivity().getPackageName(), null)), requestCode);
    }

    public void showAllowingStateLoss(FragmentManager fragmentManager, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (fragmentManager.isStateSaved()) {
                return;
            }
        }

        if (mStateSaved) {
            return;
        }

        show(fragmentManager, tag);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mStateSaved = true;
        super.onSaveInstanceState(outState);
    }

    public static XYPermissionSettingRationaleDialog newInstance(PermissionDialogConfig config,
                                                                 XYPermissionProxyFragment.OnRationalListener listener) {
        XYPermissionSettingRationaleDialog dialogFragment = new
                XYPermissionSettingRationaleDialog();
        dialogFragment.setArguments(config.toBundle());
        dialogFragment.setOnRationalListener(listener);
        return dialogFragment;
    }

    private void setOnRationalListener(XYPermissionProxyFragment.OnRationalListener listener) {
        mOnRationalListener = listener;
    }

    private View getDialogView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout
                .layout_permission_setting_rational_dialog, null, false);
        return view;
    }

}

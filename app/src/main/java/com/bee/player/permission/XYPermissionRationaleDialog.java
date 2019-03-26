package com.bee.player.permission;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.bee.player.R;


public class XYPermissionRationaleDialog extends DialogFragment {

    private boolean mStateSaved = false;
    private OnDialogListener mOnDialogListener;

    public static XYPermissionRationaleDialog newInstance(OnDialogListener listener) {
        XYPermissionRationaleDialog dialogFragment = new XYPermissionRationaleDialog();
        dialogFragment.setOnRationalListener(listener);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogview = getDialogView();

        return new AlertDialog.Builder(getActivity()).setCancelable(false).setView(dialogview)
                .setPositiveButton(R.string.str_allow, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnDialogListener != null) {
                            mOnDialogListener.onAllow();
                        }
                        mOnDialogListener = null;
                    }
                }).setNegativeButton(R.string.str_refuse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnDialogListener != null) {
                            mOnDialogListener.onDenied();
                        }
                        mOnDialogListener = null;
                    }
                }).create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mOnDialogListener != null) {
            mOnDialogListener.onDenied();
        }
        mOnDialogListener = null;
        super.onCancel(dialog);
    }



    public void showAllowingStateLoss(FragmentManager fragmentManager, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (fragmentManager == null || fragmentManager.isStateSaved()) {
                return;
            }
        }

        if (mStateSaved || isStateSaved()) {
            return;
        }

        if(fragmentManager != null ) {
            show(fragmentManager, tag);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mStateSaved = true;
        super.onSaveInstanceState(outState);
    }

    private void setOnRationalListener(OnDialogListener listener) {
        mOnDialogListener = listener;
    }

    private View getDialogView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout
                .layout_permission_rational_dialog, null, false);
        return view;
    }

    /**
     * 只用来回调这个dialog的成功和拒绝
     */
    public interface OnDialogListener {

        void onAllow();

        void onDenied();
    }
}

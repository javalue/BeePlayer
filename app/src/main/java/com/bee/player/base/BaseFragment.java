package com.bee.player.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    private static String TAG = "BaseFragment";
    protected Activity mActivity;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        try {
            final View view = inflater.inflate(getLayoutResId(), container, false);
            view.post(new Runnable() {
                @Override
                public void run() {
                    afterInject();
                }
            });
            return view;
        } catch (Throwable e) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
        return null;
    }


    protected abstract int getLayoutResId();

    protected abstract void afterInject();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
}
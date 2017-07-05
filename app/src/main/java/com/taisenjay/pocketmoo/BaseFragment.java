package com.taisenjay.pocketmoo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.taisenjay.pocketmoo.retrofit.ApiStores;
import com.taisenjay.pocketmoo.retrofit.AppClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Author : WangJian
 * Date   : 2017/7/1
 * Created by a handsome boy with love
 */

public class BaseFragment extends Fragment {
    private List<Call> calls;
    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    public Activity mActivity;

    protected int page = 1;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onDestroyView() {
        callCancel();
        super.onDestroyView();
    }

    public void addCalls(Call call) {
        if (calls == null) {
            calls = new ArrayList<>();
        }
        calls.add(call);
    }

    private void callCancel() {
        if (calls != null && calls.size() > 0) {
            for (Call call : calls) {
                if (!call.isCanceled())
                    call.cancel();
            }
            calls.clear();
        }
    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }


}

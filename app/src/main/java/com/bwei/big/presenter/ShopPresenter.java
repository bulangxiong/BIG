package com.bwei.big.presenter;

import android.os.Handler;
import android.os.Message;

import com.bwei.big.bean.Shop;
import com.bwei.big.core.DataCall;
import com.bwei.big.model.ShopModel;

public  class ShopPresenter {
    private DataCall dataCall;

    public ShopPresenter(DataCall dataCall) {
        this.dataCall = dataCall;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dataCall.loadSuccess((Shop) msg.obj);
        }
    };

    public void request(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Shop data = ShopModel.getData(url);
                Message message = mHandler.obtainMessage();
                message.obj = data;
                mHandler.sendMessage(message);
            }
        }).start();
    }
}

package com.bwei.big.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bwei.big.R;
import com.bwei.big.adapter.ShopAdapter;
import com.bwei.big.bean.Shop;
import com.bwei.big.core.DataCall;
import com.bwei.big.presenter.ShopPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataCall, ShopAdapter.TotalPriceListener {
    ShopPresenter shopPresenter=new ShopPresenter(this);
    private ExpandableListView list_cart;
    private List<Shop.DataBean> beanList=new ArrayList<>();
    String url="http://www.zhaoapi.cn/product/getCarts?uid=71";
    private ShopAdapter shopAdapter;
    private CheckBox check_all;
    private TextView goods_sum_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_cart = findViewById(R.id.list_cart);
        check_all = findViewById(R.id.check_all);
        goods_sum_price = findViewById(R.id.goods_sum_price);
        shopPresenter.request(url);
        shopAdapter = new ShopAdapter(beanList,this);
        list_cart.setAdapter(shopAdapter);
        check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shopAdapter.checkAll(isChecked);

            }
        });
       shopAdapter.setTotalPriceListener(this);

    }

    @Override
    public void loadSuccess(Shop data) {
        List<Shop.DataBean> beanList1 = data.getData();
        beanList.addAll(beanList1);
        int size = data.getData().size();
        for (int i = 0; i < size; i++) {
            list_cart.expandGroup(i);
        }
        shopAdapter.notifyDataSetChanged();
    }

    @Override
    public void totalPrice(double totalPrice) {
        goods_sum_price.setText(String.valueOf(totalPrice));
    }
}

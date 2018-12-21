package com.bwei.big.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwei.big.R;
import com.bwei.big.app.DTApplication;
import com.bwei.big.bean.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ShopAdapter+++";
    private List<Shop.DataBean> mList = new ArrayList<>();
    private Context context;
    private TotalPriceListener totalPriceListener;

    public ShopAdapter(List<Shop.DataBean> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }
    public void setTotalPriceListener(TotalPriceListener totalPriceListener){
        this.totalPriceListener =totalPriceListener;
    }
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.m_layout, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.checkBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        Shop.DataBean bean = mList.get(groupPosition);
        groupViewHolder.checkBox.setText(bean.getSellerName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder ;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.n_layout, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.checkBox1 = convertView.findViewById(R.id.item_checkbox);
            childViewHolder.imageView = convertView.findViewById(R.id.item_image);
            childViewHolder.title = convertView.findViewById(R.id.item_title);
            childViewHolder.price = convertView.findViewById(R.id.item_price);
            childViewHolder.jia = convertView.findViewById(R.id.item_jia);
            childViewHolder.jian = convertView.findViewById(R.id.item_jian);
            childViewHolder.number = convertView.findViewById(R.id.item_number);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final Shop.DataBean.ListBean listBean = mList.get(groupPosition).getList().get(childPosition);

        childViewHolder.title.setText(listBean.getTitle());
        childViewHolder.price.setText(listBean.getPrice() + "");

        String imageurl = "https" + listBean.getImages().split("https")[1];
        Log.i("dt", "imageUrl: " + imageurl);
        imageurl = imageurl.substring(0, imageurl.lastIndexOf(".jpg") + ".jpg".length());
        Glide.with(DTApplication.getContext()).load(imageurl).into(childViewHolder.imageView);//加载图片
        if (mList.get(groupPosition).getList().get(childPosition).getSelected() == 0) {
            childViewHolder.checkBox1.setChecked(false);
        } else {
            childViewHolder.checkBox1.setChecked(true);
        }
        childViewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listBean.setSelected(isChecked?1:0);
                calculatePrice();
            }
        });

        childViewHolder.number.setText(listBean.getNum()+"");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        CheckBox checkBox;
    }

    class ChildViewHolder {
        TextView title, price, jia, jian, number;
        CheckBox checkBox1;
        ImageView imageView;
    }

    public void checkAll(boolean isCheck) {
        for (int i = 0; i < mList.size(); i++) {
            Shop.DataBean bean = mList.get(i);
            bean.setCheck(isCheck);
            for (int j = 0; j < bean.getList().size(); j++) {
                Shop.DataBean.ListBean listBean = bean.getList().get(j);
                listBean.setSelected(isCheck ? 1 : 0);

            }
        }
        notifyDataSetChanged();
        calculatePrice();
    }

    private void calculatePrice() {
//        List<Shop> list = new ArrayList<>();
        double totalPrice = 0;
        for (int i = 0; i < mList.size(); i++) {
            //循环的商家

            Shop.DataBean shop = mList.get(i);
//            Shop shop = mList.get(i);
            for (int j = 0; j < shop.getList().size(); j++) {
                Shop.DataBean.ListBean listBean = shop.getList().get(j);
                if (listBean.getSelected()== 1) {
                    //如果是选中状态
                    totalPrice = totalPrice + listBean.getNum() * listBean.getPrice();
                }
            }
        }
        if (totalPriceListener != null) {

            totalPriceListener.totalPrice(totalPrice);
        }
        Log.d(TAG, "calculatePrice: "+totalPrice);
    }
    public interface TotalPriceListener{
        void totalPrice(double totalPrice);
    }
}



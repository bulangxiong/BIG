package com.bwei.big.model;

import com.bwei.big.bean.Shop;
import com.bwei.big.http.HttpUtils;
import com.google.gson.Gson;

public class ShopModel {
  public static Shop getData(String url){
      String s = HttpUtils.get(url);
      Gson gson=new Gson();
      Shop shop = gson.fromJson(s, Shop.class);
      return shop;
  }
}

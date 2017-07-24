package com.example.yizu.bean;

import android.graphics.Bitmap;
import android.media.Image;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by q on 2017/7/19.
 */
public class Goods extends BmobObject {
    private String goodsName;//物品名称
    private String classification;//分类
    private Bitmap[] pic;//三张照片
    private String description;//描述
    private String Positioning;//定位，市
    private Double deposit;//押金
    private Double moneyPer;//单位时间的租金
    private String area;//区
    private String state;//状态
    private Double StarRating;//星级
    private User user;//拥有该物品
    private BmobRelation purchase;//用户租物品，多对多
    public Double getStarRating() {
        return StarRating;
    }

    public void setStarRating(Double starRating) {
        StarRating = starRating;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Bitmap[] getPic() {
        return pic;
    }

    public void setPic(Bitmap[] pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPositioning() {
        return Positioning;
    }

    public void setPositioning(String positioning) {
        Positioning = positioning;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getMoneyPer() {
        return moneyPer;
    }

    public void setMoneyPer(Double moneyPer) {
        this.moneyPer = moneyPer;
    }
}

package cn.devin.fireprevention.Model;

/**
 * Created by Devin on 2018/3/30.
 */

public class MyLatLng
{
    //private int type;//位置信息的类别
    private String ip;
    private double lat;//维度
    private double lng;//经度

    //构造函数
    public MyLatLng(double lat,double lng) {
        //this.type=type;
        this.lat=lat;
        this.lng=lng;

    }


    public String getIp() {
        return ip;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }


    public double getLat() {
        return lat;
    }


    public void setLat(double lat) {
        this.lat = lat;
    }


    public double getLng() {
        return lng;
    }


    public void setLng(double lng) {
        this.lng = lng;
    }

}

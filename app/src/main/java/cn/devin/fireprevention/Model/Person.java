package cn.devin.fireprevention.Model;

/**
 * Created by Devin on 2018/3/30.
 */

public class Person {

    //客户端Id，作为标示号
    private int id;
    //人的位置信息
    private MyLatLng mylatlng;

    //构造函数
    public Person(int id, MyLatLng mylatlng)
    {
        this.id=id;
        this.mylatlng=mylatlng;
    }


    //get、set方法
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public MyLatLng getMyLatLng() {
        return mylatlng;
    }
    public void setMyLatLng(MyLatLng mylatlng) {
        this.mylatlng = mylatlng;
    }




}

package cn.devin.fireprevention.Model;

/**
 * Created by Devin on 2018/3/30.
 */

public class Person {
    private int id;
    private MyLatLng myLatLng;

    public Person(int id, MyLatLng myLatLng){
        this.id = id;
        this.myLatLng = myLatLng;
    }

    public int getId() {
        return id;
    }

    public MyLatLng getMyLatLng() {
        return myLatLng;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMyLatLng(MyLatLng myLatLng) {
        this.myLatLng = myLatLng;
    }
}

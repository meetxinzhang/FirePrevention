package cn.devin.fireprevention.Model;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

/**
 * Created by Devin on 2017/12/27.
 * model of Task
 */

public class Task {
    //目的地
    MyLatLng destination;
    //发布时间
    int[] pubTime;
    //主题
    String subject;
    //描述
    String describe;

    public Task(MyLatLng destination, int[] pubTime, String subject, String describe){
        this.destination = destination;
        this.pubTime = pubTime;
        this.subject = subject;
        this.describe = describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setDestination(MyLatLng destination) {
        this.destination = destination;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPubTime(int[] pubTime) {
        this.pubTime = pubTime;
    }

    public int[] getPubTime() {
        return pubTime;
    }

    public MyLatLng getDestination() {
        return destination;
    }

    public String getDescribe() {
        return describe;
    }

    public String getSubject() {
        return subject;
    }
}

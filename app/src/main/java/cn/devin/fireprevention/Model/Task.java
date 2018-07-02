package cn.devin.fireprevention.Model;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Devin on 2017/12/27.
 * model of Task
 */

public class Task
{
    private MyLatLng destination;//这个位置是任务前往的位置

    private Date time;//时间

    private String subject;//主题

    private String describe;//内容描述

    //构造函数
    public Task(MyLatLng destination,Date time,String subject,String describe) {
        this.destination=destination;
        this.time=time;
        this.describe=describe;
        this.subject=subject;

    }

    //set/get方法
    public MyLatLng getDestination() {
        return destination;
    }

    public void setDestination(MyLatLng destination) {
        this.destination = destination;
    }



    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }





}

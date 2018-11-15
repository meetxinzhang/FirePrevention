package cn.devin.fireprevention.Model;

/**
 * Created by Devin on 2018/3/30.
 */

public class MyLatLng {
    private Double lat;
    private Double lng;

    public MyLatLng(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}

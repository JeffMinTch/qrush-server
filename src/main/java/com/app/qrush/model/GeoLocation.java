package com.app.qrush.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "geo_locations")
public class GeoLocation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    private String uuid;

    @Column(name = "lat", updatable = true, nullable = false)
    private double lat;

    @Column(name = "lng", updatable = true, nullable = false)
    private double lng;

    @Column(name = "accuracy", updatable = true, nullable = false)
    private int accuracy;

    public GeoLocation() {
    }

    public GeoLocation(double lat, double lng, int accuracy) {
        this.lat = lat;
        this.lng = lng;
        this.accuracy = accuracy;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}

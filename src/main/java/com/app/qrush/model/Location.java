package com.app.qrush.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    private String uuid;

    @Column(name = "location_name", updatable = true, nullable = false)
    private String locationName;

    @Column(name = "country", updatable = true, nullable = false)
    private String country;

    @Column(name = "city", updatable = true, nullable = false)
    private String city;

    @Column(name = "postcode", updatable = true, nullable = false)
    private String postcode;

    @Column(name = "street", updatable = true, nullable = false)
    private String street;

    @Column(name = "street_number", updatable = true, nullable = false)
    private int streetNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "host_fk")
    private Host host;

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "geo_location_fk")
    private GeoLocation geoLocation;



    public Location() {
    }

    public Location(String locationName, String country, String city, String postcode, String street, int streetNumber, Host host) {
        this.locationName = locationName;
        this.country = country;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.streetNumber = streetNumber;
        this.host = host;
        this.geoLocation = new GeoLocation(37.421875199999995,-122.0851173,120);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }




}

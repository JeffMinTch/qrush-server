package com.app.qrush.model;

import com.app.qrush.model.enums.UserStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_fk")
    private Event event;

    @ElementCollection
    @CollectionTable(name = "image_names")
    private Set<String> imageNames;

    @Column(name="creation_date", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "geo_location_fk")
    private GeoLocation geoLocation;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


    public User() {
    }

    public User(Event event) {
        this.event = event;
        this.creationDate = LocalDateTime.now();
        this.geoLocation = new GeoLocation(37.421875199999995,-122.0851173,120);
        this.userStatus = UserStatus.REGISTERED;
        this.imageNames = Collections.emptySet();
    }



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(Set<String> imageNames) {
        this.imageNames = imageNames;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}

package com.app.qrush.repository;

import com.app.qrush.model.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoLocationRepository extends JpaRepository<GeoLocation, String> {

}

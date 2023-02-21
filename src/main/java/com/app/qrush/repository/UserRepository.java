package com.app.qrush.repository;

import com.app.qrush.model.enums.UserStatus;
import com.app.qrush.model.Event;
import com.app.qrush.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<List<User>> findAllByEventAndUserStatus(Event event, UserStatus userstatus);



}

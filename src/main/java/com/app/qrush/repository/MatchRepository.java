package com.app.qrush.repository;

import com.app.qrush.model.enums.MatchStatus;
import com.app.qrush.model.Match;
import com.app.qrush.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, String> {

    Optional<List<Match>> findByMatcher(User user);
    Optional<List<Match>> findByMatcherAndMatchStatus(User matcher, MatchStatus matchStatus);
    Optional<Match> findByMatcherAndMatchee(User matcher, User matchee);
}

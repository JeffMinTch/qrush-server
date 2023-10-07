package com.app.qrush.services;

import com.app.qrush.model.enums.MatchStatus;
import com.app.qrush.model.enums.UserStatus;
import com.app.qrush.model.Event;
import com.app.qrush.model.Match;
import com.app.qrush.model.User;
import com.app.qrush.repository.EventRepository;
import com.app.qrush.repository.MatchRepository;
import com.app.qrush.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchRepository matchRepository;

    public List<User> createMatches(User matcher) {
        Optional<Event> optionalEvent = eventRepository.findById(matcher.getEvent().getUuid());
        if (optionalEvent.isPresent()) {
            Optional<List<User>> optionalActiveUsers = userRepository.findAllByEventAndUserStatus(optionalEvent.get(), UserStatus.ACTIVE);
            if (optionalActiveUsers.isPresent()) {
                List<User> activeUsers = optionalActiveUsers.get();
                activeUsers.stream().filter(activeUser -> activeUser != matcher).forEach((activeUser) -> {
                    matchRepository.save(new Match(matcher, activeUser, MatchStatus.UNMATCHED));
                    matchRepository.save(new Match(activeUser, matcher, MatchStatus.UNMATCHED));
                });
                return activeUsers;

            } else {
                throw new NullPointerException();
            }
        } else {
            throw new NullPointerException();
        }
    }

    public List<User> getMatchCards(User matcher) {
        Optional<Event> optionalEvent = eventRepository.findById(matcher.getEvent().getUuid());
        if (optionalEvent.isPresent()) {
            Optional<List<User>> optionalActiveUsers = userRepository.findAllByEventAndUserStatus(optionalEvent.get(), UserStatus.ACTIVE);
            if (optionalActiveUsers.isPresent()) {
                List<User> activeUsers = optionalActiveUsers.get();
                List<User> matchUsers = activeUsers.stream().filter(activeUser -> !Objects.equals(activeUser.getUuid(), matcher.getUuid())).collect(Collectors.toList());
                return matchUsers;
            } else {
                throw new NullPointerException();
            }
        } else {
            throw new NullPointerException();
        }
    }

    public User updateCorrespondingMatchLists(User matchee, Event event) {
        return null;
    }

    public User findUserInEvent(User matcher, Event event) {
        return null;
    }

    public Match findMatch(User matcher, User matchee) {
        Optional<Match> optionalMatch = matchRepository.findByMatcherAndMatchee(matcher, matchee);
        if(optionalMatch.isEmpty()) {
            throw new NullPointerException();
        }
        return optionalMatch.get();
    }

//    public Match compareMatchStatus(User matcher, User matchee, String matchStatus) {
//
//    }


}

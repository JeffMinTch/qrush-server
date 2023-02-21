package com.app.qrush.model;


import com.app.qrush.model.enums.MatchStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uuid", unique = true)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "matcher")
    private User matcher;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "matchee")
    private User matchee;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    @Column(name="creation_date", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    public Match() {
    }

    public Match(User matcher, User matchee, MatchStatus matchStatus) {
        this.matcher = matcher;
        this.matchee = matchee;
        this.matchStatus = matchStatus;
        this.creationDate = LocalDateTime.now();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getMatcher() {
        return matcher;
    }

    public void setMatcher(User matcher) {
        this.matcher = matcher;
    }

    public User getMatchee() {
        return matchee;
    }

    public void setMatchee(User matchee) {
        this.matchee = matchee;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}

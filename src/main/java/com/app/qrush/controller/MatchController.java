package com.app.qrush.controller;

import com.app.qrush.model.enums.MatchStatus;
import com.app.qrush.model.enums.UserStatus;
import com.app.qrush.model.Event;
import com.app.qrush.model.Match;
import com.app.qrush.model.User;
import com.app.qrush.model.messages.Message;
import com.app.qrush.repository.EventRepository;
import com.app.qrush.repository.MatchRepository;
import com.app.qrush.repository.UserRepository;
import com.app.qrush.services.FileStorageService;
import com.app.qrush.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/event")
public class MatchController {

    @Autowired
    EventRepository eventRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    MatchService matchService;

    @Autowired
    MatchRepository matchRepo;

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;




    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        Optional<User> optionalMatcher = userRepo.findById(message.getSenderName());
        Optional<User> optionalMatchee = userRepo.findById(message.getReceiverName());
        System.out.println("Connected");
        if (optionalMatcher.isEmpty()) {
            throw new NullPointerException("Matcher Not Found");
        }
        if (optionalMatchee.isEmpty()) {
            throw new NullPointerException("Matchee Not Found");
        }
        User matcher = optionalMatcher.get();
        User matchee = optionalMatchee.get();
        Match match = matchService.findMatch(matcher, matchee);
        switch (message.getMessage()) {
            case NOT_INTERESTED:
                match.setMatchStatus(MatchStatus.NOT_INTERESTED);
                matchRepo.save(match);
//                return new ResponseEntity<>(HttpStatus.OK);
            case INTERESTED:
                match.setMatchStatus(MatchStatus.INTERESTED);
                matchRepo.save(match);
                Match correspondingMatch = matchService.findMatch(matchee, matcher);
                switch (correspondingMatch.getMatchStatus()) {
//                    case UNMATCHED:
//                    case NOT_INTERESTED:
//                        return new ResponseEntity<>(HttpStatus.OK);
                    case INTERESTED:
                        match.setMatchStatus(MatchStatus.MATCH);
                        correspondingMatch.setMatchStatus(MatchStatus.MATCH);
                        matchRepo.save(match);
                        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
                        return message;

//                        System.out.println(message.toString());
                        // send a notification to the corresponding React Native client
//                        String destination = "/topic/" + matcheeId;
//                        messagingTemplate.convertAndSend(destination, matcherId);

//                        return ResponseEntity.ok(match);
                    //Hier
//                        return ResponseEntity.ok(match);
                }
                break;
            default:
                throw new IllegalArgumentException("MatchStatus is wrong");


        }
        return message;
//        throw new NullPointerException();

    }

    @GetMapping("/match-cards/{userId}")
    public ResponseEntity<List<User>> matchCards(
            @PathVariable("userId") String userId
    ) {

        Optional<User> optionalUser = userRepo.findById(userId);
        System.out.println("Match Cards");
        if (optionalUser.isPresent()) {
            List<User> userList = matchService.getMatchCards(optionalUser.get());
            Collections.shuffle(userList);
            System.out.println(userList.size());
            return ResponseEntity.ok(userList);
        } else {
            throw new NullPointerException();
        }
    }

    @PostMapping("/scan/{eventId}")
    private ResponseEntity<User> scanEvent(
            @PathVariable("eventId") String eventId
    ) {
        Optional<Event> optionalEvent = eventRepo.findById(eventId);
        Event event;
        System.out.println("EventID: " + eventId);
        if (optionalEvent.isPresent()) {
            event = optionalEvent.get();
//            if(userRepo.) {
//
//            }
            User user = userRepo.save(new User(event));
            return ResponseEntity.ok(user);
        } else {
            throw new NullPointerException("Scanned Event Id doesn't exist");
        }
    }

    @PostMapping("/pictures/{userId}")
    private ResponseEntity<List<User>> pictures(
            @PathVariable("userId") String userId,
            @RequestParam("picture") MultipartFile picture
    ) throws IOException {

        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getUserStatus().equals(UserStatus.REGISTERED)) {
                user.setUserStatus(UserStatus.ACTIVE);
                Set<String> imageNames = new HashSet<>();
                imageNames.add(picture.getOriginalFilename());
                user.setImageNames(imageNames);
                fileStorageService.storeFile(picture, user);
                userRepo.save(user);
                List<User> activeUsers = matchService.createMatches(user);
                return ResponseEntity.ok(activeUsers);
            } else {
                List<User> activeUsers = matchService.getMatchCards(user);
                return ResponseEntity.ok(activeUsers);
            }

        } else {
            throw new NullPointerException("User does not exist.");
        }
    }

    @PutMapping("/add-picture/{userId}")
    private ResponseEntity<?> addPicture(
            @PathVariable("userId") String userId,
            @RequestParam("picture") MultipartFile picture
    ) {
        return null;
    }

    @DeleteMapping("/picture/{userId}/{picture}")
    private ResponseEntity<?> deletePicture(
            @PathVariable("userId") String userId,
            @PathVariable("picture") String picture

    ) {
        return null;
    }

    @PostMapping("/swipe/{matcherId}/{matcheeId}")
    private ResponseEntity<?> swipe(
            @PathVariable("matcherId") String matcherId,
            @PathVariable("matcheeId") String matcheeId,
            @RequestParam("matchStatus") MatchStatus matchStatus
    ) {
        Optional<User> optionalMatcher = userRepo.findById(matcherId);
        Optional<User> optionalMatchee = userRepo.findById(matcheeId);

        if (optionalMatcher.isEmpty()) {
            throw new NullPointerException("Matcher Not Found");
        }
        if (optionalMatchee.isEmpty()) {
            throw new NullPointerException("Matchee Not Found");
        }
        User matcher = optionalMatcher.get();
        User matchee = optionalMatchee.get();
        Match match = matchService.findMatch(matcher, matchee);
        switch (matchStatus) {
            case NOT_INTERESTED:
                match.setMatchStatus(MatchStatus.NOT_INTERESTED);
                matchRepo.save(match);
                return new ResponseEntity<>(HttpStatus.OK);
            case INTERESTED:
                match.setMatchStatus(MatchStatus.INTERESTED);
                matchRepo.save(match);
                Match correspondingMatch = matchService.findMatch(matchee, matcher);
                switch (correspondingMatch.getMatchStatus()) {
                    case UNMATCHED:
                    case NOT_INTERESTED:
                        return new ResponseEntity<>(HttpStatus.OK);
                    case INTERESTED:
                        match.setMatchStatus(MatchStatus.MATCH);
                        correspondingMatch.setMatchStatus(MatchStatus.MATCH);
                        matchRepo.save(match);
                        // send a notification to the corresponding React Native client
//                        String destination = "/topic/" + matcheeId;
//                        messagingTemplate.convertAndSend(destination, matcherId);

                        return ResponseEntity.ok(match);
                        //Hier
//                        return ResponseEntity.ok(match);
                }
                break;
            default:
                throw new IllegalArgumentException("MatchStatus is wrong");


        }
        throw new NullPointerException();

    }

    @PostMapping("/confirm/{matcher}/{matchee}")
    private ResponseEntity<?> confirm(
            @PathVariable("matcher") String matcher,
            @RequestParam("matchee") MultipartFile matchee
    ) {

        return null;
    }

    @DeleteMapping("/user/{userId}")
    private ResponseEntity<?> deletePicture(
            @PathVariable("userId") String userId
    ) {
        return null;

    }

//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public Message receiveMessage(@Payload Message message){
//        return message;
//    }
//
//    @MessageMapping("/private-message")
//    public Message recMessage(@Payload Message message){
//        messagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
//        System.out.println(message.toString());
//        return message;
//    }

    @GetMapping("/image/{userId}")
    @ResponseBody
    public ResponseEntity<Resource> loadImage(
            @PathVariable("userId") String userId
    ) {
        Resource file = null;
        System.out.println("download request");
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
//        Optional<Sample> optionalSample = sampleRepository.findByAudioUnit(audioUnit);
//        Sample sample = null;
//        if(optionalSample.isPresent()) {
//            sample = optionalSample.get();
//        } else {
//
//        }
        try {
            Path targetPath = Paths.get(user.getUuid());
            System.out.println(targetPath);
            file = fileStorageService.loadFileAsResource(user, "image.jpg");
//            audioUnit.getImageFileName(), audioUnit.getArtistAlias().getArtist().getUser().getUuid()
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("SamplePoolRestAPI: Resource not Found");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }




}

package com.app.qrush.controller;

import com.app.qrush.model.*;
import com.app.qrush.repository.EventRepository;
import com.app.qrush.repository.HostRepository;
import com.app.qrush.repository.LocationRepository;
import com.app.qrush.services.DocumentGenerator;
import com.app.qrush.services.FileStorageService;
import com.app.qrush.services.MailService;
import com.app.qrush.services.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@CrossOrigin("*")
@RestController
@RequestMapping("/host")
public class HostController {

    @Autowired
    HostRepository hostRepo;

    @Autowired
    LocationRepository locationRepo;

    @Autowired
    EventRepository eventRepo;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    MailService mailService;

    @Autowired
    QRCodeGenerator qrCodeGenerator;

    @PostMapping("/register")
    public ResponseEntity<Host> register(
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName,
            @RequestParam("email") String email
    ) {

        Host host = hostRepo.save(new Host(firstName, lastName, email));
        return ResponseEntity.ok(host);

    }

    @PostMapping("/location/{hostId}")
    public ResponseEntity<?> createLocation(
            @PathVariable("hostId") String hostId,
            @RequestParam("locationName") String locationName,
            @RequestParam("country") String country,
            @RequestParam("city") String city,
            @RequestParam("postcode") String postcode,
            @RequestParam("street") String street,
            @RequestParam("streetNumber") int streetNumber,
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam("accuracy") int accuracy
    ) {
        Optional<Host> optionalHost = hostRepo.findById(hostId);
        if (optionalHost.isPresent()) {
            Host host = optionalHost.get();
            Location location = new Location(
                    locationName,
                    country,
                    city,
                    postcode,
                    street,
                    streetNumber,
                    host
            );
            return ResponseEntity.ok(locationRepo.save(location));
        } else {
            throw new NullPointerException("Host is null");
        }
    }

    @PostMapping("/party/{locationId}")
    public ResponseEntity<Event> createEvent(
            @PathVariable("locationId") String locationId,
            @RequestParam("name") String name,
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end
    ) throws IOException, InvalidFormatException {

//        DocumentGenerator documentGenerator = new DocumentGenerator();
        Optional<Location> optionalLocation = locationRepo.findById(locationId);
        if (optionalLocation.isPresent()) {

            if (eventRepo.findEventByName(name).isEmpty()) {

                Event event = eventRepo.save(new Event(
                        name,
                        start,
                        end,
                        optionalLocation.get()
                ));

//                String qrCodeImagePath = fileStorageService.createQRStoragePath(event).toString();
                try {

                    qrCodeGenerator.generateQRCodeImage(event, 250, 250);
                    mailService.sendEmailWithAttachment();
                } catch (WriterException | IOException e) {
                    e.printStackTrace();

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                return ResponseEntity.ok(event);
            } else {
                Event event = eventRepo.findEventByName(name).get();
                return new ResponseEntity<>(event, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

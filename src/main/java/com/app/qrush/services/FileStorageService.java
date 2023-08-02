package com.app.qrush.services;



import com.app.qrush.model.Event;
import com.app.qrush.model.User;
import com.app.qrush.property.FileStorageProperties;
//import org.apache.commons.io.FileUtils;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Base64;

@Service
public class FileStorageService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    private final Path fileStorageLocation;

    private final Path qrStorageLocation;

    private final Path userStorageLocation;

    private final BlobServiceClient blobServiceClient;
    private final String containerName;

    Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties,
                              @Value("${azure.storage.connection-string}") String connectionString,
    @Value("${azure.storage.container-name}") String containerName
                              ) {
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        this.containerName = containerName;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getBaseDir())
                .toAbsolutePath().normalize();

        this.qrStorageLocation = Paths.get(fileStorageProperties.getBaseDir(), fileStorageProperties.getQrCodeDir()).toAbsolutePath().normalize();
        this.userStorageLocation = Paths.get(fileStorageProperties.getBaseDir(), fileStorageProperties.getUserDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);

        } catch (Exception ex) {
//            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    public Path getQrStorageLocation() {
        return qrStorageLocation;
    }

    public Path createQRStoragePath(Event event) {
        Path target = Paths.get(String.valueOf(this.qrStorageLocation), event.getUuid() + ".png").normalize();
        return target;
    }

//    public String storeFile(MultipartFile file, User user) throws IOException {
//        // Normalize file name
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//        try {
//            // Check if the file's name contains invalid characters
//            if (fileName.contains("..")) {
//                throw new IllegalArgumentException("Sorry! Filename contains invalid path sequence " + fileName);
//            }
//            createUserDirectory(user.getUuid());
//            // Copy file to the target location (Replacing existing file with the same name)
//            Path targetUserLocation = Paths.get(String.valueOf(this.userStorageLocation), user.getUuid());
//            Path targetLocation = targetUserLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return fileName;
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
//        }
//    }

    public String storeFile(MultipartFile file, User user) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new IllegalArgumentException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            createUserDirectory(user.getUuid());

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient("user/" + user.getUuid() + "/" + fileName);
            blobClient.upload(file.getInputStream(), file.getSize());

            return fileName;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Path createUserDirectory(String userName) {
        Path userDirectory = null;
        try {
            userDirectory = Files.createDirectories(Paths.get(String.valueOf(this.userStorageLocation), userName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userDirectory;
    }

//    public Resource loadFileAsResource(Path targetPath, String fileName) throws Exception {
////        try {
////        Path target = Paths.get(String.valueOf(this.fileStorageLocation), userName);
//        Path target = Paths.get(String.valueOf(this.userStorageLocation), String.valueOf(targetPath));
////        createPath(targetPath);
//        Path filePath = target.resolve(fileName).normalize();
//        System.out.println("Filepath: " + filePath.toString());
//        Resource resource = new UrlResource(filePath.toUri());
//        if (resource.exists()) {
//            return resource;
//        } else {
//            throw new NullPointerException("File Not found");
////            throw new MyFileNotFoundException("File not found " + fileName);
//        }
////        }
//        /*catch (MalformedURLException ex) {
//            throw new MyFileNotFoundException("File not found " + fileName, ex);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }*/
//    }

    public Resource loadFileAsResource(User user, String fileName) throws Exception {
//        Path target = Paths.get(String.valueOf(this.userStorageLocation), String.valueOf(targetPath));
//        Path filePath = target.resolve(fileName).normalize();
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();

        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("qrush");
            BlobClient blobClient = containerClient.getBlobClient("user/" + user.getUuid() + "/" + fileName);
            Resource resource = new UrlResource(blobClient.getBlobUrl());
            return resource;
//            if (resource.exists()) {
//                return resource;
//            } else {
//                throw new NullPointerException();
//            }
        } catch (MalformedURLException ex) {
            throw new NullPointerException();
        }

//        try {
//            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
//            BlobClient blobClient = containerClient.getBlobClient("user/" + user.getUuid() + "/" + fileName);
//            Resource resource = new UrlResource(blobClient.getBlobUrl());
//
//            if (blobClient.exists()) {
//                return resource;
//            } else {
//                throw new NullPointerException("File Not found");
//            }
//        } catch (MalformedURLException ex) {
//            throw ex;
//        }
    }

    public Path createPath(Path filePath) {
        Path target = Paths.get(String.valueOf(this.qrStorageLocation), String.valueOf(filePath));
        return target;
    }

}


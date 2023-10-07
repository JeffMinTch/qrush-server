package com.app.qrush.services;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.app.qrush.model.Event;
import com.app.qrush.property.ApplicationProperties;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//Quelle: https://medium.com/nerd-for-tech/how-to-generate-qr-code-in-java-spring-boot-134adb81f10d
@Service
public class QRCodeGenerator {

    private final BlobServiceClient blobServiceClient;
    private final String containerName;

    private final String baseUri;

    private final String eventUri;

    public String getBaseUri() {
        return baseUri;
    }

    public String getEventUri() {
        return eventUri;
    }

    public QRCodeGenerator(
            ApplicationProperties applicationProperties,
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container-name}") String containerName
    ) {

        this.baseUri = applicationProperties.getBaseUri();
        this.eventUri = applicationProperties.getEventUri();
        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        this.containerName = containerName;
    }

//    public String generateQRCodeImage(Event event, int width, int height, String filePath)
//            throws WriterException, IOException {
//        String qrLink = this.generateQRLink(event);
//
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(qrLink, BarcodeFormat.QR_CODE, width, height);
//
//        Path path = FileSystems.getDefault().getPath(filePath);
//        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
//
////
////        //Create Byte-Stream
////        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
////        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000002, 0xFFFFC041);
////        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, config);
////        byte[] pngData = pngOutputStream.toByteArray();
////
////
////        // Upload the QR code image to Azure Blob Storage
////        String fileName = event.getUuid() + ".png";
////        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
////        BlobClient blobClient = containerClient.getBlobClient("qr-codes/" + fileName);
////        blobClient.upload(new ByteArrayInputStream(pngData), pngData.length, true);
//
//        return qrLink;
//    }

    public String generateQRCodeImage(Event event, int width, int height) throws WriterException, IOException {
        String qrLink = generateQRLink(event);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrLink, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000002, 0xFFFFC041);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, config);
        byte[] pngData = pngOutputStream.toByteArray();

        // Upload the QR code image to Azure Blob Storage
        String fileName = "img/" + "qr-codes/" + event.getName() + ".png"; // Pseudopfad angeben
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(new ByteArrayInputStream(pngData), pngData.length, true);
        return qrLink;
    }


    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig( 0xFF000002 , 0xFFFFC041 ) ;

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream,con);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }

    private String generateQRLink(Event event) {
        String eventUri = this.getEventUri();
        return this.getEventUri() + "/" + event.getUuid();
    }

}
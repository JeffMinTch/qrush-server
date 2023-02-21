package com.app.qrush.services;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.app.qrush.model.Event;
import com.app.qrush.property.ApplicationProperties;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

//Quelle: https://medium.com/nerd-for-tech/how-to-generate-qr-code-in-java-spring-boot-134adb81f10d
@Service
public class QRCodeGenerator {

    private final String baseUri;

    private final String eventUri;

    public String getBaseUri() {
        return baseUri;
    }

    public String getEventUri() {
        return eventUri;
    }

    public QRCodeGenerator(ApplicationProperties applicationProperties) {
        this.baseUri = applicationProperties.getBaseUri();
        this.eventUri = applicationProperties.getEventUri();
    }

    public String generateQRCodeImage(Event event, int width, int height, String filePath)
            throws WriterException, IOException {
        String qrLink = this.generateQRLink(event);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrLink, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
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
        return this.getBaseUri() + this.getEventUri() + "/" + event.getUuid();
    }

}
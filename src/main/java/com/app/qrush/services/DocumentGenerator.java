package com.app.qrush.services;
// Java program to Demonstrate Adding a jpg image
// To a Word Document

// Importing Input output package for basic file handling

import java.io.*;

import com.app.qrush.property.FileStorageProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;

// Importing Apache POI package
import org.apache.poi.xwpf.usermodel.*;

import org.springframework.stereotype.Service;


public class DocumentGenerator {


    public DocumentGenerator() throws IOException, InvalidFormatException {
        // Step 1: Creating a blank document
        XWPFDocument document = new XWPFDocument();

        // Step 2: Creating a Paragraph using
        // createParagraph() method
        XWPFParagraph paragraph
                = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        // Step 3: Creating a File output stream of word
        // document at the required location
        FileOutputStream fout = new FileOutputStream(
                new File("C:\\Users\\maxmi\\OneDrive\\Dokumente\\dating-server\\qrush (1)\\qrush\\src\\main\\resources\\static\\documents\\printout.docx"));

        // Step 4: Creating a file input stream of image by
        // specifying its path
        File image = new File("C:\\Users\\maxmi\\OneDrive\\Dokumente\\dating-server\\qrush (1)\\qrush\\src\\main\\resources\\static\\img\\frame.jpg");
        FileInputStream imageData
                = new FileInputStream(image);

        // Step 5: Retrieving the image file name and image
        // type
        int imageType = XWPFDocument.PICTURE_TYPE_JPEG;
        String imageFileName = image.getName();

        // Step 6: Setting the width and height of the image
        // in pixels.
        int width = 450;
        int height = 400;
        run.addPicture(imageData, imageType, imageFileName,
                Units.toEMU(width),
                Units.toEMU(height));
        document.write(fout);

        // Step 8: Closing the connections
        fout.close();
        document.close();
    }

    // Step 7: Adding the picture using the addPicture()
    // method and writing into the document


}

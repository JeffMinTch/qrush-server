package com.app.qrush;

import com.app.qrush.property.ApplicationProperties;
import com.app.qrush.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication()
@EnableConfigurationProperties({
		FileStorageProperties.class,
		ApplicationProperties.class
})

public class QrushApplication {

	public static void main(String[] args) {
		System.out.println("Hello Dating World");
		SpringApplication.run(QrushApplication.class, args);

	}

}

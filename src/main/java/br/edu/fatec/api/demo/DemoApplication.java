package br.edu.fatec.api.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// PDFBox parameters
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
		System.setProperty("org.apache.pdfbox.rendering.UsePureJavaCMYKConversion", "true");
		// PDFBox parameters

		SpringApplication.run(DemoApplication.class, args);
	}

}

package com.proyecto.integrado.vummy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.proyecto.integrado.vummy")
public class VummyApplication {

	public static void main(String[] args) {
		SpringApplication.run(VummyApplication.class, args);
	}

}

package com.weblibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class WebLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebLibraryApplication.class, args);
    }

}

package com.sgr;

import com.sgr.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication public class FileTransferApplication
{

        public static void main(String[] args) {
                SpringApplication.run(FileTransferApplication.class, args);
        }

        @Bean CommandLineRunner init(StorageService storageService) {
                return (args) -> {
                        storageService.deleteAll();
                        storageService.init();
                };
        }
}

package com.sgr.service;

import com.sgr.config.AppProp;
import com.sgr.config.StorageProperties;
import com.sgr.exception.StorageException;
import com.sgr.exception.StorageFileNotFoundException;
import com.sgr.models.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service class FileSystemStorageService implements StorageService
{

        private final Path rootLocation;

        @Autowired public FileSystemStorageService(StorageProperties properties) {
                this.rootLocation = Paths.get(properties.getLocation());
        }

        @Override public void store(MultipartFile file, Map<String, String> model) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                try {
                        if (file.isEmpty()) {
                                throw new StorageException("Failed to store empty file " + filename);
                        }
                        if (filename.contains("..")) {
                                throw new StorageException("Cannot store file with relative path outside current directory " + filename);
                        }
                        try (InputStream inputStream = file.getInputStream()) {
                                Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                                FileInfo fileInfo=new FileInfo();
                                fileInfo.setFileName(filename);
                                fileInfo.setDownloads(0);
                                fileInfo.setCreatedAt(new Date());
                            AppProp.FileData.put(filename,fileInfo);
                            model.put("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
                                model.put("isReplaced", "false");

                        }
                } catch (IOException e) {
                        throw new StorageException("Failed to store file " + filename, e);
                }
        }

        @Override public Stream<Path> loadAll() {
                try {
                        return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation)).map(this.rootLocation::relativize);
                } catch (IOException e) {
                        throw new StorageException("Failed to read stored files", e);
                }

        }

        @Override public Path load(String filename) {
                return rootLocation.resolve(filename);
        }

        @Override public Resource loadAsResource(String filename) {
                try {
                        Path file = load(filename);
                        Resource resource = new UrlResource(file.toUri());
                        if (resource.exists() || resource.isReadable()) {
                                return resource;
                        } else {
                                throw new StorageFileNotFoundException("Could not read file: " + filename);

                        }
                } catch (MalformedURLException e) {
                        throw new StorageFileNotFoundException("Could not read file: " + filename, e);
                }
        }

        @Override public void deleteAll() {
                FileSystemUtils.deleteRecursively(rootLocation.toFile());
        }

        @Override public void init() {
                try {
                        Files.createDirectories(rootLocation);
                } catch (IOException e) {
                        throw new StorageException("Could not initialize storage", e);
                }
        }
}
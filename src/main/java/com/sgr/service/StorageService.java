package com.sgr.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file, Map<String, String> redirectAttributes, HttpServletRequest request);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
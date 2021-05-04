package com.sgr.controller;

import com.sgr.config.AppProp;
import com.sgr.config.UserDetails;
import com.sgr.exception.StorageFileNotFoundException;
import com.sgr.models.BrowserDetails;
import com.sgr.models.FileInfo;
import com.sgr.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        storageService.loadAll().forEach(
                path -> {

                    String fileUrl = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString(), null).build().toUri().toString();
                    FileInfo fileInfo = AppProp.FileData.get(path.getFileName().toString());
                    fileInfo.setFileUrl(fileUrl);
                    AppProp.FileData.put(path.getFileName().toString(), fileInfo);

                }
        );
        model.addAttribute("fileDataMap", AppProp.FileData);
        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        if (null != request) {
            FileInfo fileInfo = AppProp.FileData.get(filename);
            if (null != fileInfo) {
                BrowserDetails userAgent = UserDetails.getUserAgent(request);
                String ip = UserDetails.getIp(request);
                fileInfo.setIp(ip);
                fileInfo.setBrowser(userAgent);
                fileInfo.setLastDownload(new Date());
                fileInfo.setDownloads(fileInfo.getDownloads() + 1);
                AppProp.FileData.put(filename, fileInfo);
            }
        }

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    @ResponseBody
    public Map<String, String> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map<String, String> model = new HashMap<>();
        storageService.store(file, model, request);
        return model;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
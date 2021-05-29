package com.store.com.web.rest;

import com.store.com.service.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {
    private final ResourceLoader resourceLoader;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Value("${archivo.path}")
    private String path;

    @ResponseBody
    @PostMapping("/fileUpload")
    public String upload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String warning = "";
        if (FileUtils.upload(file, path, fileName)) {
            warning = "Carga exitosa";
        } else {
            warning = "Error al subir";
        }
        System.out.println(warning);
        return "Carga exitosa";
    }

}

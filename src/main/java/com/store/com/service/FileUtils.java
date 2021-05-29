package com.store.com.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtils {

    @SneakyThrows
    public static boolean upload(MultipartFile file, String path, String fileName) {
        String realPath = path + "/" + FileNameUtils.getFileName(fileName);
        File dest = new File(realPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}

package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

public class ConvertitoreMultipartFileArrayToFileArray {
    public static File[] convertMultipartFileArrayToFileArray(MultipartFile[] multipartFiles) throws IOException {
        File[] files = new File[multipartFiles.length];
        for (int i = 0; i < multipartFiles.length; i++) {
            files[i] = ConvertitoreMultipartFileToFile.convertiMultipartFileToFile(multipartFiles[i]);
        }
        return files;
    }
}

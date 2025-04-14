package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils.multipartConverter;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConvertitoreMultipartFileToFile {

    public static File convertiMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Creazione di un file temporaneo
        File file = File.createTempFile("file-", multipartFile.getOriginalFilename());

        // Copia il contenuto del MultipartFile nel file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        // Ritorna il file
        return file;
    }

    public static File[] convertMultipartFileArrayToFileArray(MultipartFile[] multipartFiles) throws IOException {
        File[] files = new File[multipartFiles.length];
        for (int i = 0; i < multipartFiles.length; i++) {
            files[i] = convertiMultipartFileToFile(multipartFiles[i]);
        }
        return files;
    }


}

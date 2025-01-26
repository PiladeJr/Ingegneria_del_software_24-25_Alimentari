package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.utils;

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
}

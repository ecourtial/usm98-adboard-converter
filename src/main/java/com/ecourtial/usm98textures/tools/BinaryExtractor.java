package com.ecourtial.usm98textures.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BinaryExtractor {
    
    public byte[] getFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());

        return fileContent;
    }
}

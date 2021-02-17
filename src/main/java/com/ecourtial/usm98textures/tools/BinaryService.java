package com.ecourtial.usm98textures.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class BinaryService {
    
    public byte[] getFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());

        return fileContent;
    }
    
    public void writeHexString(String filePath, String value, int position) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(this.hexStringToByteArray(value));

    }
    
    /* s must be an even-length string. Source Stackoverflow*/
private  byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}

}

package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BinaryService {

    public byte[] getFileContent(String filePath) throws Exception {
        File file = new File(filePath);
        byte[] fileContent;
        
        try {
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            throw new Exception("Image file not found: " + filePath);
        }

        return fileContent;
    }

    public void writeHexString(String filePath, String value) throws IOException, Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(this.hexStringToByteArray(value));

    }

    /* s must be an even-length string. Source Stackoverflow*/
    private byte[] hexStringToByteArray(String s) throws Exception {
        int len = s.length();
        
        if (s.length() % 2 != 0) {
            throw new Exception("Hex string to output is not of even lenght!");
        }
        
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) +
                Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    
    public String getFileCheckSum(String filePath) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        
        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(filePath);
        byte[] dataBytes = new byte[1024];

        int nread;
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        }

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        String checksum = sb.toString();
        
        return checksum;
    }
}

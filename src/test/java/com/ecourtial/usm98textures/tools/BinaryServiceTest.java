package com.ecourtial.usm98textures.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryServiceTest {
    
    @Test
    public void testGetFileContent() throws Exception {
        System.out.println("getFileContent");
        String filePath = "src/test/assets/basic-text.txt";
        BinaryService instance = new BinaryService();
        byte[] expResult = {69, 114, 105, 99};
        byte[] result = instance.getFileContent(filePath);
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testWriteHexString() throws Exception {
        System.out.println("writeHexString");
        String filePath = "src/test/assets/basic-text-output.txt";
        String value = "45726963";
        BinaryService instance = new BinaryService();
        instance.writeHexString(filePath, value);
        
        byte[] expResult = {69, 114, 105, 99};
        byte[] result = instance.getFileContent(filePath);
        assertArrayEquals(expResult, result);
    }
}

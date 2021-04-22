package com.ecourtial.usm98textures.tools;

import tools.BinaryService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryServiceTest {
    
    @Test
    public void testGetFileContent() throws Exception {
        String filePath = "src/test/assets/basic-text.txt";
        BinaryService instance = new BinaryService();
        byte[] expResult = {69, 114, 105, 99};
        byte[] result = instance.getFileContent(filePath);
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testWriteHexString() throws Exception {
        String filePath = "src/test/assets/basic-text-output.txt";
        String value = "45726963";
        BinaryService instance = new BinaryService();
        instance.writeHexString(filePath, value);
        
        byte[] expResult = {69, 114, 105, 99};
        byte[] result = instance.getFileContent(filePath);
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testOddSequence() {
        BinaryService instance = new BinaryService();
  
        try {
            instance.writeHexString("whatever", "CF7DD");
            fail("Since the string is not of even length, an exception was expected!");
        } catch (Exception e) {
            assertEquals("Hex string to output is not of even lenght!", e.getMessage());
        }
        
    }
}

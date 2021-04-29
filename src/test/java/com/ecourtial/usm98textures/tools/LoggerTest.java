package com.ecourtial.usm98textures.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import tools.BinaryService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tools.Logger;

public class LoggerTest {
  @Test
  public void testBehavior() throws IOException, NoSuchAlgorithmException {
    String originalFile = "src/test/assets/logger/log-ori.txt";
    String logFile = "src/test/assets/logger/log-test.txt";

    // Make a copy
    try (
      InputStream in = new BufferedInputStream(
        new FileInputStream(originalFile)); OutputStream out = new BufferedOutputStream(
        new FileOutputStream(logFile))) {

      byte[] buffer = new byte[1024];
      int lengthRead;
      while ((lengthRead = in .read(buffer)) > 0) {
        out.write(buffer, 0, lengthRead);
        out.flush();
      }
    }

    // Test the logger
    Logger logger = new Logger(logFile);
    
    logger.log("This is the first line");
    logger.log("And this is the second one");
    
    BinaryService binaryService = new BinaryService();
    String checksum = binaryService.getFileCheckSum(logFile);
    
      assertEquals("9b2e5a7ff3de8ddcc31f4a8bc41419cc8e805001", checksum);
  }
}
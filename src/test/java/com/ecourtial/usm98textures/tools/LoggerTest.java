package com.ecourtial.usm98textures.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import tools.BinaryService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tools.Logger;

public class LoggerTest {
  @Test
  public void testBehavior() throws IOException {
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
  }
}
package com.ecourtial.usm98textures.tools;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import tools.Logger;
import tools.LoggerService;

public class LoggerServiceTest {
    @Test
    public void testLogEnabled() throws IOException {
        Logger mockedLogger = Mockito.mock(Logger.class);
        LoggerService service = new LoggerService(mockedLogger, true);
        
        String msg1 = "This is the first message";
        String msg2 = "This is the second message";
        
        service.log(msg1);
        service.log(msg2);
        
        Mockito.verify(mockedLogger, times(1)).log(msg1);
        Mockito.verify(mockedLogger, times(1)).log(msg2);
    }
    
    @Test
    public void testLogDisabled() throws IOException {
        Logger mockedLogger = Mockito.mock(Logger.class);
        LoggerService service = new LoggerService(mockedLogger, false);
        
        String msg1 = "This is the first message";
        String msg2 = "This is the second message";
        
        service.log(msg1);
        service.log(msg2);
        
        Mockito.verify(mockedLogger, times(0)).log(msg1);
        Mockito.verify(mockedLogger, times(0)).log(msg2);
    }
}

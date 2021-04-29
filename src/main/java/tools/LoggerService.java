package tools;

import java.io.IOException;

public class LoggerService {

    private final Logger logger;
    private final boolean output;
    
    public LoggerService(Logger logger, boolean output) {
        this.logger = logger;
        this.output = output;    
    }
    
    public void log(String msg) throws IOException {
        if (this.output) {
            System.out.println(msg);
            this.logger.log(msg);
        }
    }
}
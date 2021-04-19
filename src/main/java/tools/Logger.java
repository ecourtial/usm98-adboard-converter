package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
        private static final String LOG_FILENAME = "log.txt";
        private BufferedWriter logger;
        private boolean init = false;

        public void log(String msg) throws IOException {
            if (false == this.init) {
                File logFile = new File(Logger.LOG_FILENAME);
                if (logFile.exists()) {
                    logFile.delete();
                }
                
                this.logger = new BufferedWriter(new FileWriter(Logger.LOG_FILENAME, true));
                this.init = true;
            }
            
            this.logger.append(System.lineSeparator() + msg);
        }
        
        @Override
        protected void finalize() throws IOException, Throwable {
            try {
                if (this.logger != null ){
                    this.logger.close();
                }
            } finally {
                super.finalize();
            }
        }
}

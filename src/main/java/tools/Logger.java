package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private final String filename;
    private BufferedWriter logger;
    private boolean init = false;

    public Logger(String filename) {
        this.filename = filename;
    }

    public void log(String msg) throws IOException {
        if (false == this.init) {
            File logFile = new File(this.filename);
            if (logFile.exists()) {
                logFile.delete();
            }

            this.logger = new BufferedWriter(new FileWriter(this.filename, true));
            this.init = true;
        }

        this.logger.write(msg);
        this.logger.newLine();
        this.logger.flush();
    }

    @Override
    protected void finalize() throws IOException, Throwable {
        if (this.logger != null) {
            this.logger.close();
        }
    }
}

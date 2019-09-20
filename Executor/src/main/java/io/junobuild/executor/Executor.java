package io.junobuild.executor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.apache.commons.lang3.SystemUtils;

public class Executor {

  private ProcessBuilder builder;
  private Process process;
  private BufferedWriter writer;

  Executor() {
    builder = new ProcessBuilder(shell());
  }

  Executor(File workingDir) {
    builder = new ProcessBuilder(shell());
    builder.directory(workingDir);
  }

  public void start() throws IOException {
    process = builder.start();
    writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
  }

  public void stop() throws IOException, InterruptedException {
    writeCommand("exit");
    process.waitFor();
  }

  public void exec(String command) throws IOException {
    writeCommand(command);
  }

  private String shell() {
    if (SystemUtils.IS_OS_WINDOWS) {
      return "cmd.exe";
    } else {
      return "bash";
    }
  }

  private void writeCommand(String command) throws IOException {
    writer.write(command);
    writer.newLine();
    writer.flush();
  }

}

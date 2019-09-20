package io.junobuild.executor;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

public class ExecutorTest {

  @Test
  public void testExecutor() throws Exception {
    String dir;
    if (SystemUtils.IS_OS_WINDOWS) {
      dir = "C:\\temp";
    } else {
      dir = "/tmp";
    }

    Executor executor = new Executor(new File(dir));
    executor.start();
    executor.exec("echo \"Hello, World!\">out.txt");
    executor.stop();

    BufferedReader reader = new BufferedReader(
        new FileReader(new File(dir + File.separator + "out.txt")));

    assertEquals(reader.readLine(), "\"Hello, World!\"");
  }

}

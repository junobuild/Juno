package io.junobuild.toolchain.checker;

import io.junobuild.toolchain.toolchain.IToolchain;
import java.io.File;
import org.apache.commons.lang3.SystemUtils;

public final class ToolchainChecker {

  private IToolchain toolchain;

  ToolchainChecker(final IToolchain toolchain) {
    this.toolchain = toolchain;
  }

  public boolean checkToolchain() {
    prepareTestFile();

    boolean f = checkCBinary();
    f &= checkCxxBinary();
    f &= checkCStaticLibrary();
    f &= checkCxxStaticLibrary();
    f &= checkCSharedLibrary();
    f &= checkCxxSharedLibrary();

    return f;
  }

  private boolean executeCommand(String command) {
    try {
      Process p = Runtime.getRuntime().exec(command, null, getWorkingDir());
      p.waitFor();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean checkCxxSharedLibrary() {
    // TODO: Implement
    return true;
  }

  private boolean checkCSharedLibrary() {
    // TODO: Implement
    return true;
  }

  private boolean checkCxxStaticLibrary() {
    // TODO: Implement
    return true;
  }

  private boolean checkCStaticLibrary() {
    // TODO: Implement
    return true;
  }

  private boolean checkCxxBinary() {
    // TODO: Implement
    return true;
  }

  private boolean checkCBinary() {
    // TODO: Implement
    return true;
  }

  private void prepareTestFile() {
    unzipResource(getTestResource(), getTempDir());
  }

  private void unzipResource(File file, String outDir) {
  }

  private String getTempDir() {
    if (SystemUtils.IS_OS_WINDOWS) {
      return "C:\\temp";
    } else {
      return "/tmp";
    }
  }

  private File getTestResource() {
    return new File(
        getClass().getClassLoader().getResource("io/junobuild/toolchain/toolchain/checker/test.zip")
            .getFile());
  }

  private File getWorkingDir() {
    return new File(getTempDir() + File.separator + "test");
  }

}

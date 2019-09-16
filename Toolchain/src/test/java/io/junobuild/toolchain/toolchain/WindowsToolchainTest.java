package io.junobuild.toolchain.toolchain;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import org.junit.Test;
import org.junit.BeforeClass;


public class WindowsToolchainTest {

  private static IToolchain toolchain;

  @BeforeClass
  public static void createToolchain() {
    toolchain = new WindowsToolchain(
        "C:\\Visual Studio\\bin\\cl.exe",
        "C:\\Visual Studio\\bin\\lib.exe",
        "C:\\Visual Studio\\bin\\link.exe",
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>());
  }

  @Test
  public void createCObjectFileTest() {
    String command = toolchain.createCObjectFile(
        "out\\source.c.obj",
        "source.c",
        new LinkedList<String>(Arrays.asList("W4")),
        new LinkedList<String>(Arrays.asList("foo\\include", "bar\\include")));
    String expected = "C:\\Visual Studio\\bin\\cl.exe /TC  /Ifoo\\include /Ibar\\include  /W4 /c source.c /Foout\\source.c.obj";
    assertEquals(expected, command);
  }

  @Test
  public void createCxxObjectFileTest() {
    String command = toolchain.createCxxObjectFile(
        "out\\source.cc.obj",
        "source.cc",
        new LinkedList<String>(Arrays.asList("W4")),
        new LinkedList<String>(Arrays.asList("foo\\include", "bar\\include")));
    String expected = "C:\\Visual Studio\\bin\\cl.exe /TP  /Ifoo\\include /Ibar\\include  /W4 /c source.cc /Foout\\source.cc.obj";
    assertEquals(expected, command);
  }

  @Test
  public void createStaticLibrary() {
    String command = toolchain.createStaticLibrary(
        "out\\foo.lib",
        new LinkedList<String>(Arrays.asList("out\\foo.c.o")));
    String expected = "C:\\Visual Studio\\bin\\lib.exe out\\foo.c.o /OUT:out\\foo.lib";
    assertEquals(expected, command);
  }

  @Test
  public void createCSharedLibrary() {
    String command = toolchain.createCSharedLibrary("out\\foo.dll",
        new LinkedList<String>(Arrays.asList("out\\foo.c.o")));
    String expected = "C:\\Visual Studio\\bin\\link.exe /DLL out\\foo.c.o /OUT:out\\foo.dll";
    assertEquals(expected, command);
  }

  @Test
  public void createCxxSharedLibrary() {
    String command = toolchain.createCxxSharedLibrary("out\\foo.dll",
        new LinkedList<String>(Arrays.asList("out\\foo.cc.o")));
    String expected = "C:\\Visual Studio\\bin\\link.exe /DLL out\\foo.cc.o /OUT:out\\foo.dll";
    assertEquals(expected, command);
  }

  @Test
  public void createCExecutable() {
    String command = toolchain.createCExecutable(
        "out\\foo.exe",
        new LinkedList<String>(Arrays.asList("out\\foo.c.obj")),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<String>(Arrays.asList("out\\foo\\foo.lib", "out\\bar\\bar.dll")));
    String expected = "C:\\Visual Studio\\bin\\cl.exe out\\foo.c.obj  out\\foo\\foo.lib out\\bar\\bar.lib /Feout\\foo.exe  ";
    assertEquals(expected, command);
  }

  @Test
  public void createCxxExecutable() {
    String command = toolchain.createCExecutable(
        "out\\foo.exe",
        new LinkedList<String>(Arrays.asList("out\\foo.cc.obj")),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<String>(Arrays.asList("out\\foo\\foo.lib", "out\\bar\\bar.dll")));
    String expected = "C:\\Visual Studio\\bin\\cl.exe out\\foo.cc.obj  out\\foo\\foo.lib out\\bar\\bar.lib /Feout\\foo.exe  ";
    assertEquals(expected, command);
  }

}

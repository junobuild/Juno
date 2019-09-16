package io.junobuild.toolchain.toolchain;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import org.junit.Test;
import org.junit.BeforeClass;


public class UnixToolchainTest {

  private static IToolchain toolchain;

  @BeforeClass
  public static void createToolchain() {
    toolchain = new UnixToolchain(
        "/usr/bin/gcc",
        "/usr/bin/g++",
        "/usr/bin/ar",
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>(),
        new LinkedList<>());
  }

  @Test
  public void testCreateCObjectFile() {
    String command = toolchain.createCObjectFile(
        "out/source.c.o",
        "source.c",
        new LinkedList<String>(Arrays.asList("Wall", "Wextra")),
        new LinkedList<String>(Arrays.asList("include")));
    System.out.println(command);
    String expected = "/usr/bin/gcc  -Iinclude  -Wall -Wextra -c source.c -o out/source.c.o";
    assertEquals(expected, command);
  }

  @Test
  public void testCreateCxxObjectFile() {
    String command = toolchain.createCxxObjectFile(
        "out/source.cc.o",
        "source.cc",
        new LinkedList<String>(Arrays.asList("Wall", "Wextra")),
        new LinkedList<String>(Arrays.asList("include")));
    System.out.println(command);
    String expected = "/usr/bin/g++  -Iinclude  -Wall -Wextra -c source.cc -o out/source.cc.o";
    assertEquals(expected, command);
  }

  @Test
  public void testCreateStaticLibraryFile() {
    String command = toolchain.createStaticLibrary("out/libfoo.a",
        new LinkedList<String>(Arrays.asList("out/foo.c.o", "out/bar.c.o")));
    String expected = "/usr/bin/ar rcs out/libfoo.a out/foo.c.o out/bar.c.o";
    assertEquals(expected, command);
  }

  @Test
  public void testCreateCSharedLibraryFile() {
    String command = toolchain.createCSharedLibrary("out/libfoo.so",
        new LinkedList<String>(Arrays.asList("out/foo.c.o", "out/bar.c.o")));
    String expected = "/usr/bin/gcc -shared out/foo.c.o out/bar.c.o -o out/libfoo.so";
    assertEquals(expected, command);
  }

  @Test
  public void testCreateCxxSharedLibraryFile() {
    String command = toolchain.createCxxSharedLibrary("out/libfoo.so",
        new LinkedList<String>(Arrays.asList("out/foo.cc.o", "out/bar.cc.o")));
    String expected = "/usr/bin/g++ -shared out/foo.cc.o out/bar.cc.o -o out/libfoo.so";
    assertEquals(expected, command);
  }

  @Test
  public void testCreateCExecutableFile() {
    String command = toolchain.createCExecutable(
        "out/foo",
        new LinkedList<String>(Arrays.asList("out/foo.c.o")),
        new LinkedList<String>(Arrays.asList("/usr/local/lib")),
        new LinkedList<String>(Arrays.asList("pthread")),
        new LinkedList<String>(Arrays.asList("out/foo/libfoo.a", "out/bar/libbar.so")));

    String expected = "/usr/bin/gcc -Wl,-rpath,out/bar/  -L/usr/local/lib out/foo.c.o out/foo/libfoo.a out/bar/libbar.so -lpthread -o out/foo";
    assertEquals(expected, command);
  }

  @Test
  public void testCreateCxxExecutableFile() {
    String command = toolchain.createCxxExecutable(
        "out/foo",
        new LinkedList<String>(Arrays.asList("out/foo.cc.o")),
        new LinkedList<String>(Arrays.asList("/usr/local/lib")),
        new LinkedList<String>(Arrays.asList("pthread")),
        new LinkedList<String>(Arrays.asList("out/foo/libfoo.a", "out/bar/libbar.so")));

    String expected = "/usr/bin/g++ -Wl,-rpath,out/bar/  -L/usr/local/lib out/foo.cc.o out/foo/libfoo.a out/bar/libbar.so -lpthread -o out/foo";
    assertEquals(expected, command);
  }

}
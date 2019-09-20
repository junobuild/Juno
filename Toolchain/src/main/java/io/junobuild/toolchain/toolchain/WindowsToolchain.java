package io.junobuild.toolchain.toolchain;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public final class WindowsToolchain implements IToolchain {

  private String cl;
  private String lib;
  private String link;
  private LinkedList<String> defaultCFlags;
  private LinkedList<String> defaultCxxFlags;
  private LinkedList<String> defaultCIncludeDirs;
  private LinkedList<String> defaultCxxIncludeDirs;
  private LinkedList<String> defaultCLibraryDirs;
  private LinkedList<String> defaultCxxLibraryDirs;

  public WindowsToolchain(
      final String cl,
      final String lib,
      final String link,
      final LinkedList<String> defaultCFlags,
      final LinkedList<String> defaultCxxFlags,
      final LinkedList<String> defaultCIncludeDirs,
      final LinkedList<String> defaultCxxIncludeDirs,
      final LinkedList<String> defaultCLibraryDirs,
      final LinkedList<String> defaultCxxLibraryDirs) {
    this.cl = cl;
    this.lib = lib;
    this.link = link;
    this.defaultCFlags = defaultCFlags;
    this.defaultCxxFlags = defaultCxxFlags;
    this.defaultCIncludeDirs = defaultCIncludeDirs;
    this.defaultCxxIncludeDirs = defaultCxxIncludeDirs;
    this.defaultCLibraryDirs = defaultCLibraryDirs;
    this.defaultCxxLibraryDirs = defaultCxxLibraryDirs;
  }

  @Override
  public String createCObjectFile(
      final String objectFile,
      final String sourceFile,
      final List<String> flags,
      final List<String> includeDirs) {
    return String.join(
        " ",
        cl,
        "/TC",
        String.join(" ", IToolchain.addPrefixToList(defaultCIncludeDirs, "/I")),
        String.join(" ", IToolchain.addPrefixToList(includeDirs, "/I")),
        String.join(" ", IToolchain.addPrefixToList(defaultCFlags, "/")),
        String.join(" ", IToolchain.addPrefixToList(flags, "/")),
        "/c",
        sourceFile,
        "/Fo" + objectFile);
  }

  @Override
  public String createCxxObjectFile(
      final String objectFile,
      final String sourceFile,
      final List<String> flags,
      final List<String> includeDirs) {
    return String.join(
        " ",
        cl,
        "/TP",
        String.join(" ", IToolchain.addPrefixToList(defaultCxxIncludeDirs, "/I")),
        String.join(" ", IToolchain.addPrefixToList(includeDirs, "/I")),
        String.join(" ", IToolchain.addPrefixToList(defaultCxxFlags, "/")),
        String.join(" ", IToolchain.addPrefixToList(flags, "/")),
        "/c",
        sourceFile,
        "/Fo" + objectFile);
  }

  @Override
  public String createStaticLibrary(
      final String libraryFile,
      final List<String> objectFiles) {
    return String.join(
        " ",
        lib,
        String.join(" ", objectFiles),
        "/OUT:" + libraryFile);
  }

  @Override
  public String createCSharedLibrary(
      final String libraryFile,
      final List<String> objectFiles) {
    return String.join(
        " ",
        link,
        "/DLL",
        String.join(" ", objectFiles),
        "/OUT:" + libraryFile);
  }

  @Override
  public String createCxxSharedLibrary(
      final String libraryFile,
      final List<String> objectFiles) {
    return createCSharedLibrary(libraryFile, objectFiles);
  }

  @Override
  public String createCExecutable(
      final String executableFile,
      final List<String> objectFiles,
      final List<String> libraryDirs,
      final List<String> libraries,
      final List<String> dependencies) {
    return String.join(
        " ",
        cl,
        String.join(" ", objectFiles),
        String.join(" ", libraries),
        String.join(" ", fixDeps(dependencies)),
        "/Fe" + executableFile,
        String.join(" ", IToolchain.addPrefixToList(defaultCLibraryDirs, "/LIBPATH:")),
        String.join(" ", IToolchain.addPrefixToList(libraryDirs, "/LIBPATH:")));
  }

  @Override
  public String createCxxExecutable(
      final String executableFile,
      final List<String> objectFiles,
      final List<String> libraryDirs,
      final List<String> libraries,
      final List<String> dependencies) {
    return createCExecutable(
        executableFile,
        objectFiles,
        libraryDirs,
        libraries,
        dependencies);
  }

  private List<String> fixDeps(final List<String> dependencies) {
    List<String> res = new LinkedList<>();
    for (String dep : dependencies) {
      if (FilenameUtils.getExtension(dep).equals("dll")) {
        res.add(FilenameUtils.removeExtension(dep) + ".lib");
      } else {
        res.add(dep);
      }
    }
    return res;
  }

}

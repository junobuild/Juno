package io.junobuild.toolchain.toolchain;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public final class UnixToolchain implements IToolchain {

  private String ccompiler;
  private String cxxCompiler;
  private String ar;
  private LinkedList<String> defaultCFlags;
  private LinkedList<String> defaultCxxFlags;
  private LinkedList<String> defaultCIncludeDirs;
  private LinkedList<String> defaultCxxIncludeDirs;
  private LinkedList<String> defaultCLibraryDirs;
  private LinkedList<String> defaultCxxLibraryDirs;

  public UnixToolchain(
      final String ccompiler,
      final String cxxCompiler,
      final String ar,
      final LinkedList<String> defaultCFlags,
      final LinkedList<String> defaultCxxFlags,
      final LinkedList<String> defaultCIncludeDirs,
      final LinkedList<String> defaultCxxIncludeDirs,
      final LinkedList<String> defaultCLibraryDirs,
      final LinkedList<String> defaultCxxLibraryDirs) {
    this.ccompiler = ccompiler;
    this.cxxCompiler = cxxCompiler;
    this.ar = ar;
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
        ccompiler,
        String.join(" ", IToolchain.addPrefixToList(defaultCIncludeDirs, "-I")),
        String.join(" ", IToolchain.addPrefixToList(includeDirs, "-I")),
        String.join(" ", IToolchain.addPrefixToList(defaultCFlags, "-")),
        String.join(" ", IToolchain.addPrefixToList(flags, "-")),
        "-c",
        sourceFile,
        "-o",
        objectFile);
  }

  @Override
  public String createCxxObjectFile(
      final String objectFile,
      final String sourceFile,
      final List<String> flags,
      final List<String> includeDirs) {
    return String.join(
        " ",
        cxxCompiler,
        String.join(" ", IToolchain.addPrefixToList(defaultCxxIncludeDirs, "-I")),
        String.join(" ", IToolchain.addPrefixToList(includeDirs, "-I")),
        String.join(" ", IToolchain.addPrefixToList(defaultCxxFlags, "-")),
        String.join(" ", IToolchain.addPrefixToList(flags, "-")),
        "-c",
        sourceFile,
        "-o",
        objectFile);
  }

  @Override
  public String createStaticLibrary(
      final String libraryFile,
      final List<String> objectFiles) {
    return String.join(
        " ",
        ar,
        "rcs",
        libraryFile,
        String.join(" ", objectFiles));
  }

  @Override
  public String createCSharedLibrary(
      final String libraryFile,
      final List<String> objectFiles) {
    return String.join(
        " ",
        ccompiler,
        "-shared",
        String.join(" ", objectFiles),
        "-o",
        libraryFile);
  }

  @Override
  public String createCxxSharedLibrary(
      final String libraryFile,
      final List<String> objectFiles) {
    return String.join(
        " ",
        cxxCompiler,
        "-shared",
        String.join(" ", objectFiles),
        "-o",
        libraryFile);
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
        ccompiler,
        String.join(" ",
            IToolchain.addPrefixToList(getSharedLibraryDirs(dependencies), "-Wl,-rpath,")),
        String.join(" ", IToolchain.addPrefixToList(defaultCLibraryDirs, "-L")),
        String.join(" ", IToolchain.addPrefixToList(libraryDirs, "-L")),
        String.join(" ", objectFiles),
        String.join(" ", dependencies),
        String.join(" ", IToolchain.addPrefixToList(libraries, "-l")),
        "-o",
        executableFile);
  }

  @Override
  public String createCxxExecutable(
      final String executableFile,
      final List<String> objectFiles,
      final List<String> libraryDirs,
      final List<String> libraries,
      final List<String> dependencies) {
    return String.join(
        " ",
        cxxCompiler,
        String.join(" ",
            IToolchain.addPrefixToList(getSharedLibraryDirs(dependencies), "-Wl,-rpath,")),
        String.join(" ", IToolchain.addPrefixToList(defaultCxxLibraryDirs, "-L")),
        String.join(" ", IToolchain.addPrefixToList(libraryDirs, "-L")),
        String.join(" ", objectFiles),
        String.join(" ", dependencies),
        String.join(" ", IToolchain.addPrefixToList(libraries, "-l")),
        "-o",
        executableFile);
  }

  @Override
  public String toolchainSetupCommand() {
    return "";
  }

  private List<String> getSharedLibraryDirs(final List<String> deps) {
    LinkedList<String> res = new LinkedList<>();
    for (String dep : deps) {
      if (FilenameUtils.getExtension(dep).equals("so")) {
        res.add(FilenameUtils.getFullPath(dep));
      }
    }
    return res;
  }

}

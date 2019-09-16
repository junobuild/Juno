package io.junobuild.toolchain.toolchain;

import static java.util.stream.Collectors.toList;

import java.util.List;

public interface IToolchain {

  String createCObjectFile(
      String objectFile,
      String sourceFile,
      List<String> flags,
      List<String> includeDirs);

  String createCxxObjectFile(
      String objectFile,
      String sourceFile,
      List<String> flags,
      List<String> includeDirs);

  String createStaticLibrary(
      String libraryFile,
      List<String> objectFiles);

  String createCSharedLibrary(
      String libraryFile,
      List<String> objectFiles);

  String createCxxSharedLibrary(
      String libraryFile,
      List<String> objectFiles);

  String createCExecutable(
      String executableFile,
      List<String> objectFiles,
      List<String> libraryDirs,
      List<String> libraries,
      List<String> dependencies);

  String createCxxExecutable(
      String executableFile,
      List<String> objectFiles,
      List<String> libraryDirs,
      List<String> libraries,
      List<String> dependencies);

  static List<String> addPrefixToList(List<String> list, String prefix) {
    return list.stream().map((String v) -> prefix + v).collect(toList());
  }

}

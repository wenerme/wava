package com.github.wenerme.wava.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/20
 */
public interface Inputs {

  /**
   * 获取 ClassPath 中的资源
   */
  @Nullable
  static InputStream getResourceAsStream(String name) {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    if (is != null) {
      return is;
    }
    is = ClassLoader.getSystemResourceAsStream(name);
    if (is != null) {
      return is;
    }
    return Inputs.class.getClassLoader().getResourceAsStream(name);
  }

  static List<String> getResourceFiles(String path) throws IOException {
    List<String> files = new ArrayList<>();

    InputStream stream = getResourceAsStream(path);
    if (stream != null) {
      try (InputStream in = stream;
        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
        String resource;

        while ((resource = br.readLine()) != null) {
          files.add(resource);
        }
      }
    }

    return files;
  }
}

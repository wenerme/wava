package com.github.wenerme.wava.util;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

/**
 *
 *
 * <ul>
 *   <li>ClassPath resources
 *   <li>InputStream & OutputStream
 *   <li>Conversion
 * </ul>
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2017/3/20
 */
public interface Inputs {

  /**
   * get resource in classpath
   *
   * @return {@code null} if no resource has been found
   */
  @Nullable
  static InputStream getResourceAsStream(String name) {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    if (is != null) {
      return is;
    }
    is = Inputs.class.getClassLoader().getResourceAsStream(name);
    if (is != null) {
      return is;
    }
    return ClassLoader.getSystemResourceAsStream(name);
  }

  @Nullable
  static String getResourceAsString(String name) throws IOException {
    InputStream stream = getResourceAsStream(name);
    if (stream != null) {
      return toString(stream);
    }
    return null;
  }

  /** List resource in classpath */
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

  /** Read all input stream to string using utf8 */
  static String toString(InputStream stream) throws IOException {
    return CharStreams.toString(new InputStreamReader(stream, StandardCharsets.UTF_8));
  }

  static byte[] toBytes(InputStream stream) throws IOException {
    return ByteStreams.toByteArray(stream);
  }
}

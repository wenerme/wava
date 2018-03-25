package com.github.wenerme.wava.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * <ul>
 * <li>定义常用的字符
 * <li>定义分割和合并
 * <li>定义常用操作
 * </ul>
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/7/5
 */
public interface Chars {

  static String toString(InputStream is) throws IOException {
    return CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
  }

  static String toString(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }

  static int startWithAny(String s, String... matches) {
    if (matches == null) {
      return -1;
    }
    int i = 0;
    for (String match : matches) {
      if (s.startsWith(match)) {
        return i;
      }
      i++;
    }
    return -1;
  }

  static int endWithAny(String s, String... matches) {
    if (matches == null) {
      return -1;
    }
    int i = 0;
    for (String match : matches) {
      if (s.endsWith(match)) {
        return i;
      }
      i++;
    }
    return -1;
  }

  static int startWithAny(String s, Iterable<String> matches) {
    if (matches == null) {
      return -1;
    }
    int i = 0;
    for (String match : matches) {
      if (s.startsWith(match)) {
        return i;
      }
      i++;
    }
    return -1;
  }

  static int endWithAny(String s, Iterable<String> matches) {
    if (matches == null) {
      return -1;
    }
    int i = 0;
    for (String match : matches) {
      if (s.endsWith(match)) {
        return i;
      }
      i++;
    }
    return -1;
  }

  static String reverse(String s) {
    return new StringBuilder(s).reverse().toString();
  }

  static Converter<String, String> upperCamelToUnderscoreConverter() {
    return Holder.UPPER_CAMEL_UNDERSCORE;
  }

  static Splitter commaSplitter() {
    return Holder.SP_COMMA;
  }

  static Joiner dotJoiner() {
    return Holder.JO_DOT;
  }

  static Joiner commaJoiner() {
    return Holder.JO_COMMA;
  }

  static Joiner.MapJoiner commaEqualMapJoiner() {
    return Holder.JO_COMMA_EQUAL;
  }

  static Joiner equalJoiner() {
    return Holder.JO_EQUAL;
  }

  final class Holder {

    private static final Splitter SP_COMMA = Splitter.on(',').omitEmptyStrings().trimResults();
    private static final Joiner JO_DOT = Joiner.on('.');
    private static final Joiner JO_COMMA = Joiner.on(',');
    private static final Joiner.MapJoiner JO_COMMA_EQUAL = commaJoiner().withKeyValueSeparator("=");
    private static final Joiner JO_EQUAL = Joiner.on('=');

    private static final Converter<String, String> UPPER_CAMEL_UNDERSCORE =
      CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);
  }
}

package me.wener.wava.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 *
 *
 * <ul>
 *   <li>Common word
 *   <li>CharMatcher
 *   <li>Splitter & Joiner
 * </ul>
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @see <a href="https://zh.wikipedia.org/wiki/中文数字">Wikipedia: 中文数字</a>
 * @since 2017/4/24
 */
public interface Chinesese {

  /** 中文逗号 */
  static char comma() {
    return Holder.COMMA;
  }

  /** 小写的中文数字 */
  static String upperNumerals() {
    return Holder.UPPER_NUMERALS;
  }

  /** 大写的中文数字 */
  static String lowerNumerals() {
    return Holder.LOWER_NUMERALS;
  }

  /** 中文数字 */
  static String numerals() {
    return Holder.NUMERALS;
  }

  /** 判断字符是否为中文 */
  static boolean isChinese(int c) {
    return Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN;
  }

  /** 判断字符串是否为中文 */
  static boolean isChinese(CharSequence s) {
    return s.codePoints().allMatch(Chinesese::isChinese);
  }

  static CharMatcher upperNumeralsMatcher() {
    return Holder.UPPER_NUMERALS_MATCHER;
  }

  static CharMatcher lowerNumeralsMatcher() {
    return Holder.LOWER_NUMERALS_MATCHER;
  }

  static CharMatcher numeralsMatcher() {
    return Holder.NUMERALS_MATCHER;
  }

  static CharMatcher unicodeScriptMatcher(Character.UnicodeScript unicodeScript) {
    //noinspection ConstantConditions v will never be null
    return CharMatcher.forPredicate(v -> Character.UnicodeScript.of(v) == unicodeScript);
  }

  static CharMatcher hanUnicodeScriptMatcher() {
    return Holder.HAN_MATCHER;
  }

  static Splitter commaSplitter() {
    return Holder.SP_COMMA;
  }

  static Joiner commaJoiner() {
    return Holder.JO_COMMA;
  }

  final class Holder {

    static final String UPPER_NUMERALS = "零壹贰叁肆伍陆柒捌玖拾佰仟萬億";
    static final CharMatcher UPPER_NUMERALS_MATCHER = CharMatcher.anyOf(UPPER_NUMERALS);
    static final String LOWER_NUMERALS = "〇一二三四五六七八九十百千万亿";
    static final String NUMERALS = UPPER_NUMERALS.concat(LOWER_NUMERALS);
    static final CharMatcher LOWER_NUMERALS_MATCHER = CharMatcher.anyOf(LOWER_NUMERALS);
    static final CharMatcher NUMERALS_MATCHER = CharMatcher.anyOf(NUMERALS);
    static final CharMatcher HAN_MATCHER = unicodeScriptMatcher(Character.UnicodeScript.HAN);
    static final char COMMA = '，';
    static final Splitter SP_COMMA =
        Splitter.on(Chinesese.comma()).omitEmptyStrings().trimResults();
    static final Joiner JO_COMMA = Joiner.on(Chinesese.comma());
  }
}

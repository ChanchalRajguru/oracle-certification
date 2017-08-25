package io.mincong.ocpjp.regex;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * Tests for review notes: the main points covered in this chapter.
 *
 * @author Mincong Huang
 */
public class ReviewNoteTest {

  /* Regular expressions */

  /**
   * A regex has a syntax, which can be defined by using regular and
   * special characters.
   * <p>
   * As opposed to exact matches, you can use regex to search for
   * data that matches a pattern.
   * <p>
   * Class {@link Pattern} is a compiled representation of a regular
   * expression. It doesn't define a public constructor. You can
   * instantiate this class by using its factory method
   * {@link Pattern#compile(String)}.
   */
  @Test
  public void regexCharacters() throws Exception {
    // Using a regular character
    List<Integer> startIndexes = new ArrayList<>();
    Pattern pattern = Pattern.compile("c");
    Matcher matcher = pattern.matcher("coucou");
    while (matcher.find()) {
      startIndexes.add(matcher.start());
    }
    assertThat(startIndexes).containsExactly(0, 3);

    // Using a spacial character
    startIndexes = new ArrayList<>();
    pattern = Pattern.compile("6+");
    matcher = pattern.matcher("6 66 666");
    while (matcher.find()) {
      startIndexes.add(matcher.start());
    }
    assertThat(startIndexes).containsExactly(0, 2, 5);
  }

  /**
   * Character classes aren't classes defined in the Java API. The
   * term refers to a set of characters that you an enclose within
   * square brackets <tt>[]</tt>.
   */
  @Test
  public void characterClass() throws Exception {
    Pattern pattern = Pattern.compile("[abc]");
    Matcher matcher = pattern.matcher("a b 1 2");
    List<String> results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("'a': [0,1[", "'b': [2,3[");
  }

  /**
   * Java supports predefined and custom character classes.
   */
  @Test
  public void predefinedCharacterClass() throws Exception {
    Pattern pattern = Pattern.compile("\\p{Alnum}");
    Matcher matcher = pattern.matcher("a b 1 2");
    List<String> results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("'a': [0,1[", "'b': [2,3[", "'1': [4,5[", "'2': [6,7[");
  }

  /**
   * You create a custom character class by enclosing a set of
   * characters within square brackets <tt>[]</tt>:
   * <ul>
   * <li><tt>[fdn]</tt> can be used to find an exact match of 'f',
   * 'd', or 'n'.
   * <li><tt>[^fdn]</tt> can be used to find a character that doesn't
   * match either 'f', 'd', or 'n'.
   * <li><tt>[a-cA-C]</tt> can be used to find an exact match of
   * either 'a', 'b', 'c', 'A', 'B', or 'C'.
   * </ul>
   */
  @Test
  public void customCharacterClass() throws Exception {
    Pattern pattern;
    Matcher matcher;
    List<String> results;

    // Character class: [fdn]
    pattern = Pattern.compile("[fnd]");
    matcher = pattern.matcher("found");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("'f': [0,1[", "'n': [3,4[", "'d': [4,5[");

    // Character class: [^fdn]
    pattern = Pattern.compile("[^fnd]");
    matcher = pattern.matcher("found");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("'o': [1,2[", "'u': [2,3[");

    // Character class: [a-cA-C]
    pattern = Pattern.compile("[a-cA-C]");
    matcher = pattern.matcher("found");
    results = MatcherHelper.collect(matcher);
    assertThat(results).isEmpty();
  }

  /**
   * You can use these predefined character classes as follows:
   *
   * <ul>
   * <li>A dot matches any character (and may or may not match line
   * terminators).</li>
   * <li>'\d' matches any digit: <tt>[0-9]</tt>.
   * <li>'\D' matches a non-digit: <tt>[^0-9]</tt>.
   * <li>'\s' matches a whitespace character: ' ' (space), '\t'
   * (tab), '\n' (new line), '\x0B' (end of line), '\f' (form feed),
   * '\r' (carriage).
   * <li>'\S' matches a non-whitespace character: <tt>[^\s]</tt>.
   * <li>'\w' matches a word character: <tt>[a-zA-Z_0-9]</tt>.
   * <li>'\W' matches a non-word character: <tt>[^\w]</tt>.
   * </ul>
   */
  @Test
  public void predefinedCharacterClass2() throws Exception {
    Pattern pattern;
    Matcher matcher;
    List<String> results;

    pattern = Pattern.compile(".");
    matcher = pattern.matcher("_a 1B");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'_': [0,1[",
        "'a': [1,2[",
        "' ': [2,3[",
        "'1': [3,4[",
        "'B': [4,5["
    );

    pattern = Pattern.compile("\\d");
    matcher = pattern.matcher("_a 1B");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("'1': [3,4[");

    pattern = Pattern.compile("\\D");
    matcher = pattern.matcher("_a 1B");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'_': [0,1[",
        "'a': [1,2[",
        "' ': [2,3[",
        "'B': [4,5["
    );

    pattern = Pattern.compile("\\s");
    matcher = pattern.matcher("_a 1B");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("' ': [2,3[");

    pattern = Pattern.compile("\\S");
    matcher = pattern.matcher("_a 1B");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'_': [0,1[",
        "'a': [1,2[",
        "'1': [3,4[",
        "'B': [4,5["
    );

    pattern = Pattern.compile("\\w");
    matcher = pattern.matcher("_a 1B");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'_': [0,1[",
        "'a': [1,2[",
        "'1': [3,4[",
        "'B': [4,5["
    );

    pattern = Pattern.compile("\\W");
    matcher = pattern.matcher("_a 1B");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("' ': [2,3[");
  }

  /**
   * Boundary matchers:
   *
   * <ul>
   * <li>'\b' indicates a word boundary.
   * <li>'\B' indicates a non-word boundary.
   * <li>'^' indicates the beginning of a line.
   * <li>'$' indicates the end of a line.
   * </ul>
   */
  @Test
  public void boundaryMatchers() throws Exception {
    Pattern pattern;
    Matcher matcher;
    List<String> results;

    pattern = Pattern.compile("\\b");
    matcher = pattern.matcher("Hello world!");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'': [0,0[",
        "'': [5,5[",
        "'': [6,6[",
        "'': [11,11["
    );

    pattern = Pattern.compile("\\B");
    matcher = pattern.matcher("Hello world!");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'': [1,1[",
        "'': [2,2[",
        "'': [3,3[",
        "'': [4,4[",
        "'': [7,7[",
        "'': [8,8[",
        "'': [9,9[",
        "'': [10,10[",
        "'': [12,12["
    );

    pattern = Pattern.compile("^", Pattern.MULTILINE);
    matcher = pattern.matcher("Line1\nLine2\n");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("'': [0,0[", "'': [6,6[");

    pattern = Pattern.compile("$", Pattern.MULTILINE);
    matcher = pattern.matcher("Line1\nLine2\n");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly("'': [5,5[", "'': [11,11[", "'': [12,12[");
  }

  /**
   * You can specify the number of occurrences of a pattern to match
   * in a target value by using quantifiers.
   * <p>
   * The coverage of quantifiers on this exam is limited to the
   * following greedy quantifiers:
   *
   * <ul>
   * <li><tt>X?</tt> matches X, once or not at all.
   * <li><tt>X*</tt> matches X, zero or more times.
   * <li><tt>X+</tt> matches X, one or more times.
   * <li><tt>X{min, max}</tt> matches X, within the specified range.
   * </ul>
   */
  @Test
  public void specifyNbToMatch() throws Exception {
    Pattern pattern;
    Matcher matcher;
    List<String> results;

    pattern = Pattern.compile("6? ");
    matcher = pattern.matcher(" 6 66 666 ");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "' ': [0,1[",
        "'6 ': [1,3[",
        "'6 ': [4,6[",
        "'6 ': [8,10["
    );

    pattern = Pattern.compile("6* ");
    matcher = pattern.matcher(" 6 66 666 ");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "' ': [0,1[",
        "'6 ': [1,3[",
        "'66 ': [3,6[",
        "'666 ': [6,10["
    );

    pattern = Pattern.compile("6+ ");
    matcher = pattern.matcher(" 6 66 666 ");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'6 ': [1,3[",
        "'66 ': [3,6[",
        "'666 ': [6,10["
    );

    pattern = Pattern.compile("6{2,3} ");
    matcher = pattern.matcher(" 6 66 666 ");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'66 ': [3,6[",
        "'666 ': [6,10["
    );
  }

  /**
   * Regex in Java supports Unicode, as it matches against the
   * {@link CharSequence} objects.
   * <p>
   * Class {@link Matcher} is referred to as an engine that scans a
   * target {@link CharSequence} for a matching regex pattern. Class
   * {@link Matcher} doesn't define a public constructor. You can
   * create and access a {@link Matcher} object by calling the
   * instance method {@link Pattern#matcher(CharSequence)} on an
   * object of class {@link Pattern}.
   * <p>
   * When you have access to the {@link Matcher} object, you can
   * match a complete input sequence against a pattern, match the
   * input sequence starting at the beginning, find multiple
   * occurrences of the matching pattern, or retrieve information
   * about the matching groups.
   */
  @Test
  public void unicodeSupport() throws Exception {
    Pattern pattern;
    Matcher matcher;
    List<String> results;

    pattern = Pattern.compile("\\p{IsIdeographic}");
    matcher = pattern.matcher("好（Tian）好（Tian）学（Jia）习（Ban）");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'好': [0,1[",
        "'好': [7,8[",
        "'学': [14,15[",
        "'习': [20,21["
    );

    pattern = Pattern.compile("[^\\p{IsIdeographic}]+");
    matcher = pattern.matcher("好（Tian）好（Tian）学（Jia）习（Ban）");
    results = MatcherHelper.collect(matcher);
    assertThat(results).containsExactly(
        "'（Tian）': [1,7[",
        "'（Tian）': [8,14[",
        "'（Jia）': [15,20[",
        "'（Ban）': [21,26["
    );
  }

  /* Search, parse, and build strings */
  // TODO

  /* Formatting strings */
  // TODO
}

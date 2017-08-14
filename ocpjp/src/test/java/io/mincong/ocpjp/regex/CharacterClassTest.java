package io.mincong.ocpjp.regex;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * <i>Character classes</i> aren't classes defined in the Java API.
 * The term refers to a set of characters that you can enclose within
 * square brackets (<tt>[]</tt>). When used in a regex pattern, Java
 * looks for exactly <b>one</b> of the specified <i>characters</i>
 * (not words).
 *
 * @author Mincong Huang
 */
public class CharacterClassTest {

  @Test
  public void simple1() throws Exception {
    String value = "Both organization and organisation are correct.";
    String regex = "organi[sz]ation";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    assertThat(asString(matcher)).isEqualTo("organization: [5,17[; organisation: [22,34[;");
  }

  @Test
  public void simple2() throws Exception {
    String value = "do go no";
    String regex  = "[dgn]o";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    assertThat(asString(matcher)).isEqualTo("do: [0,2[; go: [3,5[; no: [6,8[;");
  }

  @Test
  public void range() throws Exception {
    String value = "efg 789";
    String regex = "[a-f0-7]";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    assertThat(asString(matcher)).isEqualTo("e: [0,1[; f: [1,2[; 7: [4,5[;");
  }

  @Test
  public void negation() throws Exception {
    String value = "12345";
    String regex = "[^123]";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    assertThat(asString(matcher)).isEqualTo("4: [3,4[; 5: [4,5[;");
  }

  private static String asString(Matcher matcher) {
    StringBuilder b = new StringBuilder();
    while (matcher.find()) {
      b.append(matcher.group());
      b.append(": [");
      b.append(matcher.start()); // Inclusive index
      b.append(',');
      b.append(matcher.end()); // Exclusive index
      b.append("[; ");
    }
    return b.toString().trim();
  }

}

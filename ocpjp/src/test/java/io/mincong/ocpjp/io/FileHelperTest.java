package io.mincong.ocpjp.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Mincong Huang
 */
public class FileHelperTest {

  @Rule
  public TemporaryFolder temporaryDir = new TemporaryFolder();

  @Test(timeout = 1000L)
  public void copyBySingleByte() throws Exception {
    File source = temporaryDir.newFile("source.txt");
    File target = temporaryDir.newFile("target.txt");
    Files.write(source.toPath(), Arrays.asList("Line 1", "Line 2"), StandardCharsets.UTF_8);

    FileHelper.copyBySingleByte(source, target);

    List<String> outputLines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
    assertThat(outputLines).containsExactly("Line 1", "Line 2");
  }

  @Test(timeout = 1000L)
  public void copyByMultiBytes() throws Exception {
    File source = temporaryDir.newFile("source.txt");
    File target = temporaryDir.newFile("target.txt");
    Files.write(source.toPath(), Arrays.asList("Line 1", "Line 2"), StandardCharsets.UTF_8);

    FileHelper.copyByMultiBytes(source, target);

    List<String> outputLines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
    assertThat(outputLines).containsExactly("Line 1", "Line 2");
  }

}

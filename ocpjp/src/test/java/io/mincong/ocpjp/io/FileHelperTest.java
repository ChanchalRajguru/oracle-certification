package io.mincong.ocpjp.io;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;

/**
 * @author Mincong Huang
 */
public class FileHelperTest {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(2);

  @Rule
  public TemporaryFolder temporaryDir = new TemporaryFolder();

  private File source;

  private File target;

  private final int size = 10_000;

  private final List<String> lines = IntStream.range(0, size)
      .mapToObj(i -> "Line " + i)
      .collect(Collectors.toList());

  @Before
  public void setUp() throws Exception {
    source = temporaryDir.newFile("source.txt");
    target = temporaryDir.newFile("target.txt");
    Files.write(source.toPath(), lines, UTF_8);
  }

  @Test
  public void copyBySingleByte() throws Exception {
    FileHelper.copyBySingleByte(source, target);
    assertThat(Files.readAllLines(target.toPath(), UTF_8)).hasSize(size);
  }

  @Test
  public void copyByMultiBytes() throws Exception {
    FileHelper.copyByMultiBytes(source, target);
    assertThat(Files.readAllLines(target.toPath(), UTF_8)).hasSize(size);
  }

}

package io.mincong.ocpjp.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FilenameFilter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * This section lists the main points covered in this chapter.
 * <p>
 * Working with class {@link File}:
 * <ul>
 * <li>{@link File} is an abstract representation of a path to a file
 * or a directory.
 * <li>You can use an object of class {@link File} to create a new
 * file or directory, delete it, or inquire about or modify its
 * attributes.
 * <li>A {@link File} instance might not be necessarily associated
 * with an actual file or directory.
 * <li>{@link File#isFile()} returns true if the path it refers to is
 * a directory.
 * <li>{@link File#isDirectory()} returns true if the path it refers
 * to is a (regular) file.
 * <li>For a directory, {@link File#list()} returns an array of
 * subdirectories and files as string. You can use method
 * {@link File#list(FilenameFilter)} to filter the listing result.
 * <li>For a directory, {@link File#listFiles()} returns an array of
 * subdirectories and files as {@link File}. You can use method
 * {@link File#listFiles(FilenameFilter)} to filter the listing
 * result.
 * <li>You can create a {@link File} instance that represents a
 * nonexistent file on your file system. And you can even invoke
 * methods like {@link File#isFile()} without getting an exception.
 * <li>The objects of class {@link File} are immutable; the pathname
 * represented by a {@link File} object cannot be changed.
 * <li>Methods {@link File#createNewFile()}, {@link File#mkdir()},
 * and {@link File#mkdirs()} can be used to create new files or
 * directories.
 * </ul>
 *
 * @author Mala Gupta
 * @author Mincong Huang
 */
public class ReviewNoteTest {

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  private File fileA;

  private File dirA;

  @Before
  public void setUp() throws Exception {
    fileA = tempFolder.newFile("fileA");
    dirA = tempFolder.newFolder("dirA");
  }

  /* Working with class `java.io.File` */

  @Test
  public void createNewFile() throws Exception {
    File root = tempFolder.getRoot();
    File file = new File(root, "b");
    boolean isCreated = file.createNewFile();
    assertThat(isCreated).isTrue();
  }

  @Test
  public void createNewDirectory() throws Exception {
    File root = tempFolder.getRoot();
    assertThat(new File(root, "dirB").mkdir()).isTrue();
    assertThat(new File(root, "c/d/e").mkdirs()).isTrue();
  }

  @Test
  public void isDirectory() throws Exception {
    assertThat(dirA.isDirectory()).isTrue();
    assertThat(fileA.isDirectory()).isFalse();
  }

  @Test
  public void isFile() throws Exception {
    assertThat(fileA.isFile()).isTrue();
    assertThat(dirA.isFile()).isFalse();
  }

  @Test
  public void list() throws Exception {
    String[] files;

    files = tempFolder.getRoot().list();
    assertThat(files).containsExactlyInAnyOrder("fileA", "dirA");

    files = tempFolder.getRoot().list((dir, name) -> name.startsWith("file"));
    assertThat(files).containsExactly("fileA");
  }

  @Test
  public void listFiles() throws Exception {
    File[] files;

    files = tempFolder.getRoot().listFiles();
    assertThat(files).containsExactlyInAnyOrder(fileA, dirA);

    files = tempFolder.getRoot().listFiles((dir, name) -> name.startsWith("file"));
    assertThat(files).containsExactly(fileA);
  }

  @Test
  public void nonexistentFile() throws Exception {
    File file = new File("foo");
    assertThat(file.exists()).isFalse();
    assertThat(file.isFile()).isFalse(); // No exception
  }

}

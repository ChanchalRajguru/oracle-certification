package io.mincong.ocpjp.nio;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Set;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Mincong Huang
 */
public class PathTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private Path r;

  private Path d1;

  private Path d2;

  @Before
  public void setUp() throws Exception {
    r = temporaryFolder.getRoot().toPath();
    d1 = Files.createDirectory(r.resolve("dir1"));
    d2 = Files.createDirectory(r.resolve("dir2"));
  }

  @Test
  @Ignore("Only executable on Mincong's laptop")
  public void manipulatePaths() throws Exception {
    Path path = Paths.get("/Users/mincong/github/oracle-certification");
    System.out.println("toString():     " + path.toString());
    System.out.println("getRoot():      " + path.getRoot());
    System.out.println("getName(0):     " + path.getName(0));
    System.out.println("getName(1):     " + path.getName(1));
    System.out.println("getFileName():  " + path.getFileName());
    System.out.println("getNameCount(): " + path.getNameCount());
    System.out.println("getParent():    " + path.getParent());
    System.out.println("subpath(0, 2):  " + path.subpath(0, 2));
  }

  @Test
  public void exists() throws Exception {
    assertThat(Files.exists(r)).isTrue();
    assertThat(Files.exists(d1)).isTrue();
    assertThat(Files.exists(d2)).isTrue();
  }

  @Test
  public void notExists() throws Exception {
    /*
     * Method `notExists()` is NOT complement of method `exists()`.
     * It returns `true` if a target doesn't exist. If these methods
     * cannot determine the existence of a file, both of them will
     * return `false`.
     *
     * You must access the file system to verify that a particular
     * `Path` exists, or does not exist. When you are testing a
     * file's existence, three results are possible:
     *
     * 1. The file is verified to exist.
     * 2. The file is verified to not exist.
     * 3. The file's status is unknown. This result can occur when
     *    the program does not have access to the file.
     */
    assertThat(Files.notExists(r.resolve("nonexistent"))).isTrue();
  }

  @Test
  public void resolve() throws Exception {
    Path path = r.resolve("foo");
    assertThat(path.getParent()).isEqualTo(r);
  }

  @Test
  public void resolveSibling_regularFile() throws Exception {
    // Given a filepath under directory `dir1`
    Path source = Files.createFile(d1.resolve("foo"));

    // When sibling-resolving such filepath
    Path target = source.resolveSibling("bar");

    // Then target filepath is resolved using the parent of the source filepath
    assertThat(source.getParent()).isEqualTo(d1);
    assertThat(target.getParent()).isEqualTo(d1);
  }

  @Test
  public void resolveSibling_directory() throws Exception {
    // Given a dir-path under directory `dir1`
    Path source = Files.createDirectory(d1.resolve("subA"));

    // When sibling-resolving such dir-path
    Path target = source.resolveSibling("subB");

    // Then target filepath is resolved using the parent of the source filepath
    assertThat(source.getParent()).isEqualTo(d1);
    assertThat(target.getParent()).isEqualTo(d1);
  }

  @Test
  public void relativize() throws Exception {
    Path relativeD1 = r.relativize(d1);
    assertThat(relativeD1.toString()).isEqualTo("dir1");
  }

  @Test
  public void createDirectories() throws Exception {
    Files.createDirectories(r.resolve("a/b/c"));

    assertThat(r.resolve("a")).exists();
    assertThat(r.resolve("a/b")).exists();
    assertThat(r.resolve("a/b/c")).exists();
  }

  @Test
  public void copy() throws Exception {
    Path source = r.resolve("source");
    Files.write(source, Arrays.asList("Line 1", "Line 2", "Line 3"));

    Path target = r.resolve("target");
    assertThat(target).doesNotExist();

    Files.copy(source, target);
    assertThat(Files.readAllLines(target)).containsExactly("Line 1", "Line 2", "Line 3");
  }

  @Test(expected = FileAlreadyExistsException.class)
  public void copy_failed() throws Exception {
    Path source = r.resolve("source");
    Files.createFile(source);

    Path target = r.resolve("target");
    Files.createFile(target);

    Files.copy(source, target);
  }

  @Test
  public void copy_optionCopyAttributes() throws Exception {
    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");

    Path target = r.resolve("target");
    Path source = r.resolve("source");
    Files.createDirectory(source);
    Files.setPosixFilePermissions(source, permissions);

    Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
    assertThat(target).isDirectory();
    assertThat(Files.getPosixFilePermissions(target)).containsAll(permissions);
  }

  @Test
  public void copy_optionReplaceExisting() throws Exception {
    Path source = r.resolve("source");
    Files.write(source, Arrays.asList("1", "2", "3"));

    Path target = r.resolve("target");
    Files.write(target, Arrays.asList("A", "B", "C"));

    /*
     * Method `copy()` in class `Files` doesn't allow you to append
     * data to an existing file; rather, it creates a new file or
     * replaces an existing one.
     */
    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    assertThat(Files.readAllLines(target)).containsExactly("1", "2", "3");
  }

  @Test
  public void copy_inputStream() throws Exception {
    Path source = r.resolve("source");
    Files.write(source, Arrays.asList("1", "2", "3"));
    Path target = r.resolve("target");

    try (InputStream inputStream = new FileInputStream(source.toFile())) {
      Files.copy(inputStream, target);
    }
    assertThat(Files.readAllLines(target)).containsExactly("1", "2", "3");
  }

  @Test
  public void move_file() throws Exception {
    Path source = r.resolve("source");
    Files.write(source, Arrays.asList("1", "2", "3"));
    Path target = r.resolve("target");

    Files.move(source, target);

    assertThat(Files.readAllLines(target)).containsExactly("1", "2", "3");
    assertThat(source).doesNotExist();
  }

  @Test
  public void move_directory() throws Exception {
    Path regularFile = d1.resolve("file");
    Files.createFile(regularFile);

    Files.move(d1, d2, StandardCopyOption.REPLACE_EXISTING);

    assertThat(d1).doesNotExist();
    assertThat(d2).exists();
    assertThat(d2.resolve("file")).exists();
  }

  @Test(expected = NoSuchFileException.class)
  public void delete_nonexistentFile() throws Exception {
    Files.delete(r.resolve("nonexistent"));
  }

  @Test
  public void delete_existingFile() throws Exception {
    Path regularFile = r.resolve("file");
    Files.createFile(regularFile);
    Files.delete(regularFile);

    assertThat(r.resolve("file")).doesNotExist();
  }

  @Test
  public void delete_emptyDirectory() throws Exception {
    Files.delete(d1);
    assertThat(r.resolve("dir1")).doesNotExist();
  }

  @Test(expected = DirectoryNotEmptyException.class)
  public void delete_nonEmptyDirectory() throws Exception {
    Path regularFile = d1.resolve("file");
    Files.createFile(regularFile);
    Files.delete(d1);
  }

  @Test
  public void deleteIfExists_nonexistentFile() throws Exception {
    assertThat(Files.deleteIfExists(r.resolve("nonexistent"))).isFalse();
  }

}

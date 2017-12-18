package io.mincong.ocpjp.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mincong Huang
 */
public class CollectionTest {

  private List<Book> books;

  @Before
  public void setUp() throws Exception {
    books = new ArrayList<>();
    books.add(new Book("A"));
    books.add(new Book("B"));
  }

  /* Collection-related tests */

  @Test
  public void forEach_list() throws Exception {
    StringBuilder sb1 = new StringBuilder();
    books.forEach(sb1::append);
    assertThat(sb1.toString()).isEqualTo("AB");

    StringBuilder sb2 = new StringBuilder();
    books.forEach(b -> sb2.append(b.getName()).append(' '));
    assertThat(sb2.toString()).isEqualTo("A B ");
  }

  @Test
  public void forEach_streams() throws Exception {
    StringBuilder sb = new StringBuilder();
    Stream.of(new Book("A"), new Book("B"))
        .forEach(sb::append);
    assertThat(sb.toString()).isEqualTo("AB");
  }

  @Test
  public void createPipeline() throws Exception {
    List<String> names = books.stream()
        .map(Book::getName)
        .collect(Collectors.toList());
    assertThat(names).containsExactly("A", "B");
  }

  @Test
  public void filterCollection() throws Exception {
    List<Book> filtered = books.stream()
        .filter(b -> b.getName().startsWith("A"))
        .collect(Collectors.toList());
    assertThat(filtered).containsExactly(new Book("A"));
  }

  /* Stream API related tests */

  @Test
  public void peek() throws Exception {
    List<Integer> positives = new ArrayList<>();
    int sum = Stream.of(-1, 0, 1, 2, 3)
        .filter(i -> i > 0)
        .peek(positives::add)
        .mapToInt(Integer::intValue)
        .sum();

    assertThat(positives).containsExactly(1, 2, 3);
    assertThat(sum).isEqualTo(6);
  }

  @Test
  public void map() throws Exception {
    String s = Stream.of(-1, 0, 1, 2, 3)
        .filter(i -> i > 0)
        .map(String::valueOf)
        .collect(Collectors.joining(", "));
    assertThat(s).isEqualTo("1, 2, 3");
  }

  @Test
  public void findFirst_normalStream() throws Exception {
    Optional<String> first = Stream.of("A", "B").findFirst();
    assertThat(first.isPresent()).isTrue();
    assertThat(first.orElse("C")).isEqualTo("A");
  }

  @Test
  public void findFirst_emptyStream() throws Exception {
    Optional<String> first = new ArrayList<String>().stream().findFirst();
    assertThat(first.isPresent()).isFalse();
  }

  @Test
  public void findAny_normalStream() throws Exception {
    Optional<String> any = Stream.of("A", "B").findAny();
    assertThat(any.isPresent()).isTrue();
  }

  @Test
  public void findAny_emptyStream() throws Exception {
    Optional<String> any = new ArrayList<String>().stream().findAny();
    assertThat(any.isPresent()).isFalse();
  }

  @Test
  public void anyMatch() throws Exception {
    boolean hasMatched = Stream.of(-1, -2, 3).anyMatch(i -> i > 0);
    assertThat(hasMatched).isTrue();
  }

  @Test
  public void allMatch() throws Exception {
    assertThat(Stream.of(-1, -2, 3).allMatch(i -> i > 0)).isFalse();
    assertThat(Stream.of(-1, -2, 3).allMatch(i -> i < 4)).isTrue();
  }

  @Test
  public void noneMatch() throws Exception {
    assertThat(Stream.of(-1, -2, 3).noneMatch(i -> i == 0)).isTrue();
    assertThat(Stream.of(-1, -2, 3).noneMatch(i -> i < 10)).isFalse();
  }

  private static class Book {

    private String name;

    Book(String name) {
      this.name = name;
    }

    String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Book)) {
        return false;
      }

      Book book = (Book) o;

      return name.equals(book.name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }

    @Override
    public String toString() {
      return name;
    }
  }

}

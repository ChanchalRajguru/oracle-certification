package io.mincong.ocpjp.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
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

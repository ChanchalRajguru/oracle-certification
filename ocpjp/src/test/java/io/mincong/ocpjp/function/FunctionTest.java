package io.mincong.ocpjp.function;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mincong Huang
 */
public class FunctionTest {

  private List<String> words;

  private List<Integer> numbers;

  private Map<Integer, Integer> map;

  @Before
  public void setUp() throws Exception {
    words = Arrays.asList("Hi", "Java", "Functional");
    numbers = Arrays.asList(0, 1, 2, 3);
    map = new HashMap<>();
    map.put(1, 2);
    map.put(2, 4);
    map.put(3, 6);
  }

  /* Predicates */

  @Test
  public void predicate1() throws Exception {
    Predicate<String> filter = s -> s.startsWith("H");

    List<String> filteredWords = words.stream()
        .filter(filter)
        .collect(Collectors.toList());
    assertThat(filteredWords).containsExactly("Hi");

    List<String> otherWords = words.stream()
        .filter(filter.negate())
        .collect(Collectors.toList());
    assertThat(otherWords).containsExactly("Java", "Functional");
  }

  @Test
  public void predicate2() throws Exception {
    Predicate<Integer> isPositive = i -> i > 0;
    List<Integer> values = numbers.stream()
        .filter(isPositive)
        .collect(Collectors.toList());
    assertThat(values).containsExactly(1, 2, 3);
  }

  @Test
  public void intPredicate() throws Exception {
    IntPredicate isPositive = i -> i > 0;
    List<Integer> values = numbers.stream()
        .filter(isPositive::test)
        .collect(Collectors.toList());
    assertThat(values).containsExactly(1, 2, 3);
  }

  /* Consumers */

  @Test
  public void consumer() throws Exception {
    List<String> values = new ArrayList<>();
    Consumer<String> insert = values::add;
    words.forEach(insert);
    assertThat(values).containsExactly("Hi", "Java", "Functional");
  }

  @Test
  public void intConsumer() throws Exception {
    List<Integer> values = new ArrayList<>();
    IntConsumer insert = values::add;
    numbers.forEach(insert::accept);
    assertThat(values).containsExactly(0, 1, 2, 3);
  }

  @Test
  public void biConsumer() throws Exception {
    List<Integer> values = new ArrayList<>();
    BiConsumer<Integer, Integer> insertMultiplication = (x, y) -> values.add(x * y);
    map.forEach(insertMultiplication);
    assertThat(values).containsExactly(2, 8, 18);
  }

  /* Functions */

  @Test
  public void function1() throws Exception {
    Function<Integer, Integer> doubleMe = i -> i * 2;
    List<Integer> values = numbers.stream()
        .map(doubleMe)
        .collect(Collectors.toList());
    assertThat(values).containsExactly(0, 2, 4, 6);
  }

  @Test
  public void unaryOperator() throws Exception {
    UnaryOperator<Integer> doubleMe = i -> i * 2;
    List<Integer> values = numbers.stream()
        .map(doubleMe)
        .collect(Collectors.toList());
    assertThat(values).containsExactly(0, 2, 4, 6);
  }

  @Test
  public void intUnaryOperator() throws Exception {
    IntUnaryOperator doubleMe = i -> i * 2;
    IntUnaryOperator plusOne = i -> i + 1;
    IntUnaryOperator plusOneThenDouble = doubleMe.compose(plusOne);
    IntUnaryOperator doubleThenPlusOne = doubleMe.andThen(plusOne);

    Function<IntUnaryOperator, List<Integer>> f = operator -> numbers.stream()
        .map(operator::applyAsInt)
        .collect(Collectors.toList());

    assertThat(f.apply(doubleMe)).containsExactly(0, 2, 4, 6);
    assertThat(f.apply(plusOneThenDouble)).containsExactly(2, 4, 6, 8);
    assertThat(f.apply(doubleThenPlusOne)).containsExactly(1, 3, 5, 7);
  }

  @Test
  public void function2() throws Exception {
    List<String> entries = map.entrySet()
        .stream()
        .map(e -> "(" + e.getKey() + ", " + e.getValue() + ")")
        .collect(Collectors.toList());
    assertThat(entries).containsExactly("(1, 2)", "(2, 4)", "(3, 6)");
  }

  @Test
  public void intFunction() throws Exception {
    IntFunction<String> toStr = String::valueOf;
    List<String> values = numbers.stream()
        .map(toStr::apply)
        .collect(Collectors.toList());
    assertThat(values).containsExactly("0", "1", "2", "3");
  }

  @Test
  public void biFunction() throws Exception {
    BiFunction<Integer, Integer, String> f = (x, y) -> x + ", " + y;
    assertThat(f.apply(1, 2)).isEqualTo("1, 2");
  }

  /* Suppliers */

  @Test
  public void supplier() throws Exception {
    Supplier<List<Integer>> supplier = ArrayList::new;
    List<Integer> values = numbers.stream()
        .map(i -> i * 2)
        .collect(Collectors.toCollection(supplier));
    assertThat(values).containsExactly(0, 2, 4, 6);
  }

}

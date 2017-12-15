package io.mincong.ocpjp.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author Mincong Huang
 */
public class DriverManagerTest extends JdbcTest {

  @Test(expected = SQLException.class)
  public void noSuitableDriver() throws Exception {
    DriverManager.getConnection("jdbc:mysql://localhost");
  }

  @Test(expected = SQLException.class)
  public void accessDenied() throws Exception {
    DriverManager.getConnection(URL, "foo", "bar");
  }

  @Test
  public void createTable() throws Exception {
    StringBuilder sb = new StringBuilder();
    try (Statement s = connection.createStatement()) {
      s.executeUpdate("CREATE TABLE book ( id INT PRIMARY KEY )");
      try (ResultSet rs = s.executeQuery("SHOW TABLES")) {
        while (rs.next()) {
          sb.append(getRow(rs)).append('\n');
        }
      }
    }
    assertThat(sb.toString()).contains("BOOK");
  }

  private String getRow(ResultSet rs) throws SQLException {
    int max = rs.getMetaData().getColumnCount();
    List<String> row = new ArrayList<>(max);
    // Database index starts at 1.
    for (int i = 1; i <= max; i++) {
      row.add(rs.getString(i));
    }
    return String.join(", ", row);
  }

}

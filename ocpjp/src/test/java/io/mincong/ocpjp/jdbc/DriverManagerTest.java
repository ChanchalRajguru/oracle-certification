package io.mincong.ocpjp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mincong Huang
 */
public class DriverManagerTest {

  /**
   * The JDBC URL determines the appropriate driver for a given URL
   * string. For example:
   * <pre>
   * jdbc:sub-protocol://&lt;host&gt;:&lt;port&gt;/&lt;database_name&gt;
   * </pre>
   */
  private static final String URL = "jdbc:h2:mem:test";

  private Connection connection;

  @Before
  public void setUp() throws Exception {
    /*
     * Since JDBC 4.0, calling method `Class.forName("xxx");` is no
     * longer necessary.
     */
    connection = DriverManager.getConnection(URL);
  }

  @After
  public void tearDown() throws Exception {
    connection.close();
  }

  @Test
  public void createTable() throws Exception {
    Statement statement = connection.createStatement();
    statement.executeUpdate(
        "CREATE TABLE book ("
            + "  id INT PRIMARY KEY,"
            + "  title VARCHAR(1000),"
            + "  author CHAR(255),"
            + "  publication_year INT,"
            + "  unit_price REAL"
            + ")"
    );
    statement.close();
  }

}

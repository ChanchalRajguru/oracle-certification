package io.mincong.ocpjp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.After;
import org.junit.Before;

/**
 * @author Mincong Huang
 */
public abstract class JdbcTest {

  /**
   * The JDBC URL determines the appropriate driver for a given URL
   * string. For example:
   * <pre>
   * jdbc:sub-protocol://&lt;host&gt;:&lt;port&gt;/&lt;database_name&gt;
   * </pre>
   */
  protected static final String URL = "jdbc:h2:mem:test";

  protected Connection connection;

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

}

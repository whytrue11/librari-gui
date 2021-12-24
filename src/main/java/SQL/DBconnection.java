package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
  private static final String url = "jdbc:sqlserver://DESKTOP-NN8GLSR\\SQLEXPRESS;database=library";
  private static final String username = "test";
  private static final String password = "1234";

  public static Connection connection;
  static {
    try {
      connection = DriverManager.getConnection(url, username, password);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}

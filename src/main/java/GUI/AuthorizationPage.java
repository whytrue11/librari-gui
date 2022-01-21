package GUI;

import GUI.director.ProfileDirector;
import GUI.librarian.ProfileLibrarian;
import GUI.reader.ProfileReader;
import SQL.DBconnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthorizationPage extends JFrame {
  private JPanel registrationPanel;
  private JLabel welcome;
  private JLabel login;
  private JButton logInButton;
  private JButton registrationButton;
  private JTextField loginField;
  private JPasswordField passwordField;
  private JLabel password;

  public AuthorizationPage() {
    setContentPane(registrationPanel);
    setTitle("Library");
    setSize(450, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    /*//-------------TEST-------------------
    loginField.setText("admin");
    passwordField.setText("admin");
    //------------------------------------*/

    setVisible(true);

    logInButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String login = loginField.getText();
        String password = new String(passwordField.getPassword());
        password = Integer.toString(password.hashCode());

        if (login.isEmpty() || password.isEmpty()) {
          Error error = new Error();
          return;
        }

        Statement statement = null;
        try {
          statement = DBconnection.connection.createStatement();
          ResultSet resultSet = statement.executeQuery(
              "SELECT Blocked FROM [User] WHERE [User].Login ='" + login + "'");
          resultSet.next();
          int blockedStatus = resultSet.getInt("Blocked");
          if (blockedStatus == 1) {
            Blocked blocked = new Blocked();
            return;
          }


          statement = DBconnection.connection.createStatement();
          resultSet = statement.executeQuery(
              "DECLARE @AuthorizationStatus int\n" +
                  "EXEC AuthorizationUserStatus " + login + ", " + password + ", @AuthorizationStatus OUTPUT\n" +
                  "SELECT @AuthorizationStatus AS AuthorizationStatus");
          resultSet.next();
          int authorizationStatus = resultSet.getInt("AuthorizationStatus");

          if (authorizationStatus == 0) {
            Error error = new Error();
            return;
          }

          resultSet = statement.executeQuery(
              "DECLARE @Status int\n" +
                  "EXEC UserStatus " + "'" + login + "', '" + password + "', @Status OUTPUT\n" +
                  "SELECT @Status AS Status");
          resultSet.next();

          int userStatus = resultSet.getInt("Status");

          switch (userStatus) {
            case 0 -> {
              resultSet = statement.executeQuery(
                  "SELECT IdReader FROM Reader " +
                      "JOIN [User] ON [User].IdUser = Reader.UserId WHERE [User].Login = '" + login + "'");
              resultSet.next();

              Tools.userRoleId = resultSet.getInt("IdReader");
              ProfileReader profileReader = new ProfileReader(getLocation());
              setVisible(false);
            }
            case 1 -> {
              resultSet = statement.executeQuery(
                  "SELECT IdEmployee FROM Employee " +
                      "JOIN [User] ON [User].IdUser = Employee.UserId WHERE [User].Login = '" + login + "'");
              resultSet.next();

              Tools.userRoleId = resultSet.getInt("IdEmployee");
              ProfileLibrarian profileLibrarian = new ProfileLibrarian(getLocation());
              setVisible(false);
            }
            case 2 -> {
              resultSet = statement.executeQuery(
                  "SELECT IdEmployee FROM Employee " +
                      "JOIN [User] ON [User].IdUser = Employee.UserId WHERE [User].Login = '" + login + "'");
              resultSet.next();

              Tools.userRoleId = resultSet.getInt("IdEmployee");
              ProfileDirector profileDirector = new ProfileDirector(getLocation());
              setVisible(false);
            }
          }

        } catch (SQLException throwables) {
          Error error = new Error();
        }
      }
    });
    registrationButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RegistrationPage registrationPage = new RegistrationPage(getLocation());
        setVisible(false);
      }
    });
  }
}

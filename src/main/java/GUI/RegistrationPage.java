package GUI;

import GUI.reader.ProfileReader;
import SQL.DBconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistrationPage extends JFrame {
  private JPanel registrationPanel;
  private JTextField fioField;
  private JTextField phoneField;
  private JTextField mailField;
  private JTextField loginField;
  private JLabel password;
  private JButton registrationButton;
  private JPasswordField passwordField;
  private JLabel login;
  private JLabel registration;
  private JLabel fio;
  private JLabel phone;
  private JLabel mail;

  public RegistrationPage(Point point) throws HeadlessException {
    setContentPane(registrationPanel);
    setTitle("Library");
    setSize(450, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    setVisible(true);
    registrationButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String login = loginField.getText();
        String password = new String(passwordField.getPassword());
        password = Integer.toString(password.hashCode());

        String fio = fioField.getText();
        String phone = phoneField.getText();
        String mail = mailField.getText();


        if (login.isEmpty() || password.isEmpty() || fio.isEmpty() || phone.isEmpty() || mail.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          ResultSet resultSet = statement.executeQuery(
              "DECLARE @UserId int\n" +
                  "EXEC ReaderRegistration '" + login + "', '" + password
                      + "', '" + fio + "', '" + phone + "', '" + mail + "', @UserId OUTPUT\n" +
                  "SELECT @UserId AS UserId");
          resultSet.next();

          if ((Tools.userRoleId = resultSet.getInt("UserId")) == -1) {
            Error error = new Error();
            return;
          }

          ProfileReader profileReader = new ProfileReader(getLocation());
          setVisible(false);
        } catch (SQLException throwables) {
          Error error = new Error();
        }
      }
    });
  }
}

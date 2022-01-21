package GUI.director;

import GUI.AuthorizationPage;
import GUI.Error;
import GUI.Tools;
import GUI.reader.ProfileReader;
import SQL.DBconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadersDirector extends JFrame {
  private JPanel directorReadersPanel;
  private JTextField fioField;
  private JLabel fioL;
  private JLabel phoneL;
  private JTextField phoneField;
  private JLabel mailL;
  private JTextField mailField;
  private JButton addReaderButton;
  private JPanel menuPanel;
  private JLabel libraryLabelR;
  private JTextField loginField;
  private JPasswordField passwordField;
  private JLabel login;
  private JLabel password;
  private JButton updateButton;
  private JLabel readerLogin;
  private JTextField readerLoginUpdateField;
  private JButton genresButton;
  private JButton readerButton;
  private JButton logoutDirectorButton;
  private JButton debtorsDirectorButton;
  private JButton employeesDirectorButton;
  private JButton profileDirectorButton;

  public ReadersDirector(Point point) {
    setContentPane(directorReadersPanel);
    setTitle("Library");
    setSize(Tools.WIDTH, Tools.HEIGHT);
    setLocation(point);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    logoutDirectorButton.setBorderPainted(false);
    debtorsDirectorButton.setBorderPainted(false);
    employeesDirectorButton.setBorderPainted(false);
    profileDirectorButton.setBorderPainted(false);

    genresButton.setBorderPainted(false);
    readerButton.setBorderPainted(false);

    menuPanel.setPreferredSize(new Dimension(Tools.WIDTH / 5, Tools.HEIGHT));
    setVisible(true);
    employeesDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        EmployeesDirector employeesDirector = new EmployeesDirector(getLocation());
        setVisible(false);
      }
    });
    debtorsDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DebtorsDirector debtorsDirector = new DebtorsDirector(getLocation());
        setVisible(false);
      }
    });
    logoutDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AuthorizationPage authorizationPage = new AuthorizationPage();
        setVisible(false);
      }
    });
    genresButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        GenreDirector genreDirector = new GenreDirector(getLocation());
        setVisible(false);
      }
    });
    addReaderButton.addActionListener(new ActionListener() {
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
          }
        } catch (SQLException throwables) {
          Error error = new Error();
        }
      }
    });
    updateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String readerLogin = readerLoginUpdateField.getText();

        //String login = loginField.getText();
        //String password = new String(passwordField.getPassword());
        //password = Integer.toString(password.hashCode());

        String fio = fioField.getText();
        String phone = phoneField.getText();
        String mail = mailField.getText();

        if (/*login.isEmpty() || password.isEmpty() ||*/ fio.isEmpty() || phone.isEmpty() || mail.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();

          ResultSet resultSet = statement.executeQuery(
              "SELECT IdReader FROM Reader JOIN [User] ON Reader.UserId = [User].IdUser WHERE Login = '" + readerLogin + "'");
          resultSet.next();
          String readerId = resultSet.getString("IdReader");

          /*statement.executeUpdate(
              "UPDATE [User]\n" +
                  "SET Login = '" + login + "' \n" +
                  "WHERE IdUser = '" + userId + "'");
          statement.executeUpdate(
              "UPDATE [User]\n" +
                  "SET Password = '" + password + "' \n" +
                  "WHERE IdUser = '" + userId + "'");*/
          statement.executeUpdate(
              "\nUPDATE Reader\n" +
                  "SET FIO = '" + fio + "' \n" +
                  "WHERE IdReader = '" + readerId + "'");
          statement.executeUpdate(
              "\nUPDATE Reader\n" +
                  "SET Phone = '" + phone + "' \n" +
                  "WHERE IdReader = '" + readerId + "'");
          statement.executeUpdate(
              "\nUPDATE Reader\n" +
                  "SET Email = '" + mail + "' \n" +
                  "WHERE IdReader = '" + readerId + "'");
          statement.executeUpdate(
              "\nUPDATE Reader\n" +
                  "SET Email = '" + mail + "' \n" +
                  "WHERE IdReader = '" + readerId + "'");

        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }
      }
    });
  }
}

package GUI.director;

import GUI.AuthorizationPage;
import GUI.Error;
import GUI.Tools;
import SQL.DBconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class EmployeesDirector extends JFrame {
  private JLabel login;
  private JTextField loginField;
  private JPasswordField passwordField;
  private JLabel password;
  private JLabel post;
  private JPanel tablePanel;
  private JPanel directorEmployeesPanel;
  private JTextField fioField;
  private JLabel fioL;
  private JLabel phoneL;
  private JTextField phoneField;
  private JLabel mailL;
  private JTextField mailField;
  private JPanel menuPanel;
  private JButton logoutDirectorButton;
  private JButton debtorsDirectorButton;
  private JButton employeesDirectorButton;
  private JButton profileDirectorButton;
  private JLabel libraryLabelR;
  private JButton addEmployeeDirectorButton;
  private JPanel postComboBoxPanel;
  private JTextField employeeIdField;
  private JLabel employeeId;
  private JButton updateEmployee;
  private JButton genresButton;
  private JButton readerButton;

  private final JTable table;
  private final Vector<Vector<String>> dataArrayList;
  private final JComboBox postComboBox;

  public EmployeesDirector(Point point) {
    setContentPane(directorEmployeesPanel);
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

    //ComboBox
    Vector<String> genresList = new Vector<>();
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Post");

      while (resultSet.next()) {
        genresList.add(resultSet.getString("Name"));
      }
    } catch (SQLException throwables) {
      Error error = new Error();
    }

    postComboBox = new JComboBox<String>(genresList);
    postComboBoxPanel.setLayout(new BorderLayout());
    postComboBoxPanel.add(postComboBox);

    //table
    dataArrayList = new Vector<>();
    Vector<String> header = new Vector<>();
    header.add("User id");
    header.add("Employee id");
    header.add("Login");
    header.add("FIO");
    header.add("Phone");
    header.add("Mail");
    header.add("Role");

    tablePanel.setLayout(new BorderLayout());
    table = new JTable(dataArrayList, header);
    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane);

    updateTableData();

    setVisible(true);

    profileDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProfileDirector profileDirector = new ProfileDirector(getLocation());
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
    readerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ReadersDirector readersDirector = new ReadersDirector(getLocation());
        setVisible(false);
      }
    });
    addEmployeeDirectorButton.addActionListener(new ActionListener() {
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
          statement.execute(
              "DECLARE @PostId int\n" +
                  "SET @PostId = (SELECT IdPost FROM Post WHERE Post.Name = '" + postComboBox.getSelectedItem() + "')\n" +
                  "EXEC EmployeeRegistration '" + login + "', '" + password + "', '" + fio + "', '" + phone + "', '" + mail + "', @PostId");
        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }

        updateTableData();
      }
    });
    updateEmployee.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String employeeId = employeeIdField.getText();

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

          /*ResultSet resultSet = statement.executeQuery(
              "SELECT IdUser FROM [User] JOIN Employee ON Employee.UserId = [User].IdUser WHERE IdEmployee = " + employeeId);
          resultSet.next();
          String userId = resultSet.getString("IdUser");*/

          /*statement.executeUpdate(
              "UPDATE [User]\n" +
                  "SET Login = '" + login + "' \n" +
                  "WHERE IdUser = '" + userId + "'");
          statement.executeUpdate(
              "UPDATE [User]\n" +
                  "SET Password = '" + password + "' \n" +
                  "WHERE IdUser = '" + userId + "'");*/
          statement.executeUpdate(
              "\nUPDATE Employee\n" +
                  "SET FIO = '" + fio + "' \n" +
                  "WHERE IdEmployee = '" + employeeId + "'");
          statement.executeUpdate(
              "\nUPDATE Employee\n" +
                  "SET Phone = '" + phone + "' \n" +
                  "WHERE IdEmployee = '" + employeeId + "'");
          statement.executeUpdate(
              "\nUPDATE Employee\n" +
                  "SET Email = '" + mail + "' \n" +
                  "WHERE IdEmployee = '" + employeeId + "'");
          statement.executeUpdate(
              "\nUPDATE Employee\n" +
                  "SET Email = '" + mail + "' \n" +
                  "WHERE IdEmployee = '" + employeeId + "'");

        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }

        updateTableData();
      }
    });
  }

  private void updateTableData() {
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "SELECT IdUser, IdEmployee, [User].Login, FIO, Phone, Email, Post.Name AS Role \n" +
              "FROM Employee \n" +
              "JOIN Post ON PostId = IdPost\n" +
              "JOIN [User] ON UserId = IdUser");

      dataArrayList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        row.add(resultSet.getString("IdUser"));
        row.add(resultSet.getString("IdEmployee"));
        row.add(resultSet.getString("Login"));
        row.add(resultSet.getString("FIO"));
        row.add(resultSet.getString("Phone"));
        row.add(resultSet.getString("Email"));
        row.add(resultSet.getString("Role"));
        dataArrayList.add(row);
      }
      table.repaint();

    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }
}

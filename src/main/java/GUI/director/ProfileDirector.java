package GUI.director;

import GUI.AuthorizationPage;
import GUI.Error;
import GUI.Tools;
import SQL.DBconnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileDirector extends JFrame {
  private JTextField fioField;
  private JLabel fioL;
  private JLabel phoneL;
  private JTextField phoneField;
  private JLabel mailL;
  private JTextField mailField;
  private JLabel image;
  private JButton readerDataChange;
  private JPanel menuPanel;
  private JLabel libraryLabelR;
  private JButton logoutDirectorButton;
  private JButton debtorsDirectorButton;
  private JButton employeesDirectorButton;
  private JButton profileDirectorButton;
  private JPanel directorProfilePanel;
  private JButton genresButton;
  private JButton readerButton;

  public ProfileDirector(Point point) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/director.png"));
      Image icon = bufferedImage.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
      image.setIcon(new ImageIcon(icon));
    } catch (IOException e) {
      e.printStackTrace();
    }
    setContentPane(directorProfilePanel);
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

    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery("EXEC EmployeeDataId " + Tools.userRoleId);
      resultSet.next();

      String fio = resultSet.getString("FIO");
      String phone = resultSet.getString("Phone");
      String mail = resultSet.getString("Email");

      fioField.setText(fio);
      phoneField.setText(phone);
      mailField.setText(mail);

    } catch (SQLException throwables) {
      Error error = new Error();
    }

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
    readerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ReadersDirector readersDirector = new ReadersDirector(getLocation());
        setVisible(false);
      }
    });
  }
}

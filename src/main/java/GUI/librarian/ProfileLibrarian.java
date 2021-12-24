package GUI.librarian;

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

public class ProfileLibrarian extends JFrame {

  private JPanel librarianProfilePanel;
  private JTextField fioField;
  private JLabel fioL;
  private JLabel phoneL;
  private JTextField phoneField;
  private JLabel mailL;
  private JTextField mailField;
  private JLabel image;
  private JButton readerDataChange;
  private JPanel menuPanel;
  private JButton logoutLibrarianButton;
  private JButton topReadersLibrarianButton;
  private JButton debtorsLibrarianButton;
  private JButton catalogLibrarianButton;
  private JButton profileLibrarianButton;
  private JLabel libraryLabelR;

  public ProfileLibrarian(Point point) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/librarian.png"));
      Image icon = bufferedImage.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
      image.setIcon(new ImageIcon(icon));
    } catch (IOException e) {
      e.printStackTrace();
    }
    setContentPane(librarianProfilePanel);
    setTitle("Library");
    setSize(Tools.WIDTH, Tools.HEIGHT);
    setLocation(point);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    profileLibrarianButton.setBorderPainted(false);
    catalogLibrarianButton.setBorderPainted(false);
    debtorsLibrarianButton.setBorderPainted(false);
    topReadersLibrarianButton.setBorderPainted(false);
    logoutLibrarianButton.setBorderPainted(false);

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
    catalogLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CatalogLibrarian catalogLibrarian = new CatalogLibrarian(getLocation());
        setVisible(false);
      }
    });
    debtorsLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DebtorsLibrarian debtorsLibrarian = new DebtorsLibrarian(getLocation());
        setVisible(false);
      }
    });
    topReadersLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TopReadersLibrarian topBooks = new TopReadersLibrarian(getLocation());
        setVisible(false);
      }
    });
    logoutLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AuthorizationPage authorizationPage = new AuthorizationPage();
        setVisible(false);
      }
    });
    readerDataChange.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String fio = fioField.getText();
        String phone = phoneField.getText();
        String mail = mailField.getText();

        if (fio.isEmpty() || phone.isEmpty() || mail.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          statement.executeUpdate(
              "UPDATE Employee\n" +
                  "SET FIO = '" + fio + "'\n" +
                  "WHERE IdEmployee = '" + Tools.userRoleId + "'\n" +
                  "\n" +
                  "UPDATE Employee\n" +
                  "SET Phone = '" + phone + "'\n" +
                  "WHERE IdEmployee = '" + Tools.userRoleId + "'\n" +
                  "\n" +
                  "UPDATE Employee\n" +
                  "SET Email = '" + mail + "'\n" +
                  "WHERE IdEmployee = '" + Tools.userRoleId + "'");

        } catch (SQLException throwables) {
          Error error = new Error();
        }
      }
    });
  }
}

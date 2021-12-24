package GUI.reader;

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

public class ProfileReader extends JFrame {
  private JPanel readerPagePanel;
  private JTextField fioField;
  private JLabel fioL;
  private JLabel phoneL;
  private JTextField phoneField;
  private JLabel mailL;
  private JTextField mailField;
  private JLabel image;
  private JButton readerDataChange;
  private JButton logoutReaderButton;
  private JLabel libraryLabelR;
  private JButton profileReaderButton;
  private JButton catalogReaderButton;
  private JButton myBooksReaderButton;
  private JButton topBooksReaderButton;
  private JPanel menuPanel;
  private JPanel libraryPanel1;

  public ProfileReader(Point point) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/reader.png"));
      Image icon = bufferedImage.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
      image.setIcon(new ImageIcon(icon));
    } catch (IOException e) {
      e.printStackTrace();
    }
    setContentPane(readerPagePanel);
    setTitle("Library");
    setSize(Tools.WIDTH, Tools.HEIGHT);
    setLocation(point);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    profileReaderButton.setBorderPainted(false);
    catalogReaderButton.setBorderPainted(false);
    myBooksReaderButton.setBorderPainted(false);
    topBooksReaderButton.setBorderPainted(false);
    logoutReaderButton.setBorderPainted(false);

    menuPanel.setPreferredSize(new Dimension(Tools.WIDTH / 5, Tools.HEIGHT));

    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery("EXEC ReaderDataId " + Tools.userRoleId);
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
    catalogReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CatalogReader readerCatalog = new CatalogReader(getLocation());
        setVisible(false);
      }
    });
    myBooksReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ReaderBooks readerBooks = new ReaderBooks(getLocation());
        setVisible(false);
      }
    });
    topBooksReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TopBooks topBooks = new TopBooks(getLocation());
        setVisible(false);
      }
    });
    logoutReaderButton.addActionListener(new ActionListener() {
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
              "UPDATE Reader\n" +
              "SET FIO = '" + fio + "'\n" +
              "WHERE IdReader = '" + Tools.userRoleId + "'\n" +
              "\n" +
              "UPDATE Reader\n" +
              "SET Phone = '" + phone + "'\n" +
              "WHERE IdReader = '" + Tools.userRoleId + "'\n" +
              "\n" +
              "UPDATE Reader\n" +
              "SET Email = '" + mail + "'\n" +
              "WHERE IdReader = '" + Tools.userRoleId + "'");

        } catch (SQLException throwables) {
          Error error = new Error();
        }
      }
    });
  }
}

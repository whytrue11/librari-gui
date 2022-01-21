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
import java.util.Vector;

public class CatalogReader extends JFrame {
  private JPanel readerCatalogPanel;
  private JButton takeBookButton;
  private JLabel libraryLabelR1;
  private JButton logoutReaderButton;
  private JButton topBooksReaderButton;
  private JButton myBooksReaderButton;
  private JButton catalogReaderButton;
  private JButton profileReaderButton;
  private JTextField searchField;
  private JButton searchReaderButton;
  private JPanel menuPanel;
  private JPanel tablePanel;

  private final JTable table;
  private final Vector<Vector<String>> dataArrayList;

  public CatalogReader(Point point) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/search_icon.png"));
      Image icon = bufferedImage.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
      searchReaderButton.setIcon(new ImageIcon(icon));
    } catch (IOException e) {
      e.printStackTrace();
    }

    setContentPane(readerCatalogPanel);
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

    //table
    dataArrayList = new Vector<>();
    Vector<String> header = new Vector<>();
    header.add("Book name");
    header.add("Release date");
    header.add("Author FIO");
    header.add("Number of page");
    header.add("Description");
    header.add("Quantity");

    tablePanel.setLayout(new BorderLayout());
    table = new JTable(dataArrayList, header);
    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane);

    updateTableData();

    setVisible(true);
    profileReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProfileReader profileReader = new ProfileReader(getLocation());
        setVisible(false);
      }
    });
    myBooksReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ReaderBooks topBooks = new ReaderBooks(getLocation());
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
    searchReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String search = searchField.getText();

        if (search.isEmpty()) {
          updateTableData();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          ResultSet resultSet = statement.executeQuery(
              "SELECT Book.Name, ReleaseDate, AuthorFIO, NumberOfPage, Description, Quantity " +
                  "FROM Book WHERE Book.Name =" + "'" + search + "'");

          dataArrayList.clear();

          while (resultSet.next()) {
            Vector<String> row = new Vector<>();
            row.add(resultSet.getString("Name"));
            row.add(resultSet.getString("ReleaseDate"));
            row.add(resultSet.getString("AuthorFIO"));
            row.add(resultSet.getString("NumberOfPage"));
            row.add(resultSet.getString("Description"));
            row.add(resultSet.getString("Quantity"));
            dataArrayList.add(row);
          }

          table.repaint();

        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }
      }
    });
  }

  private void updateTableData() {
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "SELECT Book.Name, ReleaseDate, AuthorFIO, NumberOfPage, Description, Quantity FROM Book");

      dataArrayList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        row.add(resultSet.getString("Name"));
        row.add(resultSet.getString("ReleaseDate"));
        row.add(resultSet.getString("AuthorFIO"));
        row.add(resultSet.getString("NumberOfPage"));
        row.add(resultSet.getString("Description"));
        row.add(resultSet.getString("Quantity"));
        dataArrayList.add(row);
      }
      table.repaint();

    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }
}

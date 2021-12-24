package GUI.reader;

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

public class TopBooks extends JFrame {
  private JPanel readerTopBooksPanel;
  private JPanel menuPanel;
  private JButton logoutReaderButton;
  private JButton topBooksReaderButton;
  private JButton myBooksReaderButton;
  private JButton catalogReaderButton;
  private JButton profileReaderButton;
  private JLabel libraryLabelR1;
  private JLabel genre;
  private JPanel tablePanel;
  private JPanel comBoxPanel;

  private final JTable table;
  private final Vector<Vector<String>> topBooksList;
  private final JComboBox genresComboBox;

  public TopBooks(Point point) {
    setContentPane(readerTopBooksPanel);
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

    Vector<String> genresList = new Vector<>();
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Genre");

      while (resultSet.next()) {
        genresList.add(resultSet.getString("Name"));
      }
    } catch (SQLException throwables) {
      Error error = new Error();
    }

    genresComboBox = new JComboBox<String>(genresList);
    comBoxPanel.setLayout(new BorderLayout());
    comBoxPanel.add(genresComboBox);

    //table
    topBooksList = new Vector<>();
    Vector<String> header = new Vector<>();
    header.add("Book name");
    header.add("Release date");
    header.add("Author FIO");
    header.add("Number of page");
    header.add("Description");

    tablePanel.setLayout(new BorderLayout());
    table = new JTable(topBooksList, header);
    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane);

    updateTableData();

    setVisible(true);
    genresComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTableData();
      }
    });
    profileReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProfileReader profileReader = new ProfileReader(getLocation());
        setVisible(false);
      }
    });
    catalogReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CatalogReader catalogReader = new CatalogReader(getLocation());
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
    logoutReaderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AuthorizationPage authorizationPage = new AuthorizationPage();
        setVisible(false);
      }
    });
  }

  private void updateTableData() {
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "DECLARE @GenreId int\n" +
              "SET @GenreId = (SELECT IdGenre FROM Genre WHERE Name = '" + genresComboBox.getSelectedItem() + "')\n" +
              "EXEC BookTopGenre @GenreId");

      topBooksList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        row.add(resultSet.getString("Name"));
        row.add(resultSet.getString("ReleaseDate"));
        row.add(resultSet.getString("AuthorFIO"));
        row.add(resultSet.getString("NumberOfPage"));
        row.add(resultSet.getString("Description"));
        topBooksList.add(row);
      }
      table.repaint();

    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }
}

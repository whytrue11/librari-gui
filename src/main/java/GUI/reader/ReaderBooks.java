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

public class ReaderBooks extends JFrame {
  private JPanel readerReaderBooksPanel;
  private JPanel menuPanel;
  private JButton logoutReaderButton;
  private JButton topBooksReaderButton;
  private JButton myBooksReaderButton;
  private JButton catalogReaderButton;
  private JButton profileReaderButton;
  private JLabel libraryLabelR1;
  private JPanel tablePanel;

  private final JTable table;
  private final Vector<Vector<String>> dataArrayList;

  public ReaderBooks(Point point) {
    setContentPane(readerReaderBooksPanel);
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

    tablePanel.setLayout(new BorderLayout());
    table = new JTable(dataArrayList, header);
    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane);

    initTableData();

    setVisible(true);
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
  }

  private void initTableData() {
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "SELECT Book.Name, ReleaseDate, AuthorFIO, NumberOfPage, Description, ReturnDate\n" +
              "FROM BookCard\n" +
              "JOIN Book ON BookCard.BookId = IdBook\n" +
              "JOIN Reader ON ReaderId = IdReader\n" +
              "WHERE IdReader = " + Tools.userRoleId + " AND ReturnDate IS NULL");

      dataArrayList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        row.add(resultSet.getString("Name"));
        row.add(resultSet.getString("ReleaseDate"));
        row.add(resultSet.getString("AuthorFIO"));
        row.add(resultSet.getString("NumberOfPage"));
        row.add(resultSet.getString("Description"));
        dataArrayList.add(row);
      }
      table.repaint();

    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }
}

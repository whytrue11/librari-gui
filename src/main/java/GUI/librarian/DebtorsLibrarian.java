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
import java.util.Vector;

public class DebtorsLibrarian extends JFrame {
  private JButton searchButton;
  private JPanel librarianDebtorsPanel;
  private JTextField searchFioField;
  private JLabel fio;
  private JPanel menuPanel;
  private JButton logoutLibrarianButton;
  private JButton topReadersLibrarianButton;
  private JButton debtorsLibrarianButton;
  private JButton catalogLibrarianButton;
  private JButton profileLibrarianButton;
  private JLabel libraryLabelR;
  private JButton returnBooksButton;
  private JPanel tablePanel;
  private JButton lostBookButton;

  private final JTable table;
  private final Vector<Vector<String>> dataArrayList;

  public DebtorsLibrarian(Point point) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/search_icon.png"));
      Image icon = bufferedImage.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
      searchButton.setIcon(new ImageIcon(icon));
    } catch (IOException e) {
      e.printStackTrace();
    }
    setContentPane(librarianDebtorsPanel);
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

    //table
    dataArrayList = new Vector<>();
    Vector<String> header = new Vector<>();
    header.add("FIO");
    header.add("Phone");
    header.add("Email");
    header.add("Id book");
    header.add("Book name");
    header.add("Author FIO");
    header.add("Issue date");
    header.add("Lost");

    tablePanel.setLayout(new BorderLayout());
    table = new JTable(dataArrayList, header);
    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane);

    updateTableData();

    setVisible(true);

    profileLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProfileLibrarian profileLibrarian = new ProfileLibrarian(getLocation());
        setVisible(false);
      }
    });
    catalogLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CatalogLibrarian catalogLibrarian = new CatalogLibrarian(getLocation());
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
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String searchFIO = searchFioField.getText();

        if (searchFIO.isEmpty()) {
          updateTableData();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          ResultSet resultSet = statement.executeQuery(
              "SELECT FIO, Phone, Email, IdBook, Book.Name, AuthorFIO, IssueDate, Lost\n" +
                  "FROM BookCard\n" +
                  "JOIN Book ON BookCard.BookId = IdBook\n" +
                  "JOIN Reader ON ReaderId = IdReader AND ReturnDate IS NULL " +
                  "AND FIO = '" + searchFIO + "'");

          dataArrayList.clear();
          while (resultSet.next()) {
            Vector<String> row = new Vector<>();
            row.add(resultSet.getString("FIO"));
            row.add(resultSet.getString("Phone"));
            row.add(resultSet.getString("Email"));
            row.add(resultSet.getString("IdBook"));
            row.add(resultSet.getString("Name"));
            row.add(resultSet.getString("AuthorFIO"));
            row.add(resultSet.getString("IssueDate"));
            row.add(resultSet.getString("Lost"));
            dataArrayList.add(row);
          }
          table.repaint();

        } catch (SQLException throwables) {
          Error error = new Error();
        }
      }
    });
    returnBooksButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vector<Vector<String>> returnedBooks = new Vector<>();
        for (int rowIndex : table.getSelectedRows()) {
          returnedBooks.add(dataArrayList.get(rowIndex));
        }

        for (Vector<String> bookCard : returnedBooks) {
          String phone = bookCard.get(1);
          String bookId = bookCard.get(3);

          Statement statement = null;
          try {
            statement = DBconnection.connection.createStatement();
            statement.execute(
                "DECLARE @BookCardId int\n" +
                      "SET @BookCardId = (SELECT IdBookCard FROM BookCard " +
                      "WHERE ReaderId = (SELECT IdReader FROM Reader WHERE Phone = '" + phone + "') " +
                      "AND BookId = '" + bookId + "')\n" +
                    "EXEC ReturnBook @BookCardId");
          } catch (SQLException throwables) {
            Error error = new Error();
          }
        }

        updateTableData();
      }
    });
    lostBookButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vector<Vector<String>> returnedBooks = new Vector<>();
        for (int rowIndex : table.getSelectedRows()) {
          returnedBooks.add(dataArrayList.get(rowIndex));
        }

        for (Vector<String> bookCard : returnedBooks) {
          String phone = bookCard.get(1);
          String bookId = bookCard.get(3);

          Statement statement = null;
          try {
            statement = DBconnection.connection.createStatement();
            statement.execute(
                "DECLARE @BookCardId int\n" +
                    "SET @BookCardId = (SELECT IdBookCard FROM BookCard " +
                    "WHERE ReaderId = (SELECT IdReader FROM Reader WHERE Phone = '" + phone + "') " +
                    "AND BookId = '" + bookId + "')\n" +
                    "UPDATE BookCard\n" +
                    "SET Lost = '1'\n" +
                    "WHERE IdBookCard = @BookCardId");
          } catch (SQLException throwables) {
            Error error = new Error();
          }
        }

        updateTableData();
      }
    });
  }

  private void updateTableData() {
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "SELECT FIO, Phone, Email, IdBook, Book.Name, AuthorFIO, IssueDate, Lost\n" +
              "FROM BookCard\n" +
              "JOIN Book ON BookCard.BookId = IdBook\n" +
              "JOIN Reader ON ReaderId = IdReader AND ReturnDate IS NULL");

      dataArrayList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        row.add(resultSet.getString("FIO"));
        row.add(resultSet.getString("Phone"));
        row.add(resultSet.getString("Email"));
        row.add(resultSet.getString("IdBook"));
        row.add(resultSet.getString("Name"));
        row.add(resultSet.getString("AuthorFIO"));
        row.add(resultSet.getString("IssueDate"));
        row.add(resultSet.getString("Lost"));
        dataArrayList.add(row);
      }
      table.repaint();

    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }
}

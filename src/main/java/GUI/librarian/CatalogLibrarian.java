package GUI.librarian;

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

public class CatalogLibrarian extends JFrame {
  private JPanel librarianCatalogPanel;
  private JLabel releaseDate;
  private JTextField releaseDateField;
  private JTextField numberOfPageField;
  private JLabel numberOfPage;
  private JTextField quantityField;
  private JLabel quantity;
  private JLabel genre;
  private JButton deleteBookButton;
  private JButton addGenreButton;
  private JTextField nameField;
  private JLabel name;
  private JLabel authorFIO;
  private JTextField authorFIOField;
  private JLabel description;
  private JTextField descriptionField;
  private JButton logoutLibrarianButton;
  private JButton topReadersLibrarianButton;
  private JButton debtorsLibrarianButton;
  private JButton catalogLibrarianButton;
  private JButton profileLibrarianButton;
  private JLabel libraryLabelR;
  private JPanel menuPanel;
  private JButton addBookButton;
  private JTextField updateBookIdField;
  private JLabel bookId;
  private JButton bookUpdateButton;
  private JPanel tablePanel;
  private JPanel genreComboBoxPanel;
  private JButton giveBooksButton;
  private JTextArea userLoginField;

  private final JTable table;
  private final Vector<Vector<String>> dataArrayList;
  private final JComboBox genresComboBox;

  public CatalogLibrarian(Point point) {
    setContentPane(librarianCatalogPanel);
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

    //ComboBox
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
    genreComboBoxPanel.setLayout(new BorderLayout());
    genreComboBoxPanel.add(genresComboBox);

    //table
    dataArrayList = new Vector<>();
    Vector<String> header = new Vector<>();
    header.add("Id book");
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

    userLoginField.setText("Login");

    updateTableData();

    setVisible(true);

    profileLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProfileLibrarian profileLibrarian = new ProfileLibrarian(getLocation());
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
    addBookButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String releaseDate = releaseDateField.getText().isEmpty() ? "NULL" : releaseDateField.getText();
        String authorFIO = authorFIOField.getText();
        String numberOfPage = numberOfPageField.getText();
        String description = descriptionField.getText().isEmpty() ? "NULL" : descriptionField.getText();
        String quantity = quantityField.getText();

        if (name.isEmpty() || authorFIO.isEmpty() || numberOfPage.isEmpty() || quantity.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          if (releaseDate.equals("NULL") && description.equals("NULL")) {
            statement.execute(
                "EXEC AddBook '" + name + "', " + releaseDate + " , '" + authorFIO + "', " + numberOfPage + ", " + description + ", " + quantity);
          } else if (releaseDate.equals("NULL")) {
            statement.execute(
                "EXEC AddBook '" + name + "', " + releaseDate + " , '" + authorFIO + "', " + numberOfPage + ", '" + description + "', " + quantity);
          }
          else {
            statement.execute(
                "EXEC AddBook '" + name + "', '" + releaseDate + "' , '" + authorFIO + "', " + numberOfPage + ", " + description + ", " + quantity);
          }

        } catch (SQLException throwables) {
          Error error = new Error();
        }

        updateTableData();
      }
    });
    bookUpdateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String bookId = updateBookIdField.getText();

        String name = nameField.getText();
        String releaseDate = releaseDateField.getText().isEmpty() ? "NULL" : releaseDateField.getText();
        String authorFIO = authorFIOField.getText();
        String numberOfPage = numberOfPageField.getText();
        String description = descriptionField.getText().isEmpty() ? "NULL" : descriptionField.getText();
        String quantity = quantityField.getText();

        if (bookId.isEmpty() || name.isEmpty() || authorFIO.isEmpty() || numberOfPage.isEmpty() || quantity.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          if (releaseDate.equals("NULL")) {
            statement.executeUpdate(
                "UPDATE Book\n" +
                    "SET ReleaseDate = " + releaseDate + " \n" +
                    "WHERE IdBook = '" + Integer.valueOf(bookId) + "'");
          }
          else
          {
            statement.executeUpdate(
                "UPDATE Book\n" +
                    "SET ReleaseDate = '" + releaseDate + "' \n" +
                    "WHERE IdBook = '" + Integer.valueOf(bookId) + "'");
          }

          if (description.equals("NULL")) {
            statement.executeUpdate(
                "\nUPDATE Book\n" +
                    "SET Description = " + description + " \n" +
                    "WHERE IdBook = '" + Integer.valueOf(bookId) + "'");
          }
          else {
            statement.executeUpdate(
                "\nUPDATE Book\n" +
                    "SET Description = '" + description + "' \n" +
                    "WHERE IdBook = '" + Integer.valueOf(bookId) + "'");
          }

          statement.executeUpdate(
              "UPDATE Book\n" +
                  "SET Name = '" + name + "' \n" +
                  "WHERE IdBook = '" + Integer.valueOf(bookId) + "' \n" +
                  "\nUPDATE Book\n" +
                  "SET AuthorFIO = '" + authorFIO + "' \n" +
                  "WHERE IdBook = '" + Integer.valueOf(bookId) + "' \n" +
                  "\nUPDATE Book\n" +
                  "SET NumberOfPage = '" + Integer.valueOf(numberOfPage) + "' \n" +
                  "WHERE IdBook = '" + Integer.valueOf(bookId) + "' \n" +
                  "\nUPDATE Book\n" +
                  "SET Quantity = '" + Integer.valueOf(quantity) + "' \n" +
                  "WHERE IdBook = '" + Integer.valueOf(bookId) + "'");

        } catch (SQLException | NumberFormatException throwables) {
          Error error = new Error();
        }

        updateTableData();
      }
    });
    deleteBookButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vector<Vector<String>> selectedBooks = new Vector<>();
        for (int rowIndex : table.getSelectedRows()) {
          selectedBooks.add(dataArrayList.get(rowIndex));
        }

        for (Vector<String> book : selectedBooks) {
          String bookId = book.get(0);

          try {
            Statement statement = DBconnection.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "DECLARE @Status int\n" +
                    "EXEC DeleteBook " + bookId + ", @Status OUTPUT\n" +
                    "SELECT @Status AS Status");

            resultSet.next();
            if (resultSet.getString("Status").equals("0")) {
              Error error = new Error();
            }

          } catch (SQLException throwables) {
            Error error = new Error();
          }
        }

        updateTableData();
      }
    });
    addGenreButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String bookId = updateBookIdField.getText();

        if (bookId.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          statement.execute(
              "DECLARE @GenreId int\n" +
                  "SET @GenreId = (SELECT IdGenre FROM Genre WHERE Name = '" + genresComboBox.getSelectedItem() + "')\n" +
                  "EXEC AddGenre " + bookId + ",  @GenreId");
        } catch (SQLException throwables) {
          Error error = new Error();
        }
      }
    });
    giveBooksButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String userLogin = userLoginField.getText();

        Vector<Vector<String>> takenBooks = new Vector<>();
        for (int rowIndex : table.getSelectedRows()) {
          takenBooks.add(dataArrayList.get(rowIndex));
        }

        for (Vector<String> book : takenBooks) {
          String name = book.get(1);
          String releaseDate = book.get(2);
          String authorFIO = book.get(3);
          String numberOfPage = book.get(4);

          Statement statement = null;
          try {
            statement = DBconnection.connection.createStatement();

            if (releaseDate.isEmpty()) {
              statement.execute(
                  "DECLARE @BookId int\n" +
                      "SET @BookId = (SELECT IdBook FROM Book WHERE [Name] = " + "'" + name + "'" + "AND ReleaseDate IS NULL" +
                      " AND AuthorFIO = " + "'" + authorFIO + "'" + " AND NumberOfPage = " + "'" + numberOfPage + "')\n" +
                      "DECLARE @ReaderId int\n" +
                      "SET @ReaderId = (SELECT IdReader FROM Reader JOIN [User] ON [User].IdUser = Reader.UserId WHERE Login ='" + userLogin + "')\n" +
                      "EXEC IssueBook @ReaderId, @BookId");
            }
            else {
              statement.execute(
                  "DECLARE @BookId int\n" +
                      "SET @BookId = (SELECT IdBook FROM Book WHERE [Name] = " + "'" + name + "'" + "AND ReleaseDate = " + "'" + releaseDate + "'" +
                      " AND AuthorFIO = " + "'" + authorFIO + "'" + " AND NumberOfPage = " + "'" + numberOfPage + "')\n" +
                      "DECLARE @ReaderId int\n" +
                      "SET @ReaderId = (SELECT IdReader FROM Reader JOIN [User] ON [User].IdUser = Reader.UserId WHERE Login ='" + userLogin + "')\n" +
                      "EXEC IssueBook @ReaderId, @BookId");
            }
          } catch (SQLException throwables) {
            throwables.printStackTrace();
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
          "SELECT IdBook, Book.Name, ReleaseDate, AuthorFIO, NumberOfPage, Description, Quantity FROM Book");

      dataArrayList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        row.add(resultSet.getString("IdBook"));
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

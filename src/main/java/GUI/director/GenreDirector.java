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

public class GenreDirector extends JFrame {
  private JPanel menuPanel;
  private JButton logoutDirectorButton;
  private JButton debtorsDirectorButton;
  private JButton employeesDirectorButton;
  private JButton profileDirectorButton;
  private JButton genresButton;
  private JButton readerButton;
  private JLabel libraryLabelR;
  private JLabel genres;
  private JTextField genreField;
  private JLabel post;
  private JTextField postField;
  private JPanel directorGenrePanel;
  private JButton addGenre;
  private JButton addRole;
  private JPanel genreComboBoxPanel;
  private JPanel postComboBoxPanel;
  private JButton updateGenreButton;
  private JButton updateRoleButton;

  private final JComboBox postComboBox;
  private final JComboBox genresComboBox;
  private Vector<String> genresList;
  private Vector<String> postList;

  public GenreDirector(Point point) {
    setContentPane(directorGenrePanel);
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

    //ComboBox POST
    postList = new Vector<>();
    updatePostList();

    postComboBox = new JComboBox<String>(postList);
    postComboBoxPanel.setLayout(new BorderLayout());
    postComboBoxPanel.add(postComboBox);

    //ComboBox GENRE
    genresList = new Vector<>();
    updateGenreList();

    genresComboBox = new JComboBox<String>(genresList);
    genreComboBoxPanel.setLayout(new BorderLayout());
    genreComboBoxPanel.add(genresComboBox);

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
    readerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ReadersDirector readersDirector = new ReadersDirector(getLocation());
        setVisible(false);
      }
    });
    addGenre.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String genre = genreField.getText();

        if (genre.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          statement.execute(
              "INSERT INTO Genre (Name)\n" +
                  "VALUES ('" + genre + "')");
        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }
        updateGenreList();
      }
    });
    addRole.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String post = postField.getText();

        if (post.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          statement.execute(
              "INSERT INTO Post (Name)\n" +
                  "VALUES ('" + post + "')");
        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }
        updatePostList();
      }
    });
    updateGenreButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String genre = genreField.getText();

        if (genre.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          statement.execute(
              "UPDATE Genre\n" +
                  "SET Name = '" + genre + "'\n" +
                  "WHERE IdGenre = " +
                  "(SELECT IdGenre FROM Genre WHERE Name = '" + genresComboBox.getSelectedItem() + "')");
        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }
        updateGenreList();
      }
    });
    updateRoleButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String post = postField.getText();

        if (post.isEmpty()) {
          Error error = new Error();
          return;
        }

        try {
          Statement statement = DBconnection.connection.createStatement();
          statement.execute(
              "UPDATE Post\n" +
                  "SET Name = '" + post + "'\n" +
                  "WHERE IdPost = " +
                  "(SELECT IdPost FROM Post WHERE Name = '" + postComboBox.getSelectedItem() + "')");
        } catch (SQLException throwables) {
          Error error = new Error();
          throwables.printStackTrace();
        }
        updatePostList();
      }
    });
  }

  private void updateGenreList() {
    genresList.clear();
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Genre");

      while (resultSet.next()) {
        genresList.add(resultSet.getString("Name"));
      }
    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }

  private void updatePostList() {
    postList.clear();
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Post");

      while (resultSet.next()) {
        postList.add(resultSet.getString("Name"));
      }
    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }
}

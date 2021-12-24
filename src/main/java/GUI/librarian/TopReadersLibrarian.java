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

public class TopReadersLibrarian extends JFrame {
  private JPanel librarianTopReadersPanel;
  private JPanel menuPanel;
  private JButton logoutLibrarianButton;
  private JButton topReadersLibrarianButton;
  private JButton debtorsLibrarianButton;
  private JButton catalogLibrarianButton;
  private JButton profileLibrarianButton;
  private JLabel libraryLabelR;
  private JPanel tablePanel;

  private final JTable table;
  private final Vector<Vector<String>> dataArrayList;

  public TopReadersLibrarian(Point point) {
    setContentPane(librarianTopReadersPanel);
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
    header.add("Number books taken");

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
    debtorsLibrarianButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DebtorsLibrarian debtorsLibrarian = new DebtorsLibrarian(getLocation());
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
  }

  private void updateTableData() {
    try {
      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "SELECT FIO, Phone, Email, COUNT(*) AS 'Number books taken'\n" +
              "FROM BookCard " +
              "JOIN Reader ON ReaderId = IdReader " +
              "JOIN [User] ON UserId = IdUser " +
              "GROUP BY IdReader, [User].Login, FIO, Phone, Email " +
              "ORDER BY COUNT(*) DESC");

      dataArrayList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        row.add(resultSet.getString("FIO"));
        row.add(resultSet.getString("Phone"));
        row.add(resultSet.getString("Email"));
        row.add(resultSet.getString("Number books taken"));
        dataArrayList.add(row);
      }
      table.repaint();

    } catch (SQLException throwables) {
      Error error = new Error();
    }
  }
}

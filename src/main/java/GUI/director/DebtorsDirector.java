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
import java.util.Vector;

public class DebtorsDirector extends JFrame {
  private JPanel tablePanel;
  private JLabel libraryLabelR;
  private JPanel directorDebtorsPanel;
  private JButton banButton;
  private JPanel menuPanel;
  private JButton logoutDirectorButton;
  private JButton debtorsDirectorButton;
  private JButton employeesDirectorButton;
  private JButton profileDirectorButton;
  private JTextField debtCountField;
  private JButton searchDebtorsButton;
  private JButton genresButton;
  private JButton readerButton;

  private final JTable table;
  private final Vector<Vector<String>> dataArrayList;

  public DebtorsDirector(Point point) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/play.png"));
      Image icon = bufferedImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
      searchDebtorsButton.setIcon(new ImageIcon(icon));
    } catch (IOException e) {
      e.printStackTrace();
    }

    setContentPane(directorDebtorsPanel);
    setTitle("Library");
    setSize(Tools.WIDTH, Tools.HEIGHT);
    setLocation(point);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    logoutDirectorButton.setBorderPainted(false);
    debtorsDirectorButton.setBorderPainted(false);
    employeesDirectorButton.setBorderPainted(false);
    profileDirectorButton.setBorderPainted(false);
    searchDebtorsButton.setBorderPainted(false);

    genresButton.setBorderPainted(false);
    readerButton.setBorderPainted(false);

    menuPanel.setPreferredSize(new Dimension(Tools.WIDTH / 5, Tools.HEIGHT));

    //table
    dataArrayList = new Vector<>();
    Vector<String> header = new Vector<>();
    //header.add("User id");
    //header.add("Reader id");
    header.add("FIO");
    header.add("Phone");
    header.add("Mail");
    header.add("Blocked");

    tablePanel.setLayout(new BorderLayout());
    table = new JTable(dataArrayList, header);
    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane);

    updateTableData();

    setVisible(true);

    profileDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProfileDirector profileDirector = new ProfileDirector(getLocation());
        setVisible(false);
      }
    });
    employeesDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        EmployeesDirector employeesDirector = new EmployeesDirector(getLocation());
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
    searchDebtorsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTableData();
      }
    });
    banButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vector<Vector<String>> wantToBan = new Vector<>();
        for (int rowIndex : table.getSelectedRows()) {
          wantToBan.add(dataArrayList.get(rowIndex));
        }

        for (Vector<String> user : wantToBan) {
          String userId = user.get(0);

          Statement statement = null;
          try {
            statement = DBconnection.connection.createStatement();
            statement.executeUpdate(
                "UPDATE [User]\n" +
                    "SET Blocked = '1'\n" +
                    "WHERE IdUser = " + userId);
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
        }

        updateTableData();
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

  private void updateTableData() {
    String debtCount = debtCountField.getText();
    if (debtCount.equals("Debt count")) {
      debtCount = "5";
    }
    if (debtCount.isEmpty()) {
      Error error = new Error();
      return;
    }

    try {
      Integer.valueOf(debtCount);

      Statement statement = DBconnection.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
          "EXEC DebtorsN " + debtCount);

      dataArrayList.clear();
      while (resultSet.next()) {
        Vector<String> row = new Vector<>();
        //row.add(resultSet.getString("UserId"));
        //row.add(resultSet.getString("IdReader"));
        row.add(resultSet.getString("FIO"));
        row.add(resultSet.getString("Phone"));
        row.add(resultSet.getString("Email"));
        row.add(resultSet.getString("Blocked"));
        dataArrayList.add(row);
      }
      table.repaint();

    } catch (SQLException | NumberFormatException throwables) {
      Error error = new Error();
    }
  }
}

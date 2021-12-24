package GUI;

import javax.swing.*;

public class Blocked extends JFrame {
  private JLabel ban;
  private JPanel banPanel;

  public Blocked() {
    setContentPane(banPanel);
    setTitle("Library");
    setSize(400, 200);
    setLocationRelativeTo(null);

    setVisible(true);
  }
}

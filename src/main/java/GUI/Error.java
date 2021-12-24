package GUI;

import javax.swing.*;
import java.awt.*;

public class Error extends JFrame {
  private JLabel error;
  private JPanel errorPanel;

  public Error() {
    setContentPane(errorPanel);
    setTitle("Library");
    setSize(400, 200);
    setLocationRelativeTo(null);

    setVisible(true);
  }
}

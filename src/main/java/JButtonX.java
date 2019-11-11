import javax.swing.*;


public class JButtonX extends JButton {
    public JButtonX(String x) {
        super(x);
//        setBackground(new Color(59, 89, 182));
//        setForeground(Color.BLACK);
//        setActionCommand(x.toLowerCase());
        setFocusPainted(false);
        setFocusable(false);
    }
}

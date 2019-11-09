import javax.swing.*;
import java.awt.*;

public class AddTestPanel extends JPanel {

    AddTestPanel() {
        setLayout(new FlowLayout());
        JLabel name = new JLabel("Enter your new test name");
        add(name);
        JTextArea editText = new JTextArea("This is sample text");
        add(editText);
    }
}

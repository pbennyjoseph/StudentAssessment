import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentAssessment extends JFrame implements ActionListener {
    private JPanel mainPanel;//, login, requestNewPassword, questions, uploadAnswer;

    JPanel createMainPanel() {
        JPanel m = new JPanel();
        JLabel title = new JLabel("This is a Login Form");
        m.setLayout(new GridLayout(3, 3));
        m.add(title);
        m.add(new JLabel("Username: "));
        m.add(new JTextField(20));
        m.add(new JLabel("Password: "));
        m.add(new JPasswordField(20));
        JButton submitLogin = new JButton("Login");
        submitLogin.setActionCommand("userLogin");
        submitLogin.addActionListener(this);
        m.add(submitLogin);
        return m;
    }

    public StudentAssessment() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainPanel = createMainPanel();
        setLayout(new FlowLayout());

        add(mainPanel);
        setSize(400, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        new StudentAssessment();
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

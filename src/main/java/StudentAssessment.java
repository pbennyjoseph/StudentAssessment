import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentAssessment extends JFrame implements ActionListener {
    private JPanel mainPanel, throbber;//, login, requestNewPassword, questions, uploadAnswer;

    private void createLoader() {
        throbber = new JPanel(new FlowLayout());
        throbber.setVisible(false);
        java.net.URL throbberURL = getClass().getResource("./ajax-loader.gif");
        ImageIcon loader = new ImageIcon(throbberURL);
        JLabel thr = new JLabel("Logging you in...", loader, JLabel.CENTER);
        thr.setVisible(true);
        throbber.add(thr);
    }

    private JPanel createMainPanel() {
        JPanel m = new JPanel();
        JPanel titlePanel = new JPanel(new FlowLayout());
        JLabel title = new JLabel("Login to Access Questions");
        titlePanel.add(title);
        m.setLayout(new GridLayout(4, 1));
        JPanel userPanel = new JPanel(new FlowLayout());
        userPanel.add(new JLabel("Username: "));
        userPanel.add(new JTextField(20));
        JPanel pwdPanel = new JPanel(new FlowLayout());
        pwdPanel.add(new JLabel("Password: "));
        pwdPanel.add(new JPasswordField(20));
        JPanel submitPanel = new JPanel(new FlowLayout());
        JButton submitLogin = new JButton("Login");
        submitLogin.setFocusable(false);
        submitLogin.setActionCommand("userLogin");
        submitLogin.addActionListener(this);
        submitPanel.add(submitLogin);
        m.add(titlePanel);
        m.add(userPanel);
        m.add(pwdPanel);
        m.add(submitPanel);
        return m;
    }

    private StudentAssessment() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("Login");

        mainPanel = createMainPanel();
        createLoader();
        setLayout(new BorderLayout());

        add(mainPanel, BorderLayout.CENTER);
        add(throbber, BorderLayout.NORTH);
        pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setVisible(true);
    }

    public static void main(String[] args) {
//        new StudentAssessment();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentAssessment();
            }
        });
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            throbber.setVisible(true);
            mainPanel.setVisible(false);
            repaint();
            SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    Thread.sleep(2000);
                    return true;
                }

                protected void done() {
                    mainPanel.setVisible(true);
                    throbber.setVisible(false);
                    repaint();
                }

            };
            swingWorker.execute();
        } catch (Exception ex) {
            System.out.println("Something happened");
        }
    }
}

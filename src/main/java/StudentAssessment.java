import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StudentAssessment extends JFrame implements ActionListener {
    private static final String baseURL = "http://localhost/javamini/index.php?";
    private JPanel loginPanel, throbber, mainCardPanel;//, login, requestNewPassword, questions, uploadAnswer;
    private JPasswordField pwd;
    private JButton logoutButton;
    private JTextField username;
    private JLabel thr;
    private CardLayout cx;
    private webClient wx;

    private void createLoader() {
        throbber = new JPanel(new BorderLayout());
//        throbber.setVisible(false);
        java.net.URL throbberURL = getClass().getResource("./def-loader.gif");
        ImageIcon loader = new ImageIcon(throbberURL);
        thr = new JLabel("Logging you in...", loader, JLabel.CENTER);
        thr.setVisible(false);
        logoutButton = new JButton("Logout");
        logoutButton.setVisible(false);
        logoutButton.setFocusable(false);
        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(this);
        throbber.add(logoutButton, BorderLayout.EAST);
        throbber.add(thr, BorderLayout.CENTER);

    }

    private JPanel createMainPanel() {
        JPanel m = new JPanel();
        JPanel titlePanel = new JPanel(new FlowLayout());
        JLabel title = new JLabel("Login to Access Questions");
        titlePanel.add(title);

        m.setLayout(new GridLayout(4, 1));

        JPanel userPanel = new JPanel(new FlowLayout());
        userPanel.add(new JLabel("Username: "));
        userPanel.add(username = new JTextField(20));

        JPanel pwdPanel = new JPanel(new FlowLayout());
        pwdPanel.add(new JLabel("Password: "));
        pwdPanel.add(pwd = new JPasswordField(20));

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
        wx = new webClient();

        loginPanel = createMainPanel();
        createLoader();
        cx = new CardLayout();
        mainCardPanel = new JPanel(cx);
        setLayout(new BorderLayout());

        mainCardPanel.add(loginPanel, "mainCard");

        add(throbber, BorderLayout.NORTH);
//        mainCardPanel.add(throbber, "loaderCard");
        JPanel ip = new InstructorPanel();
        mainCardPanel.add(ip, "adminCard");

        add(mainCardPanel, BorderLayout.CENTER);
        pack();


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedure();
            }
        });

        setVisible(true);
    }

    private void exitProcedure() {
//        timerThread.setRunning(false);
        System.exit(0);
    }

    public static void main(String[] args) {
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
     * @param e The ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("userLogin")) {
            thr.setVisible(true);
            mainCardPanel.setVisible(false);
            SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                int responseStatus;

                @Override
                protected Boolean doInBackground() throws Exception {
                    String x = wx.sendGet(baseURL + "user=" + username.getText()
                            + "&pwd=" + new String(pwd.getPassword()));
                    responseStatus = Integer.parseInt(x);
                    return true;
                }

                protected void done() {
                    thr.setVisible(false);
                    mainCardPanel.setVisible(true);
                    if (responseStatus == 2) {
                        username.setText("");
                        pwd.setText("");
                        JOptionPane.showMessageDialog(null, "You are now logged in as Student");
                        CardLayout ctx = (CardLayout) mainCardPanel.getLayout();
                        setExtendedState(MAXIMIZED_BOTH);
                        logoutButton.setVisible(true);
                        ctx.show(mainCardPanel, "adminCard");
                    } else if (responseStatus == 1) {
                        JOptionPane.showMessageDialog(null, "You are now logged in as Instructor");
                        CardLayout ctx = (CardLayout) mainCardPanel.getLayout();
                        setExtendedState(MAXIMIZED_BOTH);
                        logoutButton.setVisible(true);
                        ctx.show(mainCardPanel, "adminCard");
                    } else {
                        mainCardPanel.setVisible(true);
                        JOptionPane.showMessageDialog(null, "Incorrect Details, try again");
                    }
                    repaint();
                }
            };
            swingWorker.execute();
        } else if (e.getActionCommand().equals("logout")) {
            logoutButton.setVisible(false);
            StudentAssessment.main(null);
            setVisible(false);
            dispose();
//            CardLayout ctx = (CardLayout) mainCardPanel.getLayout();
//            ctx.previous(mainCardPanel);
//            pack();
//            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//            setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        }
    }
}

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StudentAssessment extends JFrame implements ActionListener {

    //    static final String baseURL = "http://localhost/javamini/";
    static final String baseURL = "https://bennyjoseph.000webhostapp.com/javamini/";
    private static JLabel thr;
    private static CardLayout loaderLayout;
    static StudentAssessment SA_MAIN;
    private static JPanel throbber, switchPanel;
    static webClient wx;


    private static JPanel mainCardPanel;//, login, requestNewPassword, questions, uploadAnswer;
    private JPanel logoutPanel;
    private JPasswordField pwd;
    private JButtonX logoutButton, submitLogin;
    private JTextField username;

    private void createLoader() {
        throbber = new JPanel(new BorderLayout());
//        throbber.setVisible(false);
        java.net.URL throbberURL = getClass().getResource("def-loader.gif");
        ImageIcon loader = new ImageIcon(throbberURL);
        thr = new JLabel("Checking internet access.", loader, JLabel.CENTER);


        thr.setVisible(false);
        JLabel readyStatus = new JLabel("Ready.", JLabel.CENTER);
        switchPanel = new JPanel(loaderLayout = new CardLayout());
        switchPanel.add(thr, "Loader");
        switchPanel.add(readyStatus, "ready");

//        throbber.add(thr, BorderLayout.CENTER);
//        throbber.add(readyStatus, BorderLayout.CENTER);
        throbber.add(switchPanel, BorderLayout.CENTER);
        throbber.setBackground(new Color(199, 199, 199));

        logoutPanel = new JPanel(new BorderLayout());
        logoutButton = new JButtonX("Logout");
        logoutButton.setVisible(false);
        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(this);
        logoutPanel.add(logoutButton, BorderLayout.EAST);

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
        username.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pwd.requestFocus();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pwd.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        JPanel pwdPanel = new JPanel(new FlowLayout());
        pwdPanel.add(new JLabel("Password: "));
        pwdPanel.add(pwd = new JPasswordField(20));

        JPanel submitPanel = new JPanel(new FlowLayout());
        submitLogin = new JButtonX("Login");
        submitLogin.setEnabled(false);


        submitLogin.setActionCommand("userLogin");
        submitLogin.addActionListener(this);

        pwd.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitLogin.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

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

        JPanel loginPanel = createMainPanel();
        createLoader();
        CardLayout cx = new CardLayout();
        mainCardPanel = new JPanel(cx);
        setLayout(new BorderLayout());

        mainCardPanel.add(loginPanel, "mainCard");

        add(logoutPanel, BorderLayout.NORTH);
        add(throbber, BorderLayout.SOUTH);
//        mainCardPanel.add(throbber, "loaderCard");

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
        try {
            wx.close();
        } catch (Exception ignored) {

        }
        setVisible(false);
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SA_MAIN = new StudentAssessment();
            SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    if (webClient.hasInternet()) {
                        SA_MAIN.submitLogin.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(StudentAssessment.SA_MAIN, "No Internet");
                        SA_MAIN.exitProcedure();
                    }
                    StudentAssessment.hideLoader();
                    SA_MAIN.username.requestFocus();
                    thr.setText("Logging you in...");
                    return true;
                }
            };
            swingWorker.execute();
            StudentAssessment.showLoader();

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
            showLoader();
            SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                int responseStatus;

                @Override
                protected Boolean doInBackground() throws Exception {
                    ArrayList<NameValuePair> fx = new ArrayList<>();
                    fx.add(new BasicNameValuePair("user", username.getText()));
                    fx.add(new BasicNameValuePair("pwd", new String(pwd.getPassword())));
                    String x = wx.sendPost(baseURL + "index.php", fx);
                    responseStatus = Integer.parseInt(x);
                    System.out.println(x + " My printer");
                    return true;
                }

                protected void done() {
                    hideLoader();
                    if (responseStatus == 2) {
                        JPanel up = new StudentPanel(username.getText());
                        mainCardPanel.add(up, "userCard");
                        username.setText("");
                        pwd.setText("");
                        JOptionPane.showMessageDialog(StudentAssessment.SA_MAIN, "You are now logged in as Student");
                        CardLayout ctx = (CardLayout) mainCardPanel.getLayout();
                        setExtendedState(MAXIMIZED_BOTH);
                        logoutButton.setVisible(true);
                        ctx.show(mainCardPanel, "userCard");
                    } else if (responseStatus == 1) {
                        JPanel ip = new InstructorPanel();
                        mainCardPanel.add(ip, "adminCard");
                        JOptionPane.showMessageDialog(StudentAssessment.SA_MAIN, "You are now logged in as Instructor");
                        CardLayout ctx = (CardLayout) mainCardPanel.getLayout();
                        setExtendedState(MAXIMIZED_BOTH);
                        logoutButton.setVisible(true);
                        ctx.show(mainCardPanel, "adminCard");
                    } else {
                        mainCardPanel.setVisible(true);
                        JOptionPane.showMessageDialog(StudentAssessment.SA_MAIN, "Incorrect Details, try again");
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

    static void hideLoader() {
        loaderLayout.show(switchPanel, "ready");
//        readyStatus.setVisible(true);
//        thr.setVisible(false);
        StudentAssessment.thr.setText("Loading...");
        mainCardPanel.setVisible(true);
    }

    static void showLoader() {
        loaderLayout.show(switchPanel, "Loader");
        mainCardPanel.setVisible(false);
    }

    static void showLoader(String x) {
        thr.setText(x);
        loaderLayout.show(switchPanel, "Loader");
//        readyStatus.setVisible(false);
//        thr.setVisible(true);
        mainCardPanel.setVisible(false);
    }
}

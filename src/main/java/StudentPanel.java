import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentPanel extends JPanel implements ActionListener {
    public static JPanel contentAndAction, centerPanel;
    private JPanel welcome, unatt, att;
    public static JPanel SP_Main;
    String[] name, Attempted, UnAttempted;
    String username;

    StudentPanel(String username) {
        SP_Main = this;
        this.username = username;
        setLayout(new BorderLayout());
        centerPanel = new JPanel(new FlowLayout());
        add(centerPanel, BorderLayout.CENTER);
        SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
            String retval;

            @Override
            protected Boolean doInBackground() throws Exception {
                centerPanel.removeAll();
                ArrayList<NameValuePair> ax = new ArrayList<NameValuePair>();
                ax.add(new BasicNameValuePair("user", username));
                retval = StudentAssessment.wx.sendPost(StudentAssessment.baseURL
                        + "getAllTestsforUser.php", ax);
                System.out.println(retval);
                name = retval.split("@@");

                String[] attempted = name[1].split("::");
                Attempted = attempted[0].split("~");
                ArrayList<String> list = new ArrayList<String>();

                for (String s : Attempted) {
                    if (s != null && s.length() > 0) {
                        list.add(s);
                    }
                }

                Attempted = (String[]) ((ArrayList) list).toArray(new String[list.size()]);
                UnAttempted = attempted[1].split("~");
                list = new ArrayList<String>();

                for (String s : UnAttempted) {
                    if (s != null && s.length() > 0) {
                        list.add(s);
                    }
                }

                UnAttempted = (String[]) ((ArrayList) list).toArray(new String[list.size()]);
                System.out.println(name[0]);
                for (String x : Attempted) {
                    System.out.println(x);
                }
                for (String x : UnAttempted) {
                    System.out.println(x);
                }
//                JSONParser parser = new JSONParser();
//                try {
//                    Object obj = parser.parse(retval);
//
//                } catch (ClassCastException cex) {
//                    cex.printStackTrace();
//                } catch (ParseException pe) {
//                    pe.printStackTrace();
//                }
                return true;
            }

            @Override
            protected void done() {
                JPanel subGrid = new JPanel(new GridLayout(2, 1, 1, 3));
                welcome = new JPanel(new FlowLayout());
                welcome.add(new JLabel("Welcome " + name[0] + "!"));
                add(welcome, BorderLayout.NORTH);

                unatt = new JPanel();
                if (UnAttempted == null || UnAttempted.length == 0) {
                    unatt.setLayout(new FlowLayout());
                    unatt.add(new JLabel("No new Tests available."));
                } else {
                    unatt.setLayout(new GridLayout(UnAttempted.length + 1, 2, 2, 2));
                    unatt.add(new JLabel("Unattempted Tests", JLabel.CENTER));
                    unatt.add(new JLabel("Click to Attempt", JLabel.CENTER));
                    for (String x : UnAttempted) {
                        unatt.add(new JLabel(x, JLabel.CENTER));
                        JButtonX bx = new JButtonX("Take Test");
                        bx.setActionCommand(x);
                        bx.addActionListener(new ActionListener() {
                            String testName = x;
                            JPanel panel = centerPanel;

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                panel.removeAll();
                                revalidate();
                                repaint();
                                panel.add(new TakeTestPanel(username, testName));
                                revalidate();
                                repaint();
                                System.out.println("also here");

                            }
                        });
                        unatt.add(bx);
                    }
                }
                subGrid.add(unatt);

                att = new JPanel();
                if (Attempted == null || Attempted.length == 0) {
                    att.setLayout(new FlowLayout());
                    att.add(new JLabel("No Tests Attempted."));
                } else {
                    att.setLayout(new GridLayout(Attempted.length + 1, 2, 2, 2));
                    att.add(new JLabel("Attempted Tests", JLabel.CENTER));
                    att.add(new JLabel("Result and Review", JLabel.CENTER));
                    for (String x : Attempted) {
                        att.add(new JLabel(x, JLabel.CENTER));
                        JButtonX bx = new JButtonX("Review Test");
                        bx.setActionCommand(x);
                        bx.addActionListener(new ActionListener() {
                            String testName = x;

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                centerPanel.removeAll();
                                centerPanel.add(new ReviewPanel(username, testName));
                                revalidate();
                            }
                        });
                        att.add(bx);
                    }
                }
                subGrid.add(att);
                centerPanel.add(subGrid);
                revalidate();
            }
        };
        swingWorker.execute();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentPanel extends JPanel implements ActionListener {
    static JPanel centerPanel;
    private JPanel welcome, unatt, att;
    private String[] name, Attempted, UnAttempted;
    static StudentPanel SP_Main;
    private String uname;

    void makePanel() {
        SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
            String retval;

            @Override
            protected Boolean doInBackground() throws Exception {
                StudentAssessment.showLoader("Fetching Tests...");
                centerPanel.removeAll();
                revalidate();
                ArrayList<NameValuePair> ax = new ArrayList<>();
                ax.add(new BasicNameValuePair("user", uname));
                retval = StudentAssessment.wx.sendPost(StudentAssessment.baseURL
                        + "getAllTestsforUser.php", ax);
                System.out.println(retval);

                // retval is in the form of name@@unnattest~unnatttest2::atttest~atttest2
                name = retval.split("@@");

                String[] attempted = name[1].split("::");
                Attempted = attempted[0].split("~");
                ArrayList<String> list = new ArrayList<>();

                for (String s : Attempted) {
                    if (s != null && s.length() > 0) {
                        list.add(s);
                    }
                }

                Attempted = (String[]) ((ArrayList) list).toArray(new String[list.size()]);
                UnAttempted = attempted[1].split("~");
                list = new ArrayList<>();

                for (String s : UnAttempted) {
                    if (s != null && s.length() > 0) {
                        list.add(s);
                    }
                }

                UnAttempted = (String[]) ((ArrayList) list).toArray(new String[list.size()]);
                System.out.println(name[0]);
                System.out.println(name[1]);
                System.out.println(Attempted.length);
                for (String x : Attempted) {
                    System.out.println("att" + x);
                }
                for (String x : UnAttempted) {
                    System.out.println("uatt" + x);
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
                if (name[1].equals("::-1")) {
                    Attempted = null;
                    UnAttempted = null;
                }
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
//                        bx.addActionListener(SP_Main);
                        bx.addActionListener(new ActionListener() {
                            String testName = x;

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                centerPanel.removeAll();
                                SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                                    @Override
                                    protected Boolean doInBackground() {
                                        StudentAssessment.showLoader("Fetching Test Data..");
                                        centerPanel.add(new TakeTestPanel(uname, testName));
                                        return true;
                                    }

                                    @Override
                                    protected void done() {
                                        StudentAssessment.hideLoader();
                                        revalidate();
                                    }
                                };
                                swingWorker.execute();
                            }
                        });
                        unatt.add(bx);
                    }
                }
                subGrid.add(unatt);


                att = new JPanel();
                if (Attempted == null || Attempted.length == 0) {
                    att.setLayout(new FlowLayout());
                    att.add(new JLabel("No Attempted Tests Evaluated."));
                } else {
                    att.setLayout(new GridLayout(Attempted.length + 1, 2, 2, 2));
                    att.add(new JLabel("Attempted and Evaluated Tests", JLabel.CENTER));
                    att.add(new JLabel("Result and Review", JLabel.CENTER));
                    for (String x : Attempted) {
                        att.add(new JLabel(x, JLabel.CENTER));
                        JButtonX bx = new JButtonX("Review Test");
                        bx.setActionCommand(x);
//                        bx.addActionListener(SP_Main);
                        bx.addActionListener(new ActionListener() {
                            String testName = x;

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                centerPanel.removeAll();
                                SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                                    @Override
                                    protected Boolean doInBackground() {
                                        StudentAssessment.showLoader("Fetching Test Scores..");
                                        centerPanel.add(new ReviewPanel(uname, testName));
                                        return true;
                                    }

                                    @Override
                                    protected void done() {
                                        StudentAssessment.hideLoader();
                                        revalidate();
                                    }
                                };
                                swingWorker.execute();
                            }
                        });
                        att.add(bx);
                    }
                }
                subGrid.add(att);
                centerPanel.add(subGrid);
                StudentAssessment.hideLoader();
                revalidate();
            }
        };
        swingWorker.execute();
    }

    StudentPanel(String username) {
        SP_Main = this;
        uname = username;
        setLayout(new BorderLayout());
        centerPanel = new JPanel(new FlowLayout());
        makePanel();
        add(centerPanel, BorderLayout.CENTER);

    }

    // Things that won't work
    public void actionPerformed(ActionEvent e) {

//        centerPanel.removeAll();
//        String testName = e.getActionCommand();

//        StudentAssessment.showLoader("Fetching Questions...");
//        centerPanel.add(new TakeTestPanel(uname, testName));
//        revalidate();
//        StudentAssessment.hideLoader();

//        StudentAssessment.showLoader("Fetching Questions...");
//        System.out.println("Fetching...");
//        Thread t = new Thread(() ->
//                centerPanel.add(new TakeTestPanel(uname, testName)));
//        t.start();
//        try {
//            t.join();
//            StudentAssessment.hideLoader();
//            System.out.println("Fetched...");
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
    }

}


import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

class TakeTestPanel extends JPanel {
    private String[] ans;

    TakeTestPanel(String username, String testName) {
        setLayout(new BorderLayout());
        ArrayList<NameValuePair> ax = new ArrayList<>();
        ax.add(new BasicNameValuePair("testname", testName));
        String retval = null;
        try {
            retval = StudentAssessment.wx.sendPost(StudentAssessment.baseURL
                    + "getUserResponses.php", ax);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();

        JPanel QuestionsPanel = new JPanel();
        try {
            assert retval != null;
            JSONObject obj = (JSONObject) parser.parse(retval);
            JSONArray ques = (JSONArray) obj.get("questions");
            ans = new String[ques.size()];
            QuestionsPanel.setLayout(new GridLayout(2 * ques.size(), 1));
            int i = 0;
            for (Object x : ques) {
                JTextArea questionArea = new JTextArea();
                questionArea.setEditable(false);
                questionArea.setLineWrap(true);
//                System.out.println((String) x);
                questionArea.setText(((String) x).trim());
                JPanel qPanel = new JPanel(new BorderLayout());
                qPanel.add(new JLabel("Question " + (i + 1) + "   ", JLabel.CENTER), BorderLayout.NORTH);
                qPanel.add(new JScrollPane(questionArea,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
                QuestionsPanel.add(qPanel);
                JPanel ansPanel = new JPanel(new BorderLayout());
                JTextArea ansArea = new JTextArea(5, 50);
                ansArea.setTransferHandler(null);

                int finalI = i;
                ansArea.addKeyListener(new KeyListener() {
                    int id = finalI;

                    @Override

                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        ans[id] = ((JTextArea) e.getSource()).getText();
                    }
                });
                ansPanel.add(new JLabel("Your Answer: ", JLabel.CENTER), BorderLayout.NORTH);
                ansPanel.add(new JScrollPane(ansArea,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
                QuestionsPanel.add(ansPanel);
//                JPanel singleQuestion = new JPanel(new FlowLayout());
//                singleQuestion.add(qPanel);
//                singleQuestion.add(ansPanel);
//                QuestionsPanel.add(singleQuestion);
                ansArea.setLineWrap(true);
                ++i;
            }
//        JPanel flowPanel = new JPanel(new FlowLayout());
//        flowPanel.add(jsp);
            add(QuestionsPanel);
//            JScrollPane jsp = new JScrollPane(QuestionsPanel,
//                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//            jsp.setPreferredSize(new Dimension((int) (dim.width * 0.8), (int) (0.5 * dim.height)));
//            add(jsp, BorderLayout.CENTER);
//            revalidate();
        } catch (ClassCastException ignored) {

        } catch (ParseException | org.json.simple.parser.ParseException pex) {
            pex.printStackTrace();
        }
        JButtonX SubmitTest = new JButtonX("Submit");
        SubmitTest.addActionListener(e -> {
            StudentAssessment.showLoader("Submitting test...");
            JSONArray ja = new JSONArray();
            ja.addAll(Arrays.asList(ans));
            removeAll();
            StudentPanel.centerPanel.removeAll();
            revalidate();

            SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    ArrayList<NameValuePair> ax1 = new ArrayList<>();
                    ax1.add(new BasicNameValuePair("user", username));
                    ax1.add(new BasicNameValuePair("answers", ja.toJSONString()));
                    ax1.add(new BasicNameValuePair("testname", testName));

                    try {
//                        Thread.sleep(2000); // Debug content
                        System.out.println(StudentAssessment.wx.sendPost(StudentAssessment.baseURL
                                + "submitTest.php", ax1));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return true;
                }

                @Override
                protected void done() {
                    StudentAssessment.hideLoader();
                    JOptionPane.showMessageDialog(null, "Your responses have been submitted.");
                    StudentPanel.SP_Main.makePanel();
                }
            };
            swingWorker.execute();
        });
        JPanel submitPanel = new JPanel(new FlowLayout());
        submitPanel.add(SubmitTest);
        add(submitPanel, BorderLayout.SOUTH);
        revalidate();
    }
}

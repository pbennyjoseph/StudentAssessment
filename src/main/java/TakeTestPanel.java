import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class TakeTestPanel extends JPanel {
    private String[] ans;
    private String username;
    private JPanel TTP_Main;

    public TakeTestPanel(String username, String testName) {
        this.username = username;
        TTP_Main = this;
        setLayout(new BorderLayout());
        ArrayList<NameValuePair> ax = new ArrayList<NameValuePair>();
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
            JSONObject obj = (JSONObject) parser.parse(retval);
            JSONArray ques = (JSONArray) obj.get("questions");
            ans = new String[ques.size()];
            QuestionsPanel.setLayout(new GridLayout(ques.size() * 2, 1));
            int i = 0;
            for (Object x : ques) {
                JTextArea questionArea = new JTextArea(5, 50);
                questionArea.setEditable(false);
                questionArea.setLineWrap(true);
                questionArea.setText((String) x);
                JPanel qPanel = new JPanel(new FlowLayout());
                qPanel.add(new JLabel("Question " + (i + 1) + "   ", JLabel.CENTER));
                qPanel.add(questionArea);
                QuestionsPanel.add(qPanel);
                JPanel ansPanel = new JPanel(new FlowLayout());
                JTextArea ansArea = new JTextArea(5, 50);
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
                ansPanel.add(new JLabel("Your Answer: ", JLabel.CENTER));
                ansPanel.add(ansArea);
                QuestionsPanel.add(ansPanel);
                ansArea.setLineWrap(true);
                ++i;
            }

        } catch (ClassCastException ignored) {

        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        JButtonX SubmitTest = new JButtonX("Submit");
        SubmitTest.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                JSONArray ja = new JSONArray();
                ja.addAll(Arrays.asList(ans));
                ArrayList<NameValuePair> ax = new ArrayList<NameValuePair>();
                ax.add(new BasicNameValuePair("user", username));
                ax.add(new BasicNameValuePair("answers", ja.toJSONString()));
                ax.add(new BasicNameValuePair("testname", testName));
                try {
                    System.out.println(StudentAssessment.wx.sendPost(StudentAssessment.baseURL
                            + "submitTest.php", ax));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                removeAll();
                StudentPanel.centerPanel.removeAll();
                revalidate();
            }

        });
        JPanel submitPanel = new JPanel(new FlowLayout());
        submitPanel.add(SubmitTest);
        JScrollPane jsp = new JScrollPane(QuestionsPanel
                , ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(jsp, BorderLayout.CENTER);
        add(submitPanel, BorderLayout.SOUTH);
    }
}

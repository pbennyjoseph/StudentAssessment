import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ReviewPanel extends JPanel {

    ReviewPanel(String username, String testName) {
        setLayout(new BorderLayout());
        StudentAssessment.showLoader();
        ArrayList<NameValuePair> ax = new ArrayList<>();
        ax.add(new BasicNameValuePair("user", username));
        ax.add(new BasicNameValuePair("testname", testName));
        String retval = null;
        try {
            retval = StudentAssessment.wx.sendPost(StudentAssessment.baseURL
                    + "getUserTestData.php", ax);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(retval);
        assert retval != null;

        // retval is in the form of name@@unnattest~unnatttest2::atttest~atttest2

        String[] score = retval.split("@@");
        JLabel result = new JLabel("Your score for the test is " + score[0]);
        add(result, BorderLayout.NORTH);
        JSONParser parser = new JSONParser();
        try {
            JSONArray ans = (JSONArray) parser.parse(score[1]);
            JSONArray ques = (JSONArray) parser.parse(score[2]);
            JPanel QuestionsPanel = new JPanel(new FlowLayout());
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
                ansArea.setText((String) ans.get(i));
                ansPanel.add(new JLabel("Your Answer: ", JLabel.CENTER));
                ansPanel.add(ansArea);
                QuestionsPanel.add(ansPanel);
                ansArea.setLineWrap(true);
                ++i;
            }
            add(QuestionsPanel, BorderLayout.CENTER);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        StudentAssessment.hideLoader();
    }
}

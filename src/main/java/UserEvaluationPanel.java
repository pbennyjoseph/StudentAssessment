import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class UserEvaluationPanel extends JPanel implements ActionListener {
    private final String name;
    private int totalScore;
    private JButtonX submitEvaluation;
    private String studentName, ans;
    JSONArray questions;
    JSONObject newResponses;

    UserEvaluationPanel(String student, String answers, JSONArray questions, JSONObject response, String testName) {
        studentName = student;
        this.questions = questions;
        this.newResponses = response;
        this.name = testName;
        ans = answers;
        setLayout(new BorderLayout());
        JSONParser parser = new JSONParser();
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(questions.size() + 1, 3, 2, 2));
        totalScore = questions.size() * 7;

        contentPanel.add(new JLabel("Question", JLabel.CENTER));
        contentPanel.add(new JLabel("Student's Answer", JLabel.CENTER));
        contentPanel.add(new JLabel("Marks out of 10", JLabel.CENTER));
        try {
            JSONArray ja = (JSONArray) parser.parse(answers);
            Iterator ix = questions.iterator();
            for (Object answer : ja) {
                JPanel jx = new JPanel(new FlowLayout());
                JTextArea jta = new JTextArea(ix.next().toString());
                jta.setLineWrap(true);

                jx.add(new JScrollPane(jta,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
                contentPanel.add(jta);


                JTextArea jtx = new JTextArea(5, 30);
                jtx.setEditable(false);
                jtx.setText((String) answer);

                contentPanel.add(new JScrollPane(jtx,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
                JSpinner countQuestions = new JSpinner(new SpinnerNumberModel(7, 1, 10, 1));

                ((JSpinner.DefaultEditor) countQuestions.getEditor()).getTextField().setEditable(false);
                countQuestions.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        JSpinner js = (JSpinner) e.getSource();
                        int currValue = (Integer) js.getValue();
                        if (currValue > 7) {
                            totalScore++;
                        } else totalScore--;
                    }
                });
                jx.add(countQuestions);

                contentPanel.add(jx);
            }
            add(contentPanel, BorderLayout.CENTER);
            submitEvaluation = new JButtonX("Submit Evaluation");
            submitEvaluation.addActionListener(this);
            add(submitEvaluation, BorderLayout.SOUTH);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ArrayList<NameValuePair> ax = new ArrayList<NameValuePair>();
            ax.add(new BasicNameValuePair("std", studentName));
            ax.add(new BasicNameValuePair("score", totalScore + ""));
            ax.add(new BasicNameValuePair("MaxScore", "" + (questions.size() * 10)));
            ax.add(new BasicNameValuePair("stdanswers", ans));
            ax.add(new BasicNameValuePair("newResponses", newResponses.toJSONString()));
            ax.add(new BasicNameValuePair("testname", name));
            JOptionPane.showMessageDialog(null, "Was Here");
            System.out.println(StudentAssessment.wx.sendPost(StudentAssessment.baseURL + "EvalStudent.php", ax));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

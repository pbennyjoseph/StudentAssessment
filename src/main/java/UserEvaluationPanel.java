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
import java.util.ArrayList;
import java.util.Iterator;

public class UserEvaluationPanel extends JPanel implements ActionListener {
    private final String name;
    private int totalScore;
    private String studentName, ans;
    private JPanel contentPanel, buttonPanel;
    private JSONArray questions;
    private JSONObject newResponses;

    UserEvaluationPanel(String student, String answers, JSONArray questions, JSONObject response, String testName) {
        studentName = student;
        this.questions = questions;
        this.newResponses = response;
        this.name = testName;
        ans = answers;
        setLayout(new BorderLayout());
        JSONParser parser = new JSONParser();
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 1, 2, 2));
//        contentPanel.setLayout(new GridLayout(questions.size() + 1, 3, 2, 2));
        totalScore = questions.size() * 7;

        JPanel labelPanel = new JPanel(new GridLayout(1, 3));
        labelPanel.add(new JLabel("Question", JLabel.CENTER));
        labelPanel.add(new JLabel("Student's Answer", JLabel.CENTER));
        labelPanel.add(new JLabel("Marks out of 10", JLabel.CENTER));
        contentPanel.add(labelPanel);
        try {
            JSONArray ja = (JSONArray) parser.parse(answers);
            Iterator ix = questions.iterator();
            for (Object answer : ja) {
                JPanel onlyOneRowPanel = new JPanel(new FlowLayout());
                JPanel qPanel = new JPanel(new FlowLayout());
                JTextArea jta = new JTextArea(ix.next().toString().trim());
                jta.setLineWrap(true);

                qPanel.add(new JScrollPane(jta,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
                onlyOneRowPanel.add(qPanel);
                JTextArea jtx = new JTextArea(5, 30);
                jtx.setEditable(false);
                jtx.setLineWrap(true);
                jtx.setText((String) answer);
                JPanel ansPanel = new JPanel(new FlowLayout());
                ansPanel.add(new JScrollPane(jtx,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

                onlyOneRowPanel.add(ansPanel);

                JSpinner countQuestions = new JSpinner(new SpinnerNumberModel(7, 1, 10, 1));

                ((JSpinner.DefaultEditor) countQuestions.getEditor()).getTextField().setEditable(false);
                countQuestions.addChangeListener(e -> {
                    JSpinner js = (JSpinner) e.getSource();
                    int currValue = (Integer) js.getValue();
                    if (currValue > 7) {
                        totalScore++;
                    } else totalScore--;
                });
                onlyOneRowPanel.add((new JPanel(new FlowLayout())).add(countQuestions));
                contentPanel.add(onlyOneRowPanel);
            }
            add(new JScrollPane(contentPanel
                    , ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
            JButtonX submitEvaluation = new JButtonX("Submit Evaluation");
            submitEvaluation.addActionListener(this);
            buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(submitEvaluation);
            add(buttonPanel, BorderLayout.SOUTH);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StudentAssessment.showLoader();
        try {
            ArrayList<NameValuePair> ax = new ArrayList<>();
            ax.add(new BasicNameValuePair("std", studentName));
            ax.add(new BasicNameValuePair("score", totalScore + ""));
            ax.add(new BasicNameValuePair("MaxScore", "" + (questions.size() * 10)));
            ax.add(new BasicNameValuePair("stdanswers", ans));
            ax.add(new BasicNameValuePair("newResponses", newResponses.toJSONString()));
            ax.add(new BasicNameValuePair("testname", name));
            JOptionPane.showMessageDialog(null, "Sucessfully submitted evaluation");
            InstructorPanel.evaluateMarks.setEnabled(true);
            contentPanel.removeAll();
            buttonPanel.removeAll();
            revalidate();
            System.out.println(StudentAssessment.wx.sendPost(StudentAssessment.baseURL + "EvalStudent.php", ax));
            StudentAssessment.hideLoader();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class TestResponsePanel extends JPanel implements ActionListener {
    private JSONObject responses;
    private JSONArray questions;
    private JPanel TRP_Main;
    private JPanel contentPanel;
    private String testName;

    TestResponsePanel(String x) {
        TRP_Main = this;
        testName = x;
        contentPanel = new JPanel();
        SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
            boolean res;

            @Override
            protected Boolean doInBackground() throws Exception {
                StudentAssessment.showLoader("Fetching responses...");
                ArrayList<NameValuePair> ax = new ArrayList<>();
                ax.add(new BasicNameValuePair("testname", x));

                String retval = StudentAssessment.wx.sendPost(StudentAssessment.baseURL
                        + "getUserResponses.php", ax);
//                add(new JLabel(retval));
                JSONParser parser = new JSONParser();
                try {
                    Object obj = parser.parse(retval);

                    responses = (JSONObject) ((JSONObject) obj).get("responses");

                    questions = (JSONArray) ((JSONObject) obj).get("questions");
                    if (responses.isEmpty()) {
                        InstructorPanel.evaluateMarks.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "No responses to evaluate");
                        return res = false;
                    }
                } catch (ClassCastException cex) {
                    InstructorPanel.evaluateMarks.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Nothing to evaluate here");
                    return res = false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return res = false;
                }
                return res = true;
            }

            @Override
            protected void done() {
                StudentAssessment.hideLoader();
                if (!res) return;

                contentPanel.setLayout(new GridLayout(responses.size(), 2, 2, 2));
//                for (String x : (Set<String>) responses.keySet()) {
//                    contentPanel.add(new JLabel(x));
//                    JButtonX bx = new JButtonX("Evaluate");
//                    bx.addActionListener((ActionListener) TRP_Main);
//                    bx.setActionCommand(x);
//                    contentPanel.add(bx);
//                }
                for (Object x : responses.entrySet()) {
                    Map.Entry mx = (Map.Entry) x;
                    contentPanel.add(new JLabel(String.valueOf(mx.getKey())));
                    JButtonX bx = new JButtonX("Evaluate");
                    bx.addActionListener((ActionListener) TRP_Main);
                    bx.setActionCommand(mx.getKey() + "@@" + mx.getValue().toString());
                    contentPanel.add(bx);
                }
                add(contentPanel);
                revalidate();
            }
        };
        swingWorker.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.removeAll();
        String[] s = e.getActionCommand().split("@@");
        responses.remove(s[0]);
        contentPanel.add(new UserEvaluationPanel(s[0], s[1], questions, responses, testName));
        revalidate();
    }
}

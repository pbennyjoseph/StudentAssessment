import org.apache.http.NameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EvaluatorPanel extends JPanel implements ActionListener {

    private int noofTests;
    private JPanel contentPanel, buttonPanel;
    private JSONArray allTests;
    public static JPanel EP_Main;

    EvaluatorPanel() {
        EP_Main = this;
        contentPanel = new JPanel(new FlowLayout());
        buttonPanel = new JPanel(new FlowLayout());
        JButtonX proceed = new JButtonX("Proceed");
        buttonPanel.add(proceed);

        SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                StudentAssessment.showLoader("Fetching data...");
                String retval = StudentAssessment.wx.sendPost(StudentAssessment.baseURL + "getTestData.php",
                        new ArrayList<NameValuePair>());
                System.out.println(retval);
                JSONParser parser = new JSONParser();

                try {
                    Object obj = parser.parse(retval);
                    JSONObject jx = (JSONObject) obj;
                    System.out.println(jx.get("tests"));
//                    JSONArray array = (JSONArray) obj;
//                    ArrayList<String> ax = (ArrayList<String>) array.get("tests");
                    allTests = (JSONArray) jx.get("tests");
                    noofTests = allTests.size();
                    System.out.println(noofTests);
                    if (noofTests == 0) {
                        JOptionPane.showMessageDialog(null, "Nothing to Evaluate now");
                        InstructorPanel.evaluateMarks.setEnabled(true);
                        return false;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }

            @Override
            protected void done() {
                JPanel v = new JPanel(new GridLayout(noofTests, 2, 2, 2));
                for (Object x : allTests) {

                    v.add(new JLabel((String) x + " ", JLabel.CENTER));
                    JButtonX bx = new JButtonX(" Evaluate ");
                    bx.setActionCommand((String) x);
                    bx.addActionListener((ActionListener) EP_Main);
                    v.add(bx);
                }
                add((contentPanel = v));
//                add(buttonPanel);
                revalidate();
                StudentAssessment.hideLoader();
            }
        };
        swingWorker.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.removeAll();
        JPanel vx = new TestResponsePanel(e.getActionCommand());
        contentPanel.add(vx);
        revalidate();
    }
}

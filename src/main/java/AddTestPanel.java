import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class AddTestPanel extends JPanel implements ActionListener {
    private JPanel buttonPanel, contentPanel;
    private JButton proceed;
    private JTextField testNameField;
    private JSpinner countQuestions;
    private String testName;
    private String[] testQuestions;

    AddTestPanel() {
        setLayout(new FlowLayout());
        contentPanel = new JPanel(new FlowLayout());
        buttonPanel = new JPanel(new FlowLayout());

        JLabel name = new JLabel("Enter your new test name");
        contentPanel.add(name);
        testNameField = new JTextField(20);
        contentPanel.add(testNameField);
        add(contentPanel);

        proceed = new JButton("Proceed");
        proceed.setFocusable(false);
        proceed.setActionCommand("proceed");
        proceed.addActionListener(this);
        buttonPanel.add(proceed);
        add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "proceed":
                System.out.println("Here");
                testName = testNameField.getText();
                contentPanel.removeAll();
                revalidate();
//            proceed = new JButton("Next");
//            proceed.setFocusable(false);
                proceed.setActionCommand("showNoOfQuestions");
//            contentPanel.add(proceed);
                countQuestions = new JSpinner(new SpinnerNumberModel(3, 3, 10, 1));
                ((JSpinner.DefaultEditor) countQuestions.getEditor()).getTextField().setEditable(false);
                countQuestions.setSize(5, 10);
                contentPanel.add(new JLabel("Select No of Questions", JLabel.CENTER));
                contentPanel.add(countQuestions);
//            revalidate();
//            repaint();
//            InstructorPanel.IP_Main.revalidate();
//            InstructorPanel.IP_Main.repaint();
//            StudentAssessment.SA_MAIN.revalidate();
//            StudentAssessment.SA_MAIN.repaint();
                break;
            case "showNoOfQuestions":
                Integer testNumber = (Integer) countQuestions.getValue();
                proceed.setActionCommand("submitQuestions");
                contentPanel.removeAll();
                testQuestions = new String[testNumber];
                contentPanel.setLayout(new GridLayout(testNumber, 2));
                for (int i = 1; i <= testNumber; ++i) {


                    contentPanel.add(new JLabel("Question " + i, JLabel.CENTER));
                    JTextArea x = new JTextArea(5, 30);
                    JScrollPane scrollPane = new JScrollPane(x,
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    int finalI = i;
                    x.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {

                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                            testQuestions[finalI - 1] = ((JTextArea) e.getSource()).getText().trim();
                        }
                    });
                    contentPanel.add(scrollPane);
                }
                revalidate();

                System.out.println(testName + " " + testNumber);
                break;
            case "submitQuestions":
                ArrayList<NameValuePair> urlParams = new ArrayList<>();
                urlParams.add(new BasicNameValuePair("testname", testName));
                urlParams.add(new BasicNameValuePair("questions", String.join("`", testQuestions)));


                StudentAssessment.showLoader("Creating new Quiz...");
                contentPanel.setVisible(false);

                SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        try {
                            StudentAssessment.wx.sendPost(StudentAssessment.baseURL + "createTest.php", urlParams);
                        } catch (Exception ignored) {
                            JOptionPane.showMessageDialog(null, "oops! Some error has Occured");
                        }
                        return true;
                    }

                    @Override
                    protected void done() {
                        StudentAssessment.hideLoader();
                        contentPanel.removeAll();
                        buttonPanel.removeAll();
                        revalidate();
                        JOptionPane.showMessageDialog(null, "Done creating quiz " + testName);
                        InstructorPanel.addQuiz.setEnabled(true);
                    }
                };
                swingWorker.execute();
                break;
            default:  // Post Questions
                System.out.println(testName + "::");
                break;
        }
    }
}

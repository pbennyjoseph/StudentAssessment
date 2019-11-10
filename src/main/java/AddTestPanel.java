import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AddTestPanel extends JPanel implements ActionListener {
    JPanel buttonPanel, contentPanel;
    JButton proceed;
    JTextField testNameField;
    JSpinner countQuestions;
    String testName;
    Integer testNumber;
    String[] testQuestions;

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
                testNumber = (Integer) countQuestions.getValue();
                proceed.setActionCommand("submitQuestions");
                contentPanel.removeAll();
                testQuestions = new String[testNumber];
                contentPanel.setLayout(new GridLayout(testNumber, 2));
                for (int i = 1; i <= testNumber; ++i) {
                    contentPanel.add(new JLabel("Question " + i));
                    JTextField x = new JTextField();
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
                            testQuestions[finalI - 1] = ((JTextField) e.getSource()).getText();
                        }
                    });
                    contentPanel.add(x);
                }
                revalidate();

                System.out.println(testName + " " + testNumber);
                break;
            case "submitQuestions":
                for (String x : testQuestions) {
                    if (x == null) return;
                    System.out.println(x);
                }
                break;
            default:  // Post Questions
                System.out.println(testName + "::");
                break;
        }
    }
}

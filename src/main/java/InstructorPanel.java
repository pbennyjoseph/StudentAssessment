import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorPanel extends JPanel implements ActionListener {
    JPanel contentAndAction, actionPanel;
    JButton addQuiz, evaluateMarks;
    public static JPanel IP_Main;


    InstructorPanel() {
        setLayout(new BorderLayout());
        IP_Main = this;
        JLabel welcome = new JLabel("Welcome Instructor!", JLabel.CENTER);
        JPanel content = new JPanel(new FlowLayout());
        addQuiz = new JButton("Add Quiz");
        evaluateMarks = new JButton("Evaluate Responses");
        evaluateMarks.setFocusable(false);
        evaluateMarks.setActionCommand("eval");
        evaluateMarks.addActionListener(this);
        addQuiz.setActionCommand("addQuiz");
        addQuiz.setFocusable(false);
        addQuiz.addActionListener(this);


        content.add(addQuiz);
        content.add(evaluateMarks);

        contentAndAction = new JPanel(new BorderLayout());
        contentAndAction.add(content, BorderLayout.NORTH);
        actionPanel = new JPanel(new BorderLayout());
        contentAndAction.add(actionPanel, BorderLayout.CENTER);

        add(welcome, BorderLayout.NORTH);
        add(contentAndAction, BorderLayout.CENTER);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        actionPanel.removeAll();
        if (e.getActionCommand().equals("eval")) {
            actionPanel.add(new JLabel("this is evaluation", JLabel.CENTER), BorderLayout.CENTER);
            evaluateMarks.setEnabled(false);
            addQuiz.setEnabled(true);
        } else {
            actionPanel.add(new AddTestPanel(), BorderLayout.CENTER);
            addQuiz.setEnabled(false);
            evaluateMarks.setEnabled(true);
        }
        StudentAssessment.SA_MAIN.revalidate();
        StudentAssessment.SA_MAIN.repaint();
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                JFrame x = new JFrame();
//                x.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//                x.setLayout(new BorderLayout());
//                x.add(new InstructorPanel());
//                x.pack();
//                x.setVisible(true);
//            }
//        });
//    }
}

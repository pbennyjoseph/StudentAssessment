import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorPanel extends JPanel implements ActionListener {
    private JPanel actionPanel;
    static JButtonX addQuiz, evaluateMarks;


    InstructorPanel() {
        setLayout(new BorderLayout());
        JLabel welcome = new JLabel("Welcome Instructor!", JLabel.CENTER);
        JPanel content = new JPanel(new FlowLayout());
        addQuiz = new JButtonX("Add Quiz");
        evaluateMarks = new JButtonX("Evaluate Responses");
        evaluateMarks.setActionCommand("eval");
        evaluateMarks.addActionListener(this);
        addQuiz.setActionCommand("addQuiz");
        addQuiz.addActionListener(this);


        content.add(addQuiz);
        content.add(evaluateMarks);

        JPanel contentAndAction = new JPanel(new BorderLayout());
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
            actionPanel.add(new JScrollPane(new EvaluatorPanel(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
            evaluateMarks.setEnabled(false);
            addQuiz.setEnabled(true);
        } else {
            actionPanel.add(new JScrollPane(new AddTestPanel(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
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

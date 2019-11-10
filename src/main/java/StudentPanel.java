import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentPanel extends JPanel implements ActionListener {
    JPanel contentAndAction, actionPanel;
    public static JPanel SP_Main;


    StudentPanel(String studName) {
        setLayout(new BorderLayout());
        SP_Main = this;
        JLabel welcome = new JLabel("Welcome " + studName + "! ", JLabel.CENTER);
        JPanel content = new JPanel(new FlowLayout());
        JButton addQuiz = new JButton("Add Quiz");
        JButton evaluateMarks = new JButton("Evaluate Responses");
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
        } else {
            actionPanel.add(new AddTestPanel(), BorderLayout.CENTER);
        }
        StudentAssessment.SA_MAIN.revalidate();
        StudentAssessment.SA_MAIN.repaint();
    }
}

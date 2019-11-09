import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorPanel extends JPanel implements ActionListener {
    InstructorPanel() {
        setLayout(new BorderLayout());
        JLabel welcome = new JLabel("Welcome Instructor!", JLabel.CENTER);
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

        add(welcome, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eval")) {
            System.out.println("evalhere");
            add(new JLabel("this is evaluation"), BorderLayout.SOUTH);
        } else {
            System.out.println("not evalhere");
            add(new AddTestPanel(), BorderLayout.SOUTH);
        }
        StudentAssessment.sa_main.revalidate();
        StudentAssessment.sa_main.repaint();
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

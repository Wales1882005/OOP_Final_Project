import java.awt.event.*;
import javax.swing.*;

public class QuizBattleGUI extends JFrame implements ActionListener {

    private JLabel lblQuestion;
    private JLabel lblResult;
    private JButton btn1;
    private JButton btn2;
    private Questions question;

    public QuizBattleGUI() {
        
        question = new Questions();

        
        setTitle("Programming Quiz Battle");
        setSize(400, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        lblQuestion = new JLabel(question.getQuestion());
        lblQuestion.setBounds(30, 20, 320, 30);

    
        btn1 = new JButton(question.getOption1());
        btn1.setBounds(50, 80, 120, 40);
        btn1.addActionListener(this);

        
        btn2 = new JButton(question.getOption2());
        btn2.setBounds(200, 80, 120, 40);
        btn2.addActionListener(this);

        
        lblResult = new JLabel("Answer the question!");
        lblResult.setBounds(30, 150, 300, 30);

    
        add(lblQuestion);
        add(btn1);
        add(btn2);
        add(lblResult);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (question.isCorrect(button.getText())) {
            lblResult.setText("Correct! You defeated the Code Boss!");
        } else {
            lblResult.setText("Wrong! Try Again!");
        }
    }
    public static void main(String[] args) {
        new QuizBattleGUI();
    }
}

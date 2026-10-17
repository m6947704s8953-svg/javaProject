import javax.swing.*;
import java.awt.*;

public class AttemptPanel extends JPanel {

    public AttemptPanel(QuizAttempt attempt, int attemptNumber) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
	    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
	    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel title = new JLabel("Quiz Attempt #" + attemptNumber);
        title.setFont(new Font("Arial", Font.BOLD, 16));

	JLabel name = new JLabel("Name: " + attempt.getName());
        JLabel score = new JLabel("Score: " + attempt.getScore() + "/20");
        JLabel percentage = new JLabel("Percentage: " + String.format("%.1f%%", attempt.getPercentage()));

        add(title);
        add(Box.createVerticalStrut(5));
	add(name);
        add(score);
        add(percentage);

        setMaximumSize(new Dimension(Integer.MAX_VALUE,90));
    }
}
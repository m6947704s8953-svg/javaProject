import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HistoryScreen extends JPanel {
    private MainApp app;
    private ArrayList<QuizAttempt> attempts;

    public HistoryScreen(MainApp app) {
        this.app = app;
        this.attempts = loadQuizScores();
        setLayout(new BorderLayout(0, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));
        setPreferredSize(new Dimension(430, 760));

        JLabel title = new JLabel("Quiz History", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(Color.WHITE);

        int count = 1;
        for(QuizAttempt attempt: attempts) {
            cardsPanel.add(new AttemptPanel(attempt, count++));
            cardsPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        add(scrollPane, BorderLayout.CENTER);

        add(createBottomNavigation(), BorderLayout.SOUTH);
    }

    private JPanel createBottomNavigation() {
        JPanel nav = new JPanel(new GridLayout(1, 5, 5, 0));
        nav.setBackground(Color.WHITE);
        nav.setPreferredSize(new Dimension(390, 50));
        nav.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton learnBtn = new JButton("Learn");
        learnBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        learnBtn.setBackground(Color.WHITE);
        learnBtn.addActionListener(e -> app.showScreen("Learning"));

        JButton quizBtn = new JButton("Quiz");
        quizBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        quizBtn.setBackground(Color.WHITE);
        quizBtn.addActionListener(e -> app.showScreen("Quiz"));

        JButton leaderBtn = new JButton("Leaderboard");
        leaderBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        leaderBtn.setBackground(Color.WHITE);
        leaderBtn.addActionListener(e -> app.showScreen("Leaderboard"));

        JButton profileBtn = new JButton("Profiles");
        profileBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBtn.setBackground(Color.WHITE);
        profileBtn.addActionListener(e -> app.showScreen("Profiles"));

        JButton historyBtn = new JButton("History");
        historyBtn.setFont(new Font("Arial", Font.BOLD, 12));
        historyBtn.setBackground(new Color(230, 245, 255));
        historyBtn.setEnabled(false);

        nav.add(learnBtn);
        nav.add(quizBtn);
        nav.add(leaderBtn);
        nav.add(profileBtn);
        nav.add(historyBtn);

        return nav;
    }

    public static ArrayList<QuizAttempt> loadQuizScores() {
        ArrayList<QuizAttempt> attempts = new ArrayList<>();

        try (Scanner file = new Scanner(new File("quiz_scores.txt"))) {
            while(file.hasNextLine()) {
                String line = file.nextLine();
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 3) continue; // skip invalid line

                    String name = parts[0].replace("Student:", "").trim();
                    String scorePart = parts[1].replace("Track Score:", "").trim();
                    if (!scorePart.contains("/")) continue;

                    int score = Integer.parseInt(scorePart.substring(0, scorePart.indexOf("/")).trim());
                    double percentage = Double.parseDouble(parts[2].replace("Percentage:", "").replace("%", "").trim());

                    attempts.add(new QuizAttempt(name, score, percentage));
                } catch (Exception ex) {
                    System.err.println("Skipping invalid line in quiz_scores.txt: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file could not be found. " + e.getMessage());
        }
        return attempts;
    }
}
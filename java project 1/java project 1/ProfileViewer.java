import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class ProfileViewer extends JPanel {
    private MainApp app;

    private JComboBox<String> studentBox;
    private JLabel nameLabel;
    private JLabel quizzesLabel;
    private JLabel highScoreLabel;
    private JLabel averageLabel;
    private JLabel perfectLabel;

    private Map<String, UserProfile> profiles;

    public ProfileViewer(MainApp app) {
        this.app = app;
        this.profiles = loadProfiles();
        setLayout(new BorderLayout(0, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));
        setPreferredSize(new Dimension(430, 760));
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Profiles");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));

        studentBox = new JComboBox<>();
        for(String name : profiles.keySet()) {
            studentBox.addItem(name);
        }
        studentBox.setMaximumSize(new Dimension(350, 35));
        mainPanel.add(studentBox);
        mainPanel.add(Box.createVerticalStrut(25));

        JPanel card = createProfileCard();
        mainPanel.add(card);

        studentBox.addActionListener(e -> updateProfile());
        if(studentBox.getItemCount() > 0) updateProfile();

        add(mainPanel, BorderLayout.CENTER);
        add(createBottomNavigation(), BorderLayout.SOUTH);
    }

    private JPanel createProfileCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                       new LineBorder(Color.LIGHT_GRAY, 1, true),
                       new EmptyBorder(20,20,20,20)));

        JLabel avatar = new JLabel(new ImageIcon("images/profile_icon.png"));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizzesLabel = new JLabel();
        highScoreLabel = new JLabel();
        averageLabel = new JLabel();
        perfectLabel = new JLabel();

        card.add(avatar);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(25));
        card.add(quizzesLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(highScoreLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(averageLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(perfectLabel);
        card.add(Box.createVerticalStrut(10));

        return card;
    }

    private void updateProfile() {
        String name = (String) studentBox.getSelectedItem();
        if(name == null) return;

        UserProfile profile = profiles.get(name);
        nameLabel.setText(profile.getName());
        quizzesLabel.setText("Attempts Made: " + profile.getQuizzesTaken());
        highScoreLabel.setText("Highest Score: " + profile.getHighestScore());
        averageLabel.setText("Average Score: " + profile.getAvgScoreString());
        perfectLabel.setText("Perfect Scores Achieved: " + profile.getFullScoreCount());
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
        profileBtn.setFont(new Font("Arial", Font.BOLD, 12));
        profileBtn.setBackground(new Color(230, 245, 255));
        profileBtn.setEnabled(false);

        JButton historyBtn = new JButton("History");
        historyBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        historyBtn.setBackground(Color.WHITE);
        historyBtn.addActionListener(e -> app.showScreen("History"));

        nav.add(learnBtn);
        nav.add(quizBtn);
        nav.add(leaderBtn);
        nav.add(profileBtn);
        nav.add(historyBtn);

        return nav;
    }

    public static Map<String, UserProfile> loadProfiles() {
        Map<String, UserProfile> profiles = new HashMap<>();

        try (Scanner file = new Scanner(new File("quiz_scores.txt"))) {
            while (file.hasNextLine()) {
                String line = file.nextLine();
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 3) continue; // skip malformed line

                    String name = parts[0].replace("Student:", "").trim();
                    String scorePart = parts[1].replace("Track Score:", "").trim();
                    if (!scorePart.contains("/")) continue;

                    int score = Integer.parseInt(scorePart.substring(0, scorePart.indexOf("/")).trim());
                    double percentage = Double.parseDouble(parts[2].replace("Percentage:", "").replace("%", "").trim());

                    profiles.putIfAbsent(name, new UserProfile(name));
                    profiles.get(name).addResult(score, percentage);
                } catch (Exception ex) {
                    System.err.println("Skipping invalid line in quiz_scores.txt: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file could not be found. " + e.getMessage());
        }
        return profiles;
    }
}
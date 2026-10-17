import java.awt.*;
import javax.swing.*;

public class LeaderboardScreen extends JPanel {
    private MainApp app; // Changed tracking handle to match MainApp controller class
    private final String[][] sampleData = {
        {"Ali Awang", "95", "1"},  
        {"Grace S.", "85", "2"}, 
        {"Alex Kam", "75", "3"}, 
        {"Sarah M.", "60", "4"},
        {"John Doe", "40", "5"}
    };

    public LeaderboardScreen(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout(0, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));
        setPreferredSize(new Dimension(430, 760));
        initializeUI();
    }

    private void initializeUI() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Leaderboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createVerticalStrut(20));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(248, 249, 250));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (String[] user : sampleData) {
            listPanel.add(createRow(user[2], user[0], user[1]));
            listPanel.add(Box.createVerticalStrut(8));
        }

        contentPanel.add(listPanel);
        add(contentPanel, BorderLayout.CENTER);
        add(createBottomNavigation(), BorderLayout.SOUTH);
    }

    private JPanel createRow(String rank, String name, String points) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(new Color(248, 249, 250));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        JLabel rankLabel = new JLabel("#" + rank + " ", JLabel.LEFT);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rankLabel.setForeground(Color.GRAY);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel pointsLabel = new JLabel(points + " pts", JLabel.RIGHT);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pointsLabel.setForeground(new Color(0, 123, 255));

        row.add(rankLabel, BorderLayout.WEST);
        row.add(nameLabel, BorderLayout.CENTER);
        row.add(pointsLabel, BorderLayout.EAST);
        
        return row;
    }

    private JPanel createBottomNavigation() {
        JPanel nav = new JPanel(new GridLayout(1, 3, 5, 0));
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
        leaderBtn.setFont(new Font("Arial", Font.BOLD, 12));
        leaderBtn.setBackground(new Color(230, 245, 255));
        leaderBtn.setEnabled(false);

	JButton profileBtn = new JButton("Profiles");
        profileBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBtn.setBackground(Color.WHITE);
        profileBtn.addActionListener(e -> app.showScreen("Profiles"));

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
}
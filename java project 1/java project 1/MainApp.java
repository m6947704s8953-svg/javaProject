import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private LearningModule learningModule;
    private LeaderboardScreen leaderboardScreen;
    private ProfileViewer userProfile;
    private HistoryScreen history;
    private Goal3QuizPanel quizPanel;

    public MainApp() {
        setTitle("Biology SDG Learning App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Initialize screens passing 'this' app framework for navigation
        learningModule = new LearningModule(this);
        leaderboardScreen = new LeaderboardScreen(this);
	userProfile = new ProfileViewer(this);
	history = new HistoryScreen(this);
        quizPanel = new Goal3QuizPanel(this);

        mainContainer.add(learningModule, "Learning");
        mainContainer.add(leaderboardScreen, "Leaderboard");
	mainContainer.add(userProfile, "Profiles");
	mainContainer.add(history, "History");
        mainContainer.add(quizPanel, "Quiz");

        add(mainContainer);
        pack();
        setLocationRelativeTo(null);
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
    }

    public void updateScreens() {
	mainContainer.remove(leaderboardScreen);
	mainContainer.remove(userProfile);
	mainContainer.remove(history);

	leaderboardScreen = new LeaderboardScreen(this);
	userProfile = new ProfileViewer(this);
	history = new HistoryScreen(this);

	mainContainer.add(leaderboardScreen, "Leaderboard");
	mainContainer.add(userProfile, "Profiles");
	mainContainer.add(history, "History");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainApp().setVisible(true);
        });
    }
}

// =========================================================================
// EMBEDDED QUIZ MODULE CLASSES (No extra standalone files required)
// =========================================================================

interface QuizEngine {
    void startQuiz();
    void saveScore(int score, double percentage) throws ScoreStorageException;
}

class ScoreStorageException extends Exception {
    public ScoreStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

abstract class Question {
    protected String questionText;
    protected String hint;
    protected String rationale;

    public Question(String questionText, String hint, String rationale) {
        this.questionText = questionText;
        this.hint = hint;
        this.rationale = rationale;
    }

    public abstract JPanel createQuestionPanel(ActionListener submitAction);
    public abstract boolean checkAnswer();
}

class MultipleChoiceQuestion extends Question {
    private String[] options;
    private char correctAnswer;
    private JRadioButton[] radioButtons;
    private ButtonGroup group;

    public MultipleChoiceQuestion(String text, String[] options, char correct, String hint, String rationale) {
        super(text, hint, rationale);
        this.options = options;
        this.correctAnswer = correct;
    }

    @Override
    public JPanel createQuestionPanel(ActionListener submitAction) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JTextArea qText = new JTextArea(questionText);
        qText.setFont(new Font("Arial", Font.BOLD, 15));
        qText.setLineWrap(true);
        qText.setWrapStyleWord(true);
        qText.setEditable(false);
        qText.setBackground(Color.WHITE);
        qText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(qText);
        panel.add(Box.createVerticalStrut(15));

        radioButtons = new JRadioButton[options.length];
        group = new ButtonGroup();
        char label = 'A';
        for (int i = 0; i < options.length; i++) {
            radioButtons[i] = new JRadioButton(label + ") " + options[i]);
            radioButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            radioButtons[i].setBackground(Color.WHITE);
            radioButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            group.add(radioButtons[i]);
            panel.add(radioButtons[i]);
            panel.add(Box.createVerticalStrut(5));
            label++;
        }

        panel.add(Box.createVerticalStrut(15));
        JButton btnSubmit = new JButton("Submit Answer");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSubmit.addActionListener(submitAction);
        panel.add(btnSubmit);

        return panel;
    }

    @Override
    public boolean checkAnswer() {
        char selected = ' ';
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isSelected()) {
                selected = (char) ('A' + i);
                break;
            }
        }
        return selected == correctAnswer;
    }
}

class TrueFalseQuestion extends Question {
    private boolean correctAnswer;
    private JRadioButton rbTrue;
    private JRadioButton rbFalse;
    private ButtonGroup group;

    public TrueFalseQuestion(String text, boolean correct, String hint, String rationale) {
        super(text, hint, rationale);
        this.correctAnswer = correct;
    }

    @Override
    public JPanel createQuestionPanel(ActionListener submitAction) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JTextArea qText = new JTextArea("[True / False]\n" + questionText);
        qText.setFont(new Font("Arial", Font.BOLD, 15));
        qText.setLineWrap(true);
        qText.setWrapStyleWord(true);
        qText.setEditable(false);
        qText.setBackground(Color.WHITE);
        qText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(qText);
        panel.add(Box.createVerticalStrut(15));

        rbTrue = new JRadioButton("True");
        rbFalse = new JRadioButton("False");
        rbTrue.setFont(new Font("Arial", Font.PLAIN, 14));
        rbFalse.setFont(new Font("Arial", Font.PLAIN, 14));
        rbTrue.setBackground(Color.WHITE);
        rbFalse.setBackground(Color.WHITE);
        rbTrue.setAlignmentX(Component.LEFT_ALIGNMENT);
        rbFalse.setAlignmentX(Component.LEFT_ALIGNMENT);

        group = new ButtonGroup();
        group.add(rbTrue);
        group.add(rbFalse);

        panel.add(rbTrue);
        panel.add(Box.createVerticalStrut(5));
        panel.add(rbFalse);

        panel.add(Box.createVerticalStrut(15));
        JButton btnSubmit = new JButton("Submit Answer");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSubmit.addActionListener(submitAction);
        panel.add(btnSubmit);

        return panel;
    }

    @Override
    public boolean checkAnswer() {
        if (rbTrue.isSelected() && correctAnswer) return true;
        if (rbFalse.isSelected() && !correctAnswer) return true;
        return false;
    }
}

class Goal3QuizPanel extends JPanel implements QuizEngine {
    private MainApp app;
    private List<Question> questionBank;
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private String participantName = "Student";

    private JPanel cardContainer;
    private CardLayout cardLayout;
    private JLabel progressLabel;
    private JTextArea feedbackArea;
    private JButton hintButton;

    public Goal3QuizPanel(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout(0, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));
        setPreferredSize(new Dimension(430, 760));

        questionBank = new ArrayList<>();
        loadQuestions();
        initializeUI();
    }

    private void initializeUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel mainTitle = new JLabel("Biology Mini Quiz", JLabel.CENTER);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 22));
        topPanel.add(mainTitle, BorderLayout.NORTH);

        progressLabel = new JLabel("Welcome to the Biology Evaluation!", JLabel.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        progressLabel.setForeground(new Color(0, 123, 255));
        topPanel.add(progressLabel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(Color.WHITE);
        cardContainer.setBorder(BorderFactory.createLineBorder(new Color(240,240,240), 1));

        JPanel startWindow = new JPanel();
        startWindow.setLayout(new BoxLayout(startWindow, BoxLayout.Y_AXIS));
        startWindow.setBackground(Color.WHITE);
        startWindow.add(Box.createVerticalStrut(40));
        
        JLabel labelInput = new JLabel("Enter Student Identifier Name:");
        labelInput.setFont(new Font("Arial", Font.BOLD, 14));
        labelInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        startWindow.add(labelInput);
        startWindow.add(Box.createVerticalStrut(10));

        JTextField nameField = new JTextField(15);
        nameField.setMaximumSize(new Dimension(280, 35));
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        startWindow.add(nameField);
        startWindow.add(Box.createVerticalStrut(20));

        JButton startQuizBtn = new JButton("Launch System Assessment");
        startQuizBtn.setFont(new Font("Arial", Font.BOLD, 14));
        startQuizBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startQuizBtn.addActionListener(e -> {
            if (!nameField.getText().trim().isEmpty()) {
                participantName = nameField.getText().trim();
            }
            startQuiz();
        });
        startWindow.add(startQuizBtn);
        cardContainer.add(startWindow, "START_SCREEN");

        ActionListener itemSubmit = e -> evaluateSubmission();
        for (int i = 0; i < questionBank.size(); i++) {
            JPanel itemBox = questionBank.get(i).createQuestionPanel(itemSubmit);
            itemBox.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            cardContainer.add(itemBox, "Q" + i);
        }
        add(cardContainer, BorderLayout.CENTER);

        JPanel controlArea = new JPanel(new BorderLayout(0, 10));
        controlArea.setBackground(Color.WHITE);

        feedbackArea = new JTextArea(4, 20);
        feedbackArea.setFont(new Font("Arial", Font.ITALIC, 13));
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setEditable(false);
        feedbackArea.setBackground(new Color(250, 250, 250));
        controlArea.add(new JScrollPane(feedbackArea), BorderLayout.NORTH);

        hintButton = new JButton("Request Conceptual Hint");
        hintButton.setFont(new Font("Arial", Font.PLAIN, 13));
        hintButton.setEnabled(false);
        hintButton.addActionListener(e -> {
            if(currentQuestionIndex < questionBank.size()) {
                feedbackArea.setText("💡 HINT: " + questionBank.get(currentQuestionIndex).hint);
            }
        });
        controlArea.add(hintButton, BorderLayout.CENTER);

        JPanel bottomNavBar = new JPanel(new GridLayout(1, 3, 5, 0));
        bottomNavBar.setBackground(Color.WHITE);
        bottomNavBar.setPreferredSize(new Dimension(390, 50));
        bottomNavBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton navLearn = new JButton("Learn");
        navLearn.setFont(new Font("Arial", Font.PLAIN, 12));
        navLearn.setBackground(Color.WHITE);
        navLearn.addActionListener(e -> app.showScreen("Learning"));

        JButton navQuiz = new JButton("Quiz");
        navQuiz.setFont(new Font("Arial", Font.BOLD, 12));
        navQuiz.setBackground(new Color(230, 245, 255));
        navQuiz.setEnabled(false);

        JButton navLeader = new JButton("Leaderboard");
        navLeader.setFont(new Font("Arial", Font.PLAIN, 12));
        navLeader.setBackground(Color.WHITE);
        navLeader.addActionListener(e -> app.showScreen("Leaderboard"));

	JButton navProfile = new JButton("Profiles");
        navProfile.setFont(new Font("Arial", Font.PLAIN, 12));
        navProfile.setBackground(Color.WHITE);
        navProfile.addActionListener(e -> app.showScreen("Profiles"));

	JButton navHistory = new JButton("History");
        navHistory.setFont(new Font("Arial", Font.PLAIN, 12));
        navHistory.setBackground(Color.WHITE);
        navHistory.addActionListener(e -> app.showScreen("History"));

        bottomNavBar.add(navLearn);
        bottomNavBar.add(navQuiz);
        bottomNavBar.add(navLeader);
	bottomNavBar.add(navProfile);
	bottomNavBar.add(navHistory);
        controlArea.add(bottomNavBar, BorderLayout.SOUTH);

        add(controlArea, BorderLayout.SOUTH);
        cardLayout.show(cardContainer, "START_SCREEN");
    }

    @Override
    public void startQuiz() {
        currentQuestionIndex = 0;
        totalScore = 0;
        hintButton.setEnabled(true);
        refreshHeader();
        cardLayout.show(cardContainer, "Q0");
        feedbackArea.setText("Assessment initiated. Select the correct option above.");
    }

    private void evaluateSubmission() {
        if (currentQuestionIndex >= questionBank.size()) return;

        Question q = questionBank.get(currentQuestionIndex);
        boolean isCorrect = q.checkAnswer();

        if (isCorrect) {
            totalScore++;
            JOptionPane.showMessageDialog(this, "✨ Correct!\n+1 Point Added.", "Review Panel", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Incorrect.\n\nRationale:\n" + q.rationale, "Review Panel", JOptionPane.WARNING_MESSAGE);
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questionBank.size()) {
            refreshHeader();
            cardLayout.show(cardContainer, "Q" + currentQuestionIndex);
            feedbackArea.setText("Moved to next item layout.");
        } else {
            concludeQuiz();
        }
    }

    private void refreshHeader() {
        progressLabel.setText("Active Track - Question " + (currentQuestionIndex + 1) + " of " + questionBank.size());
    }

 private void concludeQuiz() {
    double percentage = ((double) totalScore / questionBank.size()) * 100;
    progressLabel.setText("Evaluation Terminated.");

    String msg = String.format("🎉 Finished!\nStudent: %s\nFinal Score: %d/%d (%.1f%%)", 
                                participantName, totalScore, questionBank.size(), percentage);

    // Generate motivational message
    String motivationalMessage;
    if (percentage >= 80) {
        motivationalMessage = "Outstanding!";
    } else if (percentage >= 60) {
        motivationalMessage = "That's good!";
    } else if (percentage >= 40) {
        motivationalMessage = "Good try!";
    } else if (percentage >= 20) {
        motivationalMessage = "You can do better!";
    } else {
        motivationalMessage = "Don't give up!";
    }

    // Append motivational message to the feedback
    msg += "\n💡 Motivational Message: " + motivationalMessage;

    // Update the feedbackArea **before adding it to the panel**
    feedbackArea.setText(msg);

    try {
        saveScore(totalScore, percentage);
        feedbackArea.append("\nMetrics successfully saved to local logs repository.");
    } catch (ScoreStorageException e) {
        feedbackArea.append("\nWarning: Could not commit data to system file logs.");
    }
}

    @Override
    public void saveScore(int score, double percentage) throws ScoreStorageException {
        try (FileWriter fw = new FileWriter("quiz_scores.txt", true);
             PrintWriter pw = new PrintWriter(new BufferedWriter(fw))) {
            pw.printf("Student: %s | Track Score: %d/20 | Percentage: %.1f%%\n", participantName, score, percentage);
        } catch (IOException e) {
            throw new ScoreStorageException("File write block encountered.", e);
        }
    }

    private void loadQuestions() {
        // All 20 biology questions from your teammate's file
        questionBank.add(new MultipleChoiceQuestion(
            "Which cell organelle is known as the \"powerhouse of the cell\" due to its role in producing energy in the form of ATP?",
            new String[]{"Chloroplast", "Mitochondria", "Ribosome", "Golgi Apparatus"}, 'B',
            "Think of the organelle responsible for aerobic respiration.",
            "Mitochondria are the sites of cellular respiration, responsible for converting nutrients into adenosine triphosphate (ATP), which powers cell activities."));

        questionBank.add(new MultipleChoiceQuestion(
            "What is the primary component of a plant cell wall that provides structural support and rigidity?",
            new String[]{"Chitin", "Cellulose", "Glycogen", "Peptidoglycan"}, 'B',
            "It is an insoluble dietary fiber that humans cannot digest but is crucial for plant stability.",
            "Cellulose is a complex carbohydrate (polysaccharide) that forms a tough, fibrous network, creating the rigid structure of plant cell walls."));

        questionBank.add(new MultipleChoiceQuestion(
            "The process of cell division that results in four genetically diverse haploid daughter cells (gametes) is called:",
            new String[]{"Mitosis", "Meiosis", "Cytokinesis", "Binary Fission"}, 'B',
            "This process is essential for sexual reproduction and genetic variation.",
            "Meiosis is a specialized type of cell division that reduces the chromosome number by half, producing four non-identical haploid sperm or egg cells."));

        questionBank.add(new MultipleChoiceQuestion(
            "What is the primary function of red blood cells (erythrocytes) in the human body?",
            new String[]{"To transport oxygen from the lungs to body tissues", "To fight off pathogens and provide immunity", "To assist in blood clotting during an injury", "To produce antibodies"}, 'A',
            "These cells lack a nucleus when mature to maximize space for oxygen-carrying pigments.",
            "Erythrocytes contain hemoglobin, an iron-rich protein that binds to oxygen in the lungs and releases it into the body's tissues."));

        questionBank.add(new MultipleChoiceQuestion(
            "Where does the primary exchange of oxygen and carbon dioxide gases take place within the human lungs?",
            new String[]{"Bronchi", "Bronchioles", "Alveoli", "Trachea"}, 'C',
            "They provide an immense surface area for respiratory exchange.",
            "Alveoli are tiny, grape-like air sacs at the end of the bronchioles. Their extremely thin walls are surrounded by capillaries, allowing rapid gas diffusion."));

        questionBank.add(new MultipleChoiceQuestion(
            "Which type of plant vascular tissue is responsible for transporting water and dissolved minerals from the roots up to the leaves?",
            new String[]{"Phloem", "Xylem", "Epidermis", "Cambium"}, 'B',
            "Its partner tissue, phloem, transports manufactured organic food (sucrose).",
            "Xylem consists of dead, hollow, tube-like cells (vessels and tracheids) that conduct water and inorganic nutrients upward through a plant."));

        questionBank.add(new MultipleChoiceQuestion(
            "Enzymes act as biological catalysts that speed up chemical reactions by:",
            new String[]{"Increasing the temperature of the reaction", "Lowering the activation energy", "Changing the shape of the final products", "Increasing the quantity of substrates"}, 'B',
            "They make reactions occur faster without being consumed in the process.",
            "Enzymes lower the activation energy barrier, meaning less energy is required for the reactant molecules to break bonds and transform into products."));

        questionBank.add(new MultipleChoiceQuestion(
            "Which hormone is secreted by the pancreas to lower blood glucose levels when they are too high?",
            new String[]{"Glucagon", "Adrenaline", "Insulin", "Thyroxine"}, 'C',
            "A deficiency or lack of response to this hormone leads to Diabetes Mellitus.",
            "Insulin signals body cells (especially liver and muscle cells) to absorb glucose from the bloodstream and store it as glycogen."));

        questionBank.add(new MultipleChoiceQuestion(
            "What type of symbiotic relationship occurs when one organism benefits while the other is neither helped nor harmed?",
            new String[]{"Mutualism", "Parasitism", "Commensalism", "Predation"}, 'C',
            "An example is barnacles attaching themselves to whales.",
            "Commensalism is a (+/0) relationship where one species benefits (e.g., getting transportation or shelter) while the host remains unaffected."));

        questionBank.add(new MultipleChoiceQuestion(
            "In a DNA molecule, the nitrogenous base Adenine (A) always pairs with which base?",
            new String[]{"Cytosine (C)", "Guanine (G)", "Thymine (T)", "Uracil (U)"}, 'C',
            "Uracil replaces Thymine, but only in RNA molecules.",
            "According to Chargaff's rules of base pairing, Adenine (A) forms two hydrogen bonds exclusively with Thymine (T) in DNA."));

        questionBank.add(new MultipleChoiceQuestion(
            "The loss of water vapor from the internal tissues of a plant, primarily through the stomata of leaves, is called:",
            new String[]{"Guttation", "Transpiration", "Photosynthesis", "Cellular Respiration"}, 'B',
            "This process helps cool the plant and drives the movement of sap.",
            "Transpiration is the evaporation of water from plants, which creates a negative pressure pull (transpirational pull) to draw more water up from the roots."));

        questionBank.add(new MultipleChoiceQuestion(
            "What is the primary function of hydrochloric acid (HCl) secreted in the human stomach?",
            new String[]{"To break down fats into fatty acids", "To create an acidic environment that activates pepsinogen into pepsin", "To convert glucose into glycogen", "To absorb water and mineral salts"}, 'B',
            "It creates an ideal environment for protein digestion.",
            "HCl drops the stomach's pH to around 1.5-2.0, which kills ingested bacteria and cleaves inactive pepsinogen into the active protein-digesting enzyme, pepsin."));

        questionBank.add(new MultipleChoiceQuestion(
            "A severe deficiency of Vitamin C in a human diet leads to which of the following diseases?",
            new String[]{"Rickets", "Scurvy", "Beriberi", "Night Blindness"}, 'B',
            "This disease historically plagued sailors who went months without fresh citrus fruits.",
            "Vitamin C is essential for the synthesis of collagen. Without it, blood vessels weaken, leading to bleeding gums and delayed wound healing—the hallmarks of scurvy."));

        questionBank.add(new MultipleChoiceQuestion(
            "What is meant by a \"positive phototropism\" response in plants?",
            new String[]{"Roots growing downward away from light", "Shoots growing toward a light source", "Roots growing toward gravitational pull", "Leaves closing up tightly when touched"}, 'B',
            "It ensures the leaves get maximum sunlight for photosynthesis.",
            "\"Photo-\" means light, and \"tropism\" is a growth movement. A \"positive\" tropism means the plant part is growing toward the directional stimulus."));

        questionBank.add(new MultipleChoiceQuestion(
            "What is the basic structural and functional unit of the human kidney responsible for filtering blood and forming urine?",
            new String[]{"Neuron", "Nephron", "Alveolus", "Hepatocyte"}, 'B',
            "Do not confuse it with a nerve cell (neuron); this one ends with \"-phron\".",
            "Each kidney contains roughly one million nephrons. These microscopic structures filter blood under pressure, reabsorb useful materials, and excrete waste."));

        questionBank.add(new MultipleChoiceQuestion(
            "Which type of blood vessel features the thinnest walls (only one cell thick) to facilitate the exchange of materials between blood and tissues?",
            new String[]{"Artery", "Vein", "Capillary", "Aorta"}, 'C',
            "They form vast networks connecting arterioles to venules.",
            "Capillaries are microscopic vessels with walls composed of a single layer of endothelial cells, making them ideal for the diffusion of gases, nutrients, and waste."));

        questionBank.add(new MultipleChoiceQuestion(
            "If an individual has a heterozygous genotype (Bb) for eye color, where Brown (B) is dominant and blue (b) is recessive, what is their phenotype?",
            new String[]{"Brown eyes", "Blue eyes", "Green eyes", "One brown eye and one blue eye"}, 'A',
            "You only see a recessive trait physically if the genotype is homozygous recessive (bb).",
            "In a heterozygous state, the dominant allele (B) completely masks the expression of the recessive allele (b). Thus, the physical appearance (phenotype) is brown."));

        questionBank.add(new MultipleChoiceQuestion(
            "During cellular respiration, which step occurs in the cytoplasm and breaks down one molecule of glucose into two molecules of pyruvate?",
            new String[]{"Glycolysis", "The Krebs Cycle (Citric Acid Cycle)", "Electron Transport Chain", "Oxidative Phosphorylation"}, 'A',
            "It is the initial, universal step of both aerobic and anaerobic respiration.",
            "Glycolysis literally means \"sugar splitting.\" It is an anaerobic process that happens in the cytosol/cytoplasm, yielding a net of 2 ATP and 2 NADH."));

        questionBank.add(new TrueFalseQuestion(
            "The primary ecological role of decomposers (like fungi and bacteria) is to break down dead organic matter and recycle nutrients back into the soil.",
            true,
            "They act as nature's recycling crew.",
            "Decomposing bacteria/fungi break down dead organic mass. Without them, ecosystems run out of raw materials."));

        questionBank.add(new TrueFalseQuestion(
            "The Fluid Mosaic Model describes the plasma membrane as a completely rigid carbohydrate lattice structure.",
            false,
            "This model explains how things move laterally within the membrane.",
            "The Fluid Mosaic Model describes a flexible phospholipid bilayer (fluid) embedded with a changing pattern of various proteins (mosaic)."));
    }
}
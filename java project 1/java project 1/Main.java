import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        panel.setBackground(new Color(245, 247, 250));

        JTextArea qText = new JTextArea(questionText);
        qText.setLineWrap(true);
        qText.setWrapStyleWord(true);
        qText.setEditable(false);
        qText.setFont(new Font("Arial", Font.BOLD, 14));
        qText.setBackground(new Color(245, 247, 250));
        qText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(qText);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        radioButtons = new JRadioButton[options.length];
        group = new ButtonGroup();
        char label = 'A';
        for (int i = 0; i < options.length; i++) {
            radioButtons[i] = new JRadioButton(label + ") " + options[i]);
            radioButtons[i].setFont(new Font("Arial", Font.PLAIN, 13));
            radioButtons[i].setBackground(new Color(245, 247, 250));
            radioButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            group.add(radioButtons[i]);
            panel.add(radioButtons[i]);
            label++;
        }

        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel hintLabel = new JLabel("<html><b>HINT:</b> " + hint + "</html>");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(hintLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton btnSubmit = new JButton("Submit Answer");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 13));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSubmit.addActionListener(submitAction);
        panel.add(btnSubmit);

        return panel;
    }

    @Override
    public boolean checkAnswer() {
        char selected = ' ';
        if (radioButtons != null) {
            for (int i = 0; i < radioButtons.length; i++) {
                if (radioButtons[i].isSelected()) {
                    selected = (char) ('A' + i);
                    break;
                }
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
        panel.setBackground(new Color(245, 247, 250));

        JTextArea qText = new JTextArea("[True / False]\n" + questionText);
        qText.setLineWrap(true);
        qText.setWrapStyleWord(true);
        qText.setEditable(false);
        qText.setFont(new Font("Arial", Font.BOLD, 14));
        qText.setBackground(new Color(245, 247, 250));
        qText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(qText);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        rbTrue = new JRadioButton("True");
        rbFalse = new JRadioButton("False");
        rbTrue.setFont(new Font("Arial", Font.PLAIN, 13));
        rbFalse.setFont(new Font("Arial", Font.PLAIN, 13));
        rbTrue.setBackground(new Color(245, 247, 250));
        rbFalse.setBackground(new Color(245, 247, 250));
        rbTrue.setAlignmentX(Component.LEFT_ALIGNMENT);
        rbFalse.setAlignmentX(Component.LEFT_ALIGNMENT);

        group = new ButtonGroup();
        group.add(rbTrue);
        group.add(rbFalse);

        panel.add(rbTrue);
        panel.add(rbFalse);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        JLabel hintLabel = new JLabel("<html><b>HINT:</b> " + hint + "</html>");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(hintLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton btnSubmit = new JButton("Submit Answer");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 13));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSubmit.addActionListener(submitAction);
        panel.add(btnSubmit);

        return panel;
    }

    @Override
    public boolean checkAnswer() {
        if (rbTrue == null || rbFalse == null) return false;
        if (rbTrue.isSelected() && correctAnswer) return true;
        if (rbFalse.isSelected() && !correctAnswer) return true;
        return false;
    }
}

class Goal3Quiz implements QuizEngine {
    private List<Question> questionBank;
    private static final String DEFAULT_SCORE_FILE = "quiz_scores.txt";
    
    private JFrame frame;
    private JPanel mainContentPanel;
    private JLabel progressLabel;
    private JTextArea feedbackArea;
    
    private String participantName = "User";
    private int currentQuestionIndex = 0;
    private int totalScore = 0;

    public Goal3Quiz() {
        questionBank = new ArrayList<>();
        loadQuestions();
        setupSmartphoneFrame();
    }

    private void setupSmartphoneFrame() {
        frame = new JFrame("Biology Quiz App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640); 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainContentPanel.setBackground(new Color(245, 247, 250));
        frame.add(mainContentPanel);

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 13));
        feedbackArea.setBackground(new Color(245, 247, 250));
    }

    // OVERLOADING 1: Menjana Mesej Motivasi
    public String generateFeedbackMessage(double percentage) {
        if (percentage >= 80.0) {
            return "Outstanding!";
        } else if (percentage >= 60.0) {
            return "That's good!"; 
        } else if (percentage >= 40.0) {
            return "Good try!";   
        } else if (percentage >= 20.0) {
            return "You can do better!";
        } else {
            return "Don't give up!";
        }
    }

    public String generateFeedbackMessage(int score, int totalQuestions) {
        if (totalQuestions == 0) return "No questions found. Don't give up!";
        double percentage = ((double) score / totalQuestions) * 100;
        return generateFeedbackMessage(percentage); 
    }

    // OVERLOADING 2: Menyimpan Fail Rekod Skor
    @Override
    public void saveScore(int score, double percentage) throws ScoreStorageException {
        saveScore(score, percentage, DEFAULT_SCORE_FILE);
    }

    public void saveScore(int score, double percentage, String customFileName) throws ScoreStorageException {
        try (FileWriter fw = new FileWriter(customFileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("User: " + participantName + " | Score: " + score + "/" + questionBank.size() + " | Percentage: " + String.format("%.1f", percentage) + "%");
        } catch (IOException e) {
            throw new ScoreStorageException("Failed to reach file repository system.", e);
        }
    }

    @Override
    public void startQuiz() {
        showWelcomeScreen();
        frame.setVisible(true);
    }

    private void showWelcomeScreen() {
        mainContentPanel.removeAll();

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("<html><center><b>Biology Quiz</b></center></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 60)));
        welcomePanel.add(titleLabel);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 50)));
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(nameLabel);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JTextField nameField = new JTextField(15);
        nameField.setMaximumSize(new Dimension(250, 30));
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(nameField);

        welcomePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(40, 167, 69));
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            if (!nameField.getText().trim().isEmpty()) {
                participantName = nameField.getText().trim();
            }
            showQuestionScreen();
        });
        welcomePanel.add(startButton);

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void showQuestionScreen() {
        mainContentPanel.removeAll();

        if (currentQuestionIndex >= questionBank.size()) {
            concludeQuiz();
            return;
        }

        if (progressLabel == null) {
            progressLabel = new JLabel();
            progressLabel.setFont(new Font("Arial", Font.BOLD, 12));
            progressLabel.setForeground(new Color(0, 123, 255));
        }
        
        progressLabel.setText("Active Track - Question " + (currentQuestionIndex + 1) + " of " + questionBank.size());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.add(progressLabel, BorderLayout.WEST);
        
        // Butang "Get Hint" dibuang dari topPanel mengikut kehendak tugasan terkini
        mainContentPanel.add(topPanel, BorderLayout.NORTH);

        Question currentQuestion = questionBank.get(currentQuestionIndex);
        
        JPanel qPanel = currentQuestion.createQuestionPanel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isCorrect = currentQuestion.checkAnswer();
                String message;
                if (isCorrect) {
                    totalScore++;
                    message = "✨ Correct!\n+1 Point (Gamification Star)";
                } else {
                    message = "❌ Incorrect.";
                }
                
                JOptionPane.showMessageDialog(frame, 
                    message + "\n\nRATIONALE:\n" + currentQuestion.rationale, 
                    "Review Screen", 
                    JOptionPane.INFORMATION_MESSAGE);

                currentQuestionIndex++;
                if (currentQuestionIndex < questionBank.size()) {
                    showQuestionScreen();
                } else {
                    concludeQuiz();
                }
            }
        });

        mainContentPanel.add(qPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void concludeQuiz() {
    double percentage = ((double) totalScore / questionBank.size()) * 100;
    progressLabel.setText("Evaluation Terminated.");
    
    // Original message
    String msg = String.format("🎉 Finished!\nStudent: %s\nFinal Score: %d/%d (%.1f%%)", 
                                participantName, totalScore, questionBank.size(), percentage);
    
    // Add motivational message according to the scale
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

    // Append motivational message
    msg += "\n💡 Motivational Message: " + motivationalMessage;

    // Display to the student
    feedbackArea.setText(msg);

    try {
        saveScore(totalScore, percentage);
        feedbackArea.append("\nMetrics successfully saved to local logs repository.");
    } catch (ScoreStorageException e) {
        feedbackArea.append("\nWarning: Could not commit data to system file logs.");
    }

        JPanel endPanel = new JPanel(new BorderLayout(10, 10));
        endPanel.setBackground(new Color(245, 247, 250));
        endPanel.add(progressLabel, BorderLayout.NORTH);
        endPanel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);
        
        mainContentPanel.add(endPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // ======================================================================
    // PENGUMPULAN 20 SOALAN BIOLOGI SEPENUHNYA
    // ======================================================================
    private void loadQuestions() {
        // Soalan 1
        questionBank.add(new MultipleChoiceQuestion(
            "Which cell organelle is known as the \"powerhouse of the cell\" due to its role in producing energy in the form of ATP?",
            new String[]{"Chloroplast", "Mitochondria", "Ribosome", "Golgi Apparatus"}, 'B',
            "Respiration happens here.",
            "Mitochondria convert glucose into ATP energy via aerobic respiration."));

        // Soalan 2
        questionBank.add(new MultipleChoiceQuestion(
            "What is the primary component of a plant cell wall that provides structural support?",
            new String[]{"Chitin", "Cellulose", "Glycogen", "Peptidoglycan"}, 'B',
            "It is a complex carbohydrate fiber.",
            "Cellulose forms a strong structural framework for plant cell walls."));

        // Soalan 3
        questionBank.add(new MultipleChoiceQuestion(
            "Which human blood cell type is fundamentally responsible for transporting oxygen throughout the body?",
            new String[]{"White Blood Cells", "Red Blood Cells", "Platelets", "Plasma Cells"}, 'B',
            "Contains hemoglobin.",
            "Red Blood Cells contain hemoglobin which binds and carries oxygen molecules."));

        // Soalan 4
        questionBank.add(new MultipleChoiceQuestion(
            "What type of macromolecule are enzymes classified as?",
            new String[]{"Carbohydrates", "Lipids", "Proteins", "Nucleic Acids"}, 'C',
            "Made of amino acid chains.",
            "Enzymes are biological catalysts primarily made up of structural protein chains."));

        // Soalan 5
        questionBank.add(new MultipleChoiceQuestion(
            "Which hormone lowers blood glucose levels by promoting absorption into body cells?",
            new String[]{"Glucagon", "Insulin", "Adrenaline", "Thyroxine"}, 'B',
            "Secreted by beta cells of the pancreas.",
            "Insulin facilitates glucose uptake, reducing high sugar levels in the blood stream."));

        // Soalan 6
        questionBank.add(new MultipleChoiceQuestion(
            "In DNA base-pairing rules, which nitrogenous base always pairs up with Adenine?",
            new String[]{"Cytosine", "Guanine", "Thymine", "Uracil"}, 'C',
            "Think of the letter T.",
            "Adenine pairs with Thymine (A-T) via two hydrogen bonds in double-stranded DNA."));

        // Soalan 7
        questionBank.add(new MultipleChoiceQuestion(
            "What structural component prevents food from entering the trachea during human swallowing processes?",
            new String[]{"Pharynx", "Larynx", "Epiglottis", "Esophagus"}, 'C',
            "A flexible cartilaginous flap.",
            "The epiglottis folds backward to cover the glottis entry to the lungs when swallowing."));

        // Soalan 8
        questionBank.add(new MultipleChoiceQuestion(
            "Which part of the human brain controls vital involuntary reflex actions such as heartbeat rate and breathing metrics?",
            new String[]{"Cerebrum", "Cerebellum", "Medulla Oblongata", "Hypothalamus"}, 'C',
            "Located at the brainstem base.",
            "The medulla oblongata regulates critical autonomic cardiac and respiratory centers."));

        // Soalan 9
        questionBank.add(new MultipleChoiceQuestion(
            "What is the functional basic filtering unit located inside a normal human kidney system?",
            new String[]{"Neuron", "Nephron", "Alveolus", "Villus"}, 'B',
            "Sounds similar to neuron but handles urine extraction.",
            "Nephrons are microscopic structures responsible for filtration, reabsorption, and urine production."));

        // Soalan 10
        questionBank.add(new MultipleChoiceQuestion(
            "Which plant tissue type is specialized for distributing manufactured organic sugars down from source leaves?",
            new String[]{"Xylem", "Phloem", "Epidermis", "Cambium"}, 'B',
            "Transports food, not water.",
            "Phloem vessels move sucrose and amino acids via active translocation streams."));

        // Soalan 11
        questionBank.add(new TrueFalseQuestion(
            "The primary ecological role of decomposers is to recycle essential soil nutrients.",
            true,
            "Nature's recycling system.",
            "Bacteria and fungi break down waste material, returning nutrients back to soil reserves."));

        // Soalan 12
        questionBank.add(new TrueFalseQuestion(
            "The Fluid Mosaic Model states that the cell membrane is completely static and rigid.",
            false,
            "Think about the word 'fluid'.",
            "The membrane is dynamic and flexible, letting components move laterally."));

        // Soalan 13
        questionBank.add(new TrueFalseQuestion(
            "Mitosis cell division phases produce exactly four genetically unique haploid daughter cells.",
            false,
            "That sounds like Meiosis.",
            "Mitosis yields two identical diploid somatic daughter cells instead."));

        // Soalan 14
        questionBank.add(new TrueFalseQuestion(
            "Arteries always carry oxygenated blood away from heart cavities, with zero exceptions in human models.",
            false,
            "Think about the pulmonary artery pathway.",
            "The pulmonary artery carries deoxygenated blood from the heart to the lungs."));

        // Soalan 15
        questionBank.add(new TrueFalseQuestion(
            "Photosynthesis light-independent reactions (Calvin Cycle) happen inside the thylakoid membrane.",
            false,
            "It happens in the fluid area.",
            "The Calvin Cycle takes place within the stroma; light reactions use the thylakoids."));

        // Soalan 16
        questionBank.add(new TrueFalseQuestion(
            "Active transport mechanisms require metabolic ATP energy expenditure to move substances against concentration gradients.",
            true,
            "Uphill movement.",
            "Moving substances against gradients requires protein pumps fueled by ATP molecules."));

        // Soalan 17
        questionBank.add(new TrueFalseQuestion(
            "Anaerobic respiration in human muscle tissue produces ethanol molecules alongside carbon dioxide.",
            false,
            "Think about muscle fatigue.",
            "Human muscles undergo lactic acid fermentation; plants/yeast produce ethanol."));

        // Soalan 18
        questionBank.add(new TrueFalseQuestion(
            "Amylase is a specialized enzyme that breaks down complex starch chains into simpler maltose sugars.",
            true,
            "Found in saliva.",
            "Salivary and pancreatic amylase targets alpha-1,4 glycosidic bonds to break down starch."));

        // Soalan 19
        questionBank.add(new TrueFalseQuestion(
            "Vaccination triggers artificial active immunity states by presenting weakened antigens to body defense lines.",
            true,
            "Builds memory cells safely.",
            "Vaccines introduce safe antigens to provoke primary immune memory cell responses."));

        // Soalan 20
        questionBank.add(new TrueFalseQuestion(
            "Bile is synthesized inside the gallbladder and is primarily used for the chemical breakdown of proteins.",
            false,
            "Bile targets fat/lipids, and look closely at where it is made.",
            "Bile is made in the liver (stored in gallbladder) and emulsifies lipids mechanically."));
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizEngine app = new Goal3Quiz();
            app.startQuiz();
        });
    }
}
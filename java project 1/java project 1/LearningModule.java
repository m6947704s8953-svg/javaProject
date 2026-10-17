import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LearningModule extends JPanel implements Displayable {
    private MainApp app; // Added reference link
    private String[] titles;
    private String[] contents;
    private String[] imagePaths;
    private String[] videoPaths;

    private int currentPage;
    private JLabel titleLabel;
    private JLabel imageLabel;
    private JLabel pageLabel;
    private JTextArea contentArea;

    private JButton previousButton;
    private JButton nextButton;
    private JButton playVideoButton;

    // Modified Constructor to accept MainApp controller hook
    public LearningModule(MainApp app) {
        this.app = app;
        currentPage = 0;
        createLearningContent();
        createGUI();
        displayPage(currentPage);
    }

    private void createLearningContent() {
        titles = new String[10];
        contents = new String[10];
        imagePaths = new String[10];
        videoPaths = new String[10];

      
    titles[0] = "Chapter 1: Cell Biology";
    contents[0] = "Cell biology is the study of cells. Cells are the basic unit of life. "
            + "All living things are made up of cells. Some organisms have only one cell, while humans, animals and plants have many cells.\n\n"
            + "Each cell has different parts called organelles. Each organelle has its own function. "
            + "The nucleus controls the activities of the cell. Ribosomes help to make proteins. "
            + "The Golgi apparatus modifies and packages substances in the cell.\n\n"
            + "Mitochondria are known as the powerhouse of the cell because they produce energy in the form of ATP. "
            + "ATP is used by cells for activities such as growth, movement, repair and active transport.\n\n"
            + "The plasma membrane surrounds the cell. It controls what enters and leaves the cell. "
            + "The plasma membrane follows the Fluid Mosaic Model. This means the membrane is flexible and made of a phospholipid bilayer with proteins embedded in it.";
    imagePaths[0] = "images/cell_biology.png";
    videoPaths[0] = "videos/cell_biology.mp4";


    titles[1] = "Chapter 2: Plant Cell Structure";
    contents[1] = "Plant cells and animal cells have some similar structures, such as the nucleus, cytoplasm, cell membrane, mitochondria and ribosomes. "
            + "However, plant cells also have special structures that animal cells do not have.\n\n"
            + "A plant cell has a cell wall, chloroplasts and a large vacuole. The cell wall is found outside the cell membrane. "
            + "It gives support, protection and a fixed shape to the plant cell.\n\n"
            + "The main substance in the plant cell wall is cellulose. Cellulose is strong and rigid, so it helps the plant stay upright. "
            + "Without a strong cell wall, plant cells would not be able to maintain their shape properly.\n\n"
            + "Chloroplasts contain chlorophyll, which absorbs light energy for photosynthesis. "
            + "The large vacuole stores cell sap and helps keep the plant cell firm. This firmness is called turgidity.";
    imagePaths[1] = "images/plant_cell.png";
    videoPaths[1] = "videos/plant_cell.mp4";

    titles[2] = "Chapter 3: Cell Division";
    contents[2] = "Cell division is important for living organisms. It allows organisms to grow, repair damaged tissues, replace dead cells and reproduce.\n\n"
            + "There are two main types of cell division: mitosis and meiosis. Mitosis produces two identical daughter cells. "
            + "It is important for growth and repair of body cells.\n\n"
            + "Meiosis is a special type of cell division that produces four daughter cells. "
            + "These daughter cells are genetically different from one another. They are also haploid, meaning they contain half the number of chromosomes.\n\n"
            + "Meiosis is important in sexual reproduction because it produces gametes, such as sperm cells and egg cells. "
            + "During fertilisation, two gametes join together to form a zygote. "
            + "Meiosis also creates genetic variation, which means offspring are not exactly the same as their parents.";
    imagePaths[2] = "images/meiosis.png";
    videoPaths[2] = "videos/cell_division.mp4";


    titles[3] = "Chapter 4: Human Transport System";
    contents[3] = "The human transport system is also called the circulatory system. "
            + "It transports oxygen, nutrients, hormones, carbon dioxide and waste products around the body.\n\n"
            + "Blood is made up of plasma, red blood cells, white blood cells and platelets. "
            + "Plasma transports dissolved substances. White blood cells protect the body against pathogens. "
            + "Platelets help blood to clot when there is an injury.\n\n"
            + "Red blood cells are also called erythrocytes. Their main function is to transport oxygen from the lungs to body tissues. "
            + "They contain haemoglobin, which combines with oxygen to form oxyhaemoglobin.\n\n"
            + "Blood vessels include arteries, veins and capillaries. Capillaries are the smallest blood vessels. "
            + "Their walls are only one cell thick, so oxygen, nutrients and waste products can move easily between blood and body cells.";
    imagePaths[3] = "images/human_transport.png";
    videoPaths[3] = "videos/human_transport.mp4";

    titles[4] = "Chapter 5: Human Respiratory System";
    contents[4] = "The human respiratory system helps the body exchange gases with the environment. "
            + "The main parts include the nose, trachea, bronchi, bronchioles, lungs and alveoli.\n\n"
            + "When we breathe in, oxygen enters the lungs. Oxygen is needed by body cells for cellular respiration. "
            + "Cellular respiration releases energy from glucose. Carbon dioxide is produced as a waste product and must be removed from the body.\n\n"
            + "Gas exchange takes place in the alveoli. Alveoli are tiny air sacs found at the end of the bronchioles. "
            + "They are surrounded by many blood capillaries.\n\n"
            + "Alveoli have thin walls and a large surface area. This makes gas exchange efficient. "
            + "Oxygen diffuses from the alveoli into the blood. Carbon dioxide diffuses from the blood into the alveoli and is breathed out.";
    imagePaths[4] = "images/respiratory_system.png";
        videoPaths[4] = "videos/respiratory_system.mp4";

    titles[5] = "Chapter 6: Plant Transport and Response";
    contents[5] = "Plants need a transport system to move water, minerals and food to different parts of the plant. "
            + "The two main transport tissues in plants are xylem and phloem.\n\n"
            + "Xylem transports water and dissolved minerals from the roots to the leaves. "
            + "This movement is usually upward. Water is needed for photosynthesis and to keep plant cells firm.\n\n"
            + "Phloem transports food such as sucrose from the leaves to other parts of the plant. "
            + "The food is produced during photosynthesis.\n\n"
            + "Transpiration is the loss of water vapour from plants. It mainly happens through stomata in the leaves. "
            + "Transpiration helps pull water upward through the xylem and also helps cool the plant.\n\n"
            + "Plants can respond to stimuli. Phototropism is a growth response to light. "
            + "Positive phototropism happens when plant shoots grow toward a light source.";
    imagePaths[5] = "images/plant_transport.png";
    videoPaths[5] = "videos/plant_transport.mp4";


    titles[6] = "Chapter 7: Enzymes and Cellular Respiration";
    contents[6] = "Enzymes are biological catalysts. A catalyst is a substance that speeds up a chemical reaction. "
            + "Enzymes are very important because many reactions in living organisms would happen too slowly without them.\n\n"
            + "Enzymes work by lowering the activation energy of a reaction. Activation energy is the minimum energy needed for a reaction to start. "
            + "When activation energy is lower, the reaction happens faster.\n\n"
            + "Enzymes are specific. This means each enzyme usually works on only one type of substrate. "
            + "Enzymes are not used up during the reaction, so they can be reused.\n\n"
            + "Cellular respiration is the process of releasing energy from glucose. The energy is stored as ATP. "
            + "Glycolysis is the first step of cellular respiration. It happens in the cytoplasm. "
            + "During glycolysis, one glucose molecule is broken down into two pyruvate molecules.";
    imagePaths[6] = "images/enzyme_respiration.png";
    videoPaths[6] = "videos/enzyme_respiration.mp4";


    titles[7] = "Chapter 8: Hormone, Homeostasis and Kidney";
    contents[7] = "Homeostasis means maintaining a stable internal environment in the body. "
            + "Examples of homeostasis include controlling body temperature, blood glucose level and water balance.\n\n"
            + "Insulin is a hormone produced by the pancreas. It helps to lower blood glucose level when the level is too high. "
            + "Insulin allows body cells to absorb glucose from the blood. It also helps the liver and muscles store glucose as glycogen.\n\n"
            + "If the body does not produce enough insulin, or if body cells do not respond to insulin properly, blood glucose level can become too high. "
            + "This condition is related to diabetes mellitus.\n\n"
            + "The kidney is important for removing waste products from the blood. It also helps control water and salt balance. "
            + "The basic functional unit of the kidney is the nephron. "
            + "The nephron filters blood, reabsorbs useful substances and helps form urine.";
    imagePaths[7] = "images/homeostasis_kidney.png";
    videoPaths[7] = "videos/homeostasis.mp4";

    titles[8] = "Chapter 9: Nutrition and Digestion";
    contents[8] = "Nutrition is important because the body needs food for energy, growth, repair and health. "
            + "A balanced diet contains carbohydrates, proteins, fats, vitamins, minerals, fibre and water.\n\n"
            + "The digestive system breaks down large food molecules into smaller molecules that can be absorbed. "
            + "The main organs include the mouth, oesophagus, stomach, small intestine, large intestine, liver and pancreas.\n\n"
            + "Hydrochloric acid is found in the stomach. It creates an acidic condition in the stomach. "
            + "This acidic condition helps activate pepsin, an enzyme that digests protein. "
            + "Hydrochloric acid also helps kill some bacteria in food.\n\n"
            + "Vitamin C is needed for healthy gums, skin and wound healing. "
            + "A serious lack of Vitamin C can cause scurvy. "
            + "Symptoms of scurvy include bleeding gums, weak blood vessels and slow wound healing.";
    imagePaths[8] = "images/digestion.png";
    videoPaths[8] = "videos/digestion.mp4";


    titles[9] = "Chapter 10: Genetics and Ecology";
    contents[9] = "Genetics is the study of inheritance. It explains how characteristics are passed from parents to offspring. "
            + "DNA carries genetic information. In DNA, Adenine always pairs with Thymine, while Cytosine pairs with Guanine.\n\n"
            + "A gene can have different forms called alleles. A dominant allele can show its effect even when only one copy is present. "
            + "A recessive allele only shows its effect when two copies are present. "
            + "For example, if brown eye allele B is dominant over blue eye allele b, a person with genotype Bb will have brown eyes.\n\n"
            + "Ecology is the study of the relationship between living organisms and their environment. "
            + "Commensalism is a relationship where one organism benefits while the other organism is not helped or harmed.\n\n"
            + "Decomposers such as fungi and bacteria break down dead plants and animals. "
            + "They recycle nutrients back into the soil. This is important because nutrients can be reused by plants and other organisms in the ecosystem.";
    imagePaths[9] = "images/genetics_ecology.png";
    videoPaths[9] = "videos/genetics_ecology.mp4";
    }

    private void createGUI() {
        setLayout(new BorderLayout(8, 8));
        setPreferredSize(new Dimension(430, 760));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        imageLabel = new JLabel("", JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(390, 210));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        pageLabel = new JLabel("", JLabel.CENTER);
        pageLabel.setFont(new Font("Arial", Font.BOLD, 13));

        contentArea = new JTextArea();
        contentArea.setFont(new Font("Arial", Font.PLAIN, 15));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setPreferredSize(new Dimension(390, 310));
        
        previousButton = new JButton("Prev");
        nextButton = new JButton("Next");
        playVideoButton = new JButton("Video");
        
        Font btnFont = new Font("Arial", Font.BOLD, 14);
        previousButton.setFont(btnFont); nextButton.setFont(btnFont); playVideoButton.setFont(btnFont);
        previousButton.setPreferredSize(new Dimension(110, 40));
        nextButton.setPreferredSize(new Dimension(110, 40));
        playVideoButton.setPreferredSize(new Dimension(110, 40));

        previousButton.addActionListener(e -> previousPage());
        nextButton.addActionListener(e -> nextPage());
        playVideoButton.addActionListener(e -> playVideo());

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(imageLabel, BorderLayout.CENTER);
        topPanel.add(pageLabel, BorderLayout.SOUTH);

        JPanel chapterControlPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        chapterControlPanel.setBackground(Color.WHITE);
        chapterControlPanel.add(previousButton);
        chapterControlPanel.add(nextButton);
        chapterControlPanel.add(playVideoButton);

        // Integrated bottom structural navigation layer
        JPanel bottomNavBar = new JPanel(new GridLayout(1, 3, 5, 0));
        bottomNavBar.setBackground(Color.WHITE);
        bottomNavBar.setPreferredSize(new Dimension(390, 50));
        bottomNavBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton navLearn = new JButton("Learn");
        navLearn.setFont(new Font("Arial", Font.BOLD, 12));
        navLearn.setBackground(new Color(230, 245, 255));
        navLearn.setEnabled(false); 

        JButton navQuiz = new JButton("Quiz");
        navQuiz.setFont(new Font("Arial", Font.PLAIN, 12));
        navQuiz.setBackground(Color.WHITE);
        navQuiz.addActionListener(e -> app.showScreen("Quiz")); // Triggers embedded panel switch

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

        JPanel southContainer = new JPanel(new BorderLayout(0, 10));
        southContainer.setBackground(Color.WHITE);
        southContainer.add(chapterControlPanel, BorderLayout.NORTH);
        southContainer.add(bottomNavBar, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southContainer, BorderLayout.SOUTH);
    }

    @Override 
    public void displayPage(int pageNumber) {
        if (pageNumber < 0 || pageNumber >= titles.length) return;
        currentPage = pageNumber;
        titleLabel.setText("<html><center>" + titles[currentPage] + "</center></html>");
        contentArea.setText(contents[currentPage]);
        contentArea.setCaretPosition(0);
        pageLabel.setText("Chapter " + (currentPage + 1) + " of " + titles.length);
        showImage(imagePaths[currentPage]);
        previousButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage < titles.length - 1);
    }

    private void showImage(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Image img = new ImageIcon(imagePath).getImage().getScaledInstance(370, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("<html><center>Image Path:<br>" + imagePath + "</center></html>");
        }
    }

    public void playVideo(){
        try {
            File videoFile = new File(videoPaths[currentPage]);
            if(!videoFile.exists()){
                JOptionPane.showMessageDialog(this, "Video not found at:\n" + videoPaths[currentPage]);
                return;
            }
            Desktop.getDesktop().open(videoFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open video file.");
        }
    }

    @Override public void nextPage() { if (currentPage < titles.length - 1) { currentPage++; displayPage(currentPage); } }
    @Override public void previousPage() { if (currentPage > 0) { currentPage--; displayPage(currentPage); } }
}
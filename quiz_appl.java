import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class quiz_appli{

    static int questionIndex = 0;
    static int score = 0;

    static String[] questions = {
            "1) What is the capital of India?",
            "2) Which language is used for Android?",
            "3) What does CPU stand for?",
            "4) Which is not a programming language?",
            "5) What is 5 + 3?"
    };

    static String[][] options = {
            {"Mumbai", "Delhi", "Chennai", "Kolkata"},
            {"Python", "Java", "C", "HTML"},
            {"Central Process Unit", "Central Processing Unit", "Computer Personal Unit", "Central Processor Utility"},
            {"Java", "C++", "HTML", "Python"},
            {"6", "7", "8", "9"}
    };

    static int[] answers = {1,1,1,2,1};

    static JFrame frame;
    static JLabel questionLabel;
    static JRadioButton[] optionButtons = new JRadioButton[4];
    static ButtonGroup group;
    static JButton nextButton;

    public static void main(String[] args) {
        createQuizGUI();
    }

    // ================= GUI =================
    static void createQuizGUI() {

        frame = new JFrame("Quiz Application");
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        questionLabel = new JLabel();
        questionLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(questionLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1));
        group = new ButtonGroup();

        for(int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            group.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
        }

        frame.add(centerPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        frame.add(nextButton, BorderLayout.SOUTH);

        loadQuestion();

        nextButton.addActionListener(e -> handleNext());

        frame.setVisible(true);
    }

    // ================= NEXT BUTTON =================
    static void handleNext() {

        int selected = -1;

        for(int i = 0; i < 4; i++) {
            if(optionButtons[i].isSelected()) {
                selected = i;
            }
        }

        if(selected == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an answer!");
            return;
        }

        if(selected == answers[questionIndex]) {
            score++;
        }

        questionIndex++;
        group.clearSelection();

        if(questionIndex < questions.length) {
            loadQuestion();
        } else {
            frame.dispose();
            storeInDatabase();
            showResultGUI();
        }
    }

    // ================= LOAD QUESTION =================
    static void loadQuestion() {

    questionLabel.setText(questions[questionIndex]);

    for(int i = 0; i < 4; i++) {
        optionButtons[i].setText(options[questionIndex][i]);
    }
    if(questionIndex == questions.length - 1) {
        nextButton.setText("Submit");
    } else {
        nextButton.setText("Next");
    }
}

    // ================= DATABASE =================
    static void storeInDatabase() {

        try {

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/quizdb",
                    "root",
                    "Jenifa@2811");

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO scores(score, total) VALUES(?, ?)");

            ps.setInt(1, score);
            ps.setInt(2, questions.length);

            ps.executeUpdate();

            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // ================= RESULT GUI =================
    static void showResultGUI() {

        JFrame resultFrame = new JFrame("Final Score");
        resultFrame.setSize(400, 200);
        resultFrame.setLayout(new GridLayout(3,1));

        resultFrame.add(new JLabel("Total Questions: " + questions.length, JLabel.CENTER));
        resultFrame.add(new JLabel("Correct Answers: " + score, JLabel.CENTER));
        resultFrame.add(new JLabel("Your Score: " + score + " / " + questions.length, JLabel.CENTER));

        resultFrame.setVisible(true);
    }

}

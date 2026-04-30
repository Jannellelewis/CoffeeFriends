import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class GameView extends JFrame {
    private GameController controller;

    private final JTextField playerNameField = new JTextField();
    private final JComboBox<GameModel.Personality> playerPersonalityCombo = new JComboBox<>(GameModel.Personality.values());
    private final JTextField companionNameField = new JTextField();
    private final JTextField companionPersonalityField = new JTextField();
    private final JProgressBar friendshipMeterBar = new JProgressBar(0, 100);
    private final JTextArea dialogueArea = new JTextArea();
    private final JButton startButton = new JButton("Start");
    private final JButton responseButton1 = new JButton("Agree");
    private final JButton responseButton2 = new JButton("Encourage");

    public GameView() {
        setTitle("CoffeeFriends");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        initUI();
        pack();
        setLocationRelativeTo(null);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    private void initUI() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        JPanel customizationPanel = new JPanel(new GridLayout(5, 2, 8, 8));

        customizationPanel.add(new JLabel("Player Name:"));
        customizationPanel.add(playerNameField);
        customizationPanel.add(new JLabel("Player Personality:"));
        customizationPanel.add(playerPersonalityCombo);
        customizationPanel.add(new JLabel("Companion Name:"));
        customizationPanel.add(companionNameField);
        customizationPanel.add(new JLabel("Companion Personality:"));
        companionPersonalityField.setEditable(false);
        companionPersonalityField.setText("Not assigned yet");
        customizationPanel.add(companionPersonalityField);
        customizationPanel.add(new JLabel());
        customizationPanel.add(startButton);

        JPanel gamePanel = new JPanel(new BorderLayout(8, 8));
        gamePanel.add(new JScrollPane(dialogueArea), BorderLayout.CENTER);

        JPanel responsePanel = new JPanel(new GridLayout(1, 2, 8, 8));
        responsePanel.add(responseButton1);
        responsePanel.add(responseButton2);
        gamePanel.add(responsePanel, BorderLayout.SOUTH);

        rootPanel.add(customizationPanel, BorderLayout.NORTH);
        rootPanel.add(gamePanel, BorderLayout.CENTER);
        rootPanel.add(friendshipMeterBar, BorderLayout.SOUTH);

        dialogueArea.setEditable(false);
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);

        startButton.addActionListener(e -> {
            if (controller != null) {
                String playerName = playerNameField.getText().trim();
                GameModel.Personality personality = (GameModel.Personality) playerPersonalityCombo.getSelectedItem();
                String companionName = companionNameField.getText().trim();
                controller.startGame(playerName, personality, companionName);
            }
        });

        responseButton1.addActionListener(e -> {
            if (controller != null) {
                controller.handleResponse(responseButton1.getText());
            }
        });

        responseButton2.addActionListener(e -> {
            if (controller != null) {
                controller.handleResponse(responseButton2.getText());
            }
        });

        setContentPane(rootPanel);
    }

    public void showWindow() {
        setVisible(true);
    }

    public void showGameScreen() {
        dialogueArea.setText("Game screen ready.");
        responseButton1.setEnabled(true);
        responseButton2.setEnabled(true);
    }

    public void updateFriendshipMeter(int value) {
        friendshipMeterBar.setValue(value);
    }

    public void updateDialogue(String dialogue) {
        dialogueArea.setText(dialogue);
    }

    public void setCompanionPersonality(GameModel.Personality personality) {
        companionPersonalityField.setText(personality == null ? "Unknown" : personality.name());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel();
            GameView view = new GameView();
            GameController controller = new GameController(model, view);
            view.setController(controller);
            view.showWindow();
        });
    }
}

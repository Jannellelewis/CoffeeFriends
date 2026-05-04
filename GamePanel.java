import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

@SuppressWarnings("this-escape")
public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JButton responseButton1 = new JButton("Agree");
    private final JButton responseButton2 = new JButton("Encourage");
    private final JButton giftButton = new JButton("Give Gift");
    private final JButton giftChoiceButton = new JButton("Give Gift");
    private final JButton continueButton = new JButton("Continue");
    private final JButton restartButton = new JButton("Restart");
    private final JButton sideGiftButton = new JButton("🎁 Give Gift");
    private final JButton resetButton = new JButton("Play Again");
    private final GameScreenPanel gameScreenPanel = new GameScreenPanel();

    public GamePanel() {
        setLayout(new BorderLayout(8, 8));
        add(gameScreenPanel, BorderLayout.CENTER);

        JPanel responsePanel = new JPanel(new GridLayout(2, 2, 8, 8));
        responsePanel.add(responseButton1);
        responsePanel.add(responseButton2);
        responsePanel.add(giftButton);
        responsePanel.add(resetButton);

        JPanel choicePanel = new JPanel(new GridLayout(1, 3, 8, 8));
        choicePanel.add(giftChoiceButton);
        choicePanel.add(continueButton);
        choicePanel.add(restartButton);

        JPanel responseContainer = new JPanel(new BorderLayout(4, 4));
        responseContainer.add(new JLabel("Choose your response:"), BorderLayout.NORTH);

        // Stack both panels
        JPanel buttonStackPanel = new JPanel();
        buttonStackPanel.setLayout(new javax.swing.BoxLayout(buttonStackPanel, javax.swing.BoxLayout.Y_AXIS));
        buttonStackPanel.add(responsePanel);
        buttonStackPanel.add(choicePanel);

        responseContainer.add(buttonStackPanel, BorderLayout.CENTER);
        add(responseContainer, BorderLayout.SOUTH);

        // Side gift button
        JPanel sidePanel = new JPanel(new BorderLayout(4, 4));
        sidePanel.add(sideGiftButton, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        // Initially disable/enable
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftButton.setEnabled(false);
        giftButton.setVisible(false);
        resetButton.setEnabled(false);
        resetButton.setVisible(false);
        giftChoiceButton.setEnabled(false);
        giftChoiceButton.setVisible(false);
        continueButton.setEnabled(false);
        continueButton.setVisible(false);
        restartButton.setEnabled(false);
        restartButton.setVisible(false);
        sideGiftButton.setEnabled(false);
        sideGiftButton.setVisible(false);
    }

    public void setController(GameController controller) {
        sideGiftButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleSideGiftClick();
            }
        });

        giftChoiceButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleLevelUpChoice("gift");
            }
        });

        continueButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleLevelUpChoice("continue");
            }
        });

        restartButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleLevelUpChoice("restart");
            }
        });

        resetButton.addActionListener(e -> {
            if (controller != null) {
                controller.resetGame();
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
    }

    public void setModel(GameModel model) {
        // If needed
    }

    public void showGameScreen() {
        responseButton1.setEnabled(true);
        responseButton2.setEnabled(true);
        gameScreenPanel.repaint();
    }

    public void updateFriendshipMeter(int value, int level) {
        gameScreenPanel.setFriendshipValue(value, level);
    }

    public void updateDialogue(String dialogue) {
        gameScreenPanel.setCompanionDialogue(dialogue);
    }

    public void setCharacterNames(String playerName, String companionName) {
        gameScreenPanel.setCharacterNames(playerName, companionName);
    }

    public void setCharacterColors(java.awt.Color playerColor, java.awt.Color companionColor) {
        gameScreenPanel.setCharacterColors(playerColor, companionColor);
    }

    public void setResponseLabels(String option1, String option2) {
        responseButton1.setText(option1);
        responseButton2.setText(option2);
    }

    public void showEndScreen(int level) {
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftButton.setVisible(false);
        sideGiftButton.setVisible(false);
        giftChoiceButton.setVisible(false);
        continueButton.setVisible(false);
        restartButton.setVisible(false);
        gameScreenPanel.setEndScreen(level);
        gameScreenPanel.repaint();
    }

    public void showLevelUpChoices(boolean canGift) {
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        if (canGift) {
            giftChoiceButton.setVisible(true);
            giftChoiceButton.setEnabled(true);
        } else {
            giftChoiceButton.setVisible(false);
            giftChoiceButton.setEnabled(false);
        }
        continueButton.setVisible(true);
        continueButton.setEnabled(true);
        restartButton.setVisible(true);
        restartButton.setEnabled(true);
        gameScreenPanel.repaint();
    }

    public void hideLevelUpChoices() {
        giftChoiceButton.setVisible(false);
        giftChoiceButton.setEnabled(false);
        continueButton.setVisible(false);
        continueButton.setEnabled(false);
        restartButton.setVisible(false);
        restartButton.setEnabled(false);
    }

    public void enableResponseButtons() {
        responseButton1.setEnabled(true);
        responseButton2.setEnabled(true);
    }

    public void updateGiftButtonVisibility(int level, GameModel model) {
        if (level >= 3 && !model.hasGivenGift()) {
            sideGiftButton.setVisible(true);
            sideGiftButton.setEnabled(true);
            gameScreenPanel.setShowGiftBanner(true);
        } else {
            sideGiftButton.setVisible(false);
            sideGiftButton.setEnabled(false);
            gameScreenPanel.setShowGiftBanner(false);
        }
    }

    public void showCompanionLeavesScreen() {
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftButton.setVisible(false);
        sideGiftButton.setVisible(false);
        giftChoiceButton.setVisible(false);
        continueButton.setVisible(false);
        restartButton.setVisible(true);
        restartButton.setEnabled(true);
        gameScreenPanel.setCompanionLeaves();
        gameScreenPanel.repaint();
    }

    public void showPeakFriendshipScreen() {
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftButton.setVisible(false);
        sideGiftButton.setVisible(false);
        giftChoiceButton.setVisible(false);
        continueButton.setVisible(false);
        restartButton.setVisible(true);
        restartButton.setEnabled(true);
        gameScreenPanel.setPeakFriendship(true);
        gameScreenPanel.repaint();
    }

    public void showCustomizationScreen() {
        gameScreenPanel.setEndScreen(false);
        gameScreenPanel.setPeakFriendship(false);
        gameScreenPanel.setShowGiftBanner(false);
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftButton.setVisible(false);
        resetButton.setVisible(false);
        sideGiftButton.setVisible(false);
        giftChoiceButton.setVisible(false);
        continueButton.setVisible(false);
        restartButton.setVisible(false);
    }

    public void setBackgroundColor(Color c) {
        gameScreenPanel.setBackgroundColor(c);
    }
}
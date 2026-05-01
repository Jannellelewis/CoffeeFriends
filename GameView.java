import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;import javax.swing.BoxLayout;import java.util.List;

public class GameView extends JFrame {
    private GameController controller;
    private GameModel model;

    private final JTextField playerNameField = new JTextField();
    private final JComboBox<GameModel.Personality> playerPersonalityCombo = new JComboBox<>(GameModel.Personality.values());
    private final JTextField companionNameField = new JTextField();
    private final JTextField companionPersonalityField = new JTextField();
    private final JProgressBar friendshipMeterBar = new JProgressBar(0, 100);
    private final JButton startButton = new JButton("Start");
    private final JButton responseButton1 = new JButton("Agree");
    private final JButton responseButton2 = new JButton("Encourage");
    private final JButton giftButton = new JButton("Give Gift");
    private final JButton giftChoiceButton = new JButton("Give Gift");
    private final JButton continueButton = new JButton("Continue");
    private final JButton restartButton = new JButton("Restart");
    private final JButton sideGiftButton = new JButton("🎁 Give Gift");
    private final JButton resetButton = new JButton("Play Again");
    private final GameScreenPanel gameScreenPanel = new GameScreenPanel();
    private boolean isEndScreen = false;
    private boolean showLevelUpChoices = false;
    private Color backgroundColor = new Color(255, 210, 220);
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public void setBackgroundColor(Color c) {
        this.backgroundColor = c;
        gameScreenPanel.repaint();
    }

    public GameView() {
        setTitle("CoffeeFriends");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 720));
        initUI();
        pack();
        setLocationRelativeTo(null);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    private void initUI() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
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
        gamePanel.add(gameScreenPanel, BorderLayout.CENTER);

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
        
        // Use a CardLayout to switch between response panel and choice panel
        responseContainer.add(responsePanel, BorderLayout.CENTER);
        
        // Add choice panel to a separate area that will be shown/hidden
        JPanel choicePanelContainer = new JPanel(new BorderLayout(4, 4));
        choicePanelContainer.add(choicePanel, BorderLayout.CENTER);
        choicePanelContainer.setVisible(false);
        
        // Stack both panels
        JPanel buttonStackPanel = new JPanel();
        buttonStackPanel.setLayout(new BoxLayout(buttonStackPanel, BoxLayout.Y_AXIS));
        buttonStackPanel.add(responsePanel);
        buttonStackPanel.add(choicePanel);
        
        responseContainer.add(buttonStackPanel, BorderLayout.CENTER);
        gamePanel.add(responseContainer, BorderLayout.SOUTH);

        // Side gift button
        JPanel sidePanel = new JPanel(new BorderLayout(4, 4));
        sidePanel.add(sideGiftButton, BorderLayout.CENTER);
        gamePanel.add(sidePanel, BorderLayout.EAST);

        cardPanel.add(customizationPanel, "customization");
        cardPanel.add(gamePanel, "game");

        rootPanel.add(cardPanel, BorderLayout.CENTER);

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

        startButton.addActionListener(e -> {
            String playerName = playerNameField.getText().trim();
            GameModel.Personality personality = (GameModel.Personality) playerPersonalityCombo.getSelectedItem();
            String companionName = companionNameField.getText().trim();
            if (playerName.isEmpty() || companionName.isEmpty()) {
                // Optionally show a message
                return;
            }
            if (controller != null) {
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
        cardLayout.show(cardPanel, "game");
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

    public void setCompanionPersonality(GameModel.Personality personality) {
        companionPersonalityField.setText(personality == null ? "Unknown" : personality.name());
    }

    public void setCharacterNames(String playerName, String companionName) {
        gameScreenPanel.setCharacterNames(playerName, companionName);
    }

    public void setResponseLabels(String option1, String option2) {
        responseButton1.setText(option1);
        responseButton2.setText(option2);
    }

    public void showEndScreen(int level) {
        isEndScreen = true;
        gameScreenPanel.setEndScreen(level);
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftButton.setVisible(false);
        sideGiftButton.setVisible(false);
        giftChoiceButton.setVisible(false);
        continueButton.setVisible(false);
        restartButton.setVisible(false);
        gameScreenPanel.repaint();
    }

    public void showLevelUpChoices() {
        showLevelUpChoices = true;
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftChoiceButton.setVisible(true);
        giftChoiceButton.setEnabled(true);
        continueButton.setVisible(true);
        continueButton.setEnabled(true);
        restartButton.setVisible(true);
        restartButton.setEnabled(true);
        gameScreenPanel.repaint();
    }

    public void hideLevelUpChoices() {
        showLevelUpChoices = false;
        giftChoiceButton.setVisible(false);
        giftChoiceButton.setEnabled(false);
        continueButton.setVisible(false);
        continueButton.setEnabled(false);
        restartButton.setVisible(false);
        restartButton.setEnabled(false);
    }

    public void showCustomizationScreen() {
        cardLayout.show(cardPanel, "customization");
        // Reset view state
        isEndScreen = false;
        showLevelUpChoices = false;
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

    public void updateGiftButtonVisibility(int level) {
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

    public void showPeakFriendshipScreen() {
        gameScreenPanel.setPeakFriendship();
        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);
        giftButton.setVisible(false);
        resetButton.setVisible(true);
        resetButton.setEnabled(true);
        gameScreenPanel.repaint();
    }

    private class GameScreenPanel extends JPanel {
        private String companionDialogue = "";
        private String playerName = "You";
        private String companionName = "Companion";
        private int animationOffset = 0;
        private Timer animationTimer;
        private int friendshipValue = 0;
        private int friendshipLevel = 1;
        private boolean endScreen = false;
        private boolean peakFriendship = false;
        private boolean showGiftBanner = false;

        public GameScreenPanel() {
            setPreferredSize(new Dimension(900, 520));
            animationTimer = new Timer(1000, e -> {
                if (!endScreen) {
                    animationOffset = (animationOffset == 0) ? 5 : 0;
                    repaint();
                }
            });
            animationTimer.start();
        }

        public void setCompanionDialogue(String dialogue) {
            this.companionDialogue = dialogue;
            repaint();
        }

        public void setCharacterNames(String playerName, String companionName) {
            this.playerName = playerName.isEmpty() ? "You" : playerName;
            this.companionName = companionName.isEmpty() ? "Companion" : companionName;
            repaint();
        }

        public void setFriendshipValue(int value, int level) {
            this.friendshipValue = value;
            this.friendshipLevel = level;
            repaint();
        }

        public void setEndScreen(int level) {
            this.endScreen = true;
            this.friendshipLevel = level;
            repaint();
        }

        public void setEndScreen(boolean show) {
            this.endScreen = show;
            repaint();
        }

        public void setPeakFriendship() {
            this.peakFriendship = true;
            repaint();
        }

        public void setPeakFriendship(boolean show) {
            this.peakFriendship = show;
            repaint();
        }

        public void setShowGiftBanner(boolean show) {
            this.showGiftBanner = show;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (peakFriendship) {
                g2.setColor(new Color(255, 215, 0)); // gold
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 48));
                g2.drawString("We have reached PEAK friendship", getWidth() / 2 - 350, getHeight() / 2 - 50);
                g2.setFont(new Font("SansSerif", Font.BOLD, 24));
                g2.drawString("With " + companionName, getWidth() / 2 - 80, getHeight() / 2 + 50);
                return;
            }

            if (endScreen) {
                g2.setColor(new Color(173, 216, 230)); // light blue
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 24));
                g2.drawString("Congratulations!", getWidth() / 2 - 100, getHeight() / 2 - 50);
                g2.drawString("You reached Friendship Level " + friendshipLevel + "!", getWidth() / 2 - 150, getHeight() / 2);
                g2.drawString("With " + companionName, getWidth() / 2 - 80, getHeight() / 2 + 50);
                return;
            }

            Color background = backgroundColor;
            g2.setColor(background);
            g2.fillRect(0, 0, getWidth(), getHeight());

            int rows = 20;
            int cols = 30;
            int cellSize = Math.min(getWidth() / cols, getHeight() / rows);
            g2.setColor(new Color(255, 190, 210));
            for (int x = 0; x < getWidth(); x += cellSize) {
                g2.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += cellSize) {
                g2.drawLine(0, y, getWidth(), y);
            }

            int playerX = getWidth() / 2 - 220;
            int companionX = getWidth() / 2 + 100;
            int characterY = getHeight() / 2 - 40 + animationOffset;
            int bodyWidth = 80;
            int bodyHeight = 120;

            g2.setColor(new Color(200, 100, 100));
            g2.fillOval(playerX, characterY, bodyWidth, bodyHeight);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString(playerName, playerX + 2, characterY - 10 + animationOffset);

            g2.setColor(new Color(120, 120, 200));
            g2.fillOval(companionX, characterY, bodyWidth, bodyHeight);
            g2.setColor(Color.BLACK);
            g2.drawString(companionName, companionX + 2, characterY - 10 + animationOffset);

            // Draw friendship bar above companion
            int barX = companionX - 20;
            int barY = characterY - 50 + animationOffset;
            int barWidth = 120;
            int barHeight = 20;
            int maxXP = friendshipLevel * 5;
            float ratio = (float) friendshipValue / maxXP;
            ratio = Math.min(1.0f, ratio); // cap at 1.0
            
            g2.setColor(Color.GRAY);
            g2.fillRect(barX, barY, barWidth, barHeight);
            int red = (int) (255 * (1 - ratio));
            int green = (int) (255 * ratio);
            g2.setColor(new Color(red, green, 0));
            g2.fillRect(barX, barY, (int) (barWidth * ratio), barHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(barX, barY, barWidth, barHeight);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Level " + friendshipLevel, barX + barWidth + 10, barY + 15);
            g2.drawString(friendshipValue + "/" + maxXP + " XP", barX - 50, barY + 15);

            // Draw gift banner
            if (showGiftBanner) {
                g2.setColor(new Color(255, 215, 0, 230)); // gold with transparency
                g2.fillRect(0, 0, getWidth(), 50);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2.drawString("🎁 Gift Unlocked! Click the gift button to give it.", 50, 35);
            }

            drawSpeechBubble(g2, playerX - 10, characterY - 140 + animationOffset, 220, 60, "Choose a response below.");
            drawSpeechBubble(g2, companionX - 10, characterY - 140 + animationOffset, calculateBubbleWidth(companionDialogue), 60, companionDialogue);
        }

        private int calculateBubbleWidth(String text) {
            FontMetrics fm = getFontMetrics(new Font("SansSerif", Font.PLAIN, 14));
            return Math.max(150, fm.stringWidth(text) + 20);
        }

        private void drawSpeechBubble(Graphics2D g2, int x, int y, int width, int height, String text) {
            Color bubbleColor = new Color(255, 255, 255, 230);
            g2.setColor(bubbleColor);
            g2.fillRoundRect(x, y, width, height, 20, 20);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(x, y, width, height, 20, 20);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            FontMetrics fm = g2.getFontMetrics();
            int textY = y + 20;
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                if (fm.stringWidth(line + word) < width - 20) {
                    line.append(word).append(" ");
                } else {
                    g2.drawString(line.toString(), x + 10, textY);
                    textY += fm.getHeight();
                    line = new StringBuilder(word + " ");
                }
            }
            g2.drawString(line.toString(), x + 10, textY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameView view = new GameView();
            GameModel model = new GameModel();
            GameController controller = new GameController(model, view);
            view.setController(controller);
            view.showWindow();
        });
    }
}

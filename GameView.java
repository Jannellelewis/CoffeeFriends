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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;

public class GameView extends JFrame {
    private GameController controller;

    private final JTextField playerNameField = new JTextField();
    private final JComboBox<GameModel.Personality> playerPersonalityCombo = new JComboBox<>(GameModel.Personality.values());
    private final JTextField companionNameField = new JTextField();
    private final JTextField companionPersonalityField = new JTextField();
    private final JProgressBar friendshipMeterBar = new JProgressBar(0, 100);
    private final JButton startButton = new JButton("Start");
    private final JButton responseButton1 = new JButton("Agree");
    private final JButton responseButton2 = new JButton("Encourage");
    private final GameScreenPanel gameScreenPanel = new GameScreenPanel();
    private boolean isEndScreen = false;
    private Color backgroundColor = new Color(255, 210, 220);

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

        JPanel responsePanel = new JPanel(new GridLayout(1, 2, 8, 8));
        responsePanel.add(responseButton1);
        responsePanel.add(responseButton2);

        JPanel responseContainer = new JPanel(new BorderLayout(4, 4));
        responseContainer.add(new JLabel("Choose your response:"), BorderLayout.NORTH);
        responseContainer.add(responsePanel, BorderLayout.CENTER);
        gamePanel.add(responseContainer, BorderLayout.SOUTH);

        rootPanel.add(customizationPanel, BorderLayout.NORTH);
        rootPanel.add(gamePanel, BorderLayout.CENTER);

        responseButton1.setEnabled(false);
        responseButton2.setEnabled(false);

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
        gameScreenPanel.setVisible(true);
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

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
            g2.setColor(Color.GRAY);
            g2.fillRect(barX, barY, barWidth, barHeight);
            float ratio = (float) friendshipValue / 100;
            int red = (int) (255 * (1 - ratio));
            int green = (int) (255 * ratio);
            g2.setColor(new Color(red, green, 0));
            g2.fillRect(barX, barY, (int) (barWidth * ratio), barHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(barX, barY, barWidth, barHeight);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Level " + friendshipLevel, barX + barWidth + 10, barY + 15);

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

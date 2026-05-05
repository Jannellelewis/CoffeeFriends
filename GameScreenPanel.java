import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

@SuppressWarnings("this-escape")
public class GameScreenPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private String companionDialogue = "";
    private String playerName = "You";
    private String companionName = "Companion";
    private int animationOffset = 0;
    private Timer animationTimer;
    private int friendshipValue = 0;
    private int friendshipLevel = 1;
    private boolean endScreen = false;
    private boolean peakFriendship = false;
    private boolean awkwardFlash = false;
    private boolean showGiftBanner = false;
    private boolean companionLeaves = false;
    private Color backgroundColor = new Color(255, 210, 220);

    public GameScreenPanel() {
        setPreferredSize(new Dimension(900, 520));
        animationTimer = new Timer(1000, e -> {
            if (!endScreen && !peakFriendship && !companionLeaves) {
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

    public void setCompanionLeaves() {
        this.companionLeaves = true;
        repaint();
    }

    public void setShowGiftBanner(boolean show) {
        this.showGiftBanner = show;
        repaint();
    }

    public void setAwkwardFlash(boolean flash) {
        this.awkwardFlash = flash;
        repaint();
    }

    public void setBackgroundColor(Color c) {
        this.backgroundColor = c;
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

        if (companionLeaves) {
            g2.setColor(new Color(255, 255, 224)); // light yellow
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.drawString("Thank you for being such a great friend!", getWidth() / 2 - 300, getHeight() / 2 - 100);
            g2.drawString(companionName + " has given you a gift and must leave now.", getWidth() / 2 - 250, getHeight() / 2 - 50);
            g2.drawString("But our friendship will always be cherished.", getWidth() / 2 - 250, getHeight() / 2);
            g2.setFont(new Font("SansSerif", Font.BOLD, 24));
            g2.drawString("Click Restart to play again.", getWidth() / 2 - 150, getHeight() / 2 + 100);
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

        g2.setColor(backgroundColor);
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
        
        if (awkwardFlash) {
            g2.setColor(new Color(255, 80, 80, 80));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(200, 0, 0));
            g2.setFont(new Font("SansSerif", Font.BOLD, 28));
            g2.drawString("😬 That felt a little awkward...", getWidth() / 2 - 220, getHeight() / 2 - 20);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.drawString("Friendship went down!", getWidth() / 2 - 100, getHeight() / 2 + 20);
        }

        drawSpeechBubble(g2, playerX - 10, characterY - 140 + animationOffset, 220, 60, "Choose a response below.");
        drawSpeechBubble(g2, companionX - 10, characterY - 140 + animationOffset, calculateBubbleWidth(companionDialogue), 60, companionDialogue);
    }

    private int calculateBubbleWidth(String text) {
        FontMetrics fm = getFontMetrics(new Font("SansSerif", Font.PLAIN, 14));
        int textWidth = fm.stringWidth(text);
        int maxWidth = Math.min(380, getWidth() - 20);
        return Math.max(180, Math.min(maxWidth, textWidth + 30));
    }

    /** Calculate the height a bubble needs to fully contain the wrapped text. */
    private int calcBubbleHeight(Graphics2D g2, String text, int bubbleWidth) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();
        int innerWidth = bubbleWidth - 20;
        String[] words = text.split(" ");
        int lines = 1;
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String test = line + word + " ";
            if (fm.stringWidth(test) > innerWidth && line.length() > 0) {
                lines++;
                line = new StringBuilder(word + " ");
            } else {
                line = new StringBuilder(test);
            }
        }
        int padding = 20; // top + bottom padding
        return padding + lines * lineHeight + 10;
    }

    private void drawSpeechBubble(Graphics2D g2, int x, int y, int width, int height, String text) {
        Color bubbleColor = new Color(255, 255, 255, 230);
        g2.setColor(bubbleColor);
        g2.fillRoundRect(x, y, width, height, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(x, y, width, height, 20, 20);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textY = y + fm.getAscent() + 8;
        int innerWidth = width - 20;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String test = line + word + " ";
            if (fm.stringWidth(test) > innerWidth && line.length() > 0) {
                g2.drawString(line.toString().trim(), x + 10, textY);
                textY += fm.getHeight();
                line = new StringBuilder(word + " ");
            } else {
                line = new StringBuilder(test);
            }
        }
        if (line.length() > 0) {
            g2.drawString(line.toString().trim(), x + 10, textY);
        }
    }
}
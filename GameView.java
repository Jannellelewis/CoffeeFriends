import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

@SuppressWarnings("this-escape")
public class GameView extends JFrame {
    private static final long serialVersionUID = 1L;
    private transient GameModel model;

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private CustomizationPanel customizationPanel;
    private GamePanel gamePanel;

    public void setBackgroundColor(Color c) {
        gamePanel.setBackgroundColor(c);
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
        customizationPanel.setController(controller);
        gamePanel.setController(controller);
    }

    public void setModel(GameModel model) {
        this.model = model;
        gamePanel.setModel(model);
    }

    private void initUI() {
        JPanel rootPanel = new JPanel(new BorderLayout(12, 12));
        customizationPanel = new CustomizationPanel();
        gamePanel = new GamePanel();
        cardPanel.add(customizationPanel, "customization");
        cardPanel.add(gamePanel, "game");
        rootPanel.add(cardPanel, BorderLayout.CENTER);
        setContentPane(rootPanel);
    }

    public void showWindow() {
        setVisible(true);
    }

    public void showGameScreen() {
        cardLayout.show(cardPanel, "game");
        gamePanel.showGameScreen();
    }

    public void updateFriendshipMeter(int value, int level) {
        gamePanel.updateFriendshipMeter(value, level);
    }

    public void updateDialogue(String dialogue) {
        gamePanel.updateDialogue(dialogue);
    }

    public void showAwkwardFeedback() {
        gamePanel.showAwkwardFeedback();
    }
    public void setCompanionPersonality(GameModel.Personality personality) {
        customizationPanel.setCompanionPersonality(personality);
    }

    public void setCharacterNames(String playerName, String companionName) {
        gamePanel.setCharacterNames(playerName, companionName);
    }

    public void setCharacterColors(java.awt.Color playerColor, java.awt.Color companionColor) {
        gamePanel.setCharacterColors(playerColor, companionColor);
    }

    public void setResponseLabels(String option1, String option2) {
        gamePanel.setResponseLabels(option1, option2);
    }

    public void showEndScreen(int level) {
        gamePanel.showEndScreen(level);
    }

    public void showLevelUpChoices(boolean canGift) {
        gamePanel.showLevelUpChoices(canGift);
    }

    public void hideLevelUpChoices() {
        gamePanel.hideLevelUpChoices();
    }

    public void enableResponseButtons() {
        gamePanel.enableResponseButtons();
    }

    public void showCustomizationScreen() {
        cardLayout.show(cardPanel, "customization");
        // Reset view state
        gamePanel.showCustomizationScreen();
    }

    public void updateGiftButtonVisibility(int level) {
        gamePanel.updateGiftButtonVisibility(level, model);
    }

    public void showCompanionLeavesScreen() {
        gamePanel.showCompanionLeavesScreen();
    }

    public void showPeakFriendshipScreen() {
        gamePanel.showPeakFriendshipScreen();
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
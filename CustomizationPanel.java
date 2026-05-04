import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridLayout;

@SuppressWarnings("this-escape")
public class CustomizationPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextField playerNameField = new JTextField();
    private final JComboBox<GameModel.Personality> playerPersonalityCombo = new JComboBox<>(GameModel.Personality.values());
    private final JTextField playerColorField = new JTextField("#FF0000");
    private final JTextField companionNameField = new JTextField();
    private final JTextField companionPersonalityField = new JTextField();
    private final JButton startButton = new JButton("Start");

    public CustomizationPanel() {
        setLayout(new GridLayout(6, 2, 8, 8));

        add(new JLabel("Player Name:"));
        add(playerNameField);
        add(new JLabel("Player Personality:"));
        add(playerPersonalityCombo);
        add(new JLabel("Player Color (Hex):"));
        add(playerColorField);
        add(new JLabel("Companion Name:"));
        add(companionNameField);
        add(new JLabel("Companion Personality:"));
        companionPersonalityField.setEditable(false);
        companionPersonalityField.setText("Not assigned yet");
        add(companionPersonalityField);
        add(new JLabel());
        add(startButton);
    }

    public void setController(GameController controller) {
        startButton.addActionListener(e -> {
            String playerName = playerNameField.getText().trim();
            GameModel.Personality personality = (GameModel.Personality) playerPersonalityCombo.getSelectedItem();
            String playerColorHex = playerColorField.getText().trim();
            String companionName = companionNameField.getText().trim();
            if (playerName.isEmpty() || companionName.isEmpty()) {
                // Optionally show a message
                return;
            }
            Color playerColor;
            try {
                playerColor = Color.decode(playerColorHex);
            } catch (NumberFormatException ex) {
                // Invalid hex, use default
                playerColor = Color.RED;
            }
            if (controller != null) {
                controller.startGame(playerName, personality, companionName, playerColor);
            }
        });
    }

    public void setCompanionPersonality(GameModel.Personality personality) {
        companionPersonalityField.setText(personality == null ? "Unknown" : personality.name());
    }
}
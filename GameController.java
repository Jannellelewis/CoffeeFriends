import javax.swing.JOptionPane;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class GameController {
    private final GameModel model;
    private final GameView view;
    private List<Color> backgroundColors = Arrays.asList(
        new Color(255,210,220),
        new Color(210,255,220),
        new Color(220,210,255),
        new Color(255,220,210),
        new Color(210,220,255)
    );
    private int colorIndex = 0;

    private void startNewDialogue() {
        GameModel.Personality companionPersonality = model.getCompanion().getPersonality();
        String dialogue = model.getDialogueBank().getRandomLine(companionPersonality);
        view.updateDialogue(dialogue);
        List<String> responses = model.getDialogueBank().getResponseOptions(dialogue);
        view.setResponseLabels(responses.get(0), responses.get(1));
    }

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }

    public void startGame(String playerName, GameModel.Personality personality, String companionName) {
        model.initializeGame(playerName, personality, companionName);
        view.setCompanionPersonality(model.getCompanion().getPersonality());
        view.setCharacterNames(model.getPlayer().getName(), model.getCompanion().getName());
        view.showGameScreen();
        startNewDialogue();
        view.updateFriendshipMeter(model.getFriendshipMeter().getValue(), model.getFriendshipMeter().getLevel());
    }

    public void handleResponse(String response) {
        if (response.equals("Continue")) {
            startNewDialogue();
        } else {
            model.getFriendshipMeter().increment();
            int val = model.getFriendshipMeter().getValue();
            if (val >= 100) {
                model.getFriendshipMeter().levelUp();
                model.getFriendshipMeter().setValue(0);
                colorIndex = (colorIndex + 1) % backgroundColors.size();
                view.setBackgroundColor(backgroundColors.get(colorIndex));
                view.updateDialogue("Yay! I'm so happy to get to know you better! Friendship Level " + model.getFriendshipMeter().getLevel());
                view.setResponseLabels("Continue", "Continue");
                view.updateFriendshipMeter(0, model.getFriendshipMeter().getLevel());
            } else {
                view.updateFriendshipMeter(val, model.getFriendshipMeter().getLevel());
                startNewDialogue();
            }
        }
    }

    private void showEndScreen() {
        view.showEndScreen(model.getFriendshipMeter().getLevel());
    }
}

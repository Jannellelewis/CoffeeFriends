import javax.swing.JOptionPane;
import java.util.List;

public class GameController {
    private final GameModel model;
    private final GameView view;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
    }

    public void startGame(String playerName, GameModel.Personality personality, String companionName) {
        model.initializeGame(playerName, personality, companionName);
        view.setCompanionPersonality(model.getCompanion().getPersonality());
        view.setCharacterNames(model.getPlayer().getName(), model.getCompanion().getName());
        String firstDialogue = model.getDialogueBank().getRandomLine(model.getCompanion().getPersonality());
        List<String> responses = model.getDialogueBank().getResponseOptions(firstDialogue);
        view.setResponseLabels(responses.get(0), responses.get(1));
        view.showGameScreen();
        view.updateDialogue(firstDialogue);
        view.updateFriendshipMeter(model.getFriendshipMeter().getValue());
    }

    public void handleResponse(String responseText) {
        model.getFriendshipMeter().increment();
        view.updateFriendshipMeter(model.getFriendshipMeter().getValue());

        if (model.getFriendshipMeter().isFull()) {
            showEndScreen();
            return;
        }

        String nextLine = model.getDialogueBank().getRandomLine(model.getCompanion().getPersonality());
        List<String> responses = model.getDialogueBank().getResponseOptions(nextLine);
        view.setResponseLabels(responses.get(0), responses.get(1));
        view.updateDialogue(nextLine);
    }

    private void showEndScreen() {
        String companionName = model.getCompanion().getName();
        JOptionPane.showMessageDialog(view, "You and " + companionName + " are now best friends!", "Best Friends", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}

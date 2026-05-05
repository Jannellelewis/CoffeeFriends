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
    private boolean awaitingLevelUpChoice = false;
    // Add this field at the top of GameController, alongside colorIndex and awaitingLevelUpChoice:
    private String currentDialogue = "";

    private void startNewDialogue() {
        GameModel.Personality companionPersonality = model.getCompanion().getPersonality();
        String dialogue = model.getDialogueBank().getRandomLine(companionPersonality);
        currentDialogue = dialogue; // ← ADD THIS LINE
        view.updateDialogue(dialogue);
        List<String> responses = model.getDialogueBank().getResponseOptions(dialogue);
        view.setResponseLabels(responses.get(0), responses.get(1));
    }

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        view.setModel(model);
    }

    public void startGame(String playerName, GameModel.Personality personality, String companionName, java.awt.Color playerColor) {
        model.initializeGame(playerName, personality, companionName, playerColor);
        view.setCompanionPersonality(model.getCompanion().getPersonality());
        view.setCharacterNames(model.getPlayer().getName(), model.getCompanion().getName());
        view.setCharacterColors(model.getPlayer().getColor(), model.getCompanion().getColor());
        view.showGameScreen();
        startNewDialogue();
        view.updateFriendshipMeter(model.getFriendshipMeter().getValue(), model.getFriendshipMeter().getLevel());
    }

    public void handleResponse(String response) {
        if (awaitingLevelUpChoice) {
            return; // Ignore response buttons while waiting for level-up choice
        }

        if (response.equals("Continue")) {
            startNewDialogue();
            view.updateGiftButtonVisibility(model.getFriendshipMeter().getLevel());
        } else {
    boolean isBadResponse = model.getDialogueBank()
        .isMismatchedResponse(currentDialogue, response);

    if (isBadResponse) {
        model.getFriendshipMeter().decrement();
        int val = model.getFriendshipMeter().getValue();
        view.updateFriendshipMeter(val, model.getFriendshipMeter().getLevel());
        view.showAwkwardFeedback(); // ← tells the view to flash feedback
        startNewDialogue();
    } else {
        model.getFriendshipMeter().increment();
        int val = model.getFriendshipMeter().getValue();
        int maxXP = model.getFriendshipMeter().getXpNeededForLevel();

        if (val >= maxXP) {
            model.getFriendshipMeter().levelUp();
            int newLevel = model.getFriendshipMeter().getLevel();

            if (newLevel == 5 && !model.hasGivenGift()) {
                view.showCompanionLeavesScreen();
                return;
            }

            colorIndex = (colorIndex + 1) % backgroundColors.size();
            view.setBackgroundColor(backgroundColors.get(colorIndex));
            view.updateDialogue("Yay! I'm so happy to get to know you better! Friendship Level " + newLevel + "!");
            view.updateFriendshipMeter(0, newLevel);
            awaitingLevelUpChoice = true;
            view.showLevelUpChoices(newLevel >= 3 && !model.hasGivenGift());
        } else {
            view.updateFriendshipMeter(val, model.getFriendshipMeter().getLevel());
            startNewDialogue();
        }
    
    }}
}

    public void handleLevelUpChoice(String choice) {
        if (!awaitingLevelUpChoice) {
            return;
        }

        awaitingLevelUpChoice = false;
        view.hideLevelUpChoices();

        if (choice.equals("gift")) {
            if (model.getFriendshipMeter().getLevel() >= 3) {
                model.setGiftGiven(true);
                view.showPeakFriendshipScreen();
            }
        } else if (choice.equals("continue")) {
            startNewDialogue();
            view.updateGiftButtonVisibility(model.getFriendshipMeter().getLevel());
            view.enableResponseButtons();
        } else if (choice.equals("restart")) {
            resetGame();
        }
    }

    public void handleSideGiftClick() {
        if (model.getFriendshipMeter().getLevel() >= 3 && !model.hasGivenGift()) {
            model.setGiftGiven(true);
            view.showPeakFriendshipScreen();
        }
    }

    public void resetGame() {
        model.resetFriendshipMeter();
        colorIndex = 0;
        view.setBackgroundColor(new Color(255, 210, 220));
        view.showCustomizationScreen();
    }

    public static void main(String[] args) {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);
        view.setController(controller);
        view.showCustomizationScreen();
    }
}

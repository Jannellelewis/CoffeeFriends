import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameModel {
    public enum Personality {
        CHEERFUL,
        SHY;

        public static Personality random() {
            Personality[] values = values();
            return values[new Random().nextInt(values.length)];
        }
    }

    public static class Player {
        private final String name;
        private final Personality personality;

        public Player(String name, Personality personality) {
            this.name = name;
            this.personality = personality;
        }

        public String getName() {
            return name;
        }

        public Personality getPersonality() {
            return personality;
        }
    }

    public static class Companion {
        private final String name;
        private final Personality personality;

        public Companion(String name, Personality personality) {
            this.name = name;
            this.personality = personality;
        }

        public String getName() {
            return name;
        }

        public Personality getPersonality() {
            return personality;
        }
    }

    public static class FriendshipMeter {
        private final Random random = new Random();
        private int value;
        private int level;

        public FriendshipMeter() {
            this.value = 0;
            this.level = 1;
        }

        public int getValue() {
            return value;
        }

        public int getLevel() {
            return level;
        }

        public int getXpNeededForLevel() {
            return level * 5;
        }

        public void increment() {
            int increment = random.nextInt(4) + 1; // 1-4
            int xpNeeded = getXpNeededForLevel();
            value += increment;
        }

        public void setValue(int value) {
            this.value = Math.max(0, value);
        }

        public void levelUp() {
            this.level++;
            this.value = 0; // reset for next level
        }

        public void resetLevel() {
            this.level = 1;
        }

        public boolean isFull() {
            return value >= getXpNeededForLevel();
        }

        public boolean canGiveGift() {
            return level >= 3;
        }
    }

    public static class DialogueBank {
        private final Map<Personality, List<String>> companionLines = new EnumMap<>(Personality.class);
        private final Map<String, List<String>> dialogueResponses = new HashMap<>();
        private final Random random = new Random();

        public DialogueBank() {
            companionLines.put(Personality.CHEERFUL, new ArrayList<>());
            companionLines.put(Personality.SHY, new ArrayList<>());

            companionLines.get(Personality.CHEERFUL).add("What a bright day to share a latte together!");
            companionLines.get(Personality.CHEERFUL).add("I love chatting with you in this cozy coffee shop.");
            companionLines.get(Personality.CHEERFUL).add("Your smile makes the coffee taste sweeter.");
            companionLines.get(Personality.CHEERFUL).add("Let's plan our next adventure after this cup!");

            companionLines.get(Personality.SHY).add("I like it when we sit quietly together.");
            companionLines.get(Personality.SHY).add("This coffee shop feels safe with you here.");
            companionLines.get(Personality.SHY).add("I'm a little nervous, but I'm happy you're here.");
            companionLines.get(Personality.SHY).add("Thank you for talking with me today.");

            // Responses for cheerful dialogues
            dialogueResponses.put("What a bright day to share a latte together!", List.of("Absolutely! The weather is perfect.", "Yes, it's a great day for coffee."));
            dialogueResponses.put("I love chatting with you in this cozy coffee shop.", List.of("Me too! What's your favorite drink?", "This place has such a nice atmosphere."));
            dialogueResponses.put("Your smile makes the coffee taste sweeter.", List.of("That's so sweet! Thank you.", "Aw, you're making me blush."));
            dialogueResponses.put("Let's plan our next adventure after this cup!", List.of("Sounds exciting! What do you have in mind?", "I'm in! Adventures are fun."));

            // Responses for shy dialogues
            dialogueResponses.put("I like it when we sit quietly together.", List.of("I enjoy that too. It's peaceful.", "Quiet moments are nice."));
            dialogueResponses.put("This coffee shop feels safe with you here.", List.of("I'm glad you feel that way.", "It's comforting to be here."));
            dialogueResponses.put("I'm a little nervous, but I'm happy you're here.", List.of("No need to be nervous around me.", "I'm happy to be here with you."));
            dialogueResponses.put("Thank you for talking with me today.", List.of("You're welcome. It was nice.", "Of course, anytime."));
        }

        public String getRandomLine(Personality personality) {
            List<String> lines = companionLines.get(personality);
            if (lines == null || lines.isEmpty()) {
                return "";
            }
            return lines.get(random.nextInt(lines.size()));
        }

        public List<String> getResponseOptions(String dialogue) {
            return dialogueResponses.getOrDefault(dialogue, List.of("That's interesting.", "I agree."));
        }
    }

    private Player player;
    private Companion companion;
    private final FriendshipMeter friendshipMeter;
    private final DialogueBank dialogueBank;
    private boolean hasGivenGift = false;

    public GameModel() {
        this.friendshipMeter = new FriendshipMeter();
        this.dialogueBank = new DialogueBank();
    }

    public boolean hasGivenGift() {
        return hasGivenGift;
    }

    public void setGiftGiven(boolean given) {
        this.hasGivenGift = given;
    }

    public void initializeGame(String playerName, Personality playerPersonality, String companionName) {
        this.player = new Player(playerName, playerPersonality);
        this.companion = new Companion(companionName, Personality.random());
        this.friendshipMeter.increment(); // placeholder for initialization behavior
    }

    public Player getPlayer() {
        return player;
    }

    public Companion getCompanion() {
        return companion;
    }

    public FriendshipMeter getFriendshipMeter() {
        return friendshipMeter;
    }

    public DialogueBank getDialogueBank() {
        return dialogueBank;
    }

    public void resetFriendshipMeter() {
        this.friendshipMeter.setValue(0);
        this.friendshipMeter.resetLevel();
        this.hasGivenGift = false;
    }
}

import java.util.ArrayList;
import java.util.EnumMap;
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
        private static final int MAX_VALUE = 100;
        private final int incrementAmount;
        private int value;

        public FriendshipMeter() {
            this.incrementAmount = 10;
            this.value = 0;
        }

        public int getValue() {
            return value;
        }

        public void increment() {
            value = Math.min(MAX_VALUE, value + incrementAmount);
        }

        public boolean isFull() {
            return value >= MAX_VALUE;
        }
    }

    public static class DialogueBank {
        private final Map<Personality, List<String>> companionLines = new EnumMap<>(Personality.class);
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
        }

        public String getRandomLine(Personality personality) {
            List<String> lines = companionLines.get(personality);
            if (lines == null || lines.isEmpty()) {
                return "";
            }
            return lines.get(random.nextInt(lines.size()));
        }
    }

    private Player player;
    private Companion companion;
    private final FriendshipMeter friendshipMeter;
    private final DialogueBank dialogueBank;

    public GameModel() {
        this.friendshipMeter = new FriendshipMeter();
        this.dialogueBank = new DialogueBank();
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
}

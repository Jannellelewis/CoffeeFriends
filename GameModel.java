import java.awt.Color;
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
        private final Color color;

        public Player(String name, Personality personality, Color color) {
            this.name = name;
            this.personality = personality;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Personality getPersonality() {
            return personality;
        }

        public Color getColor() {
            return color;
        }
    }

    public static class Companion {
        private final String name;
        private final Personality personality;
        private final Color color;

        public Companion(String name, Personality personality, Color color) {
            this.name = name;
            this.personality = personality;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Personality getPersonality() {
            return personality;
        }

        public Color getColor() {
            return color;
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
            value += increment;
        }

        // Add directly after the increment() method:
        public void decrement() {
            int penalty = random.nextInt(3) + 1; // 1–3 XP penalty
            value = Math.max(0, value - penalty);
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
        private final Map<String, List<String>> mismatchedResponses = new HashMap<>();
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

            // Mismatched responses: cheerful companion lines get shy/cold replies
            mismatchedResponses.put("What a bright day to share a latte together!", List.of("I guess... I'm not really a morning person.", "I'd rather just sit in silence."));
            mismatchedResponses.put("I love chatting with you in this cozy coffee shop.", List.of("...I didn't really want to come today.", "Can we just not talk for a bit?"));
            mismatchedResponses.put("Your smile makes the coffee taste sweeter.", List.of("I don't really smile much.", "That's a weird thing to say."));
            mismatchedResponses.put("Let's plan our next adventure after this cup!", List.of("I'd rather just go home after this.", "Adventures sound exhausting."));

            // Mismatched responses: shy companion lines get overly loud/pushy replies
            mismatchedResponses.put("I like it when we sit quietly together.", List.of("Let's liven things up! Wanna shout across the café?", "Silence is boring, let's make some noise!"));
            mismatchedResponses.put("This coffee shop feels safe with you here.", List.of("Don't be so clingy, it's just a coffee shop.", "I invited three more people, hope that's okay!"));
            mismatchedResponses.put("I'm a little nervous, but I'm happy you're here.", List.of("Nervous? Just relax and be loud!", "Stop worrying and perform that song you know!"));
            mismatchedResponses.put("Thank you for talking with me today.", List.of("We should've invited way more people!", "Next time let's make it a huge group hangout."));
        }

        public String getRandomLine(Personality personality) {
            List<String> lines = companionLines.get(personality);
            if (lines == null || lines.isEmpty()) {
                return "";
            }
            return lines.get(random.nextInt(lines.size()));
        }

        public List<String> getResponseOptions(String dialogue) {
            List<String> good = dialogueResponses.getOrDefault(dialogue, List.of("That's interesting.", "I agree."));
            List<String> bad = mismatchedResponses.getOrDefault(dialogue, List.of());

            if (!bad.isEmpty()) {
                // One good response, one mismatched response — randomly assign to button 1 or 2
                String badChoice = bad.get(random.nextInt(bad.size()));
                if (random.nextBoolean()) {
                    return List.of(good.get(0), badChoice);
                } else {
                    return List.of(badChoice, good.get(1 % good.size()));
                }
            }
            return good;
        }

        // Add this method right after getResponseOptions():
        public List<String> getMismatchedResponses(String dialogue) {
            return mismatchedResponses.getOrDefault(dialogue, List.of());
        }

        public boolean isMismatchedResponse(String dialogue, String response) {
            List<String> mismatches = mismatchedResponses.getOrDefault(dialogue, List.of());
            return mismatches.contains(response);
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

    public void initializeGame(String playerName, Personality playerPersonality, String companionName, Color playerColor) {
        this.player = new Player(playerName, playerPersonality, playerColor);
        Color companionColor = new Color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
        this.companion = new Companion(companionName, Personality.random(), companionColor);
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

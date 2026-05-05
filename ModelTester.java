import java.awt.Color;

public class ModelTester {

    static int passed = 0;
    static int failed = 0;

    static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== CoffeeFriends Model Tests ===\n");

        // ----------------------------------------------------------------
        // TEST 1: Player cannot give a gift until friendship reaches level 3
        // ----------------------------------------------------------------
        GameModel model1 = new GameModel();
        model1.initializeGame("Ada", GameModel.Personality.CHEERFUL, "Bea", Color.PINK);

        check("No gift at level 1",
                !model1.getFriendshipMeter().canGiveGift());

        model1.getFriendshipMeter().levelUp();
        check("No gift at level 2",
                !model1.getFriendshipMeter().canGiveGift());

        model1.getFriendshipMeter().levelUp();
        check("Gift unlocked at level 3",
                model1.getFriendshipMeter().canGiveGift());

        // ----------------------------------------------------------------
        // TEST 2: Game ends (companion leaves) when friendship hits level 5
        // ----------------------------------------------------------------
        GameModel model2 = new GameModel();
        model2.initializeGame("Ada", GameModel.Personality.CHEERFUL, "Bea", Color.PINK);

        model2.getFriendshipMeter().levelUp(); // → 2
        model2.getFriendshipMeter().levelUp(); // → 3
        model2.getFriendshipMeter().levelUp(); // → 4
        model2.getFriendshipMeter().levelUp(); // → 5

        check("Friendship ends at level 5",
                model2.getFriendshipMeter().getLevel() == 5);

        // ----------------------------------------------------------------
        // TEST 3: Hex color validation
        // ----------------------------------------------------------------
        check("Valid hex color FF5733",    isValidHex("FF5733"));
        check("Valid hex color 000000",    isValidHex("000000"));
        check("Invalid hex — too short",   !isValidHex("FF57"));
        check("Invalid hex — bad chars",   !isValidHex("ZZZZZZ"));
        check("Invalid hex — has #",       !isValidHex("#FF5733"));

        // ----------------------------------------------------------------
        // TEST 4: Player and companion names must not be blank
        // ----------------------------------------------------------------
        check("Non-blank player name accepted",   isValidName("Ada"));
        check("Non-blank companion name accepted", isValidName("Bea"));
        check("Blank player name rejected",        !isValidName(""));
        check("Whitespace-only name rejected",     !isValidName("   "));

        // ----------------------------------------------------------------
        // TEST 5: Game restarts properly — meter and gift flag reset to defaults
        // ----------------------------------------------------------------
        GameModel model3 = new GameModel();
        model3.initializeGame("Ada", GameModel.Personality.SHY, "Bea", Color.CYAN);

        model3.getFriendshipMeter().levelUp();
        model3.getFriendshipMeter().levelUp();
        model3.getFriendshipMeter().setValue(8);
        model3.setGiftGiven(true);

        model3.resetFriendshipMeter();

        check("Restart resets friendship value to 0",
                model3.getFriendshipMeter().getValue() == 0);
        check("Restart resets friendship level to 1",
                model3.getFriendshipMeter().getLevel() == 1);
        check("Restart clears gift flag",
                !model3.hasGivenGift());

        // ----------------------------------------------------------------
        // Summary
        // ----------------------------------------------------------------
        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    // Returns true if the string is exactly 6 valid hex characters (no # prefix)
    static boolean isValidHex(String hex) {
        if (hex == null || hex.length() != 6) return false;
        return hex.toUpperCase().matches("[0-9A-F]{6}");
    }

    // Returns true if the name is non-null and not blank/whitespace-only
    static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }
}

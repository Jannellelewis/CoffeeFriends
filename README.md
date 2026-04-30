# CoffeeFriends

Gameplay written by me: Your and a companion sit in a coffee and you guys talk, you are able to customize your name and your personailty. You can also customize the companions name but not their personality, that is randomly chosen. There is a firenship bar that increases as you talk to them

Model
    Player
        String name
        Personality personality — enum: CHEERFUL, SHY

    Companion
        String name
        Personality personality — randomly assigned from the same enum at game start
    FriendshipMeter
        int value — starts at 0, max 100
        increment() — adds a fixed amount (e.g. 10) per completed exchange
        isFull() — returns true when value ≥ 100
    DialogueBank
        Map<Personality, List<String>> companionLines — 4 hardcoded lines per personality
        getRandomLine(Personality p) — returns a random line from the matching list
Done when: All fields initialize correctly, increment() raises value, isFull() returns true at 100, getRandomLine() returns a non-null string for each personality.


View
    CustomizationScreen
        Text field for player name
        Dropdown for player personality (CHEERFUL, SHY)
        Text field for companion name
        Read-only label showing the randomly assigned companion personality
        A "Start" button
    GameScreen
        Solid rectangle background (coffee shop stand-in)
        Two labeled ovals representing the player and companion
        One JProgressBar for the shared friendship meter
        A text area displaying the companion's current dialogue line
        Two JButtons for player responses (labels set each turn by Controller)
    EndScreen
        JOptionPane popup with the text "You and [companion name] are now best friends!"
        On dismissal, the application closes
Done when: All three screens render without errors, buttons are clickable, the progress bar visually fills when its value is set, and the popup displays the correct companion name.


Controller
    CustomizationController
        Reads player name, player personality, and companion name from CustomizationScreen
        Calls new Companion(name, Personality.random()) to build the Companion
        Calls new Player(name, personality) to build the Player
        Swaps CustomizationScreen out and GameScreen in
    DialogueController
        On each turn: calls DialogueBank.getRandomLine(companion.getPersonality()) and sets it in the dialogue text area
        Sets response button labels to two fixed strings (same for all turns)
        On either button click: calls FriendshipMeter.increment(), then calls isFull()
        If isFull() is true: tells GameScreen to show the JOptionPane end popup
Done when: Clicking a response button increments the bar, the companion line changes each turn, and a full bar triggers the popup with the correct name.
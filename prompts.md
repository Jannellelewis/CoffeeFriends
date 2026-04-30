Interview with claude:
I want to build a game where you can customize a character, their appearance, their personality and play with them in Java with Swing using MVC. Before we write any code, interview me. Ask me one question at a time about gameplay, controls, win and loss conditions, and what should be on the screen. After about 8 questions, summarize what I told you as a one-page spec organized by Model, View, and Controller.
Q1:
What kind of game world does your character exist in? For example, is this a turn-based RPG, a real-time adventure, a social simulation (like a virtual pet or Tamagotchi), a puzzle game, or something else entirely?
Answer: an RPG mixed with a social stimulation like tomadochi life but only a couple entities
Q2: 
How many characters will be in the world at once? For example, just your one custom character, or your character plus one or two AI companions/NPCs they can interact with?
Answer: just my character and one simple companion i can interact with, nothing to crazy detailed or difficult
Q3:
What kinds of interactions can you have with your companion? For example, can you talk to them, give them gifts, go on quests together, battle enemies side by side — or a mix of these?
Answer: i want the main thing to just be about talking to them and they can only give a gift once, so dont go to complicated on the gift giving part
Q4: 
How does the relationship with your companion grow or change? For example, is there a friendship/affection meter that goes up the more you talk to them, and does anything special happen when it's full?
Answer: there should be a simple friendship bar that appears above them, it fills easily and there shouldnt be anything that makes it go down. When the bar gets full, show a congratulations and move on
Q5: 
When you talk to your companion, how does the dialogue work? For example, do you pick from a list of conversation topics, type something freely, or does the companion just say random things when you click "Talk"?
Answer: i want the companion to just say random things and then i can respond in different ways, dont make this way to complicated but still be fun
Q6: 
What should the game screen look like? For example, is it a simple room/background with your two characters standing in it, with UI panels for stats and buttons along the edges?
Answer: The characters should be sitting in the coffee shop, we get a side view of them. Above each of the characters is the friendship bar
Q7: 
How do you customize your character and their companion at the start? For example, do you pick things like hair color, outfit, skin tone, and a name — and do you do the same for the companion, or is the companion's appearance fixed?
You can pick the hair color, the skin tone, the name and personality type of your character. The companion character, their personality, appearance are randomly chosen, and the only thing YOU can change is their name 
Q8: 
What does "personality type" affect in the game? For example, does your character's personality change how your dialogue response options are worded, and does the companion's random personality change what kinds of things they say to you?
The dialogue and response changed based on the personality, and the companions responses should be based on THEIR personality



Prompt 1 given to Claude
USED: Given a one-week deadline and that I'm a beginner, which features in this spec should I cut for an MVP? Which are stretch goals? Then show the one page game spec without the things cut.

RESULT: it said to cut the skin tone and hair color customization (i might re-add that later), only have 1 friendship bar instead of 2, get rid of the gift mechanic, only give 2 personality types. Also the cut the part about the background being a coffee shop (might re-add)

CHANGED: i wanted to make sure the spec was clear and fit exactly so i asked it to ‘adjust the spec so it fits this: Each layer's data is enumerated, "Done" is concrete and testable, Stretch features are not here — they go in a separate section if at all, Short enough to paste into a prompt as context, Specific enough that the AI cannot drift into building a different game’ and it gave me the spec i have in the README 



Prompt 1 (to vsc ai)
USED: I am building  a game where you can customize a character, their appearance, their personality and play with them in Java with Swing using MVC. Here is my spec:
(inserted spec copied exactly from the README but it was really long here)
Generate three class shells — GameModel.java, GameView.java, GameController.java — with method stubs based on this design. GameModel must not import any Swing classes. The program should compile and open a blank window.

RESULTS: when you press run, it opens a new window with a place to put your name, then below that it asks CHEERFUL or SHY but it wont let you click. Then below that you can enter the companions name and theres a blank space for their personality type but i doesnt show what it is 

CHANGES: i wanted to be able to pick my personality type and see what the companions is so i asked 'On the window i should be allowed to select my personality type and i should be able to see the personality the companion was randomly assigned. fix that on GameView so i can do that', it did that and now theres a drop down thing for selecting personality

ERRORS: after i ran the initally prompt and before i made any changes, there were 4 problems in GameView, it said the value of the field GameView.model, Gameview.controller, GameView.responseButton1 and GameView.responseButton2 are not used. I looked at where these are and i think they just were forgotten to be called or havent been put to use yet. I asked AI this "When i ran that code, i was give 4 problems on GameView.java it said the value of the field GameView.model, Gameview.controller, GameView.responseButton1 and GameView.responseButton2 are not used. I think that it was forgotten to be called or it hasnt been used yet, what do you think. What did you resolve and why." It just said that those are warnings and to remove them/complete the problem i have to add responseButton 1 and 2 and attach action listeners to them



Prompt 2
USED: In GameView, make the background a 30x30 grid. The background color should be a light pink color. In the middle of the screen, place the character representing me (the one customizable) and next to it should be the companion. This will be the game screen. The character customization should be a little box above the game screen. The dialoge that the characters speak should be over their heads. For example if they say something its above the companions head and then i can choose how to respond based on whats above my head
RESULTS: I like the background color and graph but im going to change the cells so they are square not rectangle. RIght now there are 2 circles in the middle of the screen with dialougue above their head, but they are too close so the dialogues overlap. Also you cant see the names or choose how to respond. 
CHANGES: basically what i said before, i asked them this: 'Make it so the dialogues do not overlap eachother. Find a way to put the charcters name above their, which can be customized afetr you press start. There is also no button to select how to repsond, please add that'
ERRORS: just a simple error, the dialogues overlapped, you couldnt see the characters names or choose how to respond. Not being able to respond was definetly the bigger 'problem' out of that because thats the whole point, that was a pretty easy thing to fix



Prompt 3
USED: Make the characters move up and down just a little bit every 0.5 seconds on loop. The dialogues and names should move at the same time and pace as the characters. Make sure the dialogues fit inside the message bubbles, so adjust the message bubbles to fit the dialogue. Have the background be a 30x20 grid instead of a 30 by 30. Before giving the dialogue, make the user enter the name and personaloity before starting the conversation. For the dialogue instead of just saying what tone, give differant ways they could respond

RESULTS: the characters move up and down but i feel like its to fast/to much so im going to adjust that. The dialogue now fits in the boxes but theyre at differant heights so i might change that. I also wanted it to give dialogue responses based on what the companion says not ones that are the same each time

CHANGES: i told it 'Change it so the characters move up and down less and they do that every 1 second. The speech bubles should be at the same height. Make the dialogue response options related to what the companion is saying so it isnt random, there should be a curious and friendly respons that answers/respons accordingly based on what the companion has said'

ERRORS: none but the prompt i put in was not clear enough so i had to tweak some parts




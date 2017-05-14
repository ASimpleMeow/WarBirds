# WAR BIRDS
---

Name: Oleksandr Kononov 

Student Number: 20071032 

Course: Entertainment Systems Year 2 

Subject: Console Game Development

---

A [1942 game](https://en.wikipedia.org/wiki/1942_(video_game)) clone where the player controls a plane from a top-down perspective. This is made with LibGDX framework with Java. The player must fight through the level, shooting down enemy planes, picking up powerups on the way (both of which give score). All to fight an end level boss, which upon defeat will allow you to progress to the next level.

## Game features : 

+ Smartphone optimised display
+ Touchscreen controls (for non PC)
+ Three variation of AI enemy planes : 
    + Simple zig-zag pattern with continuous shooting
    + Locking in on players position and shooting
    + Flying in behind the player and ambushing him
+ Powerup drops from enemies : 
    + Health increase
    + Shield power to block enemy bullets
    + Speed increase
    + Bullet firerate increase
+ Nice graphical HUD (head-up display) for health and shield, health bar also turns red if it's in a critically low state with a warning sound effect.
+ Good acrade-style background music and sound effects with seperate boss music
+ External font - KarmaFuture
+ Custom buttons and background for the MenuScreen.
+ GUI count down before starting a level or fighting the boss
+ Continue option :
	+ Continue playing the highest level you reached before

---

## Game Options

+ Turn the sound effects on/off and adjust their volume
+ Turn the music on/off and adjust it's volume
+ Change the difficulty : 
	+ Controls the maximum permissible enemies that can be on the screen at any given time
	+ Difficult range is between 1 and 5


---

## Game Controls (PC): 
+ WASD for movement
+ Spacebar to shoot
+ ESC for exit


## Game Controlls (Smartphone):
+ The player is automatically positioned on the bottom of the screen
+ Touch to the left/right of the player to move
+ When the screen is touched/dragged, player will automatically shoot

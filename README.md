## Installation

To build the executable:

     ant compile
     ant jar

To run the game:

     ant run
     
To set command line arguments use :

    java --enable-preview -cp lib/zen5.jar:classes baba.main.Main -jar baba.jar
    
## Options

The game accept the following arguments :

set the starting level folder:

    --levels [LEVEL FOLDER PATH]

start at the specified level with no continuation:

    --level [LEVEL PATH] : 
    
start the game with a default rule:

    --execute [RULE]
    
### Example :

    --execute WALL AND ROCK IS WIN AND PUSH
    --execute FLAG IS ROCK
    --levels res/levels
    --level res/levels/bonus-level.csv
    
## How to play

**R** to restart the current level

**Q** to quit the game

**S** to save the current game state

**T** to load a saved game state

**D** to go to the previous level

**F** to go to the next level

**Arrow** to move

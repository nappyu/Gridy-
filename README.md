# Gridy

[![Gridy_video_link](https://github.com/nappyu/Gridy/blob/main/images/youtube.png)](https://youtu.be/LpiFU69CHWo)

### Contributors
David Kim, Hee Su Kim, Josh Rayo, Toji Nakabayashi, Won Tae Lee

### Prototype
![Prototype](https://github.com/nappyu/Gridy/blob/main/images/gridy_example.png)

** NOTE: 't' is a unit of time **

### Description
Gridy provides the users a flexible and creative language, allowing them to create a grid-style game. Depending on how the user creates the game with Gridy, it may be a rhythm game or a whack-a-mole game or something completely new. 

Users can create a user-specified n x n grid and blocks, and set properties of the blocks (each square of the grid) such as the length of time they appear, color, key bindings, and points. Users can re-use the blocks they create and place them on the grid to appear at a specified time. Using a simple system of (x coordinate, y coordinate, time coordinate), users can also write functions to more easily create complex patterns for the appearance of blocks.

### Installation
You need to set up several libraries for Gridy to work:

* JavaFX (i.e., follow the instruction here to add the JavaFX library and set-up the VM: https://www.jetbrains.com/help/idea/javafx.html#create-project)
* Download and add the exp4j jar from https://www.objecthunter.net/exp4j/download.html
* Under `Edit Configurations...`, have `ui.GridyGame` selected as the Main class
* Make sure you've followed the VM steps from the JavaFX link above. In addition to the VM options inputted previously, add `,javafx.media` at the end as well. (i.e., `--module-path C:\Users\sample\Desktop\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml,javafx.media`)


### Running the Game
Refer to the Specs section below and its examples, then write your own Gridy game inside `input.tcts` file. Run the Game. If you have written the language correctly, a UI should appear. If not, an error will be returned on the console if Gridy fails to compile.

** IMPORTANT NOTE: <br/>
* All Cell, Function and Repeat objects must be a part of (i.e., included in) the Section to appear in a game.
* Thus, the game MUST include at least one Section object.
* Section is a portion of a game relative to time. For example, a minute long Gridy game might be divided into 2 Sections; one 20secs long Section, and one 40secs long Section. Or, it might be divided into two 30secs long Sections for example.
**

## Specs

* Language: Java 
* Fixed literal - capitalize
* User strings - no cap at the beginning

<br/>

### Grammar:

```
PROGRAM ::= GCREATE DECLARE+ ADD+ ADDSECT+ DIFFICULTY?
GCREATE ::= "CreateGame(" NUMBER "," NUMBER "," NUMBER "," MUSICFILE? ");"
DECLARE ::= DCELL | DREPEAT | DFUNCTION | DSECTION
ADD ::= ADDSIMP | ADDFUNC
ADDSECT ::= "AddSection(" NAME ");"
DIFFICULTY ::= "Difficulty(" LEVEL ");"
DCELL ::= "Cell" NAME "=" "CreateCell(" NUMBER "," COLOR "," NUMBER ");"
DREPEAT ::= "Repeat" NAME "=" "CreateRepeat(" NAME "," NUMBER "," NUMBER ");"
DFUNCTION ::= "Function" NAME "=" "CreateFunction(" EQUATION "," EQUATION "," NAME "," NUMBER ");"
DSECTION ::= "Section" NAME "=" "CreateSection(" NUMBER ");"
ADDSIMP ::= NAME ".Add(" NAME "," NUMBER "," NUMBER "," NUMBER ");"
ADDFUNC ::= NAME ".Add(" NAME "," NUMBER ");"
LEVEL ::= [0-9]                                                     #### level is only from 0 to 9
NUMBER ::= [0-9]+
MUSICFILE ::= ^[A-Za-z0-9.+-*/%~\\s!]+.(?:mp3|wav)
NAME ::= [a-z][a-ZA-Z0-9]*
COLOR ::= ["red"| "green" | ...] | #[0-9a-zA-Z]
```
<br/>

### Methods:

**CreateGame(int, int, int, string)**<br/>
<em>
// CreateGame(grid_size x, grid_size y, duration, (optional: music_file_name));<br/>
// Creates a grid that is x by y with each coordinate representing a cell<br/>
@param `grid_sizeX` int the horizontal length<br/>
@param `grid_sizeY` int  the vertical length<br/>
@param `duration` int  how long the game will last in seconds, the game will terminate after this duration <br/>
@param `music_file_name` string  a name of a music file within the `bgm` folder that will play along with the game. Only accepts *.mp3 or *.wav files.<br/>
</em>
 
<br/><br/>

**CreateCell(int, string, int)**<br/>
<em>
// CreateCell(FadeOutDuration, Color, Score);<br/>
// Creates a cell that has a fade out time, color, and score<br/>
@param `FadeOutDuration` int how long a cell lasts for on the screen<br/>
@param `Color` string what color the cell will be when it appears on screen. Can be a predefined colour name like 'red' or a HEX value like '#fffff'<br/>
@param `Score` int How much a cell will be worth on hit<br/>
</em>

<br/><br/>

**CreateRepeat(string, int, int)** <br/>
<em>
// CreateRepeat(cell_name, frequency, duration); <br/>
@param `cell_nam`e string Cell to import<br/>
@param `frequency` int time before a cell repeats, ex: 3 = 3 seconds between each cell activation<br/>
@param `duration` int How long the cell would repeat for<br/>
</em>

<br/><br/>

**CreateFunction(string, string, string, int)** <br/>
<em>
// CreateFunction(mathEquation_x, mathEquation_y, cell_name, duration) <br/>
// manipulate x and y coords relative to t time in seconds for a specific cell you can use t as the reference time and change x and y based on t, for exampe equation x = t % 10 y = 0 will translate a cell horizontally on row 0 by one every second <br/>
@param `mathEquation_x` string Custom math equation for the x-coordinate<br/>
@param `mathEquation_y` string Custom math equation for the y-coordinate<br/>
@param `cell_name` string Cell to import<br/>
@param `duration` the duration of the function<br/>
</em>

<br/><br/>

**CreateSection(int)** <br/>
<em>
// CreateSection(length) <br/>
// Create a segment of the game (i.e., 30seconds of the game) <br/>
@param `length` Duration of a section <br/>
</em>

<br/><br/>

**Add(Repeat, int, int, int)**<br/>
<em>
// Add(Repeat, x, y, starttime) <br/>
// Adds a previously made Repeat object to a section at specified x, y, and start time relative to the start of a section<br/>
@param `Repeat` a name of the Repeat object the user has previously created<br/>
@param `x` int the x coordinate the user would like to place the object<br/>
@param `y` int the y coordinate the user would like to place the object<br/>
@param `starttime` int the time the user would like to place the object relative to the start of the section <br/>
</em>

<br/><br/>

**Add(Cell, int, int, int)** <br/>
<em>
// Add(Cell, x, y, starttime) <br/>
// Adds a previously made Cell object to a section at specified x, y, and start time relative to the start of a section<br/>
@param `Cell`  a name of the Cell object the user has previously created<br/>
@param `x` int the x coordinate the user would like to place the object at time t<br/>
@param `y` int the y coordinate the user would like to place the object at time t<br/>
@param `starttime` int the time the user would like to place the object relative to the start of the section <br/>
</em>

<br/><br/>

**Add(Function, int)** <br/>
<em>
// Add(Function, starttime) <br/>
// Adds a previously made repeat, Cell, or Function to a section at specified x, y, and start time relative to the start of a section<br/>
@param `Function`  a name of the Function object the user has previously created<br/>
@param `starttime` int the time the user would like to place the object relative to the start of the section <br/>
</em>

<br/><br/>

**AddSection(Section)** <br/>
<em>
// AddSection(Section)<br/>
@param `Section` a name of the Section object that the user has previously created to be added to the game. NOTE: sections are added sequentially<br/>
</em>

<br/><br/>

**Difficulty(int)** <br/>
<em>
// Difficulty(speed) <br/>
// constant that decides how much faster to go<br/>
@param `speed` int increases the speed of the entire game by this factor<br/>
</em>

<br/><br/>
### EXAMPLES:

* Example 1:
```
// Create a 10x10, 60secs long game.
CreateGame(10, 10, 60);

// Create a Cell named 'full' that has a fade out time of 1 sec, is blue, and gives 25 points when clicked.
Cell full = CreateCell(1, blue, 25); 

// Create a Cell named 'half' that fades out after 0.5secs, is colour #ff6347, and gives 50 points when clicked.
Cell half = CreateCell(0.5, #ff6347, 50);

// Create a Repeat named 'repeatHalf' that shows a Cell named 'half' 5 times within 30secs.
Repeat repeatHalf = CreateRepeat(half, 5, 30); 

// Create a Function named 'f' that makes a Cell named 'halfCell' to move from top left corner to the top right corner.
Function f = CreateFunction(t % 10, 0, halfCell); 

// Create a Section named 'a' that is 60secs long. Remember, a Section object is required to add a Cell, Function, or a Repeat object to the game.
Section a = CreateSection(60); 

// To a Section named 'a', add a Repeat named 'repeatHalf' at Grid position (5,1). Start executing the Repeat object after 0secs from the beginning of Section 'a'.
a.Add(repeatHalf, 5, 1, 0); 

// To a Section named 'a', add a Cell named 'full' at Grid position (0,1). Have this Cell shown after 20secs from the beginning of Section 'a'.
a.Add(full, 0, 1,  20); 

// To a Section named 'a', add a Function named 'f'. Start executing the Function object after 5 secs from the beginning of Section 'a'.
a.Add(f, 5); 

// Insert a Section named 'a' to the game.
AddSection(a); 

// Increment the speed by a factor of 3.
Difficulty(3);
```
* Example 2:
```
// Create a 10x10, 60secs long game, that plays 'no copyright music - lofi type beat biscuit free vlog music prod. by lukrembo.mp3' in the `bgm` folder in the background.
CreateGame(10, 10, 60, 'no copyright music - lofi type beat biscuit free vlog music prod. by lukrembo.mp3'); 

// Create a Cell named 'blue' that has a fade out time of 3secs, is blue, and gives 25 points when clicked.
Cell blue = CreateCell(3, blue, 25); 

// Create a Cell named 'red' that has a fade out time of 2secs, is red, and gives 50 points when clicked.
Cell red = CreateCell(2, red, 50); 

// Create a Function named 'f' that makes a Cell named 'red' to move diagonally from top left corner to the bottom right corner.
Function f = CreateFunction(t%10, t%10, red, 10); 

// Create a Section named 'a' that is 30secs long. Remember, a Section object is required to add a Cell, Function, or a Repeat object to the game.
Section a=CreateSection(30); 

// To a Section named 'a', add a Function named 'f'. Start executing the Function object after 5 secs from the beginning of Section 'a'.
a.Add(f, 5); 

// Insert a Section named 'a' to the game. This section would be executed for the first 30 secs of the game.
AddSection(a); 

// Create a Section named 'b' that is 10secs long. Remember, a Section object is required to add a Cell, Function, or a Repeat object to the game.
Section b = CreateSection(10); 

// To a Section named 'b', add a Cell named 'blue' at Grid position (5,1). Have this Cell shown after 2secs from the beginning of Section 'b'.
b.Add(blue, 5, 1, 2); 

// Insert a Section named 'b' to the game. This section would be executed from 30secs ~ 40secs of the game.
AddSection(b); 
```

Add entries to this file summarising each project milestone. Don't forget that these need to have been discussed with your TA before the milestone deadline; also, remember to commit and push them by then!

# Milestone 1:

## Gridy (Temporary Project Name)

### Prototype
![Prototype](https://github.students.cs.ubc.ca/cpsc410-2020w-t1/cpsc410_project1_team18/blob/master/images/gridy_example.png)

** NOTE: 't' is a unit of time **

### Description
Gridy provides the users a flexible and creative language, allowing them to create a grid-style game. Depending on how the user creates the game with Gridy, it may be a rhythm game or a whack-a-mole game or something completely new. 

Users can create a user-specified n x n grid and blocks, and set properties of the blocks (each square of the grid) such as the length of time they appear, color, key bindings, and points. Users can re-use the blocks they create and place them on the grid to appear at a specified time. Using a simple system of (x coordinate, y coordinate, time coordinate), users can also write functions to more easily create complex patterns for the appearance of blocks.

### TA Suggestions (2020-09-17):
- Give users the ability to specify the difficulty of the game (how fast squares change/move, etc.)
- Allow users to customize the point system (points per block,combo multipliers, etc.).
- Dynamically change the block’s point value based on time variable
- Make the language the highest priority while keeping the GUI simple
- Try to give as much as flexibility and space for programmability



# Milestone 2:

### Division of responsibilities:

Coming up with, and Assigning Tasks for Designing, writing, and implementing DSL language (David, Won Tae, Toji)
- Designing tokenizer
- Designing parser (need to have solid idea of features)
- Designing specifications for how to write the language
- Translating to json? <- Do we need JSON?
- Deciding format of information to be passed to the generator
- Consulting with people to get feedback
- Grammar
- Language specs
- Error handling

Coming up with, and Assigning Tasks for Designing and implementing GUI generator (Hee Su, Josh)
- Generate the grid and buttons
- Evaluator 
- Have squares fade in and out based on the imputed specs
- Optional: make a way for users to be able to quickly replay 10-20 second chunks of the stage they are making for quality of life - purposes
- Test the DSL and game as well

Everyone will work on some portion of the DSL. Few people will be assigned for GUI-related tasks.

### Road map of project:
- Sept 27 Definition of all objects and functions with parameters and initial language grammar
- Sept 29 Each team makes specific tickets for their topic
- Sept 28-31 Do a user survey to get feedback on our language and make changes to grammar as needed
- Oct 7 Basic gui generation, tokenizer, unit tests for tokenizer
- Oct 9 Complete Parser
- Oct 11 Complete Validation
- Oct 13 Complete Evaluation. Semi completed project (All your tickets should be ready for QA)
- Oct 13-15 Test edge cases, bug fixing and feature additions as needed
- Oct 15 take video presentation and final user case study




### Summary: 
Created a simple example scenario of when and how to use our language
Parameters for the different types in our language

### TA Feedback (2020-09-24):
- Focus more on the DSL language (have everyone working on some portion of DSL)
- Idea/Suggestion: Allow user to compete with an AI
- Add a more complex score system
- Create robust grammar and language parameters earlier on in the creation process
- Don’t forget error handling when making the language
- Make the language intuitive (i.e., the term 'Track' may confuse the users using the language)



# Milestone 3:
 
Language: Java 
 
## Mockup of our concrete language design (Revised version after User Study):
 
* Fixed literal - capitalize
* User strings - no cap at the beginning

**CreateGame(int, int, int, string)**<br/>
<em>
// CreateGame(grid_size x, grid_size y, duration, (optional: Absolute_path_to_music_file));<br/>
// creates a grid that is x by y with each coordinate representing a cell<br/>
@param grid_sizeX int the horizontal length<br/>
@param grid_sizeY int  the vertical length<br/>
@param duration int  how long the game will last in seconds, the game will terminate after this duration <br/>
@param optional_path string  a path to a music file which will play along with the game<br/>
</em>
 
<br/><br/>

**CreateCell(int, string, int)**<br/>
<em>
// CreateCell(FadeOutDuration, Color, Score);<br/>
// Creates a cell that has a fade out time, color, and score<br/>
@param FadeOutDuration int how long a cell lasts for on the screen<br/>
@param Color string what color the cell will be when it appears on screen<br/>
@param Score int How much a cell will be worth on hit<br/>
</em>

<br/><br/>

**CreateRepeat(string, int, int)** <br/>
<em>
// CreateRepeat(cell_name, frequency, duration); <br/>
@param cell_name string Cell to import<br/>
@param frequency int Number of times the cell will repeat to appear and fade out<br/>
@param duration int How long the cell would repeat for<br/>
</em>

<br/><br/>

**CreateFunction(string, string, string, int)** <br/>
<em>
// CreateFunction(mathEquation_x, mathEquation_y, cell_name, duration) <br/>
// manipulate x and y coords relative to t time in seconds for a specific cell you can use t as the reference time and change x and y based on t <br/>
@param mathEquation_x string Custom math equation for the x-coordinate<br/>
@param mathEquation_y string Custom math equation for the y-coordinate<br/>
@param cell_name string Cell to import<br/>
@param duration the duration of the function<br/>
</em>

<br/><br/>

**CreateSection(int)** <br/>
<em>
// CreateSection(length) <br/>
// Create a segment of the game (i.e., 30seconds of the game) <br/>
@param length Duration of a section <br/>
</em>

<br/><br/>

**Add(Repeat, int, int, int) | Add(Cell, int, int, int) | Add(Function, int)** <br/>
<em>
// Add(Repeat, x, y, starttime | Cell, x, y, starttime | Function, starttime) <br/>
// Adds a previously made repeat, Cell, or Function to a section at specified x, y, and start time relative to the start of a section<br/>
@param Repeat a Repeat object the user has previously created<br/>
@param Cell  a Cell object the user has previously created<br/>
@param Cell  a Function object the user has previously created<br/>
@param x int the x coordinate the user would like to place the object<br/>
@param y int the y coordinate the user would like to place the object<br/>
@param starttime int the time the user would like to place the object relative to the start of the section <br/>
</em>

<br/><br/>

**AddSection(Section)** <br/>
<em>
// AddSection(Section)<br/>
@param Section a section that the user has previously created to be added to the game NOTE: sections are added sequentially<br/>
</em>

<br/><br/>

**Difficulty(int)** <br/>
<em>
// Difficulty(speed) <br/>
// constant that decides how much faster to go<br/>
@param speed int increases the speed of the entire game by <br/>
</em>

### EXAMPLE:
```
CreateGame(10, 10, 60);
Cell full = CreateCell(1, blue, 25);
Cell half = CreateCell(0.5, #ff6347, 50);
Repeat repeatHalf = CreateRepeat(half, 5, 30);
Function f = CreateFunction(t % x.size, 0, halfCell);
Section a = CreateSection(60);
a.Add(repeatHalf, 5, 1, 0);
a.Add(full, 0, 1,  20);
a.Add(f, 5);
AddSection(a);
Difficulty(3);
```

```
CreateGame(10, 10, 60, 'no copyright music - lofi type beat biscuit free vlog music prod. by lukrembo.mp3');
Cell blue = CreateCell(3, blue, 25);
Cell red = CreateCell(2, red, 50);
Repeat repeatHalf = CreateRepeat(blue, 5, 30);
Function f = CreateFunction(t % 135, 0, red, 10);
Section a=CreateSection(60);
a.Add(red, 5, 1, 0);
a.Add(blue, 0, 1,  20);
a.Add(f, 5);
Section b = CreateSection(10);
b.Add(red, 5, 1, 2);
AddSection(a);
AddSection(b);
```

![Prototype](https://github.students.cs.ubc.ca/cpsc410-2020w-t1/cpsc410_project1_team18/blob/master/images/gridy_example.png)

** NOTE: 't' is a unit of time **

## Notes from the First User Study

* Create game using the base example template, made a mistake copying exactly
* Changed the x, y back to the ones asked
* Added the file path seamlessly
* Needed to specify variable names in the instruction
* Had question about the fading in and out of the cell
* We addressed this issue by adding more descriptions to the inputted variable in our documentation
* Copied the pasted the first create cell and made the full block
* Asked question about if duration is in seconds (add units to documentation)
* Figuring out syntax for Function
* So t time should be a lamda? We use it every time we create a function
* Had a question about section and what duration meant in the context of the whole game
* Says he needs a function (why?) clarify that he will not use function in this user study
* Slight confusion about duration in the CreateRepeat function
* We addressed this issue by adding more descriptions to the inputted variable in our documentation
* Says that everything in a section runs in parallel can add multiple things to a section
* Changed variable names from describing cell timings to colors
* Asks if he can add a cell to a specific offset at a specific place, read documentation and figures it out
* Successfully adds green to the section b no problem
* Successfully adds sections to the game
* Took about 15 min total
 
### Review from user
 
The function stuff still doesn't make sense but other than that it feels like a java API or something.  Documentation is lacking, section, repeat terminology feels vague proper documentation might help a lot. Diagrams within the documentation will help.

### TA Feedback

* For difficulty, add more ways to reward players, allow writers to do more with difficulty?
* Add feature of adding rules to the game
* rule : trigger, condition, action

 
## Any changes to the Original Design
* Added more detailed documentation for our grammar and inputted variables
* We created a concrete design for how the language will be written (Java style)
* Added diagrams in the documentation to enhance language comprehension
* Modified few parameter and method names to be more intuitive



# Milestone 4 contents 

* Status of implementation
* Plans for final user study
* Planned timeline for the remaining days




 
## TEAMS:

**Objects:** Toji 

**Parser/Evaluator:** David, Hee Su

**GUI:** Josh, Won Tae, (David)
 
## Plans for Final User Study:
* After full implementation, find a new person who hasn’t been exposed to Gridy yet.

* Have the user write Gridy in the input.tcts file and run the program (i.e., Main.java).

* Follow the exact same user study procedure as the first user study.

* Observe the differences and analyze the results.

## Status of Implementation (✔) & Planned Timeline (×):

✔	Sept 27 Definition of all objects and functions with parameters and initial language grammar

✔	Sept 29 Specific tickets are made for each portion of the project

✔	Sept 28-31 Do a user survey to get feedback on our language and make changes to grammar as needed

✔	Oct 7 Basic gui generation, tokenizer, unit tests for tokenizer

✔	Oct 9 Complete Parser

✔	Oct 11 Complete Validation & Main portion of the GUI

✔	Oct 13 Complete Evaluation. Semi completed project (All your tickets should be ready for QA)

✔	Oct 13-15 Test edge cases, bug fixing and feature additions as needed

✔	Oct 15 take video presentation and final user case study

✔	Oct 19 9am due


## Second user study 

## user 2 

* Still had problem with understanding what a Section was
* Documentation: Add(Function..)|Add(Cell..)|Add(Repeat) wasn't clear; she thought everything was one
* Didn't realize you had to put Repeat&Function within a Section to make those appear
* Documentation wasn't clear for Add(Cell ...) ; she thought she had to put a Cell object in somehow, not the cell name


###  Changes:
* Added detailed description within the examples of what each line does
* Separated Add methods into 3 sections as it should be
* Mentioned on the document that everything needs to be added to a Section and that there at least needs to be one Section in a game
* Changed parameter name from Cell to Cell_name


## user 3
* Clarified that in the documentation it says users should make object sequentially game -> cells -> repeats -> functions -> sections
* Seems to prefer coding it off the example rather than the documentation
* made a section for each part to be added, clarified that a section can hold many thing in it including functions and repeats
* confused by how to use the equation x,y in createFunction and the syntax of that
### changes


* updated description of frequency variable to be time in seconds before next cell appears rather then number of times it appears
* added example use cases of createFunction to show the user an example of it

Review:

Takes a while to get used to the grammar of it but after the first of each object is figured out the rest is fast. I think translations are a bit hard to understand with the use of mod but an example of a few different types of translations should be enough to let someone be able to use it without having to understand it fully.


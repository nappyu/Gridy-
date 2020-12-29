package ast;

import libs.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String NAME = "[a-z][a-zA-Z0-9]*"; // this is user created variable names
    private static final String MUSIC_FILE = "^[A-Za-z0-9+\\-\\*/%~\\s!\\.]+\\.(?:mp3|wav)";
    private static final String NUMBER = "[0-9.]+"; // integer constants
    private static final String COLOR = "^[#]*[a-zA-Z0-9]+"; // the color constants. This is how we will enforce users to enforce specific color inputs
    private static final String EQUATION = "[t + \\- \\* / % 0-9]*(x\\.size|y\\.size)*";

    private final Tokenizer tokenizer;

    public static Parser getParser(Tokenizer tokenizer) {return new Parser(tokenizer);}

    private Parser(Tokenizer tokenizer) {this.tokenizer = tokenizer;}

    // PROGRAM ::= STATEMENTS*
    public Program parseProgram() {
        List<Statement> statements = new ArrayList<>();
        while (tokenizer.moreTokens()) {
            statements.add(parseStatement());
        }
        return new Program(statements);
    }

    // STATEMENT ::= GEXP | DECLARE | ADD
    private Statement parseStatement() {
        if (tokenizer.checkToken("CreateGame") || tokenizer.checkToken("AddSection") || tokenizer.checkToken("Difficulty")) {
            return parseGEXP();
        }
        else if (tokenizer.checkToken("Cell") || tokenizer.checkToken("Repeat") || tokenizer.checkToken("Function") || tokenizer.checkToken("Section")) {
            return parseDec();
        }
        else if (tokenizer.checkToken(NAME)) {
            return parseAdd();
        } else {
            throw new RuntimeException("Unknown Statement: " + tokenizer.getNext());
        }
    }

    // GEXP ::= GCREATE | ADDSECT | DIFFICULTY
    private GEXP parseGEXP() {
        if (tokenizer.checkToken("CreateGame")) {
            return parseGCreate();
        }
        else if (tokenizer.checkToken("AddSection")) {
            return parseAddSect();
        }
        else if (tokenizer.checkToken("Difficulty")) {
            return parseDifficulty();
        } else {
            throw new RuntimeException("Unknown GEXP: " + tokenizer.getNext());
        }

    }

    // GCREATE ::= "CreateGame"(NUMBER, NUMBER, NUMBER, MUSIC_FILE*);
    private GCreate parseGCreate() {
        tokenizer.getAndCheckNext("CreateGame");
        String x = tokenizer.getAndCheckNext(NUMBER);
        String y = tokenizer.getAndCheckNext(NUMBER);
        String duration = tokenizer.getAndCheckNext(NUMBER);
        if (tokenizer.checkToken(MUSIC_FILE)) {
            String directory = tokenizer.getAndCheckNext(MUSIC_FILE);
            tokenizer.getAndCheckNext(";"); // We expect line ending here
            return new GCreate(x, y, duration, directory);
        }
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new GCreate(x, y, duration);
    }

    // ADDSECT ::= AddSection(NAME);
    private AddSection parseAddSect() {
        tokenizer.getAndCheckNext("AddSection");
        String name = tokenizer.getAndCheckNext(NAME);
        if (tokenizer.checkToken(NUMBER)) {
            String startTime = tokenizer.getAndCheckNext(NUMBER);
            tokenizer.getAndCheckNext(";"); // We expect line ending here
            return new AddSection(name, startTime);
        }
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new AddSection(name);
    }

    // DIFFICULTY ::= Difficulty(LEVEL); --- LEVEL is just integer 1-9
    private Difficulty parseDifficulty() {
        tokenizer.getAndCheckNext("Difficulty");
        String level = tokenizer.getAndCheckNext("[1-9]");
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new Difficulty(level);
    }

    // DECLARE ::= DECCELL | DECREPEAT | DECFUNCTION | DECSECTION
    private Dec parseDec() {
        if (tokenizer.checkToken("Cell")) {
            return parseDecCell();
        }
        else if (tokenizer.checkToken("Repeat")) {
            return parseDecRepeat();
        }
        else if (tokenizer.checkToken("Function")) {
            return parseDecFunction();
        }
        else if (tokenizer.checkToken("Section")) {
            return parseDecSection();
        }
        else {
            throw new RuntimeException("Unknown Declaration: " + tokenizer.getNext());
        }
    }

    // DECCELL ::= Cell NAME = CreateCell(NUMBER, COLOR, NUMBER); // throw errors for color that's not in our pre-set
    private DecCell parseDecCell() {
        tokenizer.getAndCheckNext("Cell");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext("CreateCell");
        String duration = tokenizer.getAndCheckNext(NUMBER);
        String color = tokenizer.getAndCheckNext(COLOR);
        String score = tokenizer.getAndCheckNext(NUMBER);
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new DecCell(name, duration,color, score);
    }

    // DECREPEAT ::= Repeat NAME = CreateRepeat(NAME, NUMBER, NUMBER);
    private DecRepeat parseDecRepeat() {
        tokenizer.getAndCheckNext("Repeat");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext("CreateRepeat");
        String cellName = tokenizer.getAndCheckNext(NAME);
        String freq = tokenizer.getAndCheckNext(NUMBER);
        String duration = tokenizer.getAndCheckNext(NUMBER);
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new DecRepeat(name, cellName, freq, duration);
    }

    // DECFUNCTION ::= Function NAME = CreateFunction(EQUATION, EQUATION, NAME, NUMBER);
    private DecFunction parseDecFunction() {
        tokenizer.getAndCheckNext("Function");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext("CreateFunction");
        String xEquation = tokenizer.getAndCheckNext(EQUATION);
        String yEquation = tokenizer.getAndCheckNext(EQUATION);
        String cellName = tokenizer.getAndCheckNext(NAME);
        String duration = tokenizer.getAndCheckNext(NUMBER);
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new DecFunction(name, xEquation, yEquation, cellName, duration);
    }

    // DECSECTION ::= Section NAME = CreateSection(NUMBER);
    private DecSection parseDecSection() {
        tokenizer.getAndCheckNext("Section");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext("CreateSection");
        String duration = tokenizer.getAndCheckNext(NUMBER);
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new DecSection(name, duration);
    }

    // ADD ::= ADDSIMP | ADDFUNC
    private Add parseAdd() {
        String sectionName = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext("Add");
        String name = tokenizer.getAndCheckNext(NAME);
        String firstNum = tokenizer.getAndCheckNext(NUMBER);
        if (tokenizer.checkToken(NUMBER)) {
            return parseAddSimp(sectionName, name, firstNum);
        } else {
            return parseAddFunc(sectionName, name, firstNum);
        }
    }

    // ADDSIMP ::= NAME[.]Add(NAME,NUMBER, NUMBER, NUMBER);
    private AddSimp parseAddSimp(String sectionName, String name, String firstNum) {
        String y = tokenizer.getAndCheckNext(NUMBER);
        String startTime = tokenizer.getAndCheckNext(NUMBER);
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new AddSimp(sectionName, name, firstNum, y, startTime);
    }

    // ADDFUNC ::= NAME[.]Add(NAME, NUMBER);
    private AddFunc parseAddFunc(String sectionName, String name, String startTime) {
        tokenizer.getAndCheckNext(";"); // We expect line ending here
        return new AddFunc(sectionName, name, startTime);
    }

}

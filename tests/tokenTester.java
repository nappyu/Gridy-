import org.junit.Assert;
import org.junit.Test;
import libs.SimpleTokenizer;
import libs.Tokenizer;

import java.util.Arrays;
import java.util.List;

public class tokenTester {

    @Test
    public void tsingleCell() {
        List<String> fixedLiterals = Arrays.asList(";", "\n", "CreateGame", "Cell", "CreateCell", "Repeat", "CreateRepeat",
                "Function", "CreateFunction", "Section", "CreateSection", "Add", "AddSection", "Difficulty");
        //Tokenizer tokenizer = SimpleTokenizer.createSimpleTokenizer("tsingleCell",fixedLiterals);

        String[] ans = new String[] {  "CreateGame", "10",  "10", "60", "filepath", ";",
                "Cell", "full", "CreateCell", "1", "blue" ,  "25", ";",
                "Section", "a","CreateSection", "60", ";",
                "a", "Add","full",  "0" , "1", "20", ";",
                "AddSection", "a", ";",
                "Difficulty", "3", ";"  };

       // Assert.assertArrayEquals(ans, tokenizer.getTokens());

    }

    @Test
    public void fmissingSemiColon() {
        List<String> fixedLiterals = Arrays.asList(";", "\n", "CreateGame", "Cell", "CreateCell", "Repeat", "CreateRepeat",
                "Function", "CreateFunction", "Section", "CreateSection", "Add", "AddSection", "Difficulty");
       // Tokenizer tokenizer = SimpleTokenizer.createSimpleTokenizer("./test/tokenizerTests/fmissingSemiColon.txt",fixedLiterals);


    }
}
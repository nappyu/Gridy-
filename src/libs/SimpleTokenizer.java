package libs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleTokenizer implements libs.Tokenizer {

    public static libs.Tokenizer createSimpleTokenizer(String filename)
    {
        return new SimpleTokenizer(filename);
    }
    private static final String RESERVEDWORD = "_";

    private String inputProgram;
    private String[] tokens;
    private int currentToken = 0;

    public String[] getTokens() {
        System.out.println(Arrays.toString(tokens));
        return tokens;
    }

    private SimpleTokenizer(String filename){
        try {
            inputProgram = Files.readString(Paths.get(filename));
        } catch (IOException e) {
            //System.out.println("Didn't find file");
            System.exit(0);
        }
        tokenize();
    }

    //modifies: this.tokens
    //effects: will result in a list of tokens (sitting at this.tokens) that has no spaces around tokens.
    private void tokenize (){
        String tokenizedProgram = inputProgram;
        //System.out.println("INITIAL INPUT: "+ tokenizedProgram);

        // Split the input string around the separators
        tokenizedProgram = tokenizedProgram.trim();
        tokenizedProgram = tokenizedProgram.replace("\n", "");
        tokenizedProgram = tokenizedProgram.replace(";", "_;_");

        tokens = tokenizedProgram.split(RESERVEDWORD);

        try {
            while (!checkNext().equals("NO_MORE_TOKENS")) {
                // First, trim whitespaces of the current token
                tokens[currentToken] = tokens[currentToken].trim();
                String currToken = tokens[currentToken];

                // Check whether it starts with a fixed literal token "CreateGame", "Cell", "CreateCell", "Repeat", "CreateRepeat",
                //    "Function", "CreateFunction", "Section", "CreateSection", "Add", "AddSection", or "Difficulty"
                if (checkToken("^(CreateGame|CreateCell|CreateRepeat|CreateFunction|CreateSection|Add|AddSection|Difficulty).*")) {
                    // If starts with one of these fixed literal tokens, split into that and remainder.
                    String[] fixedLiteralBody = currToken.split("[()]");
                    String[] params = fixedLiteralBody[1].split(",");

                    // Convert to ArrayList then back for easy manipulation of addition/deletion
                    ArrayList<String> temp = new ArrayList<>();
                    Collections.addAll(temp, tokens);
                    temp.remove(currentToken);

                    for (int i = params.length - 1; i >= 0 ; i--) {
                        String param = params[i].trim();
                        param = param.replaceAll("['\"]", "");
                        temp.add(currentToken, param);
                    }

                    temp.add(currentToken, fixedLiteralBody[0].trim());

                    String[] tempTokens = new String[temp.size()];
                    tempTokens = temp.toArray(tempTokens);

                    tokens = tempTokens;

                    currentToken = currentToken + params.length + 1;
                } else if (checkToken("^(Cell|Repeat|Function|Section).*")) {
                    // If starts with one of these fixed literal tokens, split into that, variable name, and remainder.
                    String[] fixedLiteralBody = currToken.split("[=|\\s+]");
                    String fixedLiteral = fixedLiteralBody[0].trim();
                    String rest = currToken.substring(fixedLiteral.length());
                    String[] params = rest.split("=");

                    // Convert to ArrayList then back for easy manipulation of addition/deletion
                    ArrayList<String> temp = new ArrayList<>();
                    Collections.addAll(temp, tokens);
                    temp.remove(currentToken);

                    for (int i = params.length - 1; i >= 0 ; i--) {
                        String param = params[i].trim();
                        temp.add(currentToken, param);
                    }
                    temp.add(currentToken, fixedLiteralBody[0].trim());

                    String[] tempTokens = new String[temp.size()];
                    tempTokens = temp.toArray(tempTokens);

                    tokens = tempTokens;

                    currentToken = currentToken + params.length;
                } else if (currToken.equals(";")){
                    getNext();
                } else {
                    String[] body = currToken.split("\\.");

                    // Convert to ArrayList then back for easy manipulation of addition/deletion
                    ArrayList<String> temp = new ArrayList<>();
                    Collections.addAll(temp, tokens);
                    temp.remove(currentToken);

                    temp.add(currentToken, body[1].trim());
                    temp.add(currentToken, body[0].trim());

                    String[] tempTokens = new String[temp.size()];
                    tempTokens = temp.toArray(tempTokens);

                    tokens = tempTokens;

                    getNext();
                }
            }
        } catch (Exception e) {
            System.out.println("TOKEN PARSING ERROR: "+ e);
            return;
        }

        currentToken=0;
        System.out.println("FINAL TOKENS: "+ Arrays.asList(tokens));
    }

    private String checkNext(){
        String token="";
        if (currentToken<tokens.length){
            token = tokens[currentToken];
        }
        else
            token="NO_MORE_TOKENS";
        return token;
    }

    @Override
    public String getNext(){
        String token="";
        if (currentToken<tokens.length){
            token = tokens[currentToken];
            currentToken++;
        }
        else
            token="NULLTOKEN";
        return token;
    }


    @Override
    public boolean checkToken(String regexp){
        String s = checkNext();
//        System.out.println("comparing: |"+s+"|  to  |"+regexp+"|");
        return (s.matches(regexp));
    }


    @Override
    public String getAndCheckNext(String regexp){
        String s = getNext();
        if (!s.matches(regexp)) {
            throw new RuntimeException("Unexpected next token for Parsing! Expected something matching: " + regexp + " but got: " + s);
        }
//        System.out.println("matched: "+s+"  to  "+regexp);
        return s;
    }

    @Override
    public boolean moreTokens(){
        return currentToken<tokens.length;
    }

}

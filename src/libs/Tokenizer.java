package libs;

public interface Tokenizer {
    String getNext();

    String[] getTokens();

    boolean checkToken(String regexp);

    String getAndCheckNext(String regexp);

    boolean moreTokens();
}

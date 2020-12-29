package libs;

import ast.Visitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public abstract class Node {
    static protected PrintWriter writer; //in case you need to write something to a file!
    public static void setWriter(String name) throws FileNotFoundException, UnsupportedEncodingException {
        writer = new PrintWriter(name, "UTF-8");
    }
    public static void closeWriter(){
        writer.close();
    }

    //abstract public void evaluate();
    abstract public <T> T accept(Visitor<T> v);
}

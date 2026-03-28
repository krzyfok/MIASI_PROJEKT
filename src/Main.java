import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CharStream inp = null;

        try {

            inp = CharStreams.fromFileName("we.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gramatykaLexer lex = new gramatykaLexer(inp);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        gramatykaParser par = new gramatykaParser(tokens);

        ParseTree tree = par.program();
        STGroup group = new STGroupFile("template.stg");

        BashGeneratorVisitor em = new BashGeneratorVisitor(group);
        ST res = em.visit(tree);


        System.out.println(res.render());

        try {
            FileWriter wr = new FileWriter("wy.sh");
            wr.write(res.render());
            wr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

public class BashGeneratorVisitor extends gramatykaBaseVisitor<ST> {

    private final STGroup stGroup;


    public BashGeneratorVisitor(STGroup group) {
        super();
        this.stGroup = group;
    }

    @Override
    public ST visitProgram(gramatykaParser.ProgramContext ctx) {
        ST scriptST = stGroup.getInstanceOf("script");

        for (gramatykaParser.StatContext statCtx : ctx.stat()) {
            ST result = visit(statCtx);
            if (result != null) {
                scriptST.add("statements", result);
            }
        }
        return scriptST;
    }

    @Override
    public ST visitCopyCmd(gramatykaParser.CopyCmdContext ctx) {
        ST st = stGroup.getInstanceOf("copy");
        String flaga = (ctx.typ != null && ctx.typ.getText().equals("katalog")) ? "-r " : "";

        st.add("flag", flaga);
        st.add("source", ctx.zrodlo.getText());
        st.add("dest", ctx.cel.getText());
        return st;
    }

    @Override
    public ST visitDeleteCmd(gramatykaParser.DeleteCmdContext ctx) {
        ST st = stGroup.getInstanceOf("delete");
        String flaga = (ctx.typ != null && ctx.typ.getText().equals("katalog")) ? "-rf " : "";

        st.add("flag", flaga);
        st.add("target", ctx.cel.getText());
        return st;
    }

    @Override
    public ST visitMoveCmd(gramatykaParser.MoveCmdContext ctx) {
        ST st = stGroup.getInstanceOf("move");
        st.add("source", ctx.zrodlo.getText());
        st.add("dest", ctx.cel.getText());
        return st;
    }

    @Override
    public ST visitCreateCmd(gramatykaParser.CreateCmdContext ctx) {
        ST st;
        if (ctx.typ.getText().equals("katalog")) {
            st = stGroup.getInstanceOf("create_dir");
        } else {
            st = stGroup.getInstanceOf("create_file");
        }
        st.add("target", ctx.cel.getText());
        return st;
    }

    @Override
    public ST visitListCmd(gramatykaParser.ListCmdContext ctx) {
        ST st = stGroup.getInstanceOf("list");
        if (ctx.cel != null) {
            st.add("target", ctx.cel.getText());
        }
        return st;
    }

    @Override
    public ST visitPrintCmd(gramatykaParser.PrintCmdContext ctx) {
        ST st = stGroup.getInstanceOf("print");
        st.add("text", ctx.tresc.getText());
        return st;
    }

    @Override
    public ST visitLoopCmd(gramatykaParser.LoopCmdContext ctx) {
        ST st = stGroup.getInstanceOf("loop");
        st.add("count", ctx.ile.getText());

        for (gramatykaParser.StatContext statCtx : ctx.stat()) {
            st.add("statements", visit(statCtx));
        }
        return st;
    }
}
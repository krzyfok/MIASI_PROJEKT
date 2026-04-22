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

    @Override
    public ST visitCdirCmd(gramatykaParser.CdirCmdContext ctx) {
        ST st = switch (ctx.typ.getText()) {
            case "katalog" -> stGroup.getInstanceOf("open_dir");
            case "plik" -> stGroup.getInstanceOf("open_file");
            default -> throw new RuntimeException("Otwarcie wymaga sprecyzowania typu (plik | katalog)!");

        };
        st.add("target", ctx.cel.getText());
        return st;
    }
    @Override
    public ST visitLdirCmd(gramatykaParser.LdirCmdContext ctx) {
        return stGroup.getInstanceOf("leave_dir");
    }

    @Override
    public ST visitPwd(gramatykaParser.PwdContext ctx) {
        return stGroup.getInstanceOf("location_me");
    }

    @Override
    public ST visitLocate_file(gramatykaParser.Locate_fileContext ctx) {
        ST st = stGroup.getInstanceOf("location_file");
        st.add("target", ctx.cel.getText());

        return st;
    }

    @Override
    public ST visitNumerateCmd(gramatykaParser.NumerateCmdContext ctx) {
        ST st = stGroup.getInstanceOf("numerate");
        String raw = ctx.cel.getText();
        String pattern = raw.substring(1,raw.length()-1);
        st.add("pattern", pattern);
        return st;
    }

    @Override
    public ST visitDefMacroCmd(gramatykaParser.DefMacroCmdContext ctx) {
        ST st = stGroup.getInstanceOf("def_macro");

        st.add("name", ctx.name.getText());
        for (gramatykaParser.StatContext statCtx : ctx.stat()) {
            st.add("statements", visit(statCtx));
        }
        return st;
    }

    @Override
    public ST visitCallMacroCmd(gramatykaParser.CallMacroCmdContext ctx) {
        ST st = stGroup.getInstanceOf("call_macro");

        st.add("name", ctx.name.getText());
        return st;
    }

    @Override
    public ST visitHealthCmd(gramatykaParser.HealthCmdContext ctx) {
        return stGroup.getInstanceOf("system_health");
    }

    @Override
    public ST visitBackupCmd(gramatykaParser.BackupCmdContext ctx) {
        ST st = stGroup.getInstanceOf("backup");
        st.add("target", ctx.cel.getText());
        return st;
    }

    @Override
    public ST visitCleanupCmd(gramatykaParser.CleanupCmdContext ctx) {
        return stGroup.getInstanceOf("cleanup");
    }
}
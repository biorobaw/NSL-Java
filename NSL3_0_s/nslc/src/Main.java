//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class Main {
    public Main() {
    }

    public static void main(String[] var0) {
        NslParser var1 = new NslParser(System.in);

        try {
            ASTCompilationUnit var2 = var1.CompilationUnit();
            if (!NslParser.error) {
                ModifyVisitor var3 = new ModifyVisitor();
                var2.jjtAccept(var3, null);
            }
        } catch (Exception var4) {
            System.err.println(var4.getMessage());
            var4.printStackTrace();
        }

    }
}

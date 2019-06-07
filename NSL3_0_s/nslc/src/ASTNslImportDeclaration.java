/* Generated By:JJTree: Do not edit this line. ASTNslImportDeclaration.java */

/* SCCS  %W--- %G% -- %U% */

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

public class ASTNslImportDeclaration extends SimpleNode {

    private boolean globing = false;

    public ASTNslImportDeclaration(int id) {
        super(id);
    }

    public ASTNslImportDeclaration(NslParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor.
     **/
    public Object jjtAccept(NslParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public boolean getGlobing() {
        return globing;
    }

    public void setGlobing(boolean value) {
        globing = value;
    }

    public String getImportName() {
        ASTName nameNode = (ASTName) jjtGetChild(0);
        return nameNode.getName();
    }

    public String toJava(NslScope scope) {

        ASTName nameNode = (ASTName) jjtGetChild(0);

        String importName = getImportName();
        if (!importName.equals("nslAllImports")) {
            first.image = "import " + importName + (globing ? ".*" : "");
            first.next = last;
        } else {
            first.image = "/*nslAllImports*/";
            first.next = last.next;
        }
        return first.image;
    }

    public void updateScope(NslScope scope) {
        String importName = getImportName();
        if (!importName.equals("nslAllImports")) {
            if (globing) {
                scope.addImportPackage(importName);
            } else {
                NslCompiler.parseType(importName, null);
            }
        }
    }

    public String toXMLOpen(String prefix) {
        String name = getImportName();
        return prefix + "<" + toString() + ">";
    }

}

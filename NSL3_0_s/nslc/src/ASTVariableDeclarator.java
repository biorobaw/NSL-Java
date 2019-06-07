/* Generated By:JJTree: Do not edit this line. ASTVariableDeclarator.java */

/* SCCS  %W--- %G% -- %U% */

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

public class ASTVariableDeclarator extends SimpleNode {
    public ASTVariableDeclarator(int id) {
        super(id);
    }

    public ASTVariableDeclarator(NslParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor.
     **/
    public Object jjtAccept(NslParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public String getName() {
        return ((ASTVariableDeclaratorId) jjtGetChild(0)).getName();
    }

    public String getTypeSuffix() {
        return ((ASTVariableDeclaratorId) jjtGetChild(0)).getTypeSuffix();
    }

    public String getVariableInitializer() {
        if (jjtGetNumChildren() > 1) {
            return ((ASTVariableInitializer) jjtGetChild(1)).getVariableInitializer();
        } else {
            return null;
        }
    }

}

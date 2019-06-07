/* Generated By:JJTree: Do not edit this line. ASTVariableInitializer.java */

/* SCCS  %W--- %G% -- %U% */

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

public class ASTVariableInitializer extends SimpleNode {
    public ASTVariableInitializer(int id) {
        super(id);
    }

    public ASTVariableInitializer(NslParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor.
     **/
    public Object jjtAccept(NslParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public String getVariableInitializer() {
        SimpleNode node = (SimpleNode) jjtGetChild(0);

        if (node instanceof ASTExpression) {
            return ((ASTExpression) node).getExpression();
        } else {
            return null;
        }
    }

}

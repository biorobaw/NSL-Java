/* Generated By:JJTree: Do not edit this line. ASTPackageDeclaration.java */

/* SCCS  %W--- %G% -- %U% */

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

public class ASTPackageDeclaration extends SimpleNode {
    public ASTPackageDeclaration(int id) {
        super(id);
    }

    public ASTPackageDeclaration(NslParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor.
     **/
    public Object jjtAccept(NslParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public String getPackageName() {
        ASTName nameNode = (ASTName) jjtGetChild(0);
        return nameNode.getName();
    }

    public String toXMLOpen(String prefix) {
        String name = getPackageName();
        return prefix + "<" + toString() + ">";
    }
}

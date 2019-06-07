/* Generated By:JJTree: Do not edit this line. ASTUnaryExpression.java */

/* SCCS  %W--- %G% -- %U% */

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

public class ASTUnaryExpression extends ASTExpression {
    public ASTUnaryExpression(int id) {
        super(id);
    }

    public ASTUnaryExpression(NslParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor.
     **/
    public Object jjtAccept(NslParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public String getOperator() {
        SimpleNode node = (SimpleNode) jjtGetChild(0);

        if (!(node instanceof ASTUnaryExpression)) {
            return "";
        }
        return first.image;
    }

    public String getExpressionType() {
        ASTExpression expr = (ASTExpression) jjtGetChild(0);
        return expr.getExpressionType();
    }

    public String getNslElemNegHeader(boolean isNsl, String temp) {
        String assign = (isNsl ? ".setReference(" : " = (");
        String get = (isNsl ? ".get(), " : ", ");
        return temp + assign + "NslSub.eval(" + temp + get + "0, ";
    }

    public String toJava(NslScope scope) {

        SimpleNode node = (SimpleNode) jjtGetChild(0);

        if (!(node instanceof ASTUnaryExpression)) {
            return "";
        }

        String operator = getOperator();

        if (operator.equals("-")) {
            String type, mults;
            ASTExpression oper;
            int dim;
            boolean isArray;

            oper = (ASTExpression) node;
            type = oper.getExpressionType();
            dim = scope.getDim(type);
            isArray = dim > 0;
            mults = oper.getExpression();

            if (isArray) {
                mults = getNslElemNegHeader(scope.isNslType(type), scope.getTempName(type)) + mults + "))";
            } else if (scope.isNslType(type)) {
                String ext = ".get()",
                        temp = scope.getTempName(type),
                        wrapStart = temp + ".setReference(",
                        wrapEnd = ")";
                mults = wrapStart + "-" + mults + ext + wrapEnd;
            } else {
                mults = "-" + mults;
            }

            expressionType = type;

            first.image = mults;
            last.image = "";
            first.next = last;
        }

        return "";
    }

    public String toXMLOpen(String prefix) {

        String operator = (getOperator().equals("") ? "" : " operator=\"" + getOperator() + "\"");

        return prefix + "<" + toString() + operator + ">";
    }
}

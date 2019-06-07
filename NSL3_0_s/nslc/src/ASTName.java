/* Generated By:JJTree: Do not edit this line. ASTName.java */

/* SCCS  %W--- %G% -- %U% */

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

import java.util.Vector;

@SuppressWarnings("Duplicates")
public class ASTName extends SimpleNode {

    String name;
    String originalName = null;
    boolean nslVariable = false;
    boolean isMethod = false;
    String variableType = null;
    NslScope methodScope = null;
    String methodName = null;

    public ASTName(int id) {
        super(id);
    }

    public ASTName(NslParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor.
     **/
    public Object jjtAccept(NslParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public String getName() {
        Token t = first;
        Token end = last;

        name = t.image;
        while (t != end) {
            t = t.next;
            name += t.image;
        }

        return name;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isMethodName() {
        return isMethod;
    }

    public boolean isNslVariable() {
        return nslVariable;
    }

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(String type) {
        variableType = type;
    }

    public NslScope getMethodScope() {
        return methodScope;
    }

    public String[] getNames(String name) {
        Vector<String> names = new Vector<>();
        String temp;

        int start = 0, pos;
        while ((pos = name.indexOf(".", start)) >= 0) {
            temp = name.substring(start, pos);
            names.addElement(temp);
            start = pos + 1;
        }
        temp = name.substring(start);
        names.addElement(temp);

        int num = names.size();
        String[] result = new String[num];
        for (int i = 0; i < num; i++) {
            result[i] = names.elementAt(i);
        }

        return result;
    }

    public void updateScope(NslScope scope) {

        if (!(parent.getClass().getName().equals("ASTPrimaryPrefix") || parent.getClass().getName().equals("ASTType"))) {
            return;
        }

        String variableName = getName();

        String[] names = getNames(variableName);

        NslVariable temp = scope.resolveVar(names[0]);
        NslMethod method;

        int pos = 1;
        if (temp == null) {
            // Check if it is a method
            method = scope.resolveMethod(names[0]);
            if (method != null) {
                methodName = names[0];
                methodScope = scope;
                isMethod = true;
                return;
            }
            // check if it is a type
            String fullName;
            String type = names[0];
            boolean found = false;
            for (int i = 1; i < names.length && !found; i++) {
                NslScope tempScope = NslCompiler.parseType(type, scope.getImportList());
                if (tempScope != null) {
                    variableType = tempScope.getClassName();
                    found = true;
                    pos = i;
                }
                type += "." + names[i];
            }
            if (!found) {
                NslScope tempScope = NslCompiler.parseType(type, scope.getImportList());
                if (tempScope != null) {
                    variableType = tempScope.getClassName(); //fullName;
                    found = true;
                    pos = names.length;
                } else {
                    // check if it is a function name
                    NslCompiler.printError("ASTName", "Type " + type + " was not found.", getFirstToken().beginLine, getFirstToken().beginColumn);
                    return;
                }
            }
        } else {
            variableType = temp.getType();
            nslVariable = scope.isNslType(variableType);
            String tempType = variableType;
            if (variableType.contains("[")) {
                tempType = variableType.substring(0, variableType.indexOf("["));
            }
            if (scope.isPrimitive(tempType)) {
                NslCompiler.parseType(tempType, scope.getImportList());
            }
        }

        NslScope localScope;

        for (int i = pos; i < names.length; i++) {
            if (variableType.contains("[")) {
                if (!names[i].equals("length")) {
                    NslCompiler.printError("ASTName", "Type " + variableType + " is an array. Trying to access something different than length", getFirstToken().beginLine, getFirstToken().beginColumn);
                    return;
                } else {
                    variableType = "int";
                    break;
                }
            }
            localScope = scope.resolveClass(variableType);
            if (localScope == null) {
                NslCompiler.printError("ASTName", "Type " + variableType + " was not found.", getFirstToken().beginLine, getFirstToken().beginColumn);
                return;
            }
            temp = localScope.resolveVar(names[i]);
            if (temp == null) {
                // check if it is a function name
                method = localScope.resolveMethod(names[i]);
                if (method == null) {
                    NslCompiler.printError("ASTName", "Variable " + names[i] + " was not found in class " + localScope.getClassName(), getFirstToken().beginLine, getFirstToken().beginColumn);
                    return;
                } else {
                    methodName = names[i];
                    methodScope = localScope;
                    isMethod = true;
                    return;
                }
            } else {
                variableType = temp.getType();
                variableType = temp.getType();
                nslVariable = scope.isNslType(variableType);
                String tempType = variableType;
                if (variableType.contains("[")) {
                    tempType = variableType.substring(0, variableType.indexOf("["));
                }
                if (scope.isPrimitive(tempType)) {
                    NslCompiler.parseType(tempType, scope.getImportList());
                }
            }
        }

    }

    public void translateNslName() {
        originalName = getName();
        first.image = NslCompiler.translateNslFunctionName(first.image);
    }

    public String toXMLOpen(String prefix) {
        String type = getVariableType();
        type = ((type == null) ? "" : " type=\"" + type + "\"");
        String method = (isMethod ? " isMethod=\"true\"" : "");
        originalName = ((originalName == null) ? name : originalName);
        return prefix + "<" + toString() + type + method + ">" + originalName;
    }
}
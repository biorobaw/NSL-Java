
/* SCCS  %W--- %G% -- %U% */

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

//
// NslScope.java
//
//////////////////////////////////////////////////////////////////////

import java.util.*;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "Duplicates", "DuplicateExpressions"})
public class NslScope {

    private String packageName;
    private String className;
    private String superClassName;
    private String classKind;
    private String fileName;
    private String fullFileName;

    private Vector<String> importList;
    private Vector classes;

    private int currentTempNumber;
    private Vector<NslVariable> tempVarList;

    private Vector currentVarScope;
    private Vector classVarScope;
    private Vector<NslMethod> classMethodScope;

    private Vector<NslScope> interfaceList;
    private Vector<NslScope> subInterfaceList;
    private Vector<NslScope> subClassList;

    private Stack varScope;

    private ASTFormalParameters classFormals;
    private ASTArguments classArguments;

    private NslScope superClassScope;
    private NslScope containerScope;

    private ASTCompilationUnit ast;

    private static boolean verbose = false;

    private boolean generateCode = false;

    private boolean staticScope = false;

    public NslScope() {

        packageName = null;
        className = null;
        superClassName = null;

        classFormals = null;
        classArguments = null;

        importList = new Vector<String>(2, 2);
        classes = new Vector(1, 1);

        classVarScope = currentVarScope = new Vector();

        varScope = new Stack();

        classMethodScope = new Vector<>();

        currentTempNumber = 0;
        tempVarList = new Vector<>();

        subClassList = new Vector<>();
        interfaceList = new Vector<>();
        subInterfaceList = new Vector<>();

        containerScope = superClassScope = null;

        importList.addElement("nslj.src.system");
        importList.addElement("nslj.src.cmd");
        importList.addElement("nslj.src.lang");
        importList.addElement("nslj.src.math");
        importList.addElement("nslj.src.display");

    }

    public NslScope(NslScope superClassScope) {
        this();
        this.superClassScope = superClassScope;
    }

    public void setSuperClassScope(NslScope scope) {
        superClassScope = scope;
    }

    public NslScope getSuperClassScope() {
        return superClassScope;
    }

    public void setContainerScope(NslScope scope) {
        containerScope = scope;
    }

    public NslScope getContainerScope() {
        return containerScope;
    }

    public boolean shouldGenerateCode() {
        return generateCode;
    }

    public void setGenerateCode(boolean value) {
        generateCode = value;
    }

    public void setAST(ASTCompilationUnit ast) {
        this.ast = ast;
    }

    public ASTCompilationUnit getAST() {
        return ast;
    }

    public void setFileName(String name) {
        fileName = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFullFileName(String name) {
        fullFileName = name;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setPackage(String name) {
        packageName = name;
    }

    public String getPackage() {
        return packageName;
    }

    public void setClassName(String name) {
        className = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassKind(String name) {
        classKind = name;
    }

    public String getClassKind() {
        return classKind;
    }

    public void setSuperClassName(String name) {
        superClassName = name;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public Vector<String> getImportList() {
        return importList;
    }

    public Vector<NslScope> getSubClassList() {
        return subClassList;
    }

    public Vector<NslScope> getInterfaceList() {
        return interfaceList;
    }

    public Vector<NslScope> getSubInterfaceList() {
        return subInterfaceList;
    }

    public void addImportPackage(String importPackage) {

        if (verbose) {
            System.err.println("NslScope [Verbose]: Adding module [" + importPackage + "] to the module import list");
        }

        importList.addElement(importPackage);
    }

    public void setStaticScope(boolean value) {
        staticScope = value;
    }

    public boolean isStaticScope() {
        return staticScope;
    }

    public Vector<NslMethod> getClassMethodScope() {
        return classMethodScope;
    }

    public void addMethod(NslMethod method) {
        classMethodScope.addElement(method);
    }

    public void addSubClass(NslScope scope) {
        subClassList.addElement(scope);
    }

    public void addInterface(NslScope scope) {
        interfaceList.addElement(scope);
    }

    public void addSubInterface(NslScope scope) {
        subInterfaceList.addElement(scope);
    }

    public NslScope resolveClass(String type) {

        NslScope scopeTmp;
        String typeName;

        Enumeration<NslScope> enumer = subClassList.elements();
        while (enumer.hasMoreElements()) {
            scopeTmp = enumer.nextElement();
            typeName = scopeTmp.getClassName();
            if (typeName.endsWith(type)) {
                return scopeTmp;
            }
        }

        // Not found in current scope, ask if it is an interface type.

        if (superClassScope != null) {
            return superClassScope.resolveClass(type);
        }

        // Not found in current scope, ask if it is a global type.

        return NslCompiler.getNslScope(type);
    }

    public NslScope resolveInterface(String type) {

        NslScope scopeTmp;
        String typeName;

        Enumeration<NslScope> enumer = subInterfaceList.elements();
        while (enumer.hasMoreElements()) {
            scopeTmp = enumer.nextElement();
            typeName = scopeTmp.getClassName();
            if (typeName.endsWith(type)) {
                return scopeTmp;
            }
        }

        // Not found in current scope, ask if it is an interface type.

        if (superClassScope != null) {
            return superClassScope.resolveInterface(type);
        }

        // Not found in current scope, ask if it is a global type.

        return NslCompiler.getNslScope(type);
    }

    public NslMethod resolveMethod(String name) {

        Enumeration<NslMethod> enumer = classMethodScope.elements();

        NslMethod methodTmp;
        String methodName;

        while (enumer.hasMoreElements()) {
            methodTmp = enumer.nextElement();
            methodName = methodTmp.getName();
            if (methodName.equals(name)) {
                return methodTmp;
            }
        }

        // look in the parent if I'm a nested class
        if (containerScope != null) {
            methodTmp = containerScope.resolveMethod(name);
            if (methodTmp != null) {
                return methodTmp;
            }
        }

        // look in super class if it exists
        if (superClassScope != null) {
            return superClassScope.resolveMethod(name);
        }

        return null;
    }

    public boolean isTypeEqual(String type1, String type2) {
        if (type1.equals(type2)) {
            return true;
        } else return type2.equals("int") && type1.equals("float") ||
                type2.equals("int") && type1.equals("double") ||
                type2.equals("float") && type1.equals("double");

    }

    public NslMethod resolveMethod(String name, String[] args) {

        if (args == null) {
            return null;
        }

        Enumeration<NslMethod> enumer = classMethodScope.elements();

        NslMethod methodTmp;
        String methodName;
        String[] formalTypes;

        String[] tempArgs = new String[args.length];
        String[] suffixArgs = new String[args.length];
        String[] tempFormals;

        for (int i = 0; i < args.length; i++) {
            args[i] = NslCompiler.getTypeName(args[i]);
            tempArgs[i] = args[i];
            suffixArgs[i] = "";
            if (tempArgs[i] != null && tempArgs[i].contains("[")) {
                tempArgs[i] = args[i].substring(0, args[i].indexOf("["));
                suffixArgs[i] = args[i].substring(args[i].indexOf("["));
            }
        }
        NslScope typeScope;
        String superName, tempType;
        boolean found;
        int count;

        while (enumer.hasMoreElements()) {
            methodTmp = enumer.nextElement();
            methodName = methodTmp.getName();

            if (methodName.equals(name)) {

                //Found the method

                formalTypes = methodTmp.getFormalTypes();
                if (formalTypes.length == args.length) {

                    // same number of arguments

                    tempFormals = new String[formalTypes.length];

                    for (int i = 0; i < formalTypes.length; i++) {
                        formalTypes[i] = NslCompiler.getTypeName(formalTypes[i]);
                        tempFormals[i] = formalTypes[i];
                        if (tempFormals[i] != null && tempFormals[i].contains("[")) {
                            tempFormals[i] = tempFormals[i].substring(0, tempFormals[i].indexOf("["));
                        }
                    }

                    count = 0;
                    for (int i = 0; i < args.length; i++) {

                        if (!isTypeEqual(formalTypes[i], args[i])) {

                            // check if it inherits from them
                            if (isPrimitive(Objects.requireNonNull(tempFormals[i])) && isPrimitive(Objects.requireNonNull(tempArgs[i]))) {
                                found = false;
                                tempType = tempArgs[i];
                                while (!found && tempType != null && ((typeScope = NslCompiler.getNslScope(tempType)) != null)) {
                                    superName = NslCompiler.getTypeName(typeScope.getSuperClassName());
                                    if (superName != null && formalTypes[i].equals(superName + suffixArgs[i])) {
                                        found = true;
                                    }
                                    tempType = superName;
                                }
                                //equal = equal && found;
                                if (found) {
                                    count++;
                                }
                            }

                        } else {
                            count++;
                        }
                    }
                    if (count == args.length) {
                        return methodTmp;
                    }
                }
            }
        }

        // look in the parent if I'm a nested class
        if (containerScope != null) {
            methodTmp = containerScope.resolveMethod(name, args);
            if (methodTmp != null) {
                return methodTmp;
            }
        }

        // look in super class if it exists
        if (superClassScope != null) {
            return superClassScope.resolveMethod(name, args);
        }

        return null;
    }

    public boolean addLocalVar(NslVariable variable) {
        NslVariable var = resolveLocalVar(variable.getName());
        if (var == null) {
            currentVarScope.addElement(variable);
            return true;
        }
        return false;
    }

    public Vector getClassVarScope() {
        return classVarScope;
    }

    public Vector getCurrentVarScope() {
        return currentVarScope;
    }

    public void pushScope() {
        if (currentVarScope != null) {
            varScope.push(currentVarScope);
        }
        currentVarScope = new Vector(10, 10);
    }

    public void popScope() {
        try {
            currentVarScope.removeAllElements();
            currentVarScope = (Vector) varScope.pop();
        } catch (EmptyStackException stackException) {
            NslCompiler.printError("NslScope", "Compiler bug. Empty variable scope and at popScope()");
            //throw stackException;
        }
    }

    public NslVariable resolveLocalVar(String name) {
        Enumeration enumer = currentVarScope.elements();
        NslVariable fieldTmp;

        // scan the current scope
        while (enumer.hasMoreElements()) {
            fieldTmp = (NslVariable) enumer.nextElement();
            if (fieldTmp.isVariable(name)) {
                return fieldTmp;
            }
        }

        return null;
    }

    public NslVariable resolveVar(String name) {
        Stack tmpStack;
        Vector scope;

        Enumeration enumer = currentVarScope.elements();
        NslVariable fieldTmp;

        // scan the current scope
        while (enumer.hasMoreElements()) {
            fieldTmp = (NslVariable) enumer.nextElement();
            if (fieldTmp.isVariable(name)) {
                return fieldTmp;
            }
        }

        // scan outer scope
        tmpStack = new Stack();

        while (!varScope.empty()) {
            enumer = ((Vector) varScope.peek()).elements();

            while (enumer.hasMoreElements()) {
                fieldTmp = (NslVariable) enumer.nextElement();
                if (fieldTmp.isVariable(name)) {
                    restoreScope(tmpStack);
                    return fieldTmp;
                }
            }

            // not found in this scope
            tmpStack.push(varScope.pop());
        }

        // not found in any scope
        restoreScope(tmpStack);

        // look in the parent if I'm a nested class
        if (containerScope != null) {
            fieldTmp = containerScope.resolveVar(name);
            if (fieldTmp != null) {
                return fieldTmp;
            }
        }

        // look in super class if it exists
        if (superClassScope != null) {
            return superClassScope.resolveVar(name);
        }

        return null;
    }

    private void restoreScope(Stack tmp) {
        try {
            while (!tmp.empty()) {
                varScope.push(tmp.pop());
            }
        } catch (EmptyStackException stackException) {
            NslCompiler.printError("NslScope", "Compiler bug. Error reconstructing scope.");
            throw stackException;
        }
    }

    public String getTempName(String type) {
        String tempName = "__temp" + currentTempNumber;
        String modifier = (staticScope ? "static " : "");
        NslVariable variable = new NslVariable(modifier + type, tempName);
        tempVarList.addElement(variable);
        currentTempNumber++;
        return tempName;
    }

    public void addTempVar(NslVariable variable) {
        tempVarList.addElement(variable);
    }

    public Vector<NslVariable> getTempVarList() {
        return tempVarList;
    }

    public void setClassFormals(ASTFormalParameters formals) {
        classFormals = formals;
    }

    public ASTFormalParameters getClassFormals() {
        return classFormals;
    }

    public void setClassArguments(ASTArguments formals) {
        classArguments = formals;
    }

    public ASTArguments getClassArguments() {
        return classArguments;
    }

    public boolean isNumeric(String type) {
        return type != null && type.contains("int") || type.contains("float") || type.contains("double");
    }

    public boolean isArray(String type) {
//      if (type!=null && isNumeric(type) && type.indexOf("[")>=0) {
        return type != null && type.contains("[");
    }

    public int getDim(String type) {
        int dim;
        if (isNslType(type)) {
            dim = getNslTypeDim(type);
        } else if (isArray(type)) {
            dim = getArrayDim(type);
        } else {
            dim = 0;
        }
        return dim;
    }

    public int getArrayDim(String type) {
        return ((type.lastIndexOf("[") - type.indexOf("[")) / 2) + 1;
    }

    public int getNslTypeDim(String name) {
        int dim = -1;
        try {
            dim = new Integer(name.substring(name.length() - 1));
        } catch (Exception ignored) {
        }
        return dim;
    }

    public String getNslTypeWithoutPort(String name) {
        if (isNslInPort(name)) {
            int start = name.indexOf("Din");
            return name.substring(0, start) + name.substring(start + 3);
        } else if (isNslOutPort(name)) {
            int start = name.indexOf("Dout");
            return name.substring(0, start) + name.substring(start + 4);
        }
        return name;
    }

    public boolean isString(String name) {
        return name.contains("String");
    }

    public boolean isNslOutPort(String name) {
        return name.contains("Dout");
    }

    public boolean isNslInPort(String name) {
        return name.contains("Din");
    }

    public boolean isNslPort(String name) {
        return (name.contains("Din") || name.contains("Dout"));
    }

    public boolean isNslNumeric(String name) {
        return (name.contains("Int") || name.contains("Float") || name.contains("Double"));
    }

    public boolean isPrimitive(String type) {
        return !type.equals("boolean") && !type.equals("char") && !type.equals("byte") &&
                !type.equals("short") && !type.equals("int") && !type.equals("long") &&
                !type.equals("float") && !type.equals("double");
    }

    public boolean isNslType(String name) {

        if (name.length() < 3 || !name.substring(0, 3).equals("Nsl")) {
            return false;
        }

        int next = 3;
        if (name.contains("Din")) {
            next += 3;
        } else if (name.contains("Dout")) {
            next += 4;
        }

        int dim = -1;

        if (name.indexOf("Float") == next) {
            try {
                dim = new Integer(name.substring(next + 5));
            } catch (Exception ex) {
                return false;
            }
        } else if (name.indexOf("Double") == next) {
            try {
                dim = new Integer(name.substring(next + 6));
            } catch (Exception ex) {
                return false;
            }
        } else if (name.indexOf("Int") == next) {
            try {
                dim = new Integer(name.substring(next + 3));
            } catch (Exception ex) {
                return false;
            }
        } else if (name.indexOf("Boolean") == next) {
            try {
                dim = new Integer(name.substring(next + 7));
            } catch (Exception ex) {
                return false;
            }
        } else if (name.indexOf("String") == next) {
            try {
                dim = new Integer(name.substring(next + 6));
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }

        return dim >= 0 && dim <= 4;

    }

}
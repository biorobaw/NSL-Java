

// Copyright: Copyright (c) 1997-2002 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

// Author: Salvador Marmol

//
// NslCompiler.java
//
//////////////////////////////////////////////////////////////////////

import exceptions.NslParseArgumentException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

@SuppressWarnings("Duplicates")
public class NslCompiler {

    private static String moduleName, inputFileName, outputFileName, currentDirectory, fullModuleName;
    private static boolean generateClass = true;
    private static boolean generateJavaCode = false;
    private static boolean generateXMLCode = false;
    private static boolean cleanFiles = false;

    public static boolean verbose = false;

    private static Vector<String> classPath;
    private static Vector<NslScope> parsedFiles;

    private static Stack<String> fileNames;

    public static void main(String[] args) {

        try {

            fileNames = new Stack<>();

            parseArguments(args);

            if (cleanFiles) {
                cleanFilesInDirectory();
                return;
            }

            initClassPath();

            parsedFiles = new Vector<>(10);

            parseType(moduleName, null);

            if (!NslParser.error) {
                String[] fileNames = null;
                if (generateClass || generateJavaCode) {
                    fileNames = dumpJavaFiles();
                }

                if (generateXMLCode) {
                    fileNames = dumpXMLFiles();
                } else if (!NslParser.error && generateClass && fileNames != null) {
                    parseJavaFiles(fileNames);
                }

                if (NslParser.error) {
                    System.err.println(NslParser.errorCount + " error" + ((NslParser.errorCount > 1) ? "s" : ""));
                }
            } else {
                System.err.println(NslParser.errorCount + " error" + ((NslParser.errorCount > 1) ? "s" : ""));
            }

        } catch (NslParseArgumentException argumentError) {

            System.err.println();
            System.err.println("Usage:   java NslCompiler [Options] \"ModelName\"");
            System.err.println("   or:   java NslCompiler -clean");
            System.err.println();
            System.err.println("Options: -java");
            System.err.println("         -xml");
            System.err.println("         -verbose");

        } catch (Exception e) {

            if (verbose) {
                e.printStackTrace();
            } else {
                System.err.println("NslCompiler [Error]: Compiler bug, use -verbose to get more details");
            }
        }

    }

    /**
     * Parses command line arguments such as:
     * <p>
     * java, xml, verbose
     *
     * @param argv The vector containing the command line arguments.
     * @throws NslParseArgumentException This exception is thrown if the user
     *                                   does not provide enough or correct information in the command
     *                                   line.
     */

    private static void parseArguments(String[] argv)
            throws NslParseArgumentException {

        if (argv.length < 1 || (argv.length == 1 && (argv[0].compareTo("-?") == 0 || argv[0].compareTo("-h") == 0))) {
            throw new NslParseArgumentException();
        }

        if (argv[argv.length - 1].equals("-clean")) {
            cleanFiles = true;
            return;
        }

        fullModuleName = moduleName = argv[argv.length - 1];

        for (int i = 0; i < argv.length - 1; i++) {

            if (argv[i].compareTo("-java") == 0) {

                generateJavaCode = true;
                generateXMLCode = false;
                generateClass = false;

            } else if (argv[i].compareTo("-xml") == 0) {

                generateJavaCode = false;
                generateXMLCode = true;
                generateClass = false;

            } else if (argv[i].compareTo("-verbose") == 0) {

                verbose = true;

            } else {
                throw new NslParseArgumentException();
            }

        }
    }

    public static void addNslScope(NslScope scope) {
        parsedFiles.addElement(scope);
    }

    public static Vector<NslScope> getParsedFiles() {
        return parsedFiles;
    }

    public static String getTypeName(String type) {
        if (type != null && type.contains(".")) {
            type = type.substring(type.lastIndexOf(".") + 1);
        }
        return type;
    }

    public static NslScope getNslScope(String type) {
        Enumeration<NslScope> enumer = parsedFiles.elements();

        NslScope scopeTmp;
        String typeName;

        while (enumer.hasMoreElements()) {
            scopeTmp = enumer.nextElement();
            typeName = scopeTmp.getClassName();
            if (getTypeName(typeName).equals(getTypeName(type))) {
                return scopeTmp;
            }
        }

        return null;
    }

    public static String[] dumpJavaFiles() {
        Enumeration<NslScope> enumer = parsedFiles.elements();

        NslScope scopeTmp;
        NslParserVisitor visitor;
        String fileName, fullFileName;
        int[] pos = new int[parsedFiles.size()];
        int numFiles = 0, index = 0;
        while (enumer.hasMoreElements()) {
            scopeTmp = enumer.nextElement();
            if (scopeTmp.shouldGenerateCode()) {
                ASTCompilationUnit ast = scopeTmp.getAST();

                fullModuleName = scopeTmp.getFullFileName();
                moduleName = scopeTmp.getFileName();
                fileName = moduleName + ".java";
                visitor = new ModifyVisitor();
                ast.jjtAccept(visitor, scopeTmp);

                generateCode(ast, fileName);
                pos[numFiles++] = index;
            }
            index++;
        }
        String[] fileNames = ((numFiles > 0) ? new String[numFiles] : null);
        for (int i = 0; i < numFiles; i++) {
            scopeTmp = parsedFiles.elementAt(pos[i]);
            fileNames[i] = scopeTmp.getFileName();
        }
        return fileNames;
    }

    public static String[] dumpXMLFiles() {
        Enumeration<NslScope> enumer = parsedFiles.elements();

        NslScope scopeTmp;
        NslParserVisitor visitor;
        String fileName;
        int[] pos = new int[parsedFiles.size()];
        int numFiles = 0, index = 0;
        while (enumer.hasMoreElements()) {
            scopeTmp = enumer.nextElement();
            if (scopeTmp.shouldGenerateCode()) {
                ASTCompilationUnit ast = scopeTmp.getAST();

                fileName = scopeTmp.getFileName() + ".xml";

                visitor = new ComputeTypeVisitor();
                ast.jjtAccept(visitor, scopeTmp);

                generateXMLCode(ast, fileName);
                pos[numFiles++] = index;
            }
            index++;
        }
        String[] fileNames = ((numFiles > 0) ? new String[numFiles] : null);
        for (int i = 0; i < numFiles; i++) {
            scopeTmp = parsedFiles.elementAt(pos[i]);
            fileNames[i] = scopeTmp.getFileName();
        }
        return fileNames;
    }

    public static String javaToNslType(String type) {
        int dim = -1;
        if ((dim = type.lastIndexOf("[")) >= 0) {
            dim++;
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < dim; i++) {
                temp.append("[]");
            }
            String kind = type.substring(dim, dim + 1);
            switch (kind) {
                case "L":
                    type = type.substring(dim + 1, type.length() - 1) + temp;
                    break;
                case "B":
                    type = "byte" + temp;
                    break;
                case "C":
                    type = "char" + temp;
                    break;
                case "D":
                    type = "double" + temp;
                    break;
                case "F":
                    type = "float" + temp;
                    break;
                case "I":
                    type = "int" + temp;
                    break;
                case "J":
                    type = "long" + temp;
                    break;
                case "S":
                    type = "short" + temp;
                    break;
                case "Z":
                    type = "boolean" + temp;
                    break;
            }
        }
        return type;
    }

    public static NslScope createNslScopeFromClass(String className) {
        NslScope scope = null;

        try {
            Class object = Class.forName(className);

            scope = new NslScope();

            int mod;

            String modifiers = Modifier.toString(object.getModifiers()),
                    packageName = ((object.getPackage() != null) ? object.getPackage().getName() : null),
                    superClassName = ((object.getSuperclass() != null) ? object.getSuperclass().getName() : null),
                    kind = (object.isInterface() ? "interface" : "class"),
                    name;

            Class type;

            scope.setPackage(packageName);
            scope.setClassKind(kind);
            scope.setClassName(className);
            scope.setSuperClassName(superClassName);

            if (superClassName != null) {
                scope.setSuperClassScope(parseType(superClassName, null));
            }

            NslVariable variable;
            Field[] fields = object.getDeclaredFields();
            for (Field field : fields) {
                type = field.getType();
                name = field.getName();
                modifiers = Modifier.toString(field.getModifiers());
                variable = new NslVariable(modifiers, javaToNslType(type.getName()), name);
                scope.addLocalVar(variable);
            }

            NslMethod method;
            Method[] methods = object.getDeclaredMethods();
            Class[] parameters;
            String[] formals;
            for (Method value : methods) {
                type = value.getReturnType();
                name = value.getName();
                modifiers = Modifier.toString(value.getModifiers());
                parameters = value.getParameterTypes();
                formals = new String[parameters.length];
                for (int j = 0; j < formals.length; j++) {
                    formals[j] = javaToNslType(parameters[j].getName());
                }
                method = new NslMethod(modifiers, javaToNslType(type.getName()), name, formals);
                scope.addMethod(method);
            }

            Class[] subClasses = object.getDeclaredClasses();
            for (Class subClass : subClasses) {
                scope.addSubClass(createNslScopeFromClass(subClass.getName()));
            }

            addNslScope(scope);

        } catch (Exception ex) {
            System.err.println("NslCompiler [Error]: Compiler Bug, class " + className + " was not found");
            if (verbose) {
                ex.printStackTrace();
            }
        }

        return scope;
    }

    public static NslParser parseFile(String fileName) {
        NslParser parser = null;
        try {
            NslFile.loadFile(fileName);
            FileInputStream inputFile = new FileInputStream(fileName);
            parser = new NslParser(inputFile);

        } catch (IOException ioException) {

            System.err.println("NslCompiler [Error]: \"" + ioException.getMessage() + "\" while opening source file " + fileName);

        } catch (Exception parseException) {

            System.err.println("NslCompiler [Error]: Compiler bug, a  parser exception was occured in " + fileName);
            if (verbose) {
                parseException.printStackTrace();
            }

        } finally {
            return parser;
        }

    }

    public static NslScope parseType(String importName, Vector importList) {

        NslScope scope = getNslScope(importName);

        if (scope != null) {
            return scope;
        }

        if (verbose) {
            System.err.println("NslCompiler [Verbose]: Parsing class: " + importName);
        }

        String importPath = importName.replace('.', (System.getProperty("file.separator")).charAt(0)),
                srcFileName = findFile(importPath + ".mod", importList),
                xmlFileName = findFile(importPath + ".xml", importList),
                javaFileName = findFile(importPath + ".java", importList),
                classFileName = findFile(importPath + ".class", importList);

        if (srcFileName != null) {
            File srcFile = new File(srcFileName);
            if (verbose) {
                System.err.println("NslCompiler [Verbose]: Parsing file: " + srcFileName);
            }

            // A mod file exists, should create a NslScope from it.

            fileNames.push(moduleName);
            fileNames.push(fullModuleName);
            moduleName = importName;
            fullModuleName = srcFileName;
            NslParser parser = parseFile(srcFileName);
            if (parser != null && !NslParser.error) {
                try {
                    ASTCompilationUnit ast = parser.CompilationUnit();

                    if (!NslParser.error) {
                        scope = new NslScope();
                        scope.setAST(ast);
                        NslParserVisitor visitor = new SymbolTableClassVisitor(scope);
                        ast.jjtAccept(visitor, null);
                        if (!NslParser.error) {
                            visitor = new SymbolTableVisitor(scope);
                            ast.jjtAccept(visitor, null);
                        }

                        // Check if the java/xml file and/or the class file should be generated
                        if ((generateClass &&
                                (classFileName == null ||
                                        (srcFile.lastModified() > (new File(classFileName)).lastModified()))) ||
                                (generateJavaCode &&
                                        (javaFileName == null ||
                                                (srcFile.lastModified() > (new File(javaFileName)).lastModified()))) ||
                                (generateXMLCode &&
                                        (xmlFileName == null ||
                                                (srcFile.lastModified() > (new File(xmlFileName)).lastModified())))) {

                            if (verbose) {
                                System.err.println("NslCompiler [Verbose]: Scheduling " + srcFileName + " for later code generation");
                            }
                            scope.setFullFileName(srcFileName);
                            scope.setFileName(importPath);
                            scope.setGenerateCode(true);
                        }

                        addNslScope(scope);
                    } else if (verbose) {
                        System.err.println("NslCompiler [Verbose]: Syntax errors were found while compiling " + srcFileName);
                    }
                } catch (ParseException parseException) {
                    System.err.println("NslCompiler [Error]: Compiler bug, a  parser exception was occured in " + srcFileName);
                    if (verbose) {
                        parseException.printStackTrace();
                    }
                }

            } else if (verbose) {
                System.err.println("NslCompiler [Error]: Couldn't get AST of " + srcFileName);
            }
            try {
                fullModuleName = fileNames.pop();
                moduleName = fileNames.pop();
            } catch (EmptyStackException stackException) {
                System.err.println("NslCompiler [Error]: Compiler Bug. Empty file name list.");
                throw stackException;
            }
        } else {

            if (javaFileName != null) {
                if (verbose) {
                    System.err.println("NslCompiler [Verbose]: The file " + javaFileName + " exists");
                }
                if (classFileName == null || ((new File(javaFileName)).lastModified() > (new File(classFileName)).lastModified())) {
                    // Update class file from it's java file
                    String[] fileNames = new String[1];
                    fileNames[0] = javaFileName.substring(0, javaFileName.lastIndexOf("."));

                    if (parseJavaFiles(fileNames)) {
                        classFileName = javaFileName.substring(0, javaFileName.lastIndexOf(".")) + ".class";
                    } else {
                        classFileName = null;
                    }
                }
            }
            if (classFileName != null) {
                String fullName = findClassInPackages(importName, importList);
                if (verbose) {
                    System.err.println("NslCompiler [Verbose]: The file " + classFileName + " exists in package " + fullName);
                }
                scope = createNslScopeFromClass(fullName);
            } else {
                // See if it is a System class
                String fullName = findClassInSystem(importName);
                if (fullName != null) {
                    scope = createNslScopeFromClass(fullName);
                } else {
                    printError("NslCompiler", "Couldn't find class " + importName);
                }
            }
        }
        return scope;
    }

    static String findClass(String name) {
        try {
            Class object = Class.forName(name);
            return name;
        } catch (Exception ignored) {
        }
        return null;
    }

    static String findClassInSystem(String name) {
        String fullName = findClass(name);
        if (fullName == null) {
            Package[] packages = Package.getPackages();
            Class object = null;

            for (Package aPackage : packages) {
                try {
                    fullName = aPackage.getName() + "." + name;
                    object = Class.forName(fullName);
                    return fullName;
                } catch (Exception ignored) {
                }
            }
            return null;
        }

        return fullName;
    }

    static String findClassInPackages(String name, Vector importList) {
        String fullName = findClassInSystem(name);
        if (fullName == null && importList != null) {
            Enumeration enumer = importList.elements();
            String packageName;
            Class object;
            while (enumer.hasMoreElements()) {
                packageName = (String) enumer.nextElement();
                fullName = packageName + "." + name;
                try {
                    object = Class.forName(fullName);
                    return fullName;
                } catch (Exception ignored) {
                }
            }
            return null;
        }
        return fullName;
    }

    /*
     *
     * Compiles a java file and genetares
     * a class file
     *
     */

    public static boolean parseJavaFiles(String[] fileNames) {

        boolean result = true;

        // Compile the Java File

        StringBuilder classpath = new StringBuilder(System.getProperty("java.class.path"));
        String[] dir = new String[fileNames.length];

        for (int i = 0; i < dir.length; i++) {
            int endIndex = fileNames[i].lastIndexOf(System.getProperty("file.separator"));
            if (endIndex != -1) {
                dir[i] = fileNames[i].substring(0, endIndex);
            } else {
                dir[i] = "";
            }

            if (!dir[i].equals(System.getProperty("user.dir")) && !dir[i].equals("")) {
                classpath.insert(0, dir[i] + ":");
            }
        }

        String[] parameters = new String[3 + fileNames.length];
        parameters[0] = "javac";
        parameters[1] = "-classpath";
        parameters[2] = classpath.toString();
        for (int i = 0; i < fileNames.length; i++) {
            parameters[3 + i] = fileNames[i] + ".java";
        }

        try {

            Process process = (Runtime.getRuntime()).exec(parameters);
            InputStream errStr = process.getErrorStream();
            byte[] errbyte = new byte[100];

            int i = 0, element = 0;

            while (element >= 0) {

                errbyte[i++] = (byte) (element = errStr.read());

                if (element == -1 && i > 1) {
                    System.err.write(errbyte, 0, i - 1);
                } else if (i == 100) {
                    System.err.write(errbyte, 0, 100);
                    i = 0;
                }

            }

            errStr.close();

            process.waitFor();


            if (process.exitValue() != 0) {
                for (String fileName : fileNames) {
                    System.err.println("NslCompiler [Error]: The class " + fileName + " was not created");
                }
                result = false;
            } else if (verbose) {
                for (String fileName : fileNames) {
                    System.err.println("NslCompiler [Verbose]: The class " + fileName + " was created");
                }
            }

        } catch (Exception processException) {
            System.err.println("NslCompiler [Error]: Process exception while compiling java file");
            System.err.println("NslCompiler [Error]: " + processException.getMessage());
            if (verbose) {
                processException.printStackTrace();
            }
            result = false;
        }

        return result;
    }

    /*
     *
     *  Searches for the file fileName in all the class path
     *  Returns the file name if it actually found it
     *  or null if it didn't find it.
     *
     */

    private static String findFileInClassPath(String fileName) {
        String result;

        String separator = System.getProperty("file.separator");
        if (fileName.indexOf(separator) > 0) {
            result = "." + separator + fileName;
        } else {
            result = fileName;
        }

        if (new File(result).exists()) {
            return result;
        }

        Enumeration<String> classPathEnumeration = classPath.elements();
        while (classPathEnumeration.hasMoreElements()) {
            String cpath = classPathEnumeration.nextElement();
            result = cpath + separator + fileName;
            if (new File(result).exists()) {
                return result;
            }
        }

        return null;
    }

    /*
     *
     *  Searches for the file fileName in the current directory or in all the class path
     *  Returns the file name if it actually found it
     *  or null if it didn't find it.
     *
     */

    private static String findFile(String fileName, Vector importList) {
        String result = findFileInClassPath(fileName);

        if (result == null && importList != null) {
            // Look into the imported directories
            String separator = System.getProperty("file.separator");
            Enumeration importEnumeration = importList.elements();
            while (importEnumeration.hasMoreElements()) {
                String importName = (String) importEnumeration.nextElement();
                importName = importName.replace('.', separator.charAt(0)) + separator + fileName;
                result = findFileInClassPath(importName);
                if (result != null) {
                    return result;
                }
            }
        }

        return result;
    }


    /*
     *
     * Finds the user directory
     *
     */

    private static String getCurrentDirectory(String fileName) {
        String directory;
        int index;
        if ((index = fileName.lastIndexOf(System.getProperty("file.separator"))) >= 0) {
            directory = fileName.substring(0, index);
        } else {
            directory = System.getProperty("user.dir");
        }

        if (verbose) {
            System.err.println("NslCompiler [Verbose]: Current Directory is " + directory);
        }

        return directory;
    }

    /*
     *
     * Lists all files in dirname and searches fname there.
     * Returns the complete file name (dirname + fname)
     * if success.
     *
     */

    private static void cleanFilesInDirectory() throws AssertionError {

        String directoryName = System.getProperty("user.dir");

        if (!directoryName.endsWith(System.getProperty("file.separator"))) {
            directoryName = directoryName + System.getProperty("file.separator");
        }

        File searchedDirectory = new File(directoryName);

        try {
            if (searchedDirectory.exists()) {
                String[] files = searchedDirectory.list();

                assert files != null;
                int fileIndex = files.length;
                String fileName, javaFileName, classFileName, xmlFileName;
                File javaFile, classFile, xmlFile;
                while (--fileIndex > -1) {
                    fileName = files[fileIndex];
                    if (fileName.endsWith(".mod")) {
                        xmlFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".xml";
                        javaFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".java";
                        classFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".class";
                        xmlFile = new File(xmlFileName);
                        javaFile = new File(javaFileName);
                        classFile = new File(classFileName);
                        if (xmlFile.exists()) {
                            xmlFile.delete();
                        }
                        if (javaFile.exists()) {
                            javaFile.delete();
                        }
                        if (classFile.exists()) {
                            classFile.delete();
                        }
                    }
                }
            }
        } catch (AssertionError ex) {
            System.err.println("NslCompiler [Error]: File does not exist");
        }

    }

    /*
     * Save all directories of the classpath into the classPath vector
     */

    private static void initClassPath() {

        currentDirectory = getCurrentDirectory(moduleName);

        String path = System.getProperty("java.class.path");
        classPath = new Vector<>();

        String cPathSep;

        // Get the separation character between the directories inside the classpath:

        if (!System.getProperty("os.name").contains("Windows"))
            cPathSep = ":";
        else
            cPathSep = ";";

        // Save all directories of the classpath into the classPath vector
        int index;
        while ((index = path.indexOf(cPathSep)) > 0) {
            classPath.addElement(path.substring(0, index));
            path = path.substring(index + 1);
        }
    }

    public static void generateCode(SimpleNode node, String outputFileName) {

        try {

            PrintStream out = new PrintStream(new FileOutputStream(outputFileName));

            Token t1 = node.getFirstToken();
            Token t = new Token();
            t.next = t1;

            while (t != node.getLastToken()) {
                t = t.next;
                print(t, out);
            }

            if (verbose) {
                System.err.println("NslCompiler [Verbose]: The java file " + outputFileName + " was created");
            }

            out.close();

        } catch (Exception ioException) {

            System.err.println("NslCompiler [Error]: \"" + ioException.getMessage() + "\" while generating java file");

        }
    }

    public static void generateXMLCode(SimpleNode node, String outputFileName) {

        try {

            PrintStream out = new PrintStream(new FileOutputStream(outputFileName));

            // Print the xml file header:

            out.println("<?xml version=\"1.0\"?>");
            out.println("<?xml-stylesheet type=\"text/xsl\" href=\"http://www-hbp.usc.edu/nsl.xsl\"?>");
            out.println();
            out.println("<!-- XML code generated by NslCompiler (c) 2002 USC -->");
            out.println();
            node.dumpXML("", out);

            if (verbose) {
                System.err.println("NslCompiler [Verbose]: The xml file " + outputFileName + " was created");
            }

            out.close();

        } catch (Exception ioException) {

            System.err.println("NslCompiler [Error]: \"" + ioException.getMessage() + "\" while generating XML file");

        }
    }

    private static void print(Token t, PrintStream out) {
        Token tt = t.specialToken;
        if (tt != null) {
            while (tt.specialToken != null) tt = tt.specialToken;
            while (tt != null) {
                out.print(addUnicodeEscapes(tt.image));
                tt = tt.next;
            }
        }
        out.print(addUnicodeEscapes(t.image));
    }


    private static String addUnicodeEscapes(String str) {
        StringBuilder retval = new StringBuilder();
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if ((ch < 0x20 || ch > 0x7e) &&
                    ch != '\t' && ch != '\n' && ch != '\r' && ch != '\f') {
                String s = "0000" + Integer.toString(ch, 16);
                retval.append("\\u").append(s.substring(s.length() - 4));
            } else {
                retval.append(ch);
            }
        }
        return retval.toString();
    }

    public static void printError(String reporter, String description, int line, int col) {
        System.err.println(moduleName + ".mod:" + line + ": " + description);
        if (verbose) {
            System.err.println("Reported by: " + reporter);
        }
        System.err.println(NslFile.getLine(fullModuleName, line));
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < (col - 1) % 80; i++) {
            spaces.append(" ");
        }
        System.err.println(spaces + "^");
        //System.err.println();
        NslParser.error = true;
        NslParser.errorCount++;
    }


    public static void printError(String reporter, String description) {
        System.err.println(moduleName + ".mod:" + description);
        if (verbose) {
            System.err.println("Reported by: " + reporter);
        }
        NslParser.error = true;
        NslParser.errorCount++;
    }

    public static String translateNslFunctionName(String functionName) {

        if (!functionName.startsWith("nsl")) {
            return functionName;
        }

        if (functionName.equals("nslPrint")) return "System.out.print";
        if (functionName.equals("nslPrintln")) return "System.out.println";
        if (functionName.equals("nslprint")) return "System.out.print";
        if (functionName.equals("nslprintln")) return "System.out.println";
        if (functionName.equals("NslPrint")) return "System.out.print";
        if (functionName.equals("NslPrintln")) return "System.out.println";

        if (functionName.equals("nslRelabel")) return "nslConnect";

        if (functionName.equals("nslAdd")) return "NslAdd.eval";
        if (functionName.equals("nslConv")) return "NslConv.eval";
        if (functionName.equals("nslConvzero")) return "NslConvZero.eval";
        if (functionName.equals("nslConvW")) return "NslConvW.eval";
        if (functionName.equals("nslConvC")) return "NslConvC.eval";

        if (functionName.equals("nslDiff")) return "system.nsldiff.eval";
        if (functionName.equals("nslFillColumns")) return "NslFillColumns.eval";
        if (functionName.equals("nslFillRows")) return "NslFillRows.eval";
        if (functionName.equals("nslMath")) return "system.nslmath.eval";
        if (functionName.equals("nslMaxValue")) return "NslMaxValue.eval";
        if (functionName.equals("nslMinValue")) return "NslMinValue.eval";
        if (functionName.equals("nslProd")) return "NslProd.eval";
        if (functionName.equals("nslProduct")) return "NslProd.eval";
        if (functionName.equals("nslRamp")) return "NslRamp.eval";
        if (functionName.equals("nslSaturation")) return "NslSaturation.eval";
        if (functionName.equals("nslSigmoid")) return "NslSigmoid.eval";
        if (functionName.equals("nslSigmoid2")) return "NslSigmoid2.eval";
        if (functionName.equals("nslStep")) return "NslStep.eval";
        if (functionName.equals("nslSub")) return "NslSub.eval";
        if (functionName.equals("nslTrans")) return "NslTrans.eval";
        if (functionName.equals("nslSum")) return "NslSum.eval";
        if (functionName.equals("nslSumRows")) return "NslSumRows.eval";
        if (functionName.equals("nslSumColumns")) return "NslSumColumns.eval";
        if (functionName.equals("nslMaxElem")) return "NslMaxElem.eval";
        if (functionName.equals("nslMinElem")) return "NslMinElem.eval";
        if (functionName.equals("nslGetSector")) return "NslGetSector.eval";
        if (functionName.equals("nslSetSector")) return "NslSetSector.eval";
        if (functionName.equals("nslElemMult")) return "NslElemMult.eval";
        if (functionName.equals("nslElemDiv")) return "NslElemDiv.eval";
        if (functionName.equals("nslConcatenateRows")) return "NslConcatenateRows.eval";
        if (functionName.equals("nslConcatenateColumns")) return "NslConcatenateColumns.eval";
        if (functionName.equals("nslGetRow")) return "NslGetRow.eval";
        if (functionName.equals("nslGetColumn")) return "NslGetColumn.eval";
        if (functionName.equals("nslRandom")) return "NslRandom.eval";
        if (functionName.equals("nslGuassian")) return "NslGuassian.eval";
        if (functionName.equals("nslAll")) return "NslAll.eval";
        if (functionName.equals("nslSome")) return "NslSome.eval";
        if (functionName.equals("nslNone")) return "NslNone.eval";
        if (functionName.equals("nslBound")) return "NslBound.eval";
        if (functionName.equals("nslDotProd")) return "NslDotProd.eval";

        // NslOperations
        if (functionName.equals("nslRint")) return "NslOperator.rint.eval";
        if (functionName.equals("nslAbs")) return "NslOperator.abs.eval";
        if (functionName.equals("nslPow")) return "NslOperator.pow.eval";
        if (functionName.equals("nslExp")) return "NslOperator.exp.eval";
        if (functionName.equals("nslLog")) return "NslOperator.log.eval";
        if (functionName.equals("nslMaxMerge")) return "NslOperator.max.eval";
        if (functionName.equals("nslMinMerge")) return "NslOperator.min.eval";
        if (functionName.equals("nslCos")) return "NslOperator.cos.eval";
        if (functionName.equals("nslSin")) return "NslOperator.sin.eval";
        if (functionName.equals("nslTan")) return "NslOperator.tan.eval";
        if (functionName.equals("nslArcCos")) return "NslOperator.acos.eval";
        if (functionName.equals("nslArcSin")) return "NslOperator.asin.eval";
        if (functionName.equals("nslArcTan")) return "NslOperator.atan.eval";
        if (functionName.equals("nslSqrt")) return "NslOperator.sqrt.eval";
        if (functionName.equals("nslDistance")) return "NslOperator.distance.eval";

        return functionName;
    }

}

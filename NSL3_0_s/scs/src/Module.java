/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * Module - The class representing the module. A module is basically composed of
 * three parts, schematic part for its internal schematic module structure, icon
 * part for its icon representation, and parts used to generate its codes.
 *
 * @author Xie, Gupta, Alexander
 * @version %W% , %G% - %U%
 * <p>
 * *var       sifVersionNum   the version of the sif file
 * <p>
 * *var       hasIcon   	does this module have an icon
 * <p>
 * *var       hasSchematic   	does this module have a schematic
 * <p>
 * *var       hasNslm   	does this module have NSLM
 * <p>
 * *var       isModel   	is this a model
 * <p>
 * *var 	getCurrentVersion	specifies whether the default for picking up icons
 * and generating code should default to a specific version of an icon
 * or float to the most recent version of that module or icon.
 * <p>
 * *var 	libNickName	specifies the name of the library to which the module belongs
 * <p>
 * *var       moduleName	                the name of the module template that this module
 * is made an instance of
 * <p>
 * *var	versionName		the version of the module
 * <p>
 * *var      	moduleType		the type of this module, either serial or
 * parallel
 * *var 	buffering 	specifies the type buffering for output ports
 * true-double buffering for output ports for simulated
 * parallel processing
 * false-no buffering for sequential processing
 * *var	myIcon		The icon representation of this module
 * <p>
 * *var	mySchematic	The schematic representation of this module .
 * <p>
 * *var	myNslm		The NSLM representation of this module .
 * @since JDK8
 */

import java.awt.*;
import javax.swing.*;
import java.util.Vector;
import java.io.*;
import javax.swing.text.*;

// aa: todo: rethink list of icons instead of
// partial modules
// because we always have to copy the names to
// the icons when we save

//todo: does this really need to extend Component; what if it extended
// Declaration instead.

@SuppressWarnings("Duplicates")
class Module extends Component {
    int sifVersionNum = SCSUtility.sifVersionNum;
    boolean hasIcon = false;
    boolean hasSchematic = false;
    boolean hasNslm = false;
    boolean isModel = false;
    boolean getCurrentVersion = false; // getCurrentVersion version of icons=1, float=0
    String libNickName = "LIB1";
    String moduleName; //template name
    String versionName;   // version  4_4_4
    String moduleType; //model,basic,input,output,purecode
    String arguments = ""; // arguments are copied to NSLM code
    boolean buffering = false; // 0=false for port buffering
    Vector variables = null; // ports, submodules, and variables

    Icon myIcon = null;
    Schematic mySchematic = null;
    NSLM myNslm = null;

    //static private boolean debugM=true;
    static private boolean debugM = false;

//    GraphicPart selPort=null;

    /**
     * Constructor of this class with no parameters.
     */
    public Module() {
        super();
        initModule();
    }

    /**
     * Constructor of this class, moduleName, and moduleType field  passed in
     */
    public Module(String moduleName, String myType) {
        super();
        initModule();
        this.moduleName = moduleName;
        moduleType = myType;
    }
    //---------------------------------------------------------------	

    /**
     * Contructor
     * return false if not equal
     *
     * @param libNickName2 nickname
     * @param moduleName2  module name
     * @param versionName2 version name
     *                     used EditorFrame for checking if an editor already has that module open
     */
    public Module(String libNickName2, String moduleName2, String versionName2) {
        super();
        initModule();
        libNickName = libNickName2;
        moduleName = moduleName2;
        versionName = versionName2;
    }

    //---------------------------------------------------------------	

    /**
     * Constructor of this class with
     *
     * @param sifVersionNum     sif version
     * @param libNickName       library nickname
     * @param moduleName        module name
     * @param moduleVersion     module version
     * @param moduleType        module type
     * @param moduleBuffering   module buffering
     * @param getCurrentVersion get current version
     * @param variables         variables
     */
    public Module(int sifVersionNum, String libNickName, String moduleName, String moduleVersion, String moduleType, String arguments, boolean moduleBuffering, boolean getCurrentVersion, Vector variables) {
        super();
        this.sifVersionNum = sifVersionNum;
        hasIcon = false;
        hasSchematic = false;
        hasNslm = false;
        isModel = false;
        this.getCurrentVersion = getCurrentVersion;  //default=float=0, specific version of icon=1;
        this.libNickName = libNickName;
        this.moduleName = moduleName;
        this.versionName = moduleVersion;
        this.moduleType = moduleType;
        this.arguments = arguments;
        this.buffering = moduleBuffering;
        this.variables = variables;

        myIcon = null;
        mySchematic = null;
        myNslm = null;
    }

    //-------------------------------------------------------
    private void initModule() {

        sifVersionNum = SCSUtility.sifVersionNum;
        hasIcon = false;
        hasSchematic = false;
        hasNslm = false;
        isModel = false;
        getCurrentVersion = false;  //default=float=0, specific version of icon=1;
        libNickName = "LIB1";
        moduleName = ""; // or template name
        versionName = "";
        moduleType = ""; //NslModule, NslModel, NslClass, NslInModule, NslOutModule
        buffering = false;
        arguments = "";
        variables = new Vector();

        myIcon = null;
        mySchematic = null;
        myNslm = null;

        //	Variables=new Vector();
        //	Makeinst="";
        //	Makeconn="";
        //	Methods="";
    }

    //---------------------------------------------------------------	

    /**
     * set the header of the module
     *
     * @param libNickName       String
     * @param moduleName        String
     * @param versionName       String
     * @param type              String
     * @param arguments         String
     * @param buffering         boolean
     * @param getCurrentVersion boolean
     * @param variables         Vector
     */
    public void setHeaderOfModule(String libNickName, String moduleName, String versionName, String type, String arguments, boolean buffering, boolean getCurrentVersion, Vector variables) {
        this.moduleName = libNickName;
        this.moduleName = moduleName;
        this.versionName = versionName;
        this.moduleType = type;
        this.getCurrentVersion = getCurrentVersion;
        this.arguments = arguments;
        this.buffering = buffering;
        this.variables = variables;
        if (this.myIcon != null) {
            this.myIcon.libNickName = this.libNickName;
            this.myIcon.moduleName = this.moduleName;
            this.myIcon.versionName = this.versionName;
        }
    }
    //---------------------------------------------------------------	

    /**
     * mostlyEquals
     *
     * @return <code>eq</code>
     * return false if not equal
     */
    // todo: would it be safe to call this equals?
    public boolean mostlyEquals(String libNickName2, String moduleName2, String versionName2) {
        if (libNickName2 == null) return false;
        if (moduleName2 == null) return (false);
        if (versionName2 == null) return (false);

        return (libNickName.equals(libNickName2)) &&
                (moduleName.equals(moduleName2)) &&
                (versionName.equals(versionName2));

    }
    //---------------------------------------------------------------	

    /**
     * mostlyEquals
     *
     * @return <code>eq</code>
     * return false if not equal
     */
    // todo: would it be safe to call this equals?
    public boolean mostlyEquals(Module module1) {
        if (module1 == null) return false;
        String libNickName2 = module1.libNickName;
        String moduleName2 = module1.moduleName;
        String versionName2 = module1.versionName;

        if (libNickName2 == null) return false;
        if (moduleName2 == null) return false;
        if (versionName2 == null) return false;

        return (libNickName.equals(libNickName2)) &&
                (moduleName.equals(moduleName2)) &&
                (versionName.equals(versionName2));

    }
    //---------------------------------------------------------------	
    // todo: create a special DeclarationVector class similar to
    // StringModuleV

    /**
     * findVar
     *
     * @return <code>declaration or variable</code>
     * return null if not found
     */
    public Declaration findVar(String name) {
        int ix;
        Declaration bob;   //could just use indexOf
        for (ix = 0; ix < variables.size(); ix++) {
            bob = (Declaration) variables.elementAt(ix);
            if (bob.varName.equals(name)) {
                return (bob);
            }
        }
        return (null);
    }
    //---------------------------------------------------------------	

    /**
     * findVarIndex
     *
     * @return <code>int</code>
     * return -1 if not found
     */
    public int findVarIndex(String name) {

        int ix;
        Declaration bob;   //could just use indexOf

        //    System.out.println("Debug:Module:input:"+name+".");
        for (ix = 0; ix < variables.size(); ix++) {

            bob = (Declaration) variables.elementAt(ix);
            //System.out.println("Debug:Module:variable:"+bob.varName+".");
            if (bob.varName.equals(name)) {
                return (ix);
            }
        }
        return (-1);

    }

    //---------------------------------------------------------------	

    /**
     * addVariable
     *
     * @return worked - if added, return true
     */
    public boolean addVariable(Declaration var) {
        int foundi;
        foundi = findVarIndex(var.varName);
        if (foundi != -1) { //already in list
            return false;
        } else {
            //add to the variables list
            variables.addElement(var);
            return true;
        }
    }

    //---------------------------------------------------------------	

    /**
     * addVariable
     *
     * @return worked - if added, return true
     */
    public boolean addVariableAt(int index, Declaration var) {
        //only one variable by that name
        int foundi;
        foundi = findVarIndex(var.varName);
        if (foundi != -1) {
            return false;  //already in list
        } else {
            //add to the variables list
            try {
                variables.add(index, var);
            } catch (ArrayIndexOutOfBoundsException eee) {
                return false;
            }
            return true;
        }
    }
    //---------------------------------------------------------------	

    /**
     * deleteVariable
     *
     * @return worked - if deleted, return true
     */
    public boolean deleteVariable(Declaration var) {
        int foundi;
        foundi = findVarIndex(var.varName);
        if (foundi == -1) {//not found
            return false;
        } else {
            //delete the variable from the list
            variables.removeElementAt(foundi);
            return (true);
        }
    }
    //---------------------------------------------------------------	

    /**
     * deleteVariable
     *
     * @return worked - if deleted, return true
     */
    public boolean deleteVariable(String varName) {
        int foundi;
        foundi = findVarIndex(varName);
        if (foundi == -1) {
            return (false);
        } else {
            //delete the variable from the list
            variables.removeElementAt(foundi);
            return (true);
        }
    }


    //---------------------------------------------------------------	

    /**
     * replaceVariable
     *
     * @return worked - true if success false otherwise
     */
    public boolean replaceVariable(String varName, Declaration var) {
        int foundi;
        foundi = findVarIndex(varName);
        if (foundi == -1) {
            return (false);
        } else {
            //delete the variable from the list
            variables.setElementAt(var, foundi);
            return (true);
        }
    }
    //---------------------------------------------------------------	

    /**
     * replaceVariable
     *
     * @return worked - true if success false otherwise
     */
    public boolean replaceVariable(int foundi, Declaration var) {
        //delete the variable from the list
        variables.setElementAt(var, foundi);
        return (true);
    }

    //---------------------------------------------------------------	

    /**
     * moveVariable
     *
     * @param gotoi can be from 0 to the size of the variable list
     * @return worked - true if success false otherwise
     * The way this works is that the goto index is as if
     * we cloned the named variable, inserted it in the list
     * at the specified location (thus the list would be
     * the incoming size plus 1), and then deleted the original
     * variable.
     * Variables move down as others are inserted.
     */
    public boolean moveVariable(int gotoi, String varName) {
        int foundi;
        foundi = findVarIndex(varName);
        int vectorsize = variables.size();
        if (gotoi < 0) {
            return false;
        }
        if (gotoi > vectorsize) {
            return false;
        }
        if (foundi == -1) {
            return false;
        } else {
            Declaration var = (Declaration) variables.elementAt(foundi);
            if (foundi == gotoi) {
                return true;
            } else if (gotoi == 0) { //put at begining of list
                variables.removeElementAt(foundi);
                variables.add(0, var); //add at begining
                return true;
            } else if (gotoi == vectorsize) { //put at end of list
                variables.removeElementAt(foundi);
                variables.add(var); //add at end
                return true;
            } else {  //now does the deletion affect the goto position
                if (foundi < gotoi) {
                    variables.removeElementAt(foundi);
                    // todo: this throws an exception - we should catch it here
                    variables.add((gotoi - 1), var); //add somewhere  from 0 to end-1
                    return true;
                } else {  //foundi>gotoi
                    variables.removeElementAt(foundi);
                    variables.add(gotoi, var); //add somewhere  from 0 to end-1
                    return true;
                }
            }
        }
    }
    //---------------------------------------------------------------	

    /**
     * fillVariableName
     *
     * @return worked
     */
    public Declaration fillVariableName(EditorFrame parentFrame, String dialogType, String message) {
        Declaration var;
        String namestr;
        namestr = JOptionPane.showInputDialog
                (null, message, "Variable Name", JOptionPane.QUESTION_MESSAGE);
        if (namestr == null) return (null); //cancel
        namestr = namestr.trim().replace(' ', '_');
        if (namestr.equals("")) return (null); //blank
        int index = findVarIndex(namestr);
        if (index == -1) {//not found
            var = new Declaration(dialogType, namestr);
        } else { //already in list
            var = (Declaration) variables.elementAt(index);
            if (!(var.varDialogType.equals(dialogType))) {
                String errstr = "Module:There is a : " + var.varDialogType + " with that name already.";
                parentFrame.warningPopup.display(errstr);
                return (null);
            }
        }
        return (var);
    }

    //---------------------------------------------------------------	

    /**
     * Get specified module from the file system
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error
     *                                occurred
     */
    public void getModuleFromFile(String libPath, String moduleName, String versionName) throws ClassNotFoundException, IOException {
        FileInputStream fis;
        ObjectInputStream ois;

        try {
            fis = SCSUtility.getModuleStream(libPath, moduleName, versionName);
            ois = new ObjectInputStream(fis);
            read(ois);
            fis.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Error:Module:getModuleFromFile  ClassNotFoundException");
            throw new ClassNotFoundException("Module getModuleFromFile ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:Module:getModuleFromFile IOException");
            throw new IOException("Module getModuleFromFile IOException");
        }
    }
    //---------------------------------------------------------------	

    /**
     * Get specified module from the file system
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error
     *                                occurred
     */

    public void getModuleFromFileUsingNick(String libraryNickName, String moduleName, String versionName)
            throws IOException, ClassNotFoundException {
        FileInputStream fis;
        ObjectInputStream ois;

        try {
            fis =
                    SCSUtility.getModuleStreamUsingNick(libraryNickName, moduleName, versionName);
            ois = new ObjectInputStream(fis);
            read(ois);
            assert fis != null;
            fis.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Error:Module:getModuleFromFileUsingNick  ClassNotFoundException");
            throw new ClassNotFoundException("Module getModuleFromFileUsingNick ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:Module:getModuleFromFileUsingNick IOException");
            throw new IOException("Module getModuleFromFileUsingNick IOException");
        }
    }
    //---------------------------------------------------------------	

    /**
     * Read this module from the input stream but only the flag info
     *
     * @throws IOException if an IO error occurred
     */
    public void readThruFlags(ObjectInputStream ois)
            throws IOException {
        if (debugM) {
            System.out.println("Debug:Module:readThruFlags"); //if debugM
        }
        try {
            sifVersionNum = ois.readInt();  // what software made this file
            hasIcon = ois.readBoolean();
            hasSchematic = ois.readBoolean();
            hasNslm = ois.readBoolean();
            isModel = ois.readBoolean();
            getCurrentVersion = ois.readBoolean();//specific version of icons=1, float=0
        } catch (IOException e) {
            System.err.println("Error:Module:readThruFlags: IOException");
            throw new IOException("Module:readThruFlags: IOException");
        }
        if (debugM) {
            System.out.println("Debug:Module:sifVersionNum: " + sifVersionNum);//if debugM
            System.out.println("Debug:Module:hasIcon: " + hasIcon);//if debug
            System.out.println("Debug:Module:hasSchematic: " + hasSchematic);//if debug
            System.out.println("Debug:Module:hasNslm: " + hasNslm);//if debug
            System.out.println("Debug:Module:isModel: " + isModel);//if debug
            System.out.println("Debug:Module:getCurrentVersion: " + getCurrentVersion);//if debug
        }
    }
    //---------------------------------------------------------------	

    /**
     * Read this module from the input stream but only until the icon info
     *
     * @throws IOException if an IO error occurred
     */
    public void readThruFlagsAndNames(ObjectInputStream ois)
            throws IOException {
        if (debugM) {
            System.out.println("Debug:Module:readThruFlagsAndNames");//if debug
        }
        try {
            readThruFlags(ois);
            libNickName = ois.readUTF();  // what lib did this come out of
            moduleName = ois.readUTF();  // this is the module name
            versionName = ois.readUTF();
            moduleType = ois.readUTF();
            arguments = ois.readUTF();
            buffering = ois.readBoolean();
            //variables
            if (variables.size() != 0)
                variables.removeAllElements();
            int n = ois.readInt();  // read number of variables
            for (int ix = 0; ix < n; ix++) {
                Declaration decl = new Declaration();
                decl.read(ois);
                variables.addElement(decl);
            }

            if (debugM) {
                System.out.println("Debug:Module:libNickName: " + libNickName);//if debug
                System.out.println("Debug:Module: moduleName: " + moduleName);//if debug
                System.out.println("Debug:Module: versionName: " + versionName);//if debug
                System.out.println("Debug:Module: moduleType is: " + moduleType);//if debug
                System.out.println("Debug:Module: bufferng is: " + buffering);//if debug
            }
        } catch (IOException e) {
            System.err.println("Error:Module: readThruFlagsAndNames IOException");
            throw new IOException("Module readThruFlagsAndNames IOException");
        }
    }

    //---------------------------------------------------------------	

    /**
     * Read this module from the input stream but only thru the icon info
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error
     *                                occurred
     */

    public void readThruIcon(ObjectInputStream ois)
            throws ClassNotFoundException, IOException {

        if (debugM) {
            System.out.println("Debug:Module: readThruIcon ");//if debug
        }
        try {
            readThruFlagsAndNames(ois);
            if (!hasIcon) {
                myIcon = null;
            } else {
                myIcon = new Icon();
                myIcon.read(ois);  //throws ClassNotFoundException
                if (debugM) {
                    System.out.println("Debug:Module:readThruIcon: " + myIcon.moduleName);//if debug
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error:Module:readThruIcon ClassNotFoundException");
            throw new ClassNotFoundException("Module:readThruIcon ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:Module:readThruIcon IOException");
            throw new IOException("Module:readThruIcon IOException");
        }
    }

    //---------------------------------------------------------------	

    /**
     * Read this module from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error
     *                                occurred
     */
    public void read(ObjectInputStream ois)
            throws ClassNotFoundException, IOException {
        try {
            readThruIcon(ois);
            if (!hasSchematic) {
                mySchematic = null;
            } else {
                mySchematic = new Schematic();
                mySchematic.read(ois);  // not recursive
            }
            if (!hasNslm) {
                myNslm = null;
            } else {
                myNslm = new NSLM();
                myNslm.read(ois);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error:Module: read ClassNotFoundException");
            throw new ClassNotFoundException("Module read ClassNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:Module: read IOException");
            throw new IOException("Module read IOException");
        }
    }
    //---------------------------------------------------------------	

    /**
     * Write this module to the output stream os.
     *
     * @throws IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os)
            throws IOException {
        try {
            os.writeInt(this.sifVersionNum);
            // write flags
            os.writeBoolean(hasIcon);
            os.writeBoolean(hasSchematic);
            os.writeBoolean(hasNslm);
            os.writeBoolean(isModel);
            os.writeBoolean(getCurrentVersion);
            os.writeUTF(libNickName);
            os.writeUTF(moduleName);
            os.writeUTF(versionName);             // 99/4/27 aa
            os.writeUTF(moduleType);                  // module type:
            os.writeUTF(arguments);
            os.writeBoolean(buffering);
            //variables
            os.writeInt(variables.size());
            for (int ix = 0; ix < variables.size(); ix++) {
                ((Declaration) (variables.elementAt(ix))).write(os);
            }

            // current choices are : model, basic, input, output, pureNSLM
            if (myIcon != null) {
                // when writing the icon template info.
                myIcon.write(os, true);    // write the icon  representation info for this particular module
            }
            if (mySchematic != null) {
                mySchematic.write(os);  // Write the schematic info
            }
            if (myNslm != null) {
                myNslm.write(os);  // Write the NSLM view of the module
            }
        } catch (IOException e) {
            System.err.println("Error:Module: write IOException");
            throw new IOException("Module write IOException");
        }
    }
    //---------------------------------------------------------------	

    /**
     * Write this module to the output stream os and PrintWrite pw
     * for a dump of the module if need be
     *
     * @throws IOException if an IO error occurred
     *                     note: this method should only be called if in EditorFrame the variable writeSuf is set to true.
     */
    public void write(ObjectOutputStream os, PrintWriter pw)
            throws IOException {
        try {
            os.writeInt(this.sifVersionNum);
            // write flags
            os.writeBoolean(hasIcon);
            os.writeBoolean(hasSchematic);
            os.writeBoolean(hasNslm);
            os.writeBoolean(isModel);
            os.writeBoolean(getCurrentVersion);
            os.writeUTF(libNickName);
            os.writeUTF(moduleName);
            os.writeUTF(versionName);             // 99/4/27 aa
            os.writeUTF(moduleType);                  // module type:
            os.writeUTF(arguments);
            os.writeBoolean(buffering);
            //--------now make a printable copy of the file
            pw.print(">>> Module: ");
            pw.print("\n");
            pw.print("sifVersionNum: ");
            pw.print((new Integer(this.sifVersionNum)).toString());
            pw.print("\n");
            // write flags
            pw.print("hasIcon: ");
            pw.print((Boolean.valueOf(hasIcon)).toString());
            pw.print("\n");
            pw.print("hasSchematic: ");
            pw.print((Boolean.valueOf(hasSchematic)).toString());
            pw.print("\n");
            pw.print("hasNslm: ");
            pw.print((Boolean.valueOf(hasNslm)).toString());
            pw.print("\n");
            pw.print("isModel: ");
            pw.print((Boolean.valueOf(isModel)).toString());
            pw.print("\n");
            pw.print("getCurrentVersion: ");
            pw.print((Boolean.valueOf(getCurrentVersion)).toString());
            pw.print("\n");
            pw.print("libNickName: ");
            pw.print(libNickName);
            pw.print("\n");
            pw.print("moduleName: ");
            pw.print(moduleName);
            pw.print("\n");
            pw.print("versionName: ");
            pw.print(versionName);             // 99/4/27 aa
            pw.print("\n");
            pw.print("moduleType: ");
            pw.print(moduleType);                  // module type:
            pw.print("\n");
            pw.print("arguments: ");
            pw.print(arguments);                  // module type:
            pw.print("\n");
            pw.print("buffering: ");
            pw.print((Boolean.valueOf(buffering)).toString());
            pw.print("\n");

            //variables
            os.writeInt(variables.size());
            pw.print("variables: ");
            pw.print(variables.size());
            pw.print("\n");

            // current choices for a variable are:model, basic, input, output, pureNSLM
            for (int ix = 0; ix < variables.size(); ix++) {
                Declaration valerie = (Declaration) (variables.elementAt(ix));
                valerie.write(os);  //todo: do we need a valerie.write(os,pw)?
                pw.println("variable:  " + ix);
                valerie.writeAllChars(pw);
            }

            if (myIcon != null) {
                // icon template info.
                // write icon representation info for this particular module
                myIcon.write(os, true);
                myIcon.writeAllChars(pw, true);
            }
            if (mySchematic != null) {
                mySchematic.write(os);  // Write the schematic info
                mySchematic.writeAllChars(pw);  // Write the schematic info
            }
            if (myNslm != null) {
                myNslm.write(os);  // Write the NSLM view of the module
                myNslm.writeAllChars(pw);  // Write the NSLM view of the module
            }
        } catch (IOException e) {
            System.err.println("Error:Module: write IOException");
            throw new IOException("Module write IOException");
        }
    }

    //---------------------------------------------------------------	

    /**
     * writeAllChars this module to the output stream os.
     */
    public void writeAllChars(PrintWriter pw) {
        /*	try {*/
        pw.print("module: ");
        pw.print("\n");
        pw.print("sifVersionNum: ");
        pw.print((new Integer(this.sifVersionNum)).toString());
        pw.print("\n");
        // write flags
        pw.print("hasIcon: ");
        pw.print((Boolean.valueOf(hasIcon)).toString());
        pw.print("\n");
        pw.print("hasSchematic: ");
        pw.print((Boolean.valueOf(hasSchematic)).toString());
        pw.print("\n");
        pw.print("hasNslm: ");
        pw.print((Boolean.valueOf(hasNslm)).toString());
        pw.print("\n");
        pw.print("isModel: ");
        pw.print((Boolean.valueOf(isModel)).toString());
        pw.print("\n");
        pw.print("getCurrentVersion: ");
        pw.print((Boolean.valueOf(getCurrentVersion)).toString());
        pw.print("\n");
        pw.print("libNickName: ");
        pw.print(libNickName);
        pw.print("\n");
        pw.print("moduleName: ");
        pw.print(moduleName);
        pw.print("\n");
        pw.print("versionName: ");
        pw.print(versionName);             // 99/4/27 aa
        pw.print("\n");
        pw.print("moduleType: ");
        pw.print(moduleType);                  // module type:
        pw.print("\n");
        pw.print("arguments: ");
        pw.print(arguments);                  // module type:
        pw.print("\n");
        pw.print("buffering: ");
        pw.print((Boolean.valueOf(buffering)).toString());
        pw.print("\n");
        //variables
        pw.print("variables: ");
        pw.print(variables.size());
        pw.print("\n");
        for (int ix = 0; ix < variables.size(); ix++) {
            ((Declaration) (variables.elementAt(ix))).writeAllChars(pw);
        }

        // current choices are : model, basic, input, output, pureNSLM
        if (myIcon != null) {
            myIcon.writeAllChars(pw, true);    // write the icon  representation info for this particular module
        }
        if (mySchematic != null) {
            mySchematic.writeAllChars(pw);  // Write the schematic info
        }
        //if (myNslm != null) {
        //myNslm.writeAllChars(pw);  // Write the NSLM view of the module
        //}
        /*
	}
	catch (IOException e) {
	    System.err.println("Error:Module: writeAllChars IOException");
	    throw new IOException("Module writeAllChars IOException");
	}
	    */
    }


    //---------------------------------------------------------------	

    /**
     * writeNslm this module to the output stream os.
     *
     * @throws IOException if an IO error occurred
     */
    public void writeNslm(PrintWriter pw) throws IOException, BadLocationException {
        if (!hasNslm) {  //if no NSLM, then return
            System.err.println("Warning:Module:writeNslm: This module does not have a NSLM view. " + moduleName);
            return;
        }

        try {
            //System.out.println("Debug:Module:writeNslm:1");
            if (!(myNslm.comment1.equals(""))) {
                pw.print("/** \n" + myNslm.comment1);
                pw.print("\n");
                pw.print("*/" + "\n");
            }
            //System.out.println("Debug:Module:writeNslm:2");
            if ((myNslm.verbatimNSLC) || (myNslm.verbatimNSLJ)) {
                writeVerbatim(pw);
                return;
            }
            //System.out.println("Debug:Module:writeNslm:3");
            //do import statements
            pw.print("\n");
            Vector<String> pathsVector = new Vector<>();

            for (int ix = 0; ix < variables.size(); ix++) {
                Declaration sid = (Declaration) variables.elementAt(ix);
                if ((sid.varDialogType.equals("SubModule")) && (!(sid.modLibNickName.equals(""))) && (!(sid.varType.equals(""))) && (!(sid.modVersion.equals("")))) {
                    String pseudoPath = sid.modLibNickName + "." + sid.varType + "." + sid.modVersion;
                    System.out.println("Debug:Module:writeNslm:" + pseudoPath);
                    if (!(pathsVector.contains(pseudoPath))) {
                        pathsVector.addElement(pseudoPath);
                        sid.writeImport(pw);//can throw BadLocationException
                    }
                }
            } //end for ix<variables.size()
            //System.out.println("Debug:Module:writeNslm:4");
            pw.print("\n");

            if (isModel) {
                pw.print("nslModel");
            } else {
                String t1 = moduleType.substring(1);
                pw.print("n" + t1);
            }
            pw.print(" " + moduleName + "(" + arguments + ")");
            if (hasNslm) {
                if (!(myNslm.extendsWhat.equals(""))) {
                    pw.print(" extends " + myNslm.extendsWhat + "(");
                    pw.print(myNslm.whatsParams + ")");
                }
            }
            //System.out.println("Debug:Module:writeNslm:5");
            pw.print("{\n");
            pw.print("\n");
            pw.print("//NSL Version: " + SCSUtility.nslmVersionNum + "\n");
            pw.print("//Sif Version: " + SCSUtility.sifVersionNum + "\n");
            pw.print("//libNickName: " + libNickName + "\n");
            pw.print("//moduleName:  " + moduleName + "\n");
            pw.print("//versionName: " + versionName + "\n");
            pw.print("//floatSubModules: " + (Boolean.valueOf(getCurrentVersion)).toString() + "\n");
            pw.print("\n");

            if (!(myNslm.comment2.equals(""))) {
                pw.print("/** \n" + myNslm.comment2);
                pw.print("\n");
                pw.print("*/" + "\n");
            }

            //variables
            pw.print("\n//variables \n");
            Declaration nVar;
            for (int ix = 0; ix < variables.size(); ix++) {
                nVar = (Declaration) (variables.elementAt(ix));
                //System.out.println("Debug:Module:variable"+ix+" "+nVar.varName);
                nVar.writeNslm(pw);
            }

            //no comment3 block since they can put this in the methods section
            pw.print("\n//methods \n");
            //todo: put the setBuffering command in initSys in methods
            //if (buffering) pw.print("setBuffering(true);\n");

            if ((myNslm != null) && (myNslm.methods != null)) {
                int strLength = myNslm.methods.getLength();
                String methStr = (myNslm.methods.getText(0, strLength)).trim(); //badLocationException
                pw.print(methStr); //not efficient
                pw.print("\n");
            }

            writeMakeConn(pw);

            pw.print("\n");
            pw.print("}//end " + moduleName + "\n");
            pw.print("\n"); //THIS IS THE LAST LINE
            pw.flush();
            System.out.println("Debug:Module:writeNslm:12");
        }//end try 1
        catch (BadLocationException e) {
            String errstr = "Module: writeNslm BadLocationException";
            System.err.println(errstr);
            throw (new BadLocationException(errstr, e.offsetRequested()));
        } catch (FileNotFoundException e) {
            System.err.println("Module: writeNslm FileNotFoundException");
            throw (new FileNotFoundException("Module writeNslm FileNotFoundException"));
        } catch (IOException e) {
            System.err.println("Error:Module: writeNslm IOException");
            throw (new IOException("Module writeNslm IOException"));
        }
    }

    //------------------------------------
    public void writeVerbatim(PrintWriter pw) throws BadLocationException {

        if (myNslm == null) return;
        if (myNslm.verbatimNSLC) {
            pw.print("verbatim_NSLC;");
        } else {
            pw.print("verbatim_NSLJ;");
        }
        if (myNslm.methods != null) {
            try {
                //todo: this is not what we want. I think we want length
                // endPos is the end of the view window
                int strLength = myNslm.methods.getLength();
                String methStr = (myNslm.methods.getText(0, strLength)).trim();//badLocationException
                pw.print(methStr); //not efficient
                pw.print("\n");
            } catch (BadLocationException e) {
                String errstr = "Module: writeVerbatim BadLocationException";
                System.err.println(errstr);
                throw (new BadLocationException(errstr, e.offsetRequested()));
            }
        }
        pw.print("verbatim_off;");
    }

    //------------------------------------
    public void writeMakeConn(PrintWriter pw) {

        if (mySchematic == null) return;
        if (mySchematic.drawableObjs == null) return;

        pw.print("public void makeConn(){");
        mySchematic.writeNslm(pw);
        pw.print("}");

    }
    //------------------------------------


} //end class Module

















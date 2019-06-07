/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * Declaration - A class representing the declaration part of variables in a
 * module. A list of variables can be found in the Module class.
 *
 * @author Weifang Xie, Alexander
 * @version %I%, %G%
 * @since JDK8
 * The name of this class should really be called Variable!
 */

import java.io.*;
import java.util.Vector;

public class Declaration {
    String varName = "";
    String varScope = "private"; //for variables not ports or submodules
    boolean varConstant = false;
    String varCategoryType = "";
    int varDimensions = 0;
    String varType = "";
    String varParams = "";
    String varInits = "";
    String varDialogType = "";
    //DialogTypes:
    //InputPort
    //OutputPort
    //SubModule
    //BasicVariable
    String varComment = "";
    boolean portBuffering = false;
    // next two fields valid input is:
    //'R' for right to left, 'T' for top to bottom
    //'L' for left to right, 'B' for bottom to top
    char portIconDirection = 'L';
    char portSchDirection = 'L';
    // valid input is: 'E' for excitatory , 'I' for inhibitory, 'O' for other
    char portSignalType = 'E';
    String modLibNickName = "";  //for submodules
    String modVersion = "";      //for submodules
    boolean modGetCurrentVersion = true;      //for submodules
    boolean inIcon = false;
    boolean inSch = false;
    boolean inNslm = false;

    /**
     * Constructor of this class with no parameters.
     */
    Declaration() {
        //varParam=new Vector();
    }

    /**
     * Constructor of this class with one parameters.
     */
    Declaration(String dialogType) {
        varDialogType = dialogType;
        if (varDialogType.equals("OutputPort")) {
            portSignalType = 'O';
        }
        if ((varDialogType.equals("InputPort")) || (varDialogType.equals("OutputPort")) || (varDialogType.equals("SubModule"))) {
            varScope = "public";
        }
    }

    /**
     * Constructor of this class with two parameters.
     * this is the constructor to use with ports
     */
    Declaration(String dialogType, String name) {

        varDialogType = dialogType;
        varName = name;
        if ((varDialogType.equals("InputPort")) || (varDialogType.equals("OutputPort")) || (varDialogType.equals("SubModule"))) {
            varScope = "public";
        }
        if (varDialogType.equals("OutputPort")) {
            portSignalType = 'O';
        }
    }

    /**
     * Constructor of this class with another declaration
     */
    Declaration(Declaration var) {
        varName = var.varName;
        varScope = var.varScope;
        varConstant = var.varConstant;
        varCategoryType = var.varCategoryType;
        this.varDimensions = var.varDimensions;
        varType = var.varType;
        varParams = var.varParams;
        varInits = var.varInits;
        varDialogType = var.varDialogType;
        varComment = var.varComment;
        this.portBuffering = var.portBuffering;
        // next two fields valid input is:
        //'R' for right to left, 'T' for top to bottom
        //'L' for left to right, 'B' for bottom to top
        this.portIconDirection = var.portIconDirection;
        this.portSchDirection = var.portSchDirection;
        // valid input is: 'E' for excitatory , 'I' for inhibitory
        this.portSignalType = var.portSignalType;
        this.modLibNickName = var.modLibNickName;
        this.modVersion = var.modVersion;
        this.modGetCurrentVersion = var.modGetCurrentVersion;      //for submodules
        this.inIcon = var.inIcon;
        this.inSch = var.inSch;
        this.inNslm = var.inNslm;
    }

    /**
     * Constructor of this class, using name, scope, type, param and init to set
     * to the corresponding fields of this class.
     *
     * @author Weifang Xie, Alexander (updated 2/08/01)
     * @version     %I%, %G%
     *
     * @param       name the name of this variable declaration
     * @param       scope    the scope of this variable declaration
     * @param       constant variabl declaration constant
     * @param       catagoryType    the type of this variable declaration without dim
     * @param       type    the type of this variable declaration with dim
     * @param       param    an string of parameters of this variable declaration
     * @param       init    initial value of this variable declaration
     * @since JDK1.1
     * The name of this class should really be called Variable!
     */


    public Declaration(String name, String scope, boolean constant, String catagoryType, String type, int varDimensions, String param, String init, String dialogType, String comment, boolean buffering, char portIconDirection, char portSchDirection, char portSignalType, String modLibNickName, String modVersion, boolean modGetCurrentVersion, boolean inicon, boolean insch, boolean innslm) {
        varName = name;
        varScope = scope;
        varConstant = constant;
        varCategoryType = catagoryType;
        this.varDimensions = varDimensions;
        varType = type;
        varParams = param;
        //    	varParams=new Vector();
        //	for (int i=0; i<param.size(); i++) {
        //	    varParams.addElement((String)param.elementAt(i));
        //	}
        varInits = init;
        varDialogType = dialogType;
        varComment = comment;
        this.portBuffering = buffering;
        // next two fields valid input is:
        //'R' for right to left, 'T' for top to bottom
        //'L' for left to right, 'B' for bottom to top
        this.portIconDirection = portIconDirection;
        this.portSchDirection = portSchDirection;
        // valid input is: 'E' for excitatory , 'I' for inhibitory
        this.portSignalType = portSignalType;
        this.modLibNickName = modLibNickName;
        this.modVersion = modVersion;
        this.modGetCurrentVersion = modGetCurrentVersion;
        this.inIcon = inicon;
        this.inSch = insch;
        this.inNslm = innslm;
    }
    //---------------------------------------------------------

    /**
     * duplicate or clone
     */
    public Declaration duplicate() {
        Declaration bob = new Declaration();
        bob.varName = this.varName;
        bob.varScope = this.varScope;
        bob.varConstant = this.varConstant;
        bob.varCategoryType = this.varCategoryType;
        bob.varDimensions = this.varDimensions;
        bob.varType = this.varType;
        bob.varParams = this.varParams;
        bob.varInits = this.varInits;
        bob.varDialogType = this.varDialogType;
        bob.varComment = this.varComment;
        bob.portBuffering = this.portBuffering;
        // next two fields valid input is:
        //'R' for right to left, 'T' for top to bottom
        //'L' for left to right, 'B' for bottom to top
        bob.portIconDirection = this.portIconDirection;
        bob.portSchDirection = this.portSchDirection;
        // valid input is: 'E' for excitatory , 'I' for inhibitory
        bob.portSignalType = this.portSignalType;
        bob.modLibNickName = this.modLibNickName;
        bob.modVersion = this.modVersion;
        bob.modGetCurrentVersion = this.modGetCurrentVersion;
        bob.inIcon = this.inIcon;
        bob.inSch = this.inSch;
        bob.inNslm = this.inNslm;
        return (bob);
    }
    //-------------------------------------------
    /**
     * merge variables
     */
    /*
      public boolean mergeVar(Declaration newguy, String editorType){ 
      boolean worked=false;

      if (newguy==null) return(worked=false);
      if (newguy.varDialogType==null) return(worked=false);
      if ((varDialogType.equals(newguy.varDialogType))) return(worked=false);
      if (newguy.varName==null) return(worked=false);

      // probably here because the two variabls have the same name
      // and same type of info
      if ((newguy.varName!=null)&&(!(newguy.varName.equal("")))) {
      varName=newguy.varName;
      }
      if ((newguy.varScope!=null)&&(!(newguy.varScope.equal("")))) {
      varScope=newguy.varScope;
      }
      if (editorType.equals("Nslm")) {  //only editor to set the constant
      varConstant=newguy.varConstant;
      }
      if (editorType.equals("Nslm")) {  //only editor to set the catagory and dim
      varCategoryType=newguy.varCategoryType;
      this.varDimensions=newguy.varDimensions;
      }
      //types should match - else go with new guy
      if ((varType==null)&&(newguy.varType==null)) {
      return(worked=false);
      } else if ((varType!=null)&&(newguy.varType==null)) {
      varType=varType;
      } else {
      varType=newguy.varType;
      }
      if (editorType.equals("Nslm")) { 
      varParams=newguy.varParams;
      } else if ((newguy.varParams!=null)&&(!(newguy.Params.equal("")))) {
      varParams=varParams;
      } else {
      varParams=newguy.varParams;
      }

      if (editorType.equals("Nslm")) { 
      varInits=newguy.varInits;
      } else if ((newguy.varInits!=null)&&(!(newguy.Inits.equal("")))) {
      varInits=varInits;
      } else {
      varInits=newguy.varInits;
      }


      if (editorType.equals("Nslm")) {
      varComment=newguy.varComment;
      } else if ((newguy.varComment!=null)&&(!(newguy.Comment.equal("")))) {
      varComment=varComment;
      } else {
      varComment=newguy.varComment;
      }

      //change port buffering no matter what
      this.portBuffering=newguy.portBuffering;


      // next two fields valid input is:
      //'R' for right to left, 'T' for top to bottom
      //'L' for left to right, 'B' for bottom to top
      if (editorType.equals("Icon")) {
      this.portIconDirection=newguy.portIconDirection;
      // valid input is: 'E' for excitatory , 'I' for inhibitory
      this.portSignalType=newguy.portSignalType;
      }
      if (editorType.equals("Schematic")) {
      this.portSchDirection=newguy.portSchDirection;
      }
      //always go with new for module information
      this.modLibNickName=newguy.modLibNickName;
      //this.modModuleName==varName;
      this.modVersion=newguy.modVersion;
      this.modGetCurrentVersion=newguy.modGetCurrentVersion;      //for submodules

      return(worked=true);
      }
    */
    //-------------------------------------------

    /**
     * scopeIsOther
     * note: check for null before calling
     */
    public boolean scopeIsOther() {

        //the scope will say something other than private,public,protected
        if (varScope == null) return (true);
        return (!varScope.equals("private")) && (!varScope.equals("public")) && (!varScope.equals("protected"));
    }

    //-------------------------------------------

    /**
     * Equal - this actually compares the varNames not the whole
     * structure.
     * Put here for Vector use: indexOf, but the equals is
     * called by the incoming variable to indexOf.
     */
    public boolean equal(Object info) {
        if (info == null) {
            return false;
        }
        if (info instanceof String) {
            return varName.equals(info);
        }
        if (info instanceof Declaration) {
            return varName.equals(((Declaration) info).varName);
        } else {
            return false;
        }

    }
    //-------------------------------------------

    /**
     * Read this variable declaration from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException {
        varName = os.readUTF();
        varScope = os.readUTF();
        varConstant = os.readBoolean();
        varCategoryType = os.readUTF();
        varDimensions = os.readInt();
        varType = os.readUTF();


        //if (varParams.size()!=0)
        //	   varParams.removeAllElements();
        //int n=os.readInt();
        //for (int i=0; i<n; i++) {
        //    varParams.addElement(os.readUTF());
        //}
        varParams = os.readUTF();
        varInits = os.readUTF();
        varDialogType = os.readUTF();
        varComment = os.readUTF();
        portBuffering = os.readBoolean();
        portIconDirection = os.readChar();
        portSchDirection = os.readChar();
        portSignalType = os.readChar();
        modLibNickName = os.readUTF();
        modVersion = os.readUTF();
        modGetCurrentVersion = os.readBoolean();
        inIcon = os.readBoolean();
        inSch = os.readBoolean();
        inNslm = os.readBoolean();
    }

    /**
     * Write this variable declaration to the output stream os.
     *
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os)
            throws IOException {
        try {
            os.writeUTF(varName);
            os.writeUTF(varScope);
            os.writeBoolean(varConstant);
            os.writeUTF(varCategoryType);
            os.writeInt(varDimensions);
            os.writeUTF(varType);

            //	    os.writeInt(varParams.size());
            //	    for (int i=0; i<varParams.size(); i++) {
            //		os.writeUTF((String)varParams.elementAt(i));
            //	    }
            os.writeUTF(varParams);
            os.writeUTF(varInits);
            os.writeUTF(varDialogType);
            os.writeUTF(varComment);
            os.writeBoolean(portBuffering);
            os.writeChar(portIconDirection);
            os.writeChar(portSchDirection);
            os.writeChar(portSignalType);
            os.writeUTF(modLibNickName);
            os.writeUTF(modVersion);
            os.writeBoolean(modGetCurrentVersion);
            os.writeBoolean(inIcon);
            os.writeBoolean(inSch);
            os.writeBoolean(inNslm);
        } catch (IOException e) {
            throw new IOException("Declaration write IOException");
        }
    }
    //---------------------------------------------------------------

    /**
     * writeAllChars this module to the output stream os.
     */
    public void writeAllChars(PrintWriter pw) {
        //	try {
        pw.print("varName: ");
        pw.print(varName);
        pw.print("\n");
        pw.print("varScope: ");
        pw.print(varScope);
        pw.print("\n");
        pw.print("varConstant: ");
        pw.print((Boolean.valueOf(varConstant)).toString());
        pw.print("\n");
        pw.print("varCategoryType: ");
        pw.print(varCategoryType);
        pw.print("\n");
        pw.print("varDimensions: ");
        pw.print((new Integer(varDimensions)).toString());
        pw.print("\n");
        pw.print("varType: ");
        pw.print(varType);
        pw.print("\n");

        pw.print("varParams: ");
        pw.print(varParams);
        pw.print("\n");
        pw.print("varInits: ");
        pw.print(varInits);
        pw.print("\n");
        pw.print("varDialogType: ");
        pw.print(varDialogType);
        pw.print("\n");
        pw.print("varComment: ");
        pw.print(varComment);
        pw.print("\n");
        pw.print("portBuffering: ");
        pw.print((Boolean.valueOf(portBuffering)).toString());
        pw.print("\n");
        pw.print("portIconDirection: ");
        pw.print((new Character(portIconDirection)).toString());
        pw.print("\n");
        pw.print("portSchDirection: ");
        pw.print((new Character(portSchDirection)).toString());
        pw.print("\n");
        pw.print("portSignalType: ");
        pw.print((new Character(portSignalType)).toString());
        pw.print("\n");

        pw.print("modLibNickName: ");
        pw.print(modLibNickName);
        pw.print("\n");

        pw.print("modVersion: ");
        pw.print(modVersion);
        pw.print("\n");
        pw.print("modGetCurrentVersion: ");
        pw.print((Boolean.valueOf(modGetCurrentVersion)).toString());
        pw.print("\n");
        pw.print("modGetCurrentVersion: ");

        //	} //end try
        //	catch (IOException e) {
        //	    System.err.println("Error:Declaration: writeAllChars IOException");
        //	    throw new IOException("Module writeAllChars IOException");
        //	}

    }//end writeAllChars
    //--------------------------------------------------

    /**
     * writeImport - write Import statments for each submodule
     *
     * @exception IOException     if an IO error occurred
     * Note: to make the mod files portable, the paths
     * should not contain the names of the whole path to the libraries;
     * The libraries should instead be in the CLASSPATH in
     * the order listed in the LIBRARY_PATHS_LIST variable
     * minus the last directory
     */
    public void writeImport(PrintWriter pw) throws IOException {
        //todo: need to also allow for NSLC type import statements
        String libPath;
        String libPathLast;
        String srcDirStr;
        String thisVersion;
        Vector pathsVector = new Vector();

        if ((varDialogType.equals("SubModule")) && (!(modLibNickName.equals(""))) && (!(varType.equals(""))) && (!(modVersion.equals("")))) {
            try {
                libPath = SCSUtility.getLibPathName(modLibNickName);
                //System.out.println("Debug:Declaration:writeImport:"+libPath);
                libPathLast = SCSUtility.getLibPathLast(libPath);
                //System.out.println("Debug:Declaration:writeImport:"+libPathLast);
                thisVersion = modVersion;
                if (modGetCurrentVersion) { //float the version
                    thisVersion = SCSUtility.getHighestVersionString(libPath, varType);
                    //go get the most recent version of this module
                }
                srcDirStr = SCSUtility.catFullPathToSrc(libPathLast, varType, thisVersion);
                pw.print("nslImport " + srcDirStr + File.separator + "*;\n");

            } catch (FileNotFoundException e) {
                System.err.println("Declaration: current module src dir not found: " + varType);
                throw (new FileNotFoundException());
            } catch (IOException e) {
                System.err.println("Declaration: current module IOException while looking for: " + varType);
                throw (new IOException());
            }

        }
    }//end writeImport
    //---------------------------------------------------------------

    /**
     * writeNslm this module to the output stream os.
     *
     *
     */
    public void writeNslm(PrintWriter pw) {

        if ((varDialogType.equals("InputPort")) ||
                (varDialogType.equals("OutputPort"))) {
            if (varScope.equals("")) varScope = "public";
            pw.print(varScope + " ");
            pw.print(varType + " " + varName);
            pw.print("(" + varParams + ")");
            pw.print("; // " + varComment + "\n");

            //todo: has to go into initSys in myNslm.methods
            //if ((varDialogType.equals("OutputPort"))&&(portBuffering)) {
            //    pw.print(varName+".nslSetBuffering(true)\n");
            //}
            return;
        }
        if (varDialogType.equals("SubModule")) {
            if (varScope.equals("")) varScope = "public";
            pw.print(varScope + " ");
            pw.print(varType + " " + varName);
            pw.print("(" + varParams + ")");
            pw.print("; // " + varComment + "\n");
            return;
        }
        if (varDialogType.equals("BasicVariable")) {
            if (varScope.equals("")) varScope = "protected";
            pw.print(varScope + " ");
            if (varConstant) {
                //todo: fix for NSLC
                pw.print("final static "); //for java only
            }
            pw.print(varType + " " + varName);
            if (!(varParams.equals(""))) {
                if (varCategoryType.startsWith("Nsl")) { //Nsl vs Native
                    pw.print("(" + varParams + ")");
                } else if (varParams.startsWith("[")) {
                    pw.print(varParams);
                } else {
                    String[] parArray = SCSUtility.tokenize(varParams);
                    for (String s : parArray) {
                        pw.print("[" + s + "]");
                    }
                }
            }
            if (!(varInits.equals(""))) {
                pw.print("=" + varInits); //todo: this may require { }
                //but user could put those in at time of editing
            }
            pw.print("; // " + varComment + "\n");
            return;
        }
        //	} //end try
        //	catch (IOException e) {
        //	    System.err.println("Declaration: writeNslm IOException");
        //	    throw new IOException("Module writeNslm IOException");
        //	}
        System.err.println("Declaration:writeNslm: unknown dialogType");
    }//end writeNslm
    //--------------------------------------------------------

    //-----------------------------------------------
} //end class Declaration










    

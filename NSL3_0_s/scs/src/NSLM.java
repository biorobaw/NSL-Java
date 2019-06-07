/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


/**
 * NSLM - A class representing the NSLM code of the module - part of Module class
 *
 * @author Xie, Gupta, Alexander
 * @version %I%, %G%
 * @since JDK8
 * <p>
 * Note: this class is part of and gets information from the Module.class.
 * To produce a .mod file it needs the Module class
 * except the schematic and icon info.
 */

import javax.swing.text.PlainDocument;
import java.io.*;


class NSLM {
    //    String arguments=""; //put in Module
    String extendsWhat;
    String whatsParams;
    String comment1; //goes before module statement
    boolean verbatimNSLC = false;
    boolean verbatimNSLJ = false;
    String comment2;//goes after module statement
    //Vector variables=null; //moved to Module
    PlainDocument methods;

    /**
     * Constructor of this class with no parameters.
     */
    public NSLM() {
        //        arguments="";
        extendsWhat = "";
        whatsParams = "";
        comment1 = "";
        boolean verbatimNSLC = false;
        boolean verbatimNSLJ = false;
        comment2 = "";
        methods = null;
    }

    /**
     * Make another copy/clone of this NSLM and then return it.
     *
     * @return    <code>NewNSLM</code> which is a copy of this NSLM
     */
    /*
    //public NSLM clone(NSLM myCode) {
    //    NSLM NewNSLM=new NSLM();
    //    int ix;
    // todo: fill in coping of one NSLm to the other
    // return NewNSLM;
    //}
    */


    /**
     * Write this NSLM to the output stream os.
     *
     * @throws IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os)
            throws IOException {

        //todo: should these be writeObject?

        int ix;
        try {
            //os.writeUTF(arguments); put in Module
            os.writeUTF(extendsWhat);
            os.writeUTF(whatsParams);
            os.writeUTF(comment1);
            os.writeBoolean(verbatimNSLC);
            os.writeBoolean(verbatimNSLJ);
            os.writeUTF(comment2);

            os.writeObject(methods);
            //System.out.println("Debug:Nslm:methods write: "+methods);
        } catch (IOException e) {
            System.err.println("Error:NSLM: write IOException");
            throw new IOException("NSLM write IOException");
        }
    }
    //----------------------------------------------------

    /**
     * Write this NSLM to the PrintWriter pw.
     */
    public void writeAllChars(PrintWriter pw) {
        int ix;

        pw.println("extendsWhat: " + extendsWhat);
        pw.println("whatsParams: " + whatsParams);
        pw.println("comment1: " + comment1);
        pw.print("verbatimNSLC: ");
        pw.println((Boolean.valueOf(verbatimNSLC)).toString());
        pw.print("verbatimNSLJ: ");
        pw.println((Boolean.valueOf(verbatimNSLJ)).toString());
        pw.println("comment2: " + comment2);

//todo: include dumping of methods
//pw.writeObject(methods); //methods is a PlainDocument
        //System.out.println("Debug:Nslm:methods writeAllChars: "+methods);
    }
    //----------------------------------------------------

    /**
     * Read this NSLM from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException {
        int ix = 0;
        int n = 0;
        String str = null;
        Declaration decl = null;

        //todo: should these be readObject


        try {
            extendsWhat = os.readUTF();
            whatsParams = os.readUTF();
            comment1 = os.readUTF();
            verbatimNSLC = os.readBoolean();
            verbatimNSLJ = os.readBoolean();
            comment2 = os.readUTF();

            methods = (PlainDocument) os.readObject();
            //System.out.println("Debug:Nslm:methods read: "+methods);
        } catch (FileNotFoundException e) {
            System.err.println("Error:NSLM read FileNotFoundException");
            throw new FileNotFoundException("NSLM read FileNotFoundException");
        } catch (IOException e) {
            System.err.println("Error: NSLM read IOException");
            throw new IOException("NSLM read IOException");
        }
    }

}  //end Class NSLM






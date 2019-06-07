/* SCCS  %W%---%G%--%U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.
/**
 * SCSUtility - A collection of useful utilities - some for graphics some for
 * file handeling.
 *
 * @author Amanda Alexander
 * @version %I%, %G%
 * @since JDK8
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.zip.*;

/**---------------------------------------------------------*/
// static class
@SuppressWarnings({"Duplicates", "SpellCheckingInspection"})
public class SCSUtility {

    // UserPref myUserPref = new UserPref();

    //file extensions in use by SCS
    // sif - scs db file
    // suf - scs dump file
    // mod - nslm output file
    // sbk - sif backup file
    // mbk - mod backup file
    // resources/notepad2.properties
    // C:\WINNT\Profiles\Administrator\SCS_PREFERENCES
    // C:\WINNT\Profiles\Administrator\SCS_LIBRARY_PATHS


    // todo: change maxFileInADir:
    //  if when reading in we see it is more than 200
    public static int gridD2 = 4; //the number of pixels in the grid divided by 2
    public static int grid = 8; //the number of pixels in the grid
    public static int gridT2 = 16; //the number of pixels in the grid time 2
    //6/13/02 aa: sifVersionNum changed from 4 to 6 because we are not writing the port color anymore, but we are writing the name of the port a connection is tied to.
    public static int sifVersionNum = 6; //the version number for this sif format
    public static String nslmVersionNum = "3_0_n"; //the version number for this mod format
    public static int maxFilesInADir = 200; //arbitrary  : could change
    public static int maxCharsInPath = 100; //arbitrary  : could change
    public static int maxCharsInNick = 20; //arbitrary  : could change
    public static int maxCharsInModuleName = 30; //arbitrary  : could change
    public static int maxCharsFreeText = 100; //arbitrary  : could change
    public static int argumentsCharsDisplayed = 60; //arbitrary  : could change
    public static int parametersCharsDisplayed = 60; //arbitrary  : could change
    public static String blankString10 = "          ";
    public static String blankString20 = "                    ";
    public static String blankString30 = "                              ";

    public static String user_home = System.getProperty("user.home");
    public static String scs_library_paths_file = "SCS_LIBRARY_PATHS";
    public static String scs_preferences_file = "SCS_PREFERENCES";

    public static String scs_library_paths_path = user_home + File.separator + scs_library_paths_file;
    public static String scs_preferences_path = System.getProperty("user.home") + File.separator + scs_preferences_file;
    public static String scs_curr_library_path = "";

    // todo: Maybe useful in future to store the paths, but they will
    // change while the user is creating modules, so dynamic is better
    // for now.
    //Vector modulePathList; //vector of Strings
    //Vector libraryList;    // vector of library nodes: nickName and path

    //---------------------------------------------------------

    /**
     *	returnCol
     */
    public static Color returnCol(String colstring) {

        Color resultCol;
        //=  new Color(1,1,1 ) ;

        //  Color.red   ;

        switch (colstring) {
            case "BLUE":
                resultCol = Color.blue;
                break;
            case "RED":
                resultCol = Color.red;
                break;
            case "GRAY":
                resultCol = Color.gray;
                break;
            case "LIGHTGRAY":
                resultCol = Color.lightGray;
                break;
            case "DARKGRAY":
                resultCol = Color.darkGray;
                break;
            case "YELLOW":
                resultCol = Color.yellow;
                break;
            case "GREEN":
                resultCol = Color.green;
                break;
            case "BLACK":
                resultCol = Color.black;
                break;
            case "CYAN":
                resultCol = Color.cyan;
                break;
            case "MAGENTA":
                resultCol = Color.magenta;
                break;
            case "ORANGE":
                resultCol = Color.orange;
                break;
            case "PINK":
                resultCol = Color.pink;
                break;
            case "WHITE":
                resultCol = Color.white;
                break;
            default:
                System.err.println("Error:SCSUtility:returnCol:unknown color " + colstring);
                resultCol = Color.red;
                break;
        }

        return resultCol;
    }


    //---------------------------------------------------------

    /**
     *	returnOppositeCol
     */
    public static Color returnOppositeCol(String colstring) {

        Color resultCol;
        //=  new Color(1,1,1 ) ;

        //  Color.red   ;

        switch (colstring) {
            case "BLUE":
            case "CYAN":
                resultCol = Color.orange;
                break;
            case "RED":
            case "PINK":
                resultCol = Color.green;
                break;
            case "GRAY":
            case "WHITE":
            case "LIGHTGRAY":
                resultCol = Color.black;
                break;
            case "DARKGRAY":
            case "BLACK":
                resultCol = Color.white;
                break;
            case "YELLOW":
                resultCol = Color.magenta;
                break;
            case "GREEN":
                resultCol = Color.red;
                break;
            case "MAGENTA":
                resultCol = Color.yellow;
                break;
            case "ORANGE":
                resultCol = Color.blue;
                break;
            default:
                System.err.println("Error:SCSUtility:returnOppositeCol:unknown color " + colstring);
                resultCol = Color.red;
                break;
        }

        return resultCol;
    }

    //--------------------------------

    /** returnColorNameString
     *
     */

    public static String returnColorNameString(Color p_color) {
        String result = "CYAN";

        //System.out.println("Color is " + p_color );
        if (p_color.equals(Color.blue))
            result = "BLUE";
        else if (p_color.equals(Color.white))
            result = "WHITE";
        else if (p_color.equals(Color.red))
            result = "RED";
        else if (p_color.equals(Color.gray))
            result = "GRAY";
        else if (p_color.equals(Color.lightGray))
            result = "LIGHTGRAY";
        else if (p_color.equals(Color.darkGray))
            result = "DARKGRAY";
        else if (p_color.equals(Color.yellow))
            result = "YELLOW";
        else if (p_color.equals(Color.green))
            result = "GREEN";
        else if (p_color.equals(Color.black))
            result = "BLACK";
        else if (p_color.equals(Color.cyan))
            result = "CYAN";
        else if (p_color.equals(Color.magenta))
            result = "MAGENTA";
        else if (p_color.equals(Color.orange))
            result = "ORANGE";
        else if (p_color.equals(Color.pink))
            result = "PINK";
        else {
            System.err.println("Error:SCSUtility:returnColorNameString:unknown input " + p_color.toString());
        }

        return result;

    }

    //---------------------------------------------------------
    public static void setColorMenu(Choice choice) {
        choice.add("BLACK");
        choice.add("WHITE");
        choice.add("RED");
        choice.add("YELLOW");
        choice.add("GREEN");
        choice.add("CYAN");
        choice.add("DARKGRAY");
        choice.add("LIGHTGRAY");
        choice.add("GRAY");
        choice.add("MAGENTA");
        choice.add("ORANGE");
        choice.add("PINK");
        choice.add("BLUE");
    }

    //---------------------------------------------------------
    public static void setTextSizeMenu(Choice choice) {
        choice.add("10");
        choice.add("12");
        choice.add("14");
        choice.add("16");
        choice.add("18");
        choice.add("20");
        choice.add("22");
    }

    //---------------------------------------------------------

    /**
     *	setCurrLibPath
     */
    static public void setCurrLibPath(String path) {
        scs_curr_library_path = path;
    }
    //---------------------------------------------------------

    /**
     *	setCurrLibPath
     */
    static public String getCurrLibPath() {
        return (scs_curr_library_path);
    }

    //---------------------------------------------------------

    /**
     *	catFullPathToSif
     *       assumes: path is the path to the src directory
     */
    static public String catFullPathToSuf(String path, String moduleName) {
        String templateWFullPath;
        if ((path == null) || (moduleName == null)) {
            return (null);
        } else if ((path.equals("")) || (moduleName.equals(""))) {
            return (null);
        } else {
            templateWFullPath = path + File.separator + moduleName + ".suf";
            return (templateWFullPath);
        }
    }

    /**---------------------------------------------------------
     *	catFullPathToSrc
     *       assumes: lib is the path to the library
     */
    static public String catFullPathToSrc(String lib, String mod, String version) {
        String templateWFullPath;
        if ((lib == null) || (mod == null) || (version == null)) {
            return (null);
        } else if ((lib.equals("")) || (mod.equals("")) || (version.equals(""))) {
            return (null);
        } else {
            templateWFullPath = lib + File.separator + mod + File.separator + version + File.separator + "src";
            return (templateWFullPath);
        }
    }

    /**---------------------------------------------------------
     *	catFullPathToSif
     *       assumes: path is the path to the src directory
     */
    static public String catFullPathToSif(String path, String moduleName) {
        String templateWFullPath;
        if ((path == null) || (moduleName == null)) {
            return (null);
        } else if ((path.equals("")) || (moduleName.equals(""))) {
            return (null);
        } else {
            templateWFullPath = path + File.separator + moduleName + ".sif";
            return (templateWFullPath);
        }
    }

    /**---------------------------------------------------------
     *	catFullPathToSbk
     *       assumes: path is the path to the src directory
     */
    static public String catFullPathToSbk(String path, String moduleName) {
        String templateWFullPath;
        if ((path == null) || (moduleName == null)) {
            return (null);
        } else if ((path.equals("")) || (moduleName.equals(""))) {
            return (null);
        } else {
            templateWFullPath = path + File.separator + moduleName + ".sbk";
            return (templateWFullPath);
        }
    }

    /**---------------------------------------------------------
     *	catFullPathToSif
     *       assumes: lib is the path to the library
     */
    static public String catFullPathToSif(String lib, String mod, String version) {
        String tempStr;
        String templateWFullPath;
        tempStr = catFullPathToSrc(lib, mod, version);
        if (tempStr == null) {
            return (null);
        }
        templateWFullPath = tempStr + File.separator + mod + ".sif";
        return (templateWFullPath);

    }

    /**---------------------------------------------------------
     *	catFullPathToSbk
     *       assumes: lib is the path to the library
     * sif backup file
     */
    static public String catFullPathToSbk(String lib, String mod, String version) {
        String tempStr;
        String templateWFullPath;
        tempStr = catFullPathToSrc(lib, mod, version);
        if (tempStr == null) {
            return (null);
        }
        templateWFullPath = tempStr + File.separator + mod + ".sbk";
        return (templateWFullPath);

    }

    /**---------------------------------------------------------
     *	getSbkString
     *  sbk=sif backup file
     *  notes:assumes fullPathToSif is not null
     */
    static public String getSbkString(String fullPathToSif) {

        String tempStr;

        int lastDot = fullPathToSif.lastIndexOf('.');
        String base = fullPathToSif.substring(0, lastDot);
        tempStr = base + ".sbk";
        //System.out.println("Debug:SCSUtility getSbkString "+tempStr);
        return (tempStr);
    }

    /**---------------------------------------------------------
     *	catFullPathToMod
     *       assumes: lib is the path to the library
     * nslm output file in text form that get compiled by NSL
     */
    static public String catFullPathToMod(String lib, String mod, String version) {
        String tempStr;
        String templateWFullPath;
        tempStr = catFullPathToSrc(lib, mod, version);
        if (tempStr == null) {
            return (null);
        }
        templateWFullPath = tempStr + File.separator + mod + ".mod";
        return (templateWFullPath);
    }

    /**---------------------------------------------------------
     *	setNickAndPathFilePath
     */

    static public void setNickAndPathFilePath() throws NullPointerException {
        boolean ok = false;
        String testdir = "";
        File paths;
        String pathString = "";
        FileInputStream fis = null;

        Vector result = new Vector();
        int count = 0;

        try {
            paths = new File(scs_library_paths_path);
            pathString = paths.getAbsolutePath();
            scs_library_paths_path = pathString;
        } catch (NullPointerException e) {
            System.err.println("Error:SCSUtility:setNickAndPathFilePath: NullPointerException : " + pathString);
            throw new NullPointerException("SCSUtility:setNickAndPathFile:NullPointerException");
        }
    }

    /**---------------------------------------------------------
     *	deleteFile
     */
    public static void deleteFile(String filepath, Frame parent) {
        UserPref myUserPref = new UserPref();
        WarningDialogOkCancel warning = new WarningDialogOkCancel((Frame) parent, "warning");

        File tempFile = new File(filepath);

        if (!tempFile.renameTo(new File(myUserPref.trashDir, new File(filepath).getName()))) {

            warning.setMsg("SCSUtility: Unable to delete module.. Please check if " + myUserPref.trashDir + " exists...");
            warning.display();
        }
    }

    /**---------------------------------------------------------
     *	appendToFile
     * todo: change to java's append to file
     */
    public static void stackOnFile(String filepath, String putme) throws FileNotFoundException, IOException {

        boolean done = false;
        String line;
        String testdir = "";
        File file1;
        String file1String;
        FileInputStream fis;
        Vector<String> linesVector = new Vector<>();
        int count = 0;

        //    WarningDialogOkCancel warning=new WarningDialogOkCancel((Frame)parent , "warning");

        try {
            file1 = new File(filepath);
            file1String = file1.getAbsolutePath();
            fis = new FileInputStream(file1String);
            BufferedReader in_br = new BufferedReader(new InputStreamReader(fis));
            while (!done) {
                line = in_br.readLine();
                if (line == null) {
                    done = true;
                } else {
                    linesVector.addElement(line);
                    count++;
                    //System.out.println("Debug:SCSUtility:stackOnFile:"+line);
                }
            }//end while
            fis.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility: stackOnFile: read: FileNotFoundException : " + filepath);
            throw new FileNotFoundException("SCSUtility:stackOnFile:FileNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:SCSUtility: stackOnFile: read: IOException: " + filepath);
            throw new IOException("SCSUtility:stackOnFile:IOException");
        }
        try { //open file and append
            FileOutputStream fos = new FileOutputStream(filepath);
            PrintWriter pout = new PrintWriter(fos);

            pout.println(putme); //put new library on top of file

            if (pout.checkError()) { //does flush as well
                System.err.println("Error:LibraryFileManager:addNewLibrary: would not write to :" + filepath);
            }

            for (int i = 0; i < count; i++) {
                pout.println(linesVector.elementAt(i));
                //System.out.println("Debug:SCSUtility:stackOnFile:"+linesVector.elementAt(i));
            }

            if (pout.checkError()) { //does flush as well
                System.err.println("Error:LibraryFileManager:addNewLibrary: would not write to :" + filepath);
            }
            pout.close();
        } //end try
        catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility:stackOnFile:write: FileNotFoundException : " + filepath);
            throw new FileNotFoundException("SCSUtility:stackOnFile:FileNotFoundException");
        }
    }

    /**---------------------------------------------------------
     *	readLibraryNickAndPathsFile
     * todo: change to java's Property class
     */

    static public Vector<String> readLibraryNickAndPathsFile() throws FileNotFoundException, IOException {
        boolean done = false;
        String line;
        String testdir = "";
        File paths = null;
        String pathString = "";
        FileInputStream fis;

        Vector<String> result = new Vector<>();
        int count = 0;

        //      libraries.removeAllElements();
        try {
            //paths=new File(scs_library_paths_path);
            //pathString=paths.getAbsolutePath();
            fis = new FileInputStream(scs_library_paths_path);
            BufferedReader in_br = new BufferedReader(new InputStreamReader(fis));
            while (!done) {
                line = in_br.readLine();
                if (line == null) {
                    done = true;
                } else if (!(line.equals(""))) { //if blank skip
                    result.addElement(line);
                    count++;
                    //System.out.println("Debug:SCSUtility:readLibraryNickAndPathsFile: "+line);
                }
            }
            fis.close();
            return (result);
        } catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility: readLibraryNickAndPathsFile: FileNotFoundException: " + scs_library_paths_path);
            throw new FileNotFoundException("SCSUtility:readLibraryNickAndPathsFile");
        } catch (IOException e) {
            System.err.println("Error:SCSUtility: readLibraryNickAndPathsFile: IOException " + scs_library_paths_path);
            throw new IOException("SCSUtility:readLibraryNickAndPathsFile");
        }
    }

    //------------------------------------

    /**
     * vectorToNickAndPathArray
     * return  an array of strings
     * todo: change to java's Property class
     */
    // --------------------------------------------------
    static String[] vectorToNickAndPathArray(Vector<String> nicksAndPaths) {
        int i;

        String[] result_buffer = new String[maxFilesInADir];

        for (i = 0; i < nicksAndPaths.size(); i++) {
            result_buffer[i] = nicksAndPaths.elementAt(i);
        }
        return (result_buffer);
    }

    //------------------------------------

    /**
     * parseOutLibNickName
     * return  a string
     * todo: change to java's Property class
     */
    //note: please trim the line before calling this proceedure
    // --------------------------------------------------
    static String parseOutLibNickName(String line) {
        int i = 0;
        int spacenum;
        String resultBuffer;

        // first element in string is nick name

        //System.out.println("Debug:SCSUtility:parseOutLibNickName line:"+line);
        spacenum = line.indexOf("=");
        if (spacenum <= 0) {
            //System.out.println("Debug:SCSUtility:parseOutLibNickName "+spacenum);
            //System.err.println("Error:SCSUtility:parseOutLibNickName:library lines must be of the form: nickName=libraryPath in file "+scs_library_paths_path);
            return (null);
        } else {
            resultBuffer = (line.substring(0, spacenum)).trim();
        }
        //System.out.println("Debug:SCSUtility:parseOutLibNickName "+resultBuffer);
        return (resultBuffer);
    }
    //------------------------------------

    /**
     * parseOutLibPathName
     * return  a string
     * todo: change to java's Property class
     */
    // --------------------------------------------------
    static String parseOutLibPathName(String line) {
        //note: please trim the line before calling this proceedure
        // and check that it is not null.
        // second element in string is path
        int i = 0;
        int spacenum;
        String resultBuffer;

        //System.out.println("Debug:SCSUtility:parseOutLibPathName "+line);
        spacenum = line.indexOf("=");
        if (spacenum <= 0) {
            //System.out.println("Debug:SCSUtility:parseOutLibPathName "+spacenum);
            //System.err.println("SCSUtility:parseOutLibPathName:library lines must be of the form: nickName=libraryPath in file "+scs_library_paths_path);
            return (null);
        } else {
            //todo: change to getToken
            spacenum = line.lastIndexOf("=") + 1; // assumes no spaces after path name
            if (spacenum >= line.length()) {
                //System.err.println("SCSUtility:parseOutLibPathName:spaces after path name in :"+scs_library_paths_path);
                return (null);
            } else {
                resultBuffer = (line.substring(spacenum)).trim();
            }
        }
        //System.out.println("Debug:SCSUtility:parseOutLibPathName "+resultBuffer);
        return (resultBuffer);
    }

    //------------------------------------

    /**
     * vectorToPathArray
     * return  an array of strings
     * todo: change to java's Property class
     */
    // --------------------------------------------------
    static String[] vectorToPathArray(Vector nicksAndPaths) {

        int i, j;
        if (nicksAndPaths == null) {
            System.err.println("Error:SCSUtility:vectorToPathArray: vector of nicknames and pathnames are null");
            return (null);
        }
        int sz = nicksAndPaths.size();
        String[] result_buffer = new String[sz];
        String temp;
        i = 0;
        j = 0;
        while (i < sz) {
            String line = (String) nicksAndPaths.elementAt(i);
            line = line.trim();
            if (!line.equals("")) {
                temp = parseOutLibPathName(line);
                if ((temp == null) || (temp.equals(""))) {
                    return (null);
                } else {
                    result_buffer[j] = temp;
                    j++;
                }
                i++;
            }

        }
        return (result_buffer);
    }
    //------------------------------------

    /**
     * vectorToNickNameArray
     * return  an array of strings
     * todo: change to java's Property class
     */
    // --------------------------------------------------
    static String[] vectorToNickNameArray(Vector nicksAndPaths) {
        int i, j;
        int spacenum;
        String line;

        i = 0;
        j = 0;
        int sz = nicksAndPaths.size();
        String[] result_buffer = new String[sz];
        String temp;

        while (i < sz) {
            // first element in string is nick name
            line = ((String) nicksAndPaths.elementAt(i)).trim();
            if (!line.equals("")) {
                temp = parseOutLibNickName(line);

                if ((temp == null) || (temp.equals(""))) {
                    return (null);
                } else {
                    result_buffer[j] = temp;
                    j++;
                }
            }
            i++;
        }
        return (result_buffer);
    }
    //------------------------------------

    /**
     * getLibNickName ( String libPathName) {
     * return the library nick name given a library path name
     * if not found, return "NOTFOUND" string
     * if error, return "null" string
     * todo: change to java's Property class
     */
    // --------------------------------------------------
    static public String getLibNickName(String findMeLibPathStr) throws FileNotFoundException, IOException {

        boolean done = false;
        String line;
        String lib_path;
        String lib_nick;

        System.out.println("Debug:SCSUtility:getLibNickName " + findMeLibPathStr);
        try {
            // read  the file of library path names
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(scs_library_paths_file)));
            while (!done) { // while nick and paths in file
                line = null;
                line = in.readLine(); // throws IOException
                if (line == null) {
                    done = true;  //eof
                } else {
                    line = line.trim();
                    if (line.equals("")) {
                        System.err.println("Warning:SCSUtility:getLibNickName blank line in file " + scs_library_paths_file);
                    } else {
                        lib_path = parseOutLibPathName(line);
                        if (lib_path == null) {
                            System.err.println("Error:SCSUtility:getLibNickName no equal sign in:" + line);
                            done = true;
                            return (null);
                        } else if (lib_path.equals(findMeLibPathStr)) {
                            lib_nick = parseOutLibNickName(line);
                            if (lib_nick == null) {
                                System.err.println("Error:SCSUtility:getLibNickName no equal sign in:" + line);
                                done = true;
                                return (null);
                            } else {
                                done = true;
                                //System.out.println("Debug:SCSUtility:getLibNickName "+lib_nick);
                                return (lib_nick);
                            }
                        }
                    }// if found
                } // if not blank
            } //end while
        }  //end try
        catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility:getLibNickName:FileNotFoundException " + scs_library_paths_path);
            throw (new FileNotFoundException());
        } catch (IOException e) {
            System.err.println("Error:SCSUtility:IOException:getLibNickName: " + scs_library_paths_path);
            throw (new IOException());
        }
        // if not other error and nick name not found return NOTFOUND string
        //System.out.println("Debug:SCSUtility:getLibNickName "+"NOTFOUND");
        return ("NOTFOUND");
    }

    //------------------------------------

    /**
     * getLibPathName ( String libNickName) {
     * return the library path given a library NickName
     * if not found, return "NOTFOUND" string
     * if error, return "null" string
     * todo: change to java's Property class
     */
    // --------------------------------------------------
    static public String getLibPathName(String libNickName) throws FileNotFoundException, IOException {

        boolean done = false;
        String line;
        String lib_path;
        String lib_nick;
        //System.out.println("Debug:SCSUtility:getLibPathName "+libNickName);
        try {
            // read  the file of library path names
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(scs_library_paths_path)));
            while (!done) { // while nick and paths in file
                line = null;
                line = in.readLine(); //throws IO exception
                if (line == null) {
                    done = true;  //eof
                } else {
                    line = line.trim();
                    if (line.equals("")) {
                        //blank line;
                        System.err.println("Warning:SCSUtility:getLibPathName blank line in " + scs_library_paths_file);
                    } else {
                        lib_nick = parseOutLibNickName(line);
                        if (lib_nick == null) {
                            System.err.println("Error:SCSUtility:getLibPathName no equal sign in :" + line);
                            done = true;
                            return (null);
                        } else if (lib_nick.equals(libNickName)) {
                            lib_path = parseOutLibPathName(line);

                            if (lib_path == null) {
                                System.err.println("Error:SCSUtility:getLibPathName no equal sign in :" + line);
                                done = true;
                                return (null);
                            } else {
                                done = true;
                                //System.out.println("Debug:SCSUtility:getLibPathName "+lib_path);
                                return (lib_path);
                            }
                        }// else if
                    } ////if not blank line
                } ////if not null
            } //end while
        }  //end try

        catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility:FileNotFoundException:getLibPathName: " + scs_library_paths_path);
            throw (new FileNotFoundException());
        } catch (IOException e) {
            System.err.println("Error:SCSUtility:IOException:getLibPathName: " + scs_library_paths_path);
            throw (new IOException());
        }
        //System.out.println("Debug:SCSUtility:getLibPathName "+"NOTFOUND");
        // if not other error and nick name not found return NOTFOUND string
        return ("NOTFOUND");
    }

    // --------------------------------------------------

    /**
     * getLibPathLast
     * @param libPath String
     */
    // --------------------------------------------------
    static public String getLibPathLast(String libPath) {
        String lastPart = "";
        if (libPath == null) return null;
        if (libPath.equals("")) return null;
        int len = libPath.length(); //remove end slashes
        //tried to use File.seperator but it only took the one direction
        // and sometime the paths use both Unix and MS notation for
        // directory seperation
        // what happend to using dots??
        boolean found = false;
        while ((!(libPath.equals(""))) &&
                ((libPath.endsWith("/")) || (libPath.endsWith("\\")))) {
            libPath = libPath.substring(0, (len - 1));
            //System.out.println("Debug:SCSUtility:getLibPathLast:"+libPath);
            len = libPath.length();
            found = true;
        }
        int lastForwardSlash = libPath.lastIndexOf("/");
        int lastBackwardSlash = libPath.lastIndexOf("\\");
        //System.out.println("Debug:SCSUtility:getLibPathLast2:"+libPath);
        //System.out.println("Debug:SCSUtility:getLibPathLast2:"+lastForwardSlash);
        //System.out.println("Debug:SCSUtility:getLibPathLast2:"+lastBackwardSlash);
        if (lastBackwardSlash > lastForwardSlash) {
            lastPart = libPath.substring(lastBackwardSlash + 1, len);
        }
        if (lastForwardSlash >= lastBackwardSlash) {
            lastPart = libPath.substring(lastForwardSlash + 1, len);
        }
        return (lastPart);
    }
    //------------------------------------

    /**
     * getSrcPathUsingNick
     * return the path name string given library NickName, module name and version number
     * @param libNickName String
     * @param modulename String
     * @param version String
     * @throws FileNotFoundException file not found
     * @throws IOException I/O exception
     * @returns returnStr String that start with the libpath and ends with "/src".
     */

    static String getSrcPathUsingNick(String libNickName, String modulename, String version) throws FileNotFoundException, IOException {

        boolean done = false;
        String lib_path;
        String returnStr;

        //System.out.println("Debug:SCSUtility:getSrcPathUsingNick:Module name is :  " + modulename );
        try {
            lib_path = getLibPathName(libNickName);
        } catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick: FileNotFoundException with " + scs_library_paths_path);
            throw (new FileNotFoundException());
        } catch (IOException e) {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick: IOException with " + scs_library_paths_path);
            throw (new IOException());
        }
        if (lib_path == null) {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick:something wrong with " + scs_library_paths_file);
            return (null);
        }
        if (lib_path.equals("NOTFOUND")) {
            System.err.println("ERROR:SCSUtility:getSrcPathUsingNick: no match for alias " + libNickName);
            return (null);
        }
        // otherwise use lib_path
        returnStr = catFullPathToSrc(lib_path, modulename, version);
        if (returnStr == null) {
            System.err.println("ERROR:SCSUtility:getSrcPathUsingNick: either lib_path, modulename, or version is null");
            return (null);
        }

        return (returnStr);
    }

    /**---------------------------------------------------------
     *	copyStringFile
     */
    public static void copyStringFile(String fromFilePath, String toFilePath) throws FileNotFoundException, IOException {

        boolean done = false;
        String line;
        File fromFile;
        String fromFileString;
        FileInputStream fis;

        File toFile;
        String toFileString;
        FileOutputStream fos;

        PrintWriter pout;

        int count = 0; //for debug

        try {
            fromFile = new File(fromFilePath);
            if (!(fromFile.exists())) {
                System.err.println("Error:SCSUtility: copyStringFile: FileNotFoundException : " + fromFilePath);
                throw new FileNotFoundException("SCSUtility:copyStringFile:FileNotFoundException " + fromFilePath);
            }
            toFile = new File(toFilePath);
	    /* it is ok to overwrite the backup file
	       if (toFile.exists()) {
	       System.err.println("Error:SCSUtility:file exists"+toFilePath);
	       }
	    */
            fromFileString = fromFile.getAbsolutePath();
            fis = new FileInputStream(fromFileString);
            BufferedReader in_br = new BufferedReader(new InputStreamReader(fis));

            toFileString = toFile.getAbsolutePath();
            fos = new FileOutputStream(toFileString);
            pout = new PrintWriter(fos);

            while (!done) {
                line = in_br.readLine();
                if (line == null) {
                    done = true;
                } else {
                    pout.println(line);
                    count++;
                    //System.out.println("Debug:SCSUtility:copyStringFile:"+line);
                }
            }//end while
            fis.close();
            if (pout.checkError()) { //does flush as well
                System.err.println("Error:SCSUtility:copyStringFile: would not write to :" + toFilePath);
                throw new IOException("SCSUtility:copyStringFile:IOException " + toFilePath);
            }
            pout.close();
        }//end try
        catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility: copyStringFile: read: FileNotFoundException : " + fromFilePath);
            throw new FileNotFoundException("SCSUtility:copyStringFile:FileNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:SCSUtility: copyStringFile: read: IOException: " + fromFilePath);
            throw new IOException("SCSUtility:copyStringFile:IOException " + fromFilePath);
        }
    }


    /**---------------------------------------------------------
     *	copyModuleFile
     */
    public static void copyModuleFile(String fromFilePath, String toFilePath) throws FileNotFoundException, IOException, ClassNotFoundException {

        boolean done = false;
        String line = "";
        File fromFile;
        String fromFileString = "";
        FileInputStream fis;
        ObjectInputStream ois;

        File toFile;
        String toFileString;
        FileOutputStream fos;
        ObjectOutputStream oos;

        //PrintWriter pout=null;

        int count = 0; //for debug
        Module tempModule;

        try {
            fromFile = new File(fromFilePath);
            if (!(fromFile.exists())) {
                System.err.println("Error:SCSUtility: copyModuleFile: FileNotFoundException : " + fromFilePath);
                throw new FileNotFoundException("SCSUtility:copyModuleFile:FileNotFoundException " + fromFilePath);
            }
            fromFileString = fromFile.getAbsolutePath();
            fis = new FileInputStream(fromFileString);
            ois = new ObjectInputStream(fis);

            toFile = new File(toFilePath);
            toFileString = toFile.getAbsolutePath();
            fos = new FileOutputStream(toFileString);
            oos = new ObjectOutputStream(fos);

            tempModule = new Module();
            tempModule.read(ois);
            tempModule.write(oos);

            fis.close();
            fos.close();
        }//end try
        catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility: copyModuleFile: read: FileNotFoundException : " + fromFilePath);
            throw new FileNotFoundException("SCSUtility:copyModuleFile:FileNotFoundException");
        } catch (IOException e) {
            System.err.println("Error:SCSUtility: copyModuleFile: read: IOException: " + fromFilePath);
            throw new IOException("SCSUtility:copyModuleFile:IOException " + fromFilePath);
        } catch (ClassNotFoundException e) {
            System.err.println("Error:SCSUtility: copyModuleFile: read: ClassNotFoundException: " + fromFilePath);
            throw new ClassNotFoundException("SCSUtility:copyModuleFile:IOException " + fromFilePath);
        }

    }

    //------------------------------------

    /**
     * return the module file stream with given src path, module name
     */
    // --------------------------------------------------
    static FileInputStream getModuleStream(String src_path, String modulename) throws FileNotFoundException {
        File tempfile = null;
        FileInputStream fis;
        //      Properties settings = new Properties();

        boolean done = false;
        try {
            //System.out.println("Debug:SCSUtility:getModuleStream:1 Lib path is:" +lib_path+" Module name is :  " + modulename );
            String testPathAndFile = catFullPathToSif(src_path, modulename);
            tempfile = new File(testPathAndFile);
            //System.out.println("Debug:SCSUtility:getModuleStream:5 " + tempfile.getAbsolutePath());
            fis = new FileInputStream(tempfile.getAbsolutePath());

            if (tempfile.exists())   // does file exist
            {
                // add the path info about module into vector
                //System.out.println("Debug:SCSUtility:getModuleStream:6  " + tempfile.getAbsolutePath() );
                return (fis);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility:getModuleStream: FileNotFoundException");
            throw new FileNotFoundException("Error:SCSUtility:getModuleStream: FileNotFoundException: " + tempfile.getAbsolutePath());
        }
        return (null);
    }

    //------------------------------------

    /**
     * return the module file stream with given library path, module name and version number
     */
    // --------------------------------------------------
    static FileInputStream getModuleStream(String lib_path, String modulename, String version) throws FileNotFoundException {

        FileInputStream fis;

        boolean done = false;


        //System.out.println("Debug:SCSUtility:getModuleStream:1 Lib path is:" +lib_path+" Module name is :  " + modulename );
        String srcStr = catFullPathToSrc(lib_path, modulename, version);
        fis = getModuleStream(srcStr, modulename);
        return (fis);

    }

    //------------------------------------

    /**
     * return the module file stream with given library NickName, module name and version number
     */
    // --------------------------------------------------
    static FileInputStream getModuleStreamUsingNick(String libNickName, String modulename, String version) throws FileNotFoundException, IOException {
        FileInputStream fis;
        String src_path;

        //System.out.println("Debug:SCSUtility::getModuleStreamUsingNick:Module name is :  " + modulename );
        src_path = getSrcPathUsingNick(libNickName, modulename, version);

        if (src_path == null) {
            System.err.println("Error:SCSUtility:something wrong with " + scs_library_paths_file);
            return (null);
        }
        // otherwise use lib_path
        fis = getModuleStream(src_path, modulename);
        return (fis);
    }
    //------------------------------------

    /**
     * return the module file stream with given library path, module name and version number
     */
    // --------------------------------------------------
    static public String getHighestVersionString(String lib_path, String modulename) throws FileNotFoundException {
        FileInputStream fis = null;
        File pointerToDir;
        String highestVersion;

        //System.out.println("Debug:SCSUtility:getLatestVersionString:lib_path "+lib_path+" modulename: "+modulename);
        pointerToDir = new File(lib_path, modulename);
        //System.out.println("Debug:SCSUtility:getLatestVersionString:pointerToDir.getAbsolutePath():"+pointerToDir.getAbsolutePath());
        //fis=new FileInputStream(pointerToDir.getAbsolutePath()) ;

        //	catch (FileNotFoundException e) {
        //	    System.err.println("Error:SCSUtility:getHighestVersion:file not found "+lib_path+" modulename: "+modulename);
        //	    throw new FileNotFoundException();
        //	}

        if (!pointerToDir.exists()) {   // does directory exist

            System.err.println("ERROR:SCSUtility: file not found: " + pointerToDir.getAbsolutePath());
            return (null);
        }
        if (!pointerToDir.isDirectory()) {   // is this a directory
            System.err.println("ERROR:SCSUtility: not a directory: " + pointerToDir.getAbsolutePath());
            return (null);
        }

        String[] filenames = pointerToDir.list();

        assert filenames != null;
        if (filenames.length == 0) {
            System.err.println("ERROR:SCSUtility: no version files in: " + pointerToDir.getAbsolutePath());
            return (null);
        }
        highestVersion = filenames[0];
        //System.out.println("Debug:SCSUtility:getHighestVersionString:lib_path "+lib_path+" filenames.length: "+filenames.length);
        for (int i = 1; i < filenames.length; i++) {
            // todo: should do more than this
            // note:  must account for miscallaneous files in this directory
            if (filenames[i].compareTo(highestVersion) > 0) { // comes after in dict order
                if (filenames[i].indexOf("_") > 0) { // we use 1_2_3 notation
                    highestVersion = filenames[i];
                }
            }
        }
        if (highestVersion.indexOf("_") > 0) { // we use 1_2_3 notation
            return (highestVersion);
        } else {
            return (null);
        }
    }

    //------------------------------------

    /**
     * return the module file stream with given library path, module name
     */
    // --------------------------------------------------
    static FileInputStream getModuleStreamUsingNoVersion(String libPath, String moduleName) throws FileNotFoundException {

        FileInputStream fis = null;
        boolean done = false;

        //System.out.println("Debug:SCSUtility:getModuleStreamUsingNoVersion:Module name is :  " + moduleName );

        if (libPath == null) {
            System.err.println("Error:SCSUtility:something wrong with " + scs_library_paths_file);
            return (null);
        }
        if (libPath.equals("NOTFOUND")) {
            System.err.println("ERROR:SCSUtility: no match for path " + libPath);
            return (null);
        }
        // otherwise use libPath
        String version = getHighestVersionString(libPath, moduleName);
        //System.out.println("Debug:SCSUtility: version:"+version);
        if (version != null) {
            fis = getModuleStream(libPath, moduleName, version);
        }
        return (fis);
    }
    //------------------------------------

    /**
     * return the module file stream with given library NickName, module name
     */
    // --------------------------------------------------
    static FileInputStream getModuleStreamUsingNickAndNoVersion(String libNickName, String modulename) throws FileNotFoundException, IOException {

        FileInputStream fis;
        boolean done = false;
        String libPath;
        //System.out.println("Debug:SCSUtility:getModuleStreamUsingNickAndNoVersion:Module name is :  " + modulename );

        try {
            libPath = getLibPathName(libNickName);
            //System.out.println("Debug:SCSUtility:getModuleStreamUsingNickAndNoVersion: got libPath "+libPath );
        } catch (FileNotFoundException e) {
            System.err.println("Error:SCSUtility:FileNotFoundException:getModuleStreamUsingNickAndNoVersion: " + scs_library_paths_path);
            throw (new FileNotFoundException());
        } catch (IOException e) {
            System.err.println("Error:SCSUtility:IOException:getModuleStreamUsingNickAndNoVersion: " + scs_library_paths_path);
            throw (new IOException());
        }

        if (libPath == null) {
            System.err.println("Error:SCSUtility:something wrong with " + scs_library_paths_file);
            return (null);
        }
        if (libPath.equals("NOTFOUND")) {
            System.err.println("Error:SCSUtility: no match for alias " + libNickName);
            return (null);
        }
        // otherwise use libPath
        //System.out.println("Debug:SCSUtility:getModuleStreamUsingNickAndNoVersion: going to get version" );
        String version = getHighestVersionString(libPath, modulename);
        if (version == null) {
            System.err.println("Debug:SCSUtility:getModuleStreamUsingNickAndNoVersion: no version for module " + modulename);
            throw (new FileNotFoundException());
        } else {
            //System.out.println("Debug:SCSUtility:getModuleStreamUsingNickAndNoVersion: version "+version );
            //System.out.println("Debug:SCSUtility:getModuleStreamUsingNickAndNoVersion: going to get module stream " );
            fis = getModuleStream(libPath, modulename, version);
            //if (fis!=null) {
            //System.out.println("Debug:SCSUtility:getModuleStreamUsingNickAndNoVersion: got module stream" );
            //}
        }
        return (fis);
    }

    //*******************************************************************
    //---------------------------------------------------------------------
    public static void hierarchicalZip(boolean zipLib, String toBeZippedDir, String placeHereDir, String zipFileName, int filterCount, String[] filters) {
        // hierarchicalZip is a zipOfZips:
        // Must go thru modules and zip their directories
        // and place them in a temp directory
        // then zip them all
        //if (! zipLib) {
        //}
    }

    //---------------------------------------------------------------------
    public static void zipMe(boolean zipLib, String toBeZippedDir, String placeHereDir, String zipFileName, int filterCount, String[] filters) {

        String finalToBeZippedDir;
        String subZipFileName;
        String[] subZips = new String[4];

        if (!zipLib) {
            for (int i = 0; i < filterCount; i++) {
                finalToBeZippedDir = toBeZippedDir + File.separator + filters[filterCount];
                subZipFileName = filters[filterCount] + ".zip";
                recursiveZip(finalToBeZippedDir, toBeZippedDir, zipFileName);
                subZips[filterCount] = subZipFileName;
            }
            // now zip the different zips
            zipThese(toBeZippedDir, subZips, placeHereDir, zipFileName);
        }
    }

    //---------------------------------------------------------------------
    public static void recursiveZip(String toBeZippedDir, String placeHereDir, String zipFileName) {
        File toBeZippedDirFile;
        String canonicalToBeZippedDir;
        try {
            toBeZippedDirFile = new File(toBeZippedDir + File.separator);
            canonicalToBeZippedDir = toBeZippedDirFile.getCanonicalPath();
            if (!toBeZippedDirFile.isDirectory()) {
                System.err.println(toBeZippedDir + "is not a directory");
                return;
            }
            File zipFile = new File(placeHereDir, zipFileName);
            ZipOutputStream zipOutputStream = new ZipOutputStream(
                    new FileOutputStream(zipFile));

            folderZipCompressor(zipOutputStream, canonicalToBeZippedDir, Objects.requireNonNull(toBeZippedDirFile.list()));
            zipOutputStream.close();

        } catch (Exception ex) {
            System.err.println("Error:SCSUtility:recursiveZip: exception caught.");
            ex.printStackTrace();
            //todo:should re-throw the exception
        }

    }

    //---------------------------------------------------------------------
    public static void zipThese(String toBeZippedDir, String[] thingsToZip, String placeHereDir, String zipFileName) {
        File toBeZippedDirFile;
        String canonicalToBeZippedDir;
        try {
            toBeZippedDirFile = new File(toBeZippedDir + File.separator);
            canonicalToBeZippedDir = toBeZippedDirFile.getCanonicalPath();
            if (!toBeZippedDirFile.isDirectory()) {
                System.err.println(toBeZippedDir + "is not a directory");
                return;
            }
            //Create the zip file 'zipFileName' in the same directory that is being zipped
            File zipFile = new File(placeHereDir, zipFileName);
            ZipOutputStream zipOutputStream = new ZipOutputStream(
                    new FileOutputStream(zipFile));

            folderZipCompressor(zipOutputStream, canonicalToBeZippedDir, thingsToZip);
            zipOutputStream.close();

        } catch (Exception ex) {
            System.err.println("Error:SCSUtility:zipThese: exception caught.");
            ex.printStackTrace();
            //todo:should re-throw the exception
        }

    }

    //---------------------------------------------------------------------

    public static void folderZipCompressor(ZipOutputStream zipOutputStream, String canonicalToBeZippedDir, String zippedNames[]) {

        byte[] buffer = new byte[1024];
        int bytesread;
        FileInputStream fis;
        File toBeZippedDirFile = null;


        for (String zippedName : zippedNames) {
            File zippedFile = new File(canonicalToBeZippedDir, zippedName);
            if (!zippedFile.exists()) {
                System.err.println("the file to be zipped doesn't exists" + zippedFile.getPath());
            }

            if (zippedFile.isFile()) {
                try {
                    CRC32 crc32 = new CRC32();
                    int n;
                    FileInputStream fileinputstream = new FileInputStream(zippedFile);
                    while ((n = fileinputstream.read(buffer)) > -1) {
                        crc32.update(buffer, 0, n);
                    }

                    ZipEntry zipEntry = new ZipEntry(convertToZIPName(canonicalToBeZippedDir, zippedFile));
                    zipEntry.setMethod(ZipEntry.DEFLATED);
                    zipEntry.setSize(zippedFile.length());
                    zipEntry.setTime(zippedFile.lastModified());
                    zipEntry.setCrc(crc32.getValue());

                    zipOutputStream.putNextEntry(zipEntry);
                    for (fis = new FileInputStream(zippedFile); (bytesread = fis.read(buffer, 0, 1024)) != -1; ) {
                        zipOutputStream.write(buffer, 0, bytesread);
                    }
                    zipOutputStream.closeEntry();
                    fis.close();

                } catch (IOException ex) {
                    System.err.println("Error:SCSUtility:folderZipCompressor:IOException " + ex + " ocurred");
                }

            } else if (zippedFile.isDirectory()) {

                folderZipCompressor(zipOutputStream, zippedFile.getPath() + File.separator
                        , Objects.requireNonNull(new File(zippedFile + File.separator).list()));
            }

        }
    }

    //---------------------------------------------------------------------

    //Convert file's pathname to a form acceptable to ZIP files
    public static String convertToZIPName(String root, File f) throws IOException {

        String pname = f.getCanonicalPath();
        String rootname = root.substring(root.lastIndexOf(File.separatorChar) + 1);
        pname = pname.substring(root.length() + 1);
        pname = pname.replace(File.separatorChar, '/');
        return (rootname + "/" + pname);
    }
    //---------------------------------------------------------------------

    //--------------------------------------------------------------------
    public static void setFirstColumn(GridBagConstraints gbc) {
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 0; //first column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
    }

    //--------------------------------------------------------------------
    public static void setFirstColumn(GridBagConstraints gbc, int rows) {
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 0; //first column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;
        gbc.gridheight = rows;
    }

    //--------------------------------------------------------------------
    public static void setFirstColumn(GridBagConstraints gbc, int rows, int columns) {
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 0; //first column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = columns;
        gbc.gridheight = rows;
    }

    //--------------------------------------------------------------------
    public static void setSecondColumn(GridBagConstraints gbc, int rows) {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 1; //second column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = rows;

    }

    //--------------------------------------------------------------------
    //note: I believe this method is having trouble setting the end of
    //the row.
    public static void setSecondColumn(GridBagConstraints gbc, int rows, int columns) {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 1;//second column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = columns;
        gbc.gridheight = rows;


    }

    /** -------------------------------------------------------------
     * addRadioButton
     * @param       buttonPanel JPanel
     * @param       group ButtonGroup
     * @param       buttonName String
     * @param       selected boolean - is the button selected
     * @param       listener ActionListener - class in which the actionPerformed methods is found.
     * default is that buttons will be centered
     * must set the alignment in the calling buttonPanel
     */
    public static JRadioButton addRadioButton(JPanel buttonPanel, ButtonGroup group, String buttonName, boolean selected, ActionListener listener) {

        JRadioButton button = new JRadioButton(buttonName, selected);
        button.addActionListener(listener);
        //set the name of the button in the model as well since the ButtonModel
        // is all we can get from the button group.
        (button.getModel()).setActionCommand(buttonName);

        group.add(button);
        buttonPanel.add(button);
        return button;
    }

    //--------------------------------------------------------------------

    /**    addCheckBox - adds a checkbox to a panel
     * @param       panel JPanel
     * @param       name String
     * @param       listener ActionListener - class in which the actionPerformed methods is found.
     */

    public static JCheckBox addCheckBox(JPanel panel, String name, ActionListener listener) {
        JCheckBox check = new JCheckBox(name);
        check.addActionListener(listener);
        panel.add(check);
        return (check);
    }

    //--------------------------------------------------------------------
    //  item on or off
    public static ButtonGroup addOnOffButtonPanel(Container panelIn, GridBagLayout
            gblayout, GridBagConstraints gbcon, String label, boolean on, ActionListener listener) {
        JLabel label1 = new JLabel(label);
        JRadioButton onButton;
        JRadioButton offButton;

        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(label1, gbcon);
        panelIn.add(label1, gbcon);
        //--
        JPanel onOffPanel = new JPanel();
        FlowLayout fl1 = new FlowLayout(FlowLayout.LEFT);
        onOffPanel.setLayout(fl1);

        ButtonGroup group1 = new ButtonGroup();
        onButton = SCSUtility.addRadioButton(onOffPanel, group1, "On", on, listener);

        offButton = SCSUtility.addRadioButton(onOffPanel, group1, "Off", !on, listener);
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(onOffPanel, gbcon);
        panelIn.add(onOffPanel, gbcon);

        return (group1);
    }

    //--------------------------------------------------------------------
    //  item on or off
    public static boolean setOnOffButtonGroup(ButtonGroup bg, boolean value) {
        ButtonModel bnm; //actually a radio button
        String cmd;
        if (bg == null) {
            return (false);
        } else {
            bnm = bg.getSelection(); //on=true
            //System.out.println("Debug:SCSUtility:bnm :"+bnm);
            cmd = bnm.getActionCommand();
            //System.out.println("Debug:SCSUtility:cmd :"+cmd);
            if ((cmd.equals("On")) && (!value)) {
                bnm.setSelected(false);
            }

            return (true);
        }
    }

    //--------------------------------------------------------------------
    //  item on or off
    public static boolean getOnOffValue(ButtonGroup bg) {

        boolean value;
        String label = bg.getSelection().getActionCommand();

        value = label.equals("On");
        return (value);
    }
    //--------------------------------------------------------------------


    //--------------------------------------------------------------------
    //  item on or off
    public static ButtonGroup addOppositesButtonPanel(Container panelIn, GridBagLayout
            gblayout, GridBagConstraints gbcon, String label, boolean on, String str1, String str2, ActionListener listener) {
        JLabel label1 = new JLabel(label);
        JRadioButton onButton;
        JRadioButton offButton;

        SCSUtility.setFirstColumn(gbcon, 1);
        gblayout.setConstraints(label1, gbcon);
        panelIn.add(label1, gbcon);
        //--
        JPanel onOffPanel = new JPanel();
        FlowLayout fl1 = new FlowLayout(FlowLayout.LEFT);
        onOffPanel.setLayout(fl1);

        ButtonGroup group1 = new ButtonGroup();
        onButton = SCSUtility.addRadioButton(onOffPanel, group1, str1, on, listener);

        offButton = SCSUtility.addRadioButton(onOffPanel, group1, str2, !on, listener);
        SCSUtility.setSecondColumn(gbcon, 1);
        gblayout.setConstraints(onOffPanel, gbcon);
        panelIn.add(onOffPanel, gbcon);

        return (group1);
    }

    //--------------------------------------------------------------------
    //  item on or off
    public static boolean setOppositesButtonGroup(ButtonGroup bg, boolean value, String str1) {
        ButtonModel bnm; //actually a radio button
        String cmd;
        if (bg == null) {
            return (false);
        } else {
            bnm = bg.getSelection(); //on=true
            //System.out.println("Debug:SCSUtility:bnm :"+bnm);
            cmd = bnm.getActionCommand();
            //System.out.println("Debug:SCSUtility:cmd :"+cmd);
            if ((cmd.equals(str1)) && (!value)) {
                bnm.setSelected(false);
            }
            return (true);
        }
    }

    //--------------------------------------------------------------------
    //  item on or off
    public static boolean getOppositesValue(ButtonGroup bg, String str1) {

        boolean value;
        String label = bg.getSelection().getActionCommand();

        value = label.equals(str1);
        return (value);
    }
    //--------------------------------------------------------------------

    //------------------------------------------------------------

    /**
     * Take the given string and chop it up into a series
     * of strings on whitespace boundries.  This is useful
     * for trying to get an array of strings out of the
     * resource file.
     * FROM notepad.java from Sun.
     */
    public static String[] tokenize(String input) {
        Vector<String> v = new Vector<>();
        StringTokenizer t = new StringTokenizer(input);
        String[] cmd;

        while (t.hasMoreTokens())
            v.addElement(t.nextToken());
        cmd = new String[v.size()];
        for (int i = 0; i < cmd.length; i++)
            cmd[i] = v.elementAt(i);

        return cmd;
    }
} //end Class SCSUtility.java





























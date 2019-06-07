/* SCCS  @(#)SubData.mod	1.1---09/24/99--18:57:31 */
/* old kversion @(#)SubData.mod	1.8 --- 08/05/99 -- 13:56:42 : jversion  @(#)SubData.mod	1.2---04/23/99--18:39:51 */
// SubData 
nslImport nslAllImports;

verbatim_NSLJ;
import java.io.*;
import java.util.StringTokenizer;
verbatim_off;

nslModule SubData (){
   NslDoutDouble2 SNRmedburst_out(9,9)   ;

verbatim_NSLJ;
   private StringTokenizer st;
verbatim_off;

public void initRun() {
verbatim_NSLJ;
    byte[] bytebuf = new byte[80000];
    FileInputStream fis ;


    String strbuf = "";

      try {
      fis=new FileInputStream ("sub.dat")  ;


      for(int i=0; i<10; i++) {
      //     while(true) {
	int ret = fis.read(bytebuf);
	strbuf += new String(bytebuf, 0);
	if (ret==-1)
	  break;
      }
    } catch (java.io.IOException e) {
      System.err.println("Error in opening file 'sub.dat'");

      return;
    }

    st= new StringTokenizer(strbuf)  ;
verbatim_off;  

    readData();
   
}

public void simRun() {
  /* @@@ */ System.err.println("@@@@  SubData entered @@@@");
    if(!st.hasMoreTokens()) {nslprintln("End of sub data");return;}
    readData();
  }

public void readData() {
    int i, j;
      for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  SNRmedburst_out[i][j] = 0+Double.valueOf(st.nextToken()).doubleValue();
	}
}
//=================
// the following was in readData
    /*
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  visinput_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  ThLIPmem_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}


    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  PFCmem_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}

      for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  FEFmem_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
      for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  LIPmem_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
	*/
      /*
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  SNCdop_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  PFCgo_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  FEFsac_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  PFCfovea_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  LIPvis_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  ThPFCmem_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}

    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  SNRlatburst_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}

    BSGsaccade_out= Double.valueOf(st.nextToken()).doubleValue();

    for(i=0; i<2; i++) 
	  BSGEyeMovement_out[i] = Double.valueOf(st.nextToken()).doubleValue();

    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  ThFEFmem_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}

    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  SCbu_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  SCsac_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}

    for(i=0; i<9; i++) 
	for(j=0; j<9; j++){
	  LimbicCortex_out[i][j] = Double.valueOf(st.nextToken()).doubleValue();
	}
	*/


} // end class



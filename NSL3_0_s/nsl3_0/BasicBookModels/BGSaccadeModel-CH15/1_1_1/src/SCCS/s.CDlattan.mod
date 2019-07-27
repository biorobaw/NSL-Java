h32119
s 00074/00000/00000
d D 1.1 99/09/24 18:57:11 aalx 1 0
c date and time created 99/09/24 18:57:11 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)CDlattan.mod	1.8 --- 08/05/99 -- 13:56:07 : jversion  @(#)CDlattan.mod	1.2---04/23/99--18:39:20 */
/* $Log: CDlattan.mod,v $
 * Revision 1.1  1998/02/02 22:34:33  erhan
 * crowley3.0.d added
 *
 * Revision 1.2  1998/01/30 19:59:54  erhan
 * ver 5
 * */
/* nocomments=true */
// Import statements
nslImport nslAllImports;

// Leaf nodes

//
// CDlattan
//
/**
 * CDlattan class
 * Represents the Caudate Tonically Active Cells Layer
 * @see     CDlattan
 * @version 0.1 96/11/19
 * @author  Michael Crowley
 * @var private CDlatinh_in - input coming from CDlatinh module (of type NslDouble2)<p>
 * @var private SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 * @var private CDlattan_out - output going to CDlatburst module (of type NslDouble2)<p>
 */
nslModule CDlattan(int CorticalArraySize)
  
{
//inputs
 public NslDinDouble2 SNCdop_in(CorticalArraySize,CorticalArraySize);
 public NslDinDouble2 CDlatinh_in(CorticalArraySize,CorticalArraySize);
//outputs
 public NslDoutDouble2 CDlattan_out(CorticalArraySize,CorticalArraySize);

  // private variables
  private double cdlattantm;
  private double cdlattanTONIC;
  private double CDSNCdopK;
  private double CDltsigma1;
  private double CDltsigma2;
  private double CDltsigma3;
  private double CDltsigma4;


 NslDouble2 cdlattan     (CorticalArraySize,CorticalArraySize);


public void initRun () {
    cdlattantm = 0.01;
    cdlattanTONIC = 10;
    CDSNCdopK = 0.5;
    CDltsigma1 = 0;
    CDltsigma2 = 10;
    CDltsigma3 = 0;
    CDltsigma4 = 10;
  }
public void simRun () {
  // System.err.println("@@@@ CDlattan simRun entered @@@@");
    cdlattan = nslDiff (cdlattan,cdlattantm, 
                              - cdlattan + cdlattanTONIC - CDlatinh_in
                                         - (CDSNCdopK * SNCdop_in));
    CDlattan_out = Nsl2Sigmoid.eval(cdlattan,CDltsigma1, CDltsigma2,
                                     CDltsigma3, CDltsigma4);
}


}  //end class




E 1

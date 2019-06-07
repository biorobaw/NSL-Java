h05812
s 00047/00000/00000
d D 1.1 99/09/24 18:57:23 aalx 1 0
c date and time created 99/09/24 18:57:23 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)MedBulk.mod	1.8 --- 08/05/99 -- 13:56:28 : jversion  @(#)MedBulk.mod	1.2---04/23/99--18:39:36 */

nslImport nslAllImports;

/////////////////////////////////////////////
//Module: MedBulk - Part of the Thalamus
/////////////////////////////////////////////

//
//MedBulk
//
nslModule MedBulk(int CorticalArraySize, int MaxConnections) {

// output ports

    public NslDoutInt2 SNRMapCount_bulk (CorticalArraySize,CorticalArraySize);

    public NslDoutDouble3 SNRweights_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);

    public NslDoutInt3 SNRxmap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);
    public NslDoutInt3 SNRymap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);

    public NslDoutInt3 FEFxmap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);
    public NslDoutInt3 FEFymap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);
    public NslDoutInt3 LIPxmap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);
    public NslDoutInt3 LIPymap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);
    public NslDoutInt3 PFCxmap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);
    public NslDoutInt3 PFCymap_bulk (CorticalArraySize,CorticalArraySize,MaxConnections);

  public void initRun()
  {
    SNRMapCount_bulk=0;
    SNRweights_bulk=0;
    SNRxmap_bulk=0;
    SNRymap_bulk=0;
    FEFxmap_bulk=0;
    FEFymap_bulk=0;
    LIPxmap_bulk=0;
    LIPymap_bulk=0;
    PFCxmap_bulk=0;
    PFCymap_bulk=0;
  }

} //end class


E 1
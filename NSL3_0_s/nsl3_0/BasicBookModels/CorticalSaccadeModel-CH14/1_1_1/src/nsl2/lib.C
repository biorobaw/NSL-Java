
#  include "nsl_include.h" 
# include "math.h"


/* retina = NSLeyemove(INP,X,Y) */
nsl_matrix NSLeyemove(nsl_matrix& INP, nsl_data& X, nsl_data& Y)

{ 
int retinax, retinay, retinadim,x,y,i,j;
float xf,yf;
nsl_matrix retina(9,9);
int inpdim = INP.get_imax();


xf = X.elem();
yf = Y.elem();

x = (int) xf;
y = (int) yf;
retinax = 8;
retinay = 8;
retinadim = 8;


/* now for the fun part !! */


for (i = 0; i <= retinax; i++)
	for (j = 0; j <= retinay; j++)

	   if ( (i+x+9 <= 26) && (i+x+9 >= 0) &&
		(j+y+9 <= 26) && (j+y+9 >= 0))
		 retina.elem(i,j)  = 
			INP.elem(i- y+9,j+x+9);
		 else retina.elem(i,j) = 0;

return retina;

}


/* OL = NSLwinnertakeall(IL); */
nsl_matrix NSLwinnertakeall(nsl_matrix& IL)
{
nsl_matrix OL(9,9);
float thresh;
int ILx, ILy, ILdim,i,j,threshi, threshj;

/* initialization */


thresh = 70;
threshi = 0;
threshj = 0;

/* layer boundaries */
ILx = 8;
ILy = 8;
ILdim = 8;

/* first determine the movement necessary from the SC by
finding the maximum cell there . don't count the center*/

for (i = 0; i <= ILx; i++)
	for (j = 0; j <= ILy; j++)
		if (IL.elem(i,j) > thresh)
			{thresh = IL.elem(i,j);
			 threshi = i;
			 threshj = j;
			}
if (thresh > 70)
	OL.elem(threshi,threshj) = thresh;

return OL;

}




nsl_matrix NSLshift(nsl_data& X, nsl_data& Y,nsl_data& K)
/* create the convolution mask that will generate a Berthoz-like
shift of activity across the QV cell layer.
	call:   MASK = NSLshift(X,Y,k);*/

{
int I,J,maskdim,maskx,masky;

/* layer boundaries */
nsl_matrix MASK(9,9);
float Xnorm, Ynorm,k,Xdata,Ydata;
maskdim = 8;
maskx = 8;
masky = 8;

I = 4;
J = 4;
k = K.get_data();
Xdata = X.get_data();
Ydata = Y.get_data();
Xnorm = X.get_data()/1000;
Ynorm = Y.get_data()/1000;

MASK.elem(J,I) = 1;

if ( Ynorm > .0005) /* up */
	{MASK.elem(J,I) =  1- Ynorm;
	 MASK.elem(J-1,I) =   Ynorm;
	 MASK.elem(J+1,I) =  - Ynorm;	
	}

if ( Ynorm < -0.0005) /* down */
	{MASK.elem(J,I) =  1+ Ynorm;
	 MASK.elem(J-1,I) =   Ynorm;
	 MASK.elem(J+1,I) =  - Ynorm;
	}

if ( Xnorm > 0.0005) /* right */
	{MASK.elem(J,I) =  1- Xnorm;
	 MASK.elem(J,I+1) =   Xnorm;
	 MASK.elem(J,I-1) =  - Xnorm;
	}
	
if ( Xnorm < -0.0005) /* left */
	{MASK.elem(J,I) =  1+ Xnorm;
	 MASK.elem(J,I+1) =   Xnorm;
	 MASK.elem(J,I-1) =  - Xnorm;
	}
/*down left*/
if ( Ynorm < -.005 &&  Xnorm < -.005)
	{
	 MASK.elem(J+1,I-1) =  -Ydata/k -Xdata/k;
	 MASK.elem(J-1,I+1) =  Ydata/k + Xdata/k;
	}

/*up rigth*/
if ( Ynorm > 0.005 &&  Xnorm > .005)
	{
	 MASK.elem(J-1,I+1) =  Ydata/k + Xdata/k;
	 MASK.elem(J+1,I-1) =  -Ydata/k - Xdata/k;
	}

/*up left*/
if ( Ynorm > .005 && Xnorm < -.005)
	{
	 MASK.elem(J-1,I-1) =  Ydata/k -Xdata/k;
	 MASK.elem(J+1,I+1) =  -Ydata/k +Xdata/k;
	}

/*down right*/
if ( Ynorm < -.005 &&  Xnorm > .005)
	{
	 MASK.elem(J+1,I+1) =  -Ydata/k + Xdata/k;
	 MASK.elem(J-1,I-1) =  Ydata/k - Xdata/k;
	}


return MASK;
}



float max(float x, float y)
{ 
	if (x >= y) 
		return(x); 
	else 	return(y);
}






nsl_matrix NSLXshift
	(nsl_data& X, nsl_data& Y, nsl_data& K, nsl_data& Ctr,
		nsl_data& Bfactor)
/* create the convolution mask that will generate a Berthoz-like
shift of activity across the QV cell layer.
	call:   MASK = NSLshift(X,Y,k);*/

{
int I,J,maskdim,maskx,masky;

/* layer boundaries */
nsl_matrix MASK(9,9);
float k,h,v,sinTheta,cosTheta,
		A,B,C,D,E,G,H,II,JJ,KK,Smagnitude,S,ctr,bf;
maskdim = 8;
maskx = 8;
masky = 8;

I = 4;
J = 4;
k = K.get_data();
h = X.get_data();
v = Y.get_data();
ctr = Ctr.get_data();
bf = Bfactor.get_data();

/* printf("QV k: %f  h: %f  v: %f  \n",k,h,v); */

/* here we will an analytical geometry approach to projecting the horizontal
and vertical components of the eye velocity onto the qvmask to implement shifiting
how radical!!!  */

Smagnitude = sqrt((h*h) + (v*v));
S = Smagnitude/k;

if (Smagnitude < 1)
		 MASK.elem(J,I) = 1;

else
{

cosTheta = h/ Smagnitude;
sinTheta = v/ Smagnitude;


A = S*(max((sinTheta-cosTheta),0)); /*vertical comp */
B = bf*S*( 1-((cosTheta-sinTheta)*(cosTheta-sinTheta)))/1.414214;
C = S*(max((cosTheta-sinTheta),0)); /*horiz comp */

D =  (1-S)*ctr;

E = S*(max((sinTheta+cosTheta),0)); /*vertical comp */
G = S*(max((-cosTheta-sinTheta),0)); /*horiz comp */
H = S*(max((-cosTheta+sinTheta),0)); /*horiz comp */

II = S*(max((-sinTheta+cosTheta),0)); /*vertical comp */
JJ = S*(max((-sinTheta-cosTheta),0)); /*vertical comp */

KK = S*(max((cosTheta+sinTheta),0)); /*horiz comp */


/* printf("  Diagonal B:  %f \n",B); */
/*up right - Theta between 0 and pi/2 */
if ( h >= 0 &&  v >= 0 ) 
	{
	 MASK.elem(J-1,I) = A;
	 MASK.elem(J+1,I) = -A;	
	 MASK.elem(J-1,I+1) =  B;
	 MASK.elem(J+1,I-1) =  -B;	 
	 MASK.elem(J,I+1) = C;
	 MASK.elem(J,I-1) = -C;
	 MASK.elem(J,I) = D;
	}

/*up left*/
if  ( h <= 0 &&  v >= 0 ) 
	{
	 MASK.elem(J-1,I) = E;
	 MASK.elem(J+1,I) = -E;	
	 MASK.elem(J-1,I-1) =  -B;
	 MASK.elem(J+1,I+1) =  B;	 
	 MASK.elem(J,I-1) = G;
	 MASK.elem(J,I+1) = -G;
	 MASK.elem(J,I) = D;
	}



/*down left*/
if ( h <= 0 &&  v <= 0 )
	{
	 MASK.elem(J,I-1) = H;
	 MASK.elem(J,I+1) = -H;	
	 MASK.elem(J+1,I-1) =  B;
	 MASK.elem(J-1,I+1) =  -B;	 
	 MASK.elem(J+1,I) = II;
	 MASK.elem(J-1,I) = -II;
	 MASK.elem(J,I) = D;
	}



/*down right*/
if ( h >= 0 &&  v <= 0 )
	{
	 MASK.elem(J+1,I) = JJ;
	 MASK.elem(J-1,I) = -JJ;
	 MASK.elem(J+1,I+1) =  -B;
	 MASK.elem(J-1,I-1) =  B;	 
	 MASK.elem(J,I+1) = KK;
	 MASK.elem(J,I-1) = -KK;
	 MASK.elem(J,I) = D;
	}

} /*end else */

return MASK;
}


nsl_matrix NSLX2shift
	(nsl_data& X, nsl_data& Y, nsl_data& K, nsl_data& Ctr,
		nsl_data& Bfactor)
/* create the convolution mask that will generate a Berthoz-like
shift of activity across the QV cell layer.
	call:   MASK = NSLshift(X,Y,k);*/

{
int I,J,maskdim,maskx,masky;

/* layer boundaries */
nsl_matrix MASK(9,9);
float k,h,v,sinTheta,cosTheta,
		A,B,C,D,E,G,H,II,JJ,KK,Smagnitude,S,ctr,bf;
maskdim = 8;
maskx = 8;
masky = 8;

I = 4;
J = 4;
k = K.get_data();
h = X.get_data();
v = Y.get_data();
ctr = Ctr.get_data();
bf = Bfactor.get_data();

/* printf("QV k: %f  h: %f  v: %f  \n",k,h,v); */

/* here we will an analytical geometry approach to projecting the horizontal
and vertical components of the eye velocity onto the qvmask to implement shifiting
how radical!!!  */

Smagnitude = sqrt((h*h) + (v*v));
S = Smagnitude/k;

if (Smagnitude < 1)
		 MASK.elem(J,I) = 1;

else
{

cosTheta = h/ Smagnitude;
sinTheta = v/ Smagnitude;


A = S*sinTheta; /*vertical comp */
B = bf*S*( 1-((cosTheta-sinTheta)*(cosTheta-sinTheta)))/1.414214;
C = S*cosTheta; /*horiz comp */

D =  (1-S)*ctr;

E = S*sinTheta; /*vertical comp */
G = S*(-cosTheta); /*horiz comp */
H = S*(-cosTheta); /*horiz comp */

II = S*(-sinTheta); /*vertical comp */
JJ = S*(-sinTheta); /*vertical comp */

KK = S*(cosTheta); /*horiz comp */


/* printf("  Diagonal B:  %f \n",B); */

/*up right - Theta between 0 and pi/2 */
if ( h >= 0 &&  v >= 0 ) 
	{
	 MASK.elem(J-1,I) = A;  
	 MASK.elem(J+1,I) = -A;	
	 MASK.elem(J-1,I+1) =  B;
	 MASK.elem(J+1,I-1) =  -B;	 
	 MASK.elem(J,I+1) = C;
	 MASK.elem(J,I-1) = -C;
	 MASK.elem(J,I) = D;
	}

/*up left*/
if  ( h <= 0 &&  v >= 0 ) 
	{
	 MASK.elem(J-1,I) = E;
	 MASK.elem(J+1,I) = -E;	
	 MASK.elem(J-1,I-1) =  -B;
	 MASK.elem(J+1,I+1) =  B;	 
	 MASK.elem(J,I-1) = G;
	 MASK.elem(J,I+1) = -G;
	 MASK.elem(J,I) = D;
	}



/*down left*/
if ( h <= 0 &&  v <= 0 )
	{
	 MASK.elem(J,I-1) = H;
	 MASK.elem(J,I+1) = -H;	
	 MASK.elem(J+1,I-1) =  B;
	 MASK.elem(J-1,I+1) =  -B;	 
	 MASK.elem(J+1,I) = II;
	 MASK.elem(J-1,I) = -II;
	 MASK.elem(J,I) = D;
	}



/*down right*/
if ( h >= 0 &&  v <= 0 )
	{
	 MASK.elem(J+1,I) = JJ;
	 MASK.elem(J-1,I) = -JJ;
	 MASK.elem(J+1,I+1) =  -B;
	 MASK.elem(J-1,I-1) =  B;	 
	 MASK.elem(J,I+1) = KK;
	 MASK.elem(J,I-1) = -KK;
	 MASK.elem(J,I) = D;
	}

} /*end else */

return MASK;
}


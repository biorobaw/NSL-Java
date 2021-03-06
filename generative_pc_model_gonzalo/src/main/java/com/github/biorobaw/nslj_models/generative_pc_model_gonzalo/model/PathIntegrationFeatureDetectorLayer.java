package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model;
/* M�dulo NSL que representa una capa de neuronas cuya activaci�n
   codifica la se�al de path integration generada por la capa
   DynamicRemappingLayer cada vez que la rata experimenta un
   movimiento.
   Alejandra Barrera
   Versi�n: 1
   Fecha: 18 de marzo de 2005
*/



import java.util.Random;

import com.github.biorobaw.nslj.lang.NslDinDouble1;
import com.github.biorobaw.nslj.lang.NslDoutDouble1;
import com.github.biorobaw.nslj.lang.NslDoutDouble2;
import com.github.biorobaw.nslj.lang.NslModule;

public class PathIntegrationFeatureDetectorLayer extends NslModule
{
	public static final int PI_HEIGHT  = Configuration
	.getInt("PathIntegrationFeatureDetectorLayer.LAYER_HEIGHT"); // 20 original
	public static final int PI_WIDTH = Configuration
	.getInt("PathIntegrationFeatureDetectorLayer.LAYER_WIDTH");
	public NslDinDouble1 PIps1dim;
	public NslDoutDouble2 piFDL2dim;				        
	public NslDoutDouble1 piFDL1dim;
    private double[][] activation;
    private double[][] hebbianWeights;
    private int[][] index_max_hebbian;
    private int[] winnerNode;
    public static final int DIM1= PI_HEIGHT;
	public static final int DIM2= PI_WIDTH;
	public static final double CONNECTIVITY_RATE = 0.5D;
	public static final int NEIGHBORHOOD = 5;
    public static final int SPIKES = 20; // M neuronas mas activas de cada vecindario
    public static final int TOTAL_NEURONS = DIM1*DIM2;
    public static final int IN_DIM= 2*DynamicRemappingLayer.DYNAMIC_REMAPPING_HEIGHT*2*DynamicRemappingLayer.DYNAMIC_REMAPPING_WIDTH;
    private double[] piFDL1dimAux;

	public PathIntegrationFeatureDetectorLayer(String nslName, NslModule nslParent)
	{
		super(nslName, nslParent);
		PIps1dim= new NslDinDouble1("PIps1dim",this,IN_DIM);

		piFDL2dim= new NslDoutDouble2("piFDL2dim",this,DIM1,DIM2);
		activation= new double[DIM1][DIM2];
		piFDL1dim= new NslDoutDouble1("piFDL1dim",this,TOTAL_NEURONS);
		piFDL1dimAux= new double[TOTAL_NEURONS];

		hebbianWeights = new double[IN_DIM][TOTAL_NEURONS];
		winnerNode = new int[TOTAL_NEURONS];
		index_max_hebbian = new int[NEIGHBORHOOD][SPIKES];
		initHebbianNet();
	}

	public void initHebbianNet()
    	{
        	Random random = new Random();
	        for(int i = 0; i < TOTAL_NEURONS; i++)
        	    winnerNode[i] = 0;
	        for(int i = 0; i < NEIGHBORHOOD; i++)
	        {
        	    for(int j = 0; j < SPIKES; j++)
                	index_max_hebbian[i][j] = -1;
        	}
	        int i2 = 0;
	        int j2 = 0;
	        // Inicializa las conexiones entre la capa DLR y la capa PIFDL según la razon de conectividad definida por CONNECTIVITY_RATE
        	for(int j = 0; j < IN_DIM; j++)
        	{
            	    for(int k = 0; k < TOTAL_NEURONS; k++)
		    			if(random.nextDouble() > CONNECTIVITY_RATE)
                		{
	                    	hebbianWeights[j][k] = 0.0D;
		            		j2++;
                		} else
              			{
		            		hebbianWeights[j][k] = random.nextDouble();
                	    	i2++;
						}                	
        	}

        	// Normaliza pesos pp26 ecuacion 7
	        for(int l1 = 0; l1 < TOTAL_NEURONS; l1++)
        	{
	            double d = 0.0D;
        	    for(int k = 0; k < IN_DIM; k++)
                	d += hebbianWeights[k][l1];
	            for(int l = 0; l < IN_DIM; l++)
        	        hebbianWeights[l][l1] /= d;
	        }

	        for(int i1 = 0; i1 < TOTAL_NEURONS; i1++)
	            piFDL1dimAux[i1] = 0.0D;
	}
	
	public void simRun()
	{
		learnHebbian(PIps1dim.get(), piFDL1dimAux, 0);
		piFDL2dim.set(activation);
		piFDL1dim.set(piFDL1dimAux);
	}

	public void learnHebbian(double[] ad, double[] ad1, int i)
	{

        double[] ad2 = new double[SPIKES];
        for(int k = 0; k < SPIKES; k++)
            ad2[k] = -1D;

        double[][] ad3 = new double[NEIGHBORHOOD][SPIKES];
        int[][] ai = new int[NEIGHBORHOOD][SPIKES];
         for(int l = 0; l < NEIGHBORHOOD; l++)
        {
            for(int j3 = 0; j3 < SPIKES; j3++)
            {
                int l8 = index_max_hebbian[l][j3];
                ai[l][j3] = l8;
                int l6 = l8 / DIM1;
                int l7 = l8 % DIM2;
                if(l8 == -1)
                    ad3[l][j3] = 0.0D;
                else
                    ad3[l][j3] = activation[l6][l7];
            }

        }

        for(int j1 = 0; j1 < DIM1; j1++)
        {
            for(int k3 = 0; k3 < DIM2; k3++)
                activation[j1][k3] = 0.0D;

        }

            for(int k1 = 0; k1 < NEIGHBORHOOD; k1++) 
            {
                int l9 = k1 * (TOTAL_NEURONS / NEIGHBORHOOD);
                for(int i5 = 0; i5 < TOTAL_NEURONS / NEIGHBORHOOD; i5++)
                {
                    int i9 = l9 + i5;
                    int i7 = i9 / DIM1;
                    int i8 = i9 % DIM2;
                    for(int k6 = 0; k6 < IN_DIM; k6++) {
                    	//System.err.println("PI::i7: "+i7+ " i8: "+i8+" k6: "+k6+" i9: "+i9);
                        activation[i7][i8] += ad[k6] * hebbianWeights[k6][i9];
                    }
                }

            }

            for(int l1 = 0; l1 < TOTAL_NEURONS; l1++)
                ad1[l1 + i * TOTAL_NEURONS] = 0.0D;

            for(int i2 = 0; i2 < NEIGHBORHOOD; i2++)
            {
                int i10 = i2 * (TOTAL_NEURONS / NEIGHBORHOOD);
                for(int l3 = 0; l3 < SPIKES; l3++)
                {
                    for(int j5 = 0; j5 < SPIKES; j5++)
                        ad2[j5] = -1D;

                    for(int k5 = 0; k5 < TOTAL_NEURONS / NEIGHBORHOOD; k5++)
                    {
                        int j9 = i10 + k5;
                        int j7 = j9 / DIM1;
                        int j8 = j9 % DIM2;
                        if(activation[j7][j8] > ad2[l3])
                        {
                            ad2[l3] = activation[j7][j8];
                            index_max_hebbian[i2][l3] = j9;
                        }
                    }

                    int k9 = index_max_hebbian[i2][l3];
                    int k7 = k9 / DIM1;
                    int k8 = k9 % DIM2;
                    activation[k7][k8] = 0.0D;

                    double d1=0.001D;
                    double d5 = 0.0D;
                    for(int i6 = 0; i6 < IN_DIM; i6++)
                    {
                        hebbianWeights[i6][index_max_hebbian[i2][l3]] += d1 * ad[i6] * ((double)(SPIKES - l3) / (double) SPIKES) * hebbianWeights[i6][index_max_hebbian[i2][l3]];
                        d5 += hebbianWeights[i6][index_max_hebbian[i2][l3]];
                    }
                    for(int j6 = 0; j6 < IN_DIM; j6++)
                        hebbianWeights[j6][index_max_hebbian[i2][l3]] /= d5;
                    ad1[index_max_hebbian[i2][l3] + i * TOTAL_NEURONS] = (double)(SPIKES - l3) / (double)SPIKES;  
                    winnerNode[index_max_hebbian[i2][l3]] += (double)(SPIKES - l3);  
                    ad2[l3] = -1D;
                }
            }
            for(int j2 = 0; j2 < DIM1; j2++)
            {
                for(int i4 = 0; i4 < DIM2; i4++)
                {
                    int j10 = j2 * DIM2 + i4 + i * TOTAL_NEURONS;
                    activation[j2][i4] = ad1[j10];
                }
            }		
	} //learnHebbian
}

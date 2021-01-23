package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model;
//import com.sun.image.codec.jpeg.*;
import java.awt.GraphicsConfiguration;
import java.awt.geom.Point2D.Double;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.Viewer;

//import com.sun.j3d.utils.universe.Viewer;


public class TopWorldCanvas extends WorldCanvas {
    
    public TopWorldCanvas(GraphicsConfiguration gc, worldBranchGroup branchGroup) {
	super(gc, branchGroup);
    }
    
     public TopWorldCanvas(GraphicsConfiguration gc) {
	super(gc);
    }
    
    public void finalise() {
	simpleU.addBranchGraph(rootBranchGroup);

        TransformGroup viewTrans = simpleU.getViewingPlatform().getViewPlatformTransform();
        Transform3D myTransform3D2 = new Transform3D();
        myTransform3D2.rotX(Math.PI/180.0d*-90);

        Viewer[] v = simpleU.getViewingPlatform().getViewers();
        v[0].getView().setFrontClipDistance(0.0001);
	simpleU.getViewingPlatform().setNominalViewingTransform();
        
        Transform3D myTransform3D = new Transform3D();
        myTransform3D.setTranslation(rootBranchGroup.getTopView());
        myTransform3D.mul(myTransform3D2);
        viewTrans.setTransform(myTransform3D);
    }
    
    public void moveRobot(Vector3f vector) {
        rootBranchGroup.moveRobot(vector);
    }
    
    public  Double getGlobalPosition() {
    	return rootBranchGroup.getRobotView();
    }

    public  Double getFood() {
    	Double result = new Double(rootBranchGroup.getFood().x,rootBranchGroup.getFood().z);
    	return result;
    }

}


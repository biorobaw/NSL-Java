package com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model;
//import com.sun.image.codec.jpeg.*;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.Viewer;

//import com.sun.j3d.utils.universe.Viewer;

public class ThirdViewWorldCanvas extends WorldCanvas {
    
    public ThirdViewWorldCanvas(GraphicsConfiguration gc, worldBranchGroup branchGroup) {
	super(gc, branchGroup);
    }
    
     public ThirdViewWorldCanvas(GraphicsConfiguration gc) {
	super(gc);
    }
    
    public void finalise() {
	simpleU.addBranchGraph(rootBranchGroup);

        Viewer[] v = simpleU.getViewingPlatform().getViewers();
        v[0].getView().setFrontClipDistance(0.0001);
	simpleU.getViewingPlatform().setNominalViewingTransform();
	TransformGroup viewTrans = simpleU.getViewingPlatform().getViewPlatformTransform();
        Transform3D myTransform3D2 = new Transform3D();
        myTransform3D2.rotY(Math.PI/180.0d*40);
        
        Transform3D myTransform3D = new Transform3D();
        myTransform3D.setTranslation(rootBranchGroup.getThirdView());
        myTransform3D.mul(myTransform3D2);
        
        Transform3D myTransform3D3 = new Transform3D();
        myTransform3D3.rotX(Math.PI/180.0d*-30);
        myTransform3D.mul(myTransform3D3);
        
        viewTrans.setTransform(myTransform3D);
        
    }
    
    public void moveRobot(Vector3f vector) {
        rootBranchGroup.moveRobot(vector);
    }
    
}


package robot;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/**
 *
 * @author nicolas
 */
public class HSVSlider extends javax.swing.JFrame {

    /** Creates new form KTGrab */
    public HSVSlider() {
        initComponents();
        this.setSize(150, 200);
        setVisible(true);
    }

    int iterCurrentColor = 0;
    
    private void initComponents() {
   		java.awt.GridBagConstraints gridBagConstraintsPanel = new java.awt.GridBagConstraints();

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
 
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(320, 240));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Min Hue");
        gridBagConstraintsPanel.gridx = 1;
        gridBagConstraintsPanel.gridy = 1;
        //gridBagConstraintsPanel.gridwidth = 3;
        jPanel1.add(jLabel1, gridBagConstraintsPanel);
        jLabel2.setText("Max Hue");
        gridBagConstraintsPanel.gridx = 1;
        gridBagConstraintsPanel.gridy = 2;
        jPanel1.add(jLabel2, gridBagConstraintsPanel);

        jLabel3.setText("Min Saturation");
        gridBagConstraintsPanel.gridx = 1;
        gridBagConstraintsPanel.gridy = 3;
        jPanel1.add(jLabel3, gridBagConstraintsPanel);
        jLabel4.setText("Max Saturation");
        gridBagConstraintsPanel.gridx = 1;
        gridBagConstraintsPanel.gridy = 4;
        jPanel1.add(jLabel4, gridBagConstraintsPanel);

        jLabel5.setText("Min Value");
        gridBagConstraintsPanel.gridx = 1;
        gridBagConstraintsPanel.gridy = 5;
        jPanel1.add(jLabel5, gridBagConstraintsPanel);

        jLabel6.setText("Max Value");
        gridBagConstraintsPanel.gridx = 1;
        gridBagConstraintsPanel.gridy = 6;
        jPanel1.add(jLabel6, gridBagConstraintsPanel);
        
        gridBagConstraintsPanel.gridx = 1;
        gridBagConstraintsPanel.gridy = 7;
        jPanel1.add(currentColorLabel, gridBagConstraintsPanel);
        
        jsMinHue.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                jLabel1.setText("Min Hue (" + jsMinHue.getValue()+")");
            }
          });    
        gridBagConstraintsPanel.gridx = 2;
        gridBagConstraintsPanel.gridy = 1;
        jPanel1.add(jsMinHue, gridBagConstraintsPanel);
        
        jsMaxHue.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                jLabel2.setText("Max Hue (" + jsMaxHue.getValue()+")");
            }
          });
        gridBagConstraintsPanel.gridx = 2;
        gridBagConstraintsPanel.gridy = 2;
        jPanel1.add(jsMaxHue, gridBagConstraintsPanel);
        
        jsMinSat.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                jLabel3.setText("Min Sat (" + jsMinSat.getValue()+")");
            }
          });   
        gridBagConstraintsPanel.gridx = 2;
        gridBagConstraintsPanel.gridy = 3;
        jPanel1.add(jsMinSat, gridBagConstraintsPanel);
        
        jsMaxSat.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                jLabel4.setText("Max Sat (" + jsMaxSat.getValue()+")");
            }
          });
        gridBagConstraintsPanel.gridx = 2;
        gridBagConstraintsPanel.gridy = 4;
        jPanel1.add(jsMaxSat, gridBagConstraintsPanel);
        
        jsMinVal.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                jLabel5.setText("Min Val (" + jsMinVal.getValue()+")");
            }
          });   
        gridBagConstraintsPanel.gridx = 2;
        gridBagConstraintsPanel.gridy = 5;
        jPanel1.add(jsMinVal, gridBagConstraintsPanel);

        jsMaxVal.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                jLabel6.setText("Max Val (" + jsMaxVal.getValue()+")");
            }
          });
        gridBagConstraintsPanel.gridx = 2;
        gridBagConstraintsPanel.gridy = 6;
        jPanel1.add(jsMaxVal, gridBagConstraintsPanel);

        nextButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				iterCurrentColor++;
				setSlidersCurrentColor();
			}
			

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
          });
        gridBagConstraintsPanel.gridx = 2;
        gridBagConstraintsPanel.gridy = 7;
        jPanel1.add(nextButton, gridBagConstraintsPanel);

        
        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }
    
    private javax.swing.JSlider jsMinHue = new javax.swing.JSlider(0,256,0);
    private javax.swing.JSlider jsMaxHue = new javax.swing.JSlider(0,256,256);
    private javax.swing.JSlider jsMinSat = new javax.swing.JSlider(0,256,0);
    private javax.swing.JSlider jsMaxSat = new javax.swing.JSlider(0,256, 256);
    private javax.swing.JSlider jsMinVal = new javax.swing.JSlider(0,256, 0);
    private javax.swing.JSlider jsMaxVal = new javax.swing.JSlider(0,256, 256);
    private JButton nextButton = new JButton("Next");
    private JLabel currentColorLabel = new JLabel("Current color");
    
    private javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
    private javax.swing.JPanel jPanel1;

    private void setSlidersCurrentColor() {
    	if (iterCurrentColor>=SpatialCognitionInterfaceOpenCV.calibratedColorsHSV.length)
    		iterCurrentColor=0;
    	HSVRange currentColor = SpatialCognitionInterfaceOpenCV.calibratedColorsHSV[iterCurrentColor];
    	jsMinHue.setValue((int) currentColor.getHue().getMin());
    	jsMaxHue.setValue((int) currentColor.getHue().getMax());
    	jsMinSat.setValue((int) currentColor.getSaturation().getMin());
    	jsMaxSat.setValue((int) currentColor.getSaturation().getMax());
    	jsMinVal.setValue((int) currentColor.getValue().getMin());
    	jsMaxVal.setValue((int) currentColor.getValue().getMax());

    	currentColorLabel.setForeground(SpatialCognitionInterfaceOpenCV.calibratedColorsRGB[iterCurrentColor]);
	}

    HSVRange getHSVRange() {
    	//System.out.println("HSVSlider:: "+ jsMinHue.getValue() + ", "+ jsMaxHue.getValue() + ", "+ jsMinSat.getValue() + ", "+ jsMaxSat.getValue() + ", "+ jsMinVal.getValue() + ", "+ jsMaxVal.getValue() + ".");
    	return new HSVRange(new Range (jsMinHue.getValue(), jsMaxHue.getValue()), new Range (jsMinSat.getValue(), jsMaxSat.getValue()), new Range (jsMinVal.getValue(), jsMaxVal.getValue()));
    }
}

/* SCCS  @(#)CrowleyParams.mod	1.1---09/24/99--18:57:14 */
/* old kversion @(#)CrowleyParams.mod	1.8 --- 08/05/99 -- 13:56:14 */
/* copyright Michael Crowley 1997 */
verbatim_NSLJ;

import java.awt.*;
import java.lang.*;
import java.awt.event.*;

verbatim_off;

nslImport nslAllImports;

verbatim_NSLJ;

class CrowleyParams extends java.awt.Frame implements ActionListener {
  private TextField tf_dop;

  public static void addComponent (Container container, Component component,
				   int gridx, int gridy, 
				   int gridwidth, int gridheight,
				   int fill, int anchor) 
    throws AWTException {
      LayoutManager lm = container.getLayout();
      if(!(lm instanceof GridBagLayout)){
	throw new AWTException("Invalid layout" + lm);
      } else {
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = gridx;
	gbc.gridy = gridy;
	gbc.gridwidth = gridwidth;
	gbc.gridheight = gridheight;
	gbc.fill = fill;
	gbc.anchor = anchor;
	((GridBagLayout)lm).setConstraints(component,gbc);
	container.add(component);
      }
  }
  
  CrowleyParams() {
    Label l;
    Button b;

    setTitle("Model Parameters");
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    setLayout(gb);
    try{
      l = new Label("Network Parameters");
      addComponent(this, l, 0, 0, 2, 1,
		   GridBagConstraints.NONE, GridBagConstraints.CENTER);
      l = new Label("Name");
      addComponent(this, l, 0, 1, 1, 1,
		   GridBagConstraints.NONE, GridBagConstraints.CENTER);
      l = new Label("Value");
      addComponent(this, l, 1, 1, 1, 1,
		   GridBagConstraints.NONE, GridBagConstraints.CENTER);


      b = new Button("Dopamine");
      b.addActionListener(this);
      addComponent(this, b, 0, 2, 1, 1,
		   GridBagConstraints.NONE, GridBagConstraints.CENTER);
      tf_dop = new TextField("30.0",5);
      addComponent(this, tf_dop, 1, 2, 1, 1,
		   GridBagConstraints.NONE, GridBagConstraints.CENTER);

      b = new Button("Accept");
      b.addActionListener(this);
      addComponent(this, b, 0, 3, 1, 1,
		   GridBagConstraints.NONE, GridBagConstraints.CENTER);
      b = new Button("Cancel");
      b.addActionListener(this);
      addComponent(this, b, 1, 3, 1, 1,
		   GridBagConstraints.NONE, GridBagConstraints.CENTER);
    } catch(Exception e){
      e.printStackTrace();
    }
    pack();
    setSize(200,150);
  }

  public void actionPerformed(ActionEvent e){
    NslNumeric nslDop;
    double dv;
    String s = e.getActionCommand();
    CrowleyHelp nh;

    if("Dopamine".equals(s)){
      System.out.println("Pop up help text");
      nh = new CrowleyHelp(this, "This is a longwinded explanation of the pros and cons of changing the value of Dopamine here.");
      nh.setVisible(true);
      nh.dispose();
    }
    else if("Accept".equals(s)){
      nslDop = (NslNumeric)nslj.src.nsls.struct.s_Nsl.resolve_var("lc.LimbicCortex_out");
      ((NslDouble2)nslDop).set(Double.valueOf(tf_dop.getText()).doubleValue());
      System.out.println("Should set Dopamine to " + tf_dop.getText());
      setVisible(false);
    }
    else if("Cancel".equals(s)){
      nslDop = (NslNumeric)nslj.src.nsls.struct.s_Nsl.resolve_var("lc.LimbicCortex_out");
      dv = ((NslDouble2)nslDop).get(0,0);
      tf_dop.setText(Double.toString(dv));
      setVisible(false);
    }
  }
  
}
verbatim_off;

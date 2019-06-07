/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)SetMappingParamDialog.mod	1.8 --- 08/05/99 -- 13:56:41 : jversion  @(#)SetMappingParamDialog.mod	1.8---08/05/99--13:56:41 */


verbatim_NSLJ;
/*
    A basic extension of the java.awt.Dialog class
 */

import java.awt.*;

public class SetMappingParamDialog extends Dialog {
	void buttonOK_Clicked(Event event) {
		// to do: place event handler code here.
	  try {
	    med.MappingParameters(Integer.valueOf(patch_num.getText()).intValue(), 
				  Integer.valueOf(patch_size.getText()).intValue(), 
				  Double.valueOf(fill_ratio.getText()).doubleValue(),
				  Double.valueOf(nslConnectection_ratio.getText()).doubleValue(), 
				  Double.valueOf(learning_rate.getText()).doubleValue()); 
	  } catch (NumberFormatException e) {
	    System.err.println("Make sure the first two arguments are integer type\n"+
			       "and the next three are double floating pointer type");
	    return;
	  }
		disable();
		dispose();
	}
	public SetMappingParamDialog(Frame parent, boolean modal, Med med) {
	  this(parent, modal);
	  this.med = med;
	}

	public SetMappingParamDialog(Frame parent, boolean modal) {

	    super(parent, modal);

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(insets().left + insets().right + 430,insets().top + insets().bottom + 270);
		patch_size = new java.awt.TextField();
		patch_size.setText("3");
		patch_size.reshape(insets().left + 252,insets().top + 75,63,30);
		add(patch_size);
		patch_num = new java.awt.TextField();
		patch_num.setText("9");
		patch_num.reshape(insets().left + 252,insets().top + 45,63,30);
		add(patch_num);
		lb_patch_num = new java.awt.Label("Number of Patches (max 16)",Label.RIGHT);
		lb_patch_num.reshape(insets().left + 60,insets().top + 48,180,15);
		add(lb_patch_num);
		lb_patch_size = new java.awt.Label("Patch Size");
		lb_patch_size.reshape(insets().left + 60,insets().top + 77,180,15);
		add(lb_patch_size);
		lb_fill_ratio = new java.awt.Label("Fill Ratio");
		lb_fill_ratio.reshape(insets().left + 60,insets().top + 106,180,15);
		add(lb_fill_ratio);
		lb_nslConnectection_ratio = new java.awt.Label("Connection Ratio (0-1)");
		lb_nslConnectection_ratio.reshape(insets().left + 60,insets().top + 135,180,15);
		add(lb_nslConnectection_ratio);
		lb_learning_rate = new java.awt.Label("Learning Rate (~0.05)");
		lb_learning_rate.reshape(insets().left + 60,insets().top + 164,180,15);
		add(lb_learning_rate);
		label1 = new java.awt.Label("Mapping Parameter Initialization",Label.RIGHT);
		label1.reshape(insets().left + 50,insets().top + 8,323,40);
		label1.setFont(new Font("Dialog", Font.PLAIN, 16));
		label1.setForeground(new Color(255));
		add(label1);
		fill_ratio = new java.awt.TextField();
		fill_ratio.setText("0.5");
		fill_ratio.reshape(insets().left + 252,insets().top + 105,63,30);
		add(fill_ratio);
		nslConnectection_ratio = new java.awt.TextField();
		nslConnectection_ratio.setText("0.5");
		nslConnectection_ratio.reshape(insets().left + 252,insets().top + 135,63,30);
		add(nslConnectection_ratio);
		learning_rate = new java.awt.TextField();
		learning_rate.setText("0.05");
		learning_rate.reshape(insets().left + 252,insets().top + 165,63,30);
		add(learning_rate);
		button_OK = new java.awt.Button("OK");
		button_OK.reshape(insets().left + 175,insets().top + 203,60,30);
		add(button_OK);
		setTitle("");
		//}}
	}

	public SetMappingParamDialog(Frame parent, String title, boolean modal) {
	    this(parent, modal);
	    setTitle(title);
	}

    public synchronized void show() {
    	Rectangle bounds = getParent().bounds();
    	Rectangle abounds = bounds();

    	move(bounds.x + (bounds.width - abounds.width)/ 2,
    	     bounds.y + (bounds.height - abounds.height)/2);

    	super.show();
    }

	public boolean handleEvent(Event event) {
	    if(event.id == Event.WINDOW_DESTROY) {
	        hide();
	        return true;
	    }
		if (event.target == button_OK && event.id == Event.ACTION_EVENT) {
			buttonOK_Clicked(event);
		}
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	java.awt.TextField patch_size;
	java.awt.TextField patch_num;
	java.awt.Label lb_patch_num;
	java.awt.Label lb_patch_size;
	java.awt.Label lb_fill_ratio;
	java.awt.Label lb_nslConnectection_ratio;
	java.awt.Label lb_learning_rate;
	java.awt.Label label1;
	java.awt.TextField fill_ratio;
	java.awt.TextField nslConnectection_ratio;
	java.awt.TextField learning_rate;
	java.awt.Button button_OK;
	//}}

  Med med;
}
verbatim_off;
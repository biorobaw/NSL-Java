h39853
s 00144/00000/00000
d D 1.1 99/09/24 18:57:16 aalx 1 0
c date and time created 99/09/24 18:57:16 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)EventForm.mod	1.8 --- 08/05/99 -- 13:56:16 */

verbatim_NSLJ;
/*
    A basic extension of the java.awt.Dialog class
 */

import java.awt.*;

public class EventForm extends Dialog {
	void buttonConfirm_Clicked(Event event) {
		// to do: place event handler code here.
		System.out.println("Confirm clicked");
	}

	void buttonCancel_Clicked(Event event) {
		// to do: place event handler code here.
		dispose();
	}



	public EventForm(Frame parent, boolean modal) {

	    super(parent, modal);

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(insets().left + insets().right + 422,insets().top + insets().bottom + 237);
		label08 = new java.awt.Label("0 - 8");
		label08.reshape(insets().left + 21,insets().top + 60,84,30);
		add(label08);
		label090 = new java.awt.Label("0 - 90");
		label090.reshape(insets().left + 21,insets().top + 98,84,30);
		add(label090);
		label0Endtime = new java.awt.Label("0 - End Time");
		label0Endtime.reshape(insets().left + 21,insets().top + 143,84,30);
		add(label0Endtime);
		labelRange = new java.awt.Label("RANGE");
		labelRange.reshape(insets().left + 21,insets().top + 15,84,30);
		labelRange.setFont(new Font("Dialog", Font.BOLD, 12));
		add(labelRange);
		labelStartTime = new java.awt.Label("Start Time");
		labelStartTime.reshape(insets().left + 112,insets().top + 143,63,18);
		add(labelStartTime);
		labelIntensity = new java.awt.Label("Intensity");
		labelIntensity.reshape(insets().left + 112,insets().top + 98,63,18);
		add(labelIntensity);
		labelx = new java.awt.Label("X");
		labelx.reshape(insets().left + 112,insets().top + 60,63,18);
		add(labelx);
		varStartTime = new java.awt.TextField();
		varStartTime.setText("0");
		varStartTime.reshape(insets().left + 175,insets().top + 143,63,18);
		add(varStartTime);
		varIntensity = new java.awt.TextField();
		varIntensity.setText("0");
		varIntensity.reshape(insets().left + 175,insets().top + 98,63,18);
		add(varIntensity);
		varX = new java.awt.TextField();
		varX.setText("0");
		varX.reshape(insets().left + 175,insets().top + 60,63,18);
		add(varX);
		labelStopTime = new java.awt.Label("Stop Time");
		labelStopTime.reshape(insets().left + 252,insets().top + 143,63,18);
		add(labelStopTime);
		varStopTime = new java.awt.TextField();
		varStopTime.setText("0");
		varStopTime.reshape(insets().left + 315,insets().top + 143,63,18);
		add(varStopTime);
		labely = new java.awt.Label("Y");
		labely.reshape(insets().left + 252,insets().top + 60,63,18);
		add(labely);
		varY = new java.awt.TextField();
		varY.setText("0");
		varY.reshape(insets().left + 315,insets().top + 60,63,18);
		add(varY);
		labelValues = new java.awt.Label("VALUES");
		labelValues.reshape(insets().left + 203,insets().top + 15,84,30);
		labelValues.setFont(new Font("Dialog", Font.BOLD, 12));
		add(labelValues);
		buttonConfirm = new java.awt.Button("Confirm");
		buttonConfirm.reshape(insets().left + 91,insets().top + 195,84,30);
		add(buttonConfirm);
		buttonCancel = new java.awt.Button("Cancel");
		buttonCancel.reshape(insets().left + 231,insets().top + 195,84,30);
		add(buttonCancel);
		setTitle("Event Input Dialog");
		setResizable(false);
		//}}
	}

	public EventForm(Frame parent, String title, boolean modal) {
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
		if (event.target == buttonCancel && event.id == Event.ACTION_EVENT) {
			buttonCancel_Clicked(event);
		}
		if (event.target == buttonConfirm && event.id == Event.ACTION_EVENT) {
			buttonConfirm_Clicked(event);
		}
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	java.awt.Label label08;
	java.awt.Label label090;
	java.awt.Label label0Endtime;
	java.awt.Label labelRange;
	java.awt.Label labelStartTime;
	java.awt.Label labelIntensity;
	java.awt.Label labelx;
	java.awt.TextField varStartTime;
	java.awt.TextField varIntensity;
	java.awt.TextField varX;
	java.awt.Label labelStopTime;
	java.awt.TextField varStopTime;
	java.awt.Label labely;
	java.awt.TextField varY;
	java.awt.Label labelValues;
	java.awt.Button buttonConfirm;
	java.awt.Button buttonCancel;
	//}}
}
verbatim_off;
E 1

h59087
s 00028/00000/00000
d D 1.1 99/09/24 18:57:13 aalx 1 0
c date and time created 99/09/24 18:57:13 by aalx
e
u
U
f e 0
t
T
I 1
/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)CrowleyHelp.mod	1.6 --- 08/05/99 -- 13:56:12 */

verbatim_NSLJ;
import java.awt.*;
import java.awt.event.*;

public class CrowleyHelp extends Dialog implements ActionListener {
  
  CrowleyHelp(Frame parent, String message){
    super(parent, "NSLJ Help", true);
    add("Center", new TextArea(message,10,50,
			       TextArea.SCROLLBARS_VERTICAL_ONLY));
    Button b = new Button("OK");
    b.addActionListener(this);
    add("South", b);
    pack();
  }

  public void actionPerformed(ActionEvent e){
    String s = e.getActionCommand();

    if("OK".equals(s)){
      setVisible(false);
    }
  }
}
verbatim_off;
E 1

/**
 * Polytechnic West Project example
 */
 
package core;

import javax.swing.JLabel;
import java.awt.*;

public class StatusBar extends JLabel {
	/** 
	 * Constructor 
	 */
    public StatusBar() {
        super();
        super.setPreferredSize(new Dimension(200, 16));
        setMessage("Ready");
    }
    
    /**
     * Set message
     * @param message new message
     */
    public void setMessage(String message) {
        setText(" " + message);        
    }  
}

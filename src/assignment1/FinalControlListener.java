package assignment1;

import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

import prefuse.controls.Control;
import prefuse.controls.ControlAdapter;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

class FinalControlListener extends ControlAdapter implements Control {
	public void itemClicked(VisualItem item, MouseEvent e)
    {
        if(item instanceof NodeItem)
        {
        	
        	//IMPLEMENTING CODE FOR DISPLAYING THE LABEL AND VALUE ON CLICKING ON THE NODE
        	
            String label = ((String) item.get("label"));
            String value = (String) item.get("value");
            JPopupMenu jpub = new JPopupMenu();
            jpub.add("Label: " + label);
            jpub.add("Value: " + value);
            jpub.show(e.getComponent(),(int) e.getX(), (int) e.getY());
        }
    }

}
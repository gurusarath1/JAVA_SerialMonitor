/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author Guru Sarath
 */
class AboutMeDialogBox extends JDialog implements GLOBAL_CONSTANTS{
    
    public AboutMeDialogBox()
    {
        JLabel TextToPrint1 = new JLabel("T.Guru Sarath");
        JLabel TextToPrint2 = new JLabel("Version - "+version);
        JLabel TextToPrint3 = new JLabel("Desigend on -"+LastModified);
        
        setLayout(new FlowLayout());
        
        add(TextToPrint1);
        add(TextToPrint2);
        add(TextToPrint3);
        
        setTitle("About");
        setResizable(false);
        setSize(170,100);
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author Guru Sarath
 */
public class Menu_ {
    
    JMenuBar thisMenuBar = new JMenuBar();
    AboutMeDialogBox aboutMe = new AboutMeDialogBox();
    
    public Menu_(JFrame targetFrame)
    {
        
        
        JMenu FileMenu = new JMenu("FILE");

        Action exitAction = new AbstractAction("Exit")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {
                System.exit(0);
            }
                    
        };
        
        
        Action aboutAction = new AbstractAction("----- ABOUT -----")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {
                
                aboutMe.setVisible(true);
            }
                    
        };
        
        
        FileMenu.add(exitAction);
        FileMenu.add(aboutAction);
        thisMenuBar.add(FileMenu);
        

        //targetFrame.add(thisMenuBar); Doesnt work !!
        targetFrame.setJMenuBar(thisMenuBar);
        thisMenuBar.setVisible(true);
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import java.awt.Toolkit;

/**
 *
 * @author Guru Sarath
 */
interface GLOBAL_CONSTANTS {
    
    final float version_float = 3.0f;
    final String version = "V3.0.1";
    final String Author = "Guru Sarath";
    final String LastModified = "25 Nov 2017";
    final String MainWindowTitle = "Serial Monitor - " + version + "- COMPORTS - ";
    
    final Toolkit TLK = Toolkit.getDefaultToolkit();
    final int ScreenWidth = TLK.getScreenSize().width;
    final int ScreenHeight = TLK.getScreenSize().height;
}

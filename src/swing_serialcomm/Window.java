/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Guru Sarath
 */
public class Window implements GLOBAL_CONSTANTS{
    
        JFrame MainWindow = new JFrame();
        Toolkit TLK = Toolkit.getDefaultToolkit();
        
        public static SerialPort chosenPort;
        public static boolean runThread = false; 
        public static String newSeperator = "";
        
        
        public Window()
        {
            
            SerialPort[] allPorts = SerialPort.getCommPorts();
            
            Menu_ menus = new Menu_(MainWindow);
            
            MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            MainWindow.setSize(TLK.getScreenSize().width/2, TLK.getScreenSize().height/2);
            MainWindow.setTitle("Serial Monitor " + version);
            MainWindow.setLayout(new BorderLayout());
            
            JTextArea Serial_output = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(Serial_output);
            JPanel TopPanel = new JPanel();
            JPanel BottomPanel = new JPanel();
            
            JPanel MainControl = new JPanel();
            Border MainControl_Border = BorderFactory.createTitledBorder("Main Controls");
            MainControl.setBorder(MainControl_Border);
            
            JComboBox<String> portList = new JComboBox<>();
            JButton Connect = new JButton("Connect");
            JCheckBox no_newLine = new JCheckBox("Ignore New Line");

            ButtonGroup RadioGroup = new ButtonGroup();
            JRadioButton hyphen_radio = new JRadioButton("Hyphen",false);
            JRadioButton vertical_radio = new JRadioButton("Vertical Line",false);
            JRadioButton space_radio = new JRadioButton("Space",false);
            JRadioButton noChar_radio = new JRadioButton("No Char",true);
            
            JPanel RadioButtonPanel = new JPanel();
            Border RadioBorder = BorderFactory.createTitledBorder("Replacement for new line");
            RadioButtonPanel.setBorder(RadioBorder);
            
            RadioGroup.add(hyphen_radio);
            RadioGroup.add(vertical_radio);
            RadioGroup.add(space_radio);
            RadioGroup.add(noChar_radio);
            
            hyphen_radio.addActionListener((ActionEvent ae) -> {newSeperator = "-";});
            vertical_radio.addActionListener((ActionEvent ae) -> {newSeperator = "|";});
            space_radio.addActionListener((ActionEvent ae) -> {newSeperator = "  ";});
            noChar_radio.addActionListener((ActionEvent ae) -> {newSeperator = "";});
                
            noChar_radio.setEnabled(false);
            space_radio.setEnabled(false);
            vertical_radio.setEnabled(false);
            hyphen_radio.setEnabled(false);
            
            RadioButtonPanel.add(noChar_radio);
            RadioButtonPanel.add(space_radio);
            RadioButtonPanel.add(vertical_radio);
            RadioButtonPanel.add(hyphen_radio);
            
            JLabel lbl_bottom = new JLabel("DATA : ");
            JButton SendData = new JButton("Send");
            JTextField TextToSend = new JTextField("",50);
            SendData.setEnabled(false);
            TextToSend.setEnabled(false);
            
            SendData.addActionListener((ActionEvent ae) -> {
                
                int size = TextToSend.getText().getBytes().length;
                byte[] toSend = new byte[size];
                toSend = TextToSend.getText().getBytes();
                chosenPort.writeBytes(toSend, size);
            
            });
            
            
            
            no_newLine.addActionListener((ActionEvent ae) -> {
            
                if(no_newLine.isSelected())
                {
                    noChar_radio.setEnabled(true);
                    space_radio.setEnabled(true);
                    vertical_radio.setEnabled(true);
                    hyphen_radio.setEnabled(true);
                } else {

                    noChar_radio.setEnabled(false);
                    space_radio.setEnabled(false);
                    vertical_radio.setEnabled(false);
                    hyphen_radio.setEnabled(false);
                    
                    
                }
                
            
            });
            
            
            
            
            for(SerialPort port : allPorts)
            {
                portList.addItem(port.getSystemPortName());
            }
            
            ActionListener button_action = (ActionEvent ae) -> {
                
                if(portList.getSelectedItem() == null)
                {
                    Serial_output.append("\n ----------- NO PORT SELECTED -----------\n(Connect a serial device and try again)\n");
                }
                else if(Connect.getText().equals("Connect"))
                {
                    
                    chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 10, 10);
                    
                    if(chosenPort.openPort())
                    {
                        Connect.setText("Disconnect");
                        portList.setEnabled(false);
                        SendData.setEnabled(true);
                        TextToSend.setEnabled(true);
                    }
                    
                    Runnable ComScanner = () -> {
                        Scanner read_from_comm = new Scanner(chosenPort.getInputStream());
                        
                        runThread = true;
                        
                        while(read_from_comm.hasNextLine() && runThread)
                        {
                            
                            StringBuffer textFromComm = new StringBuffer();
                            
                            if(no_newLine.isSelected())
                            {
                                textFromComm.append(read_from_comm.nextLine());
                                textFromComm.append(newSeperator);
                            }
                            else
                                textFromComm.append(read_from_comm.nextLine() + "\n");
                            
                            Serial_output.append(textFromComm.toString());
                            
                            Serial_output.setCaretPosition(Serial_output.getText().length());
                            
                            
                            
                        }
                        
                        read_from_comm.close();
                    
                    };
                    
                    Thread scanCom = new Thread(ComScanner);
                    
                    scanCom.start();
                   
                } else if(Connect.getText().equals("Disconnect"))
                {
                    runThread = false;
                  
                    chosenPort.closePort();
                    Connect.setText("Connect");
                    portList.setEnabled(true);
                    SendData.setEnabled(false);
                    TextToSend.setEnabled(false);
                    
                }
            
            };
            
            class Key_Pressed implements KeyListener
            {

                public Key_Pressed() {}
    
                public void keyTyped(KeyEvent e){;}


                public void keyPressed(KeyEvent e){;}
                    

                public void keyReleased(KeyEvent e)
                {
    
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {                
                        int size = TextToSend.getText().getBytes().length;
                        byte[] toSend = new byte[size];
                        toSend = TextToSend.getText().getBytes();
                        chosenPort.writeBytes(toSend, size);
                    }
    
                }
    
            }
            
            
            TextToSend.addKeyListener(new Key_Pressed());
            Connect.addActionListener(button_action);
            Serial_output.setLineWrap(true);
            
            MainControl.add(portList);
            MainControl.add(Connect);            
            MainControl.add(no_newLine);
            
            TopPanel.add(MainControl);
            TopPanel.add(RadioButtonPanel);
            
            BottomPanel.add(lbl_bottom);
            BottomPanel.add(TextToSend);
            BottomPanel.add(SendData);
            
            
            MainWindow.add(TopPanel, BorderLayout.NORTH);
            MainWindow.add(scrollPane, BorderLayout.CENTER);
            MainWindow.add(BottomPanel, BorderLayout.SOUTH);
            
            MainWindow.setVisible(true);
            
        }
}

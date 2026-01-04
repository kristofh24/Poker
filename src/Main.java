/*
 *
 * Project Title: Poker
 * Author(s): Kristof Helli
 * Date: 2025-05-22
 *
 */

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

import gui.*;
import gui.Frame; //prevent ambiguity
import enums.*;
import utils.Utility;

public class Main
{
    private static JFrame mainFrame;

    public static void main(String[] args)
    {
        //manage main menu and stuff here
        
        SwingUtilities.invokeLater(()->{
            final int cWIDTH=Utility.canvasWidth;
            final int cHEIGHT=Utility.canvasHeight;
            
            ArrayList<Player> plrs=new ArrayList<Player>();

            mainFrame=new JFrame();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //mainFrame.setSize(960,640);//960,640);
            mainFrame.pack();
            mainFrame.setLayout(null);
            mainFrame.setVisible(true);

            mainFrame.setSize(cWIDTH+mainFrame.getInsets().left+mainFrame.getInsets().right,
                              cHEIGHT+mainFrame.getInsets().top+mainFrame.getInsets().bottom);

            Frame bgFrame=new Frame();
            bgFrame.setBounds(-10,-10,mainFrame.getWidth()+20,mainFrame.getHeight()+20);
            bgFrame.setColor(new Color(10,95,45));
            
            Frame design=new Frame();
            design.setBounds(75,75,cWIDTH-150,cHEIGHT-150);
            design.setColor(new Color(10,95,45));
            design.setBorderColor(new Color(255,255,255));
            design.setBorderWidth(5);
            design.setCornerWidth(50);

            mainFrame.add(bgFrame,-1);
            mainFrame.add(design,0);

            Container contentFrame=new Container();
            contentFrame.setBounds(mainFrame.getBounds());
            
            mainFrame.add(contentFrame,0);

            TextLabel TitleP=new TextLabel("P",cWIDTH/2-255,165,80);
            TitleP.setRotation(-15);
            TitleP.setHAlign("center");
            TitleP.setVAlign("center");
            TextLabel TitleO=new TextLabel("O",cWIDTH/2-135,140,80);
            TitleO.setRotation(-7);
            TitleO.setHAlign("center");
            TitleO.setVAlign("center");
            TextLabel TitleK=new TextLabel("K",cWIDTH/2,130,80);
            TitleK.setHAlign("center");
            TitleK.setVAlign("center");
            TextLabel TitleE=new TextLabel("E",cWIDTH/2+120,140,80);
            TitleE.setRotation(7);
            TitleE.setHAlign("center");
            TitleE.setVAlign("center");
            TextLabel TitleR=new TextLabel("R",cWIDTH/2+245,165,80);
            TitleR.setRotation(15);
            TitleR.setHAlign("center");
            TitleR.setVAlign("center");

            contentFrame.add(TitleP);
            contentFrame.add(TitleO);
            contentFrame.add(TitleK);
            contentFrame.add(TitleE);
            contentFrame.add(TitleR);

            TextBox name=new TextBox(16,Restriction.NUMLETONLY);
            name.setBounds(cWIDTH/2+60,330,160,30);
            name.setCornerWidth(20);
            name.setBorderWidth(1);
            JLabel nameLabel=new JLabel("Enter player name");
            nameLabel.setBounds(name.getX()+10,name.getY()-20,name.getWidth(),20);
            nameLabel.setForeground(Color.white);

            TextBox bank=new TextBox(10,Restriction.NUMBERONLY);
            bank.setBounds(cWIDTH/2+60,390,160,30);
            bank.setCornerWidth(20);
            bank.setBorderWidth(1);
            JLabel bankLabel=new JLabel("Enter bank amount");
            bankLabel.setBounds(bank.getX()+10,bank.getY()-20,bank.getWidth(),20);
            bankLabel.setForeground(Color.white);

            TextButton addPlr=new TextButton("Add to Table");
            addPlr.setBounds(cWIDTH/2+60,420,160,30);
            addPlr.setBorderWidth(1);
            addPlr.setCornerWidth(20);

            TextButton start=new TextButton("Start");
            start.setBounds(cWIDTH/2+60,480,160,30);
            start.setBorderWidth(1);
            start.setCornerWidth(20);
            
            Table plrList=new Table(2,10);
            plrList.setBounds(cWIDTH/2-230,cHEIGHT/2-125,250,360);
            plrList.setColor(Color.white);
            
            contentFrame.add(name);
            contentFrame.add(nameLabel);
            contentFrame.add(bank);
            contentFrame.add(bankLabel);
            contentFrame.add(addPlr);
            contentFrame.add(start);
            contentFrame.add(plrList);

            addPlr.addOnClick(()->{
                if (plrs.size()==10) return;
                if (name.getText().isEmpty()||bank.getText().isEmpty()) return;
                
                //actually add to a player list
                Player p=new Player(name.getText(),Integer.parseInt(bank.getText()));
                plrs.add(p);

                plrList.addElement(name.getText());
                plrList.addElement("$"+bank.getText());

                name.setText("");
                bank.setText("");
            });

            start.addOnClick(()->{
                if (plrs.size()<2) return; //maybe increase minimum plr count?

                //contentFrame.getContentPane().removeAll();
                contentFrame.removeAll();
                contentFrame.revalidate();
                contentFrame.repaint();

                Game.newGame(contentFrame,plrs);
            });
            
        });
    }
}
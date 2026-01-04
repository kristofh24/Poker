package gui;

import java.awt.*;
import javax.swing.*;

public class Frame extends JComponent {
    private int cornerWidth;
    
    private Color color;
    private Color borderColor;

    private int borderWidth;
    
    public Frame()
    {
        cornerWidth=0;

        color=new Color(255,255,255);
        borderColor=new Color(0,0,0);

        borderWidth=0;
    }

    public int getCornerWidth(){return cornerWidth;}
    public Color getColor(){return color;}
    public Color getBorderColor(){return borderColor;}
    public int getBorderWidth(){return borderWidth;}

    public void setCornerWidth(int c)
    {
        this.cornerWidth=c;
        repaint();
    }
    public void setColor(Color color)
    {
        this.color=color;
        repaint();
    }
    public void setBorderColor(Color color)
    {
        this.borderColor=color;
        repaint();
    }
    public void setBorderWidth(int w)
    {
        this.borderWidth=w;
        repaint();
    }

    @Override
    public void paint(Graphics graph)
    {
        Graphics2D g=(Graphics2D)graph;

        if (borderWidth>0)
        {
            g.setColor(borderColor);
            g.fillRoundRect(0,0,getWidth(),getHeight(),cornerWidth,cornerWidth);
        }

        /*AlphaComposite ac=AlphaComposite.getInstance(AlphaComposite.CLEAR,1.0f);
        g.setComposite(ac);*/

        g.setColor(color);
        g.fillRoundRect(borderWidth,borderWidth,getWidth()-2*borderWidth,getHeight()-2*borderWidth,cornerWidth,cornerWidth);
    }
}
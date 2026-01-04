package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import utils.Lambda;

public class TextButton extends JComponent
{
    private boolean hover=false;
    
    private JLabel text;
    private Color color=Color.WHITE;
    private Color hColor=new Color(225,225,225); //hover color

    private int borderWidth;
    private int cornerWidth;
    
    private Lambda onClick=null;
    
    public TextButton(String t)
    {
        setLayout(null);
        
        text=new JLabel(t);
        text.setVerticalAlignment(SwingConstants.CENTER);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(text,0);

        borderWidth=0;
        cornerWidth=0;
        
        //events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (onClick!=null) onClick.call();
            }
            
            @Override
            public void mouseEntered(MouseEvent e)
            {
                hover=true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e)
            {
                hover=false;
                repaint();
            }
        });
    }
    
    public void setText(String t){text.setText(t);repaint();}
    public void setColor(Color c){color=c;repaint();}
    public void sethColor(Color c){hColor=c;repaint();} //need?
    public void addOnClick(Lambda lambda){onClick=lambda;}

    public void setBorderWidth(int w){borderWidth=w;repaint();}
    public void setCornerWidth(int w){cornerWidth=w;repaint();}
    
    @Override
    public void setBounds(int x,int y,int w,int h)
    {
        super.setBounds(x,y,w,h);
        text.setBounds(0,0,w,h);
    }

    @Override
    protected void paintComponent(Graphics graph)
    {
        Graphics2D g=(Graphics2D)graph.create();
        
        int insetTop=1-(borderWidth%2);
        int insetBot=2-(borderWidth%2);
        if (borderWidth==0)
            insetTop=insetBot=0;

        g.setColor(hover?hColor:color);
        g.fillRoundRect(insetTop,insetTop,getWidth()-insetBot,getHeight()-insetBot,cornerWidth,cornerWidth);
        if (borderWidth>0)
        {
            g.setColor(new Color(75,75,75));
            g.setStroke(new BasicStroke(borderWidth));
            g.drawRoundRect(insetTop,insetTop,getWidth()-insetBot,getHeight()-insetBot,cornerWidth,cornerWidth);
        }

        g.dispose();
    }
}
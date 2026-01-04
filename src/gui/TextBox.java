package gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import enums.Restriction;

class RestrictedDocument extends PlainDocument
{
    private int maxChar;
    private Restriction restriction;
    
    public RestrictedDocument(int max,Restriction r)
    {
        super();

        maxChar=max;
        restriction=r;
    }
    
    @Override
    public void insertString(int offset,String s,AttributeSet attr) throws BadLocationException
    {
        switch (restriction)
        {
            case NONE: break;
            case NUMBERONLY:
                s=s.replaceAll("[^0-9]","");
                break;
            case LETTERONLY:
                s=s.replaceAll("[^a-zA-Z]","");
                break;
            case NUMLETONLY:
                s=s.replaceAll("[^a-zA-Z0-9]","");
                break;
        }
         
        String text=getText(0,getLength());
        String result=s;

        int nSize=text.length()+s.length();
        if (nSize>maxChar)
        {
            int diff=nSize-maxChar;
            result=s.substring(0,s.length()-diff); //should never be less than 0...
        }

        super.insertString(offset,result,attr);
    }
}

public class TextBox extends JTextField {
    private Color color;
    private int borderWidth;
    private int cornerWidth;

    public TextBox(int max,Restriction r) 
    {
        super();

        setDocument(new RestrictedDocument(max,r));
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(false);
        setBorder(null);

        color=Color.WHITE;
        borderWidth=0;
        cornerWidth=0;
    }

    public void setColor(Color c){color=c;repaint();}
    public void setBorderWidth(int w){borderWidth=w;repaint();}
    public void setCornerWidth(int w){cornerWidth=w;repaint();}

    @Override
    protected void paintComponent(Graphics graph)
    {
        Graphics2D g=(Graphics2D)graph.create();
        
        int insetTop=1-(borderWidth%2);
        int insetBot=2-(borderWidth%2);
        if (borderWidth==0)
            insetTop=insetBot=0;

        //draw over previous background
        g.setComposite(AlphaComposite.SrcOver); //this is the default but good to set anyway just in case
        g.setColor(Color.RED);//getBackground());
        g.fillRoundRect(insetTop,insetTop,getWidth()-insetBot,getHeight()-insetBot,cornerWidth,cornerWidth);
        
        g.setColor(color);
        g.fillRoundRect(insetTop,insetTop,getWidth()-insetBot,getHeight()-insetBot,cornerWidth,cornerWidth);
        if (borderWidth>0)
        {
            g.setColor(new Color(75,75,75));
            g.setStroke(new BasicStroke(borderWidth));
            g.drawRoundRect(insetTop,insetTop,getWidth()-insetBot,getHeight()-insetBot,cornerWidth,cornerWidth);
        }


        g.dispose();

        super.paintComponent(graph);
    }
}
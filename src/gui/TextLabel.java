package gui;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

import utils.Utility;

public class TextLabel extends JComponent
{
    private int x;
    private int y;

    private double rotation;

    private String text;
    private String font="TimesRoman";
    private int size=8;
    private Color color=Color.WHITE;

    private String hAlign="left";
    private String vAlign="top";

    private FontMetrics fm;

    public TextLabel(String t,int x,int y)
    {
        this(t,x,y,8);
    }
    public TextLabel(String t,int x,int y,int s)
    {
        //experimental
        try
        {
            GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
            File file=new File("src/fonts/CelticMD.ttf");
            Font f=Font.createFont(Font.TRUETYPE_FONT,file);
            ge.registerFont(f);
        
            font="CelticMD";
        } catch (FontFormatException|IOException exception)
        {
            exception.printStackTrace();
        }

        text=t;

        this.x=x;
        this.y=y;

        rotation=0.0;

        setFontSize(s);
    }

    private void upd()
    {
        fm=getFontMetrics(new Font(font,Font.PLAIN,size));
        setBounds(0,0,Utility.canvasWidth,Utility.canvasHeight); //change this up a little bit
    }

    public void setRotation(double r){rotation=r;}

    public void setText(String t){text=t;upd();}
    public void setFont(String f){font=f;upd();}
    public void setFontSize(int s){size=s;upd();}
    public void setColor(Color c){color=c;}

    public void setHAlign(String h){hAlign=h;upd();}
    public void setVAlign(String v){vAlign=v;upd();}

    @Override
    protected void paintComponent(Graphics graph)
    {
        Graphics2D g=(Graphics2D)graph.create();

        //g.fillRect(0,0,getWidth(),getHeight());

        int h=0,v=0;
        switch (hAlign)
        {
            case "left":
                h=0;
                break;
            case "center":
                h=fm.stringWidth(text)/2;
                break;
            case "right":
                h=fm.stringWidth(text);
                break;
        }
        switch (vAlign)
        {
            case "top":
                v=0;
                break;
            case "center":
                v=fm.getHeight()/2;
                break;
            case "bottom":
                v=fm.getHeight();
                break;
        }

        g.rotate(Math.toRadians(rotation),x,y);

        g.setColor(color);
        g.setFont(new Font(font,Font.PLAIN,size));
        g.drawString(text,x-h,y-v+fm.getHeight()); //getAscent for regular font; getHeight for custom font??????

        g.dispose();
    }
}
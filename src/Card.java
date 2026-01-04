import java.awt.*;
import javax.swing.*;

import enums.Property;

public class Card extends JComponent
{
    public static int getValue(String s) //returns the integer value of a card
    {
        String _s=s.substring(0,s.length()-1);
        switch (_s)
        {
            case "A": return 14; //place at the top
            case "K": return 13;
            case "Q": return 12;
            case "J": return 11;
            default: return Integer.parseInt(_s);
        }
    }
    
    private final int WIDTH=110;
    private final int HEIGHT=170;
    
    private double x,y,r,sx,sy;

    private String value;
    private String suit;
    private boolean hidden=false;
    private boolean highlight=false;
    
    public Card(int x,int y,boolean hidden,String data)
    {
        super();
        
        this.x=x-WIDTH/2;
        this.y=y-HEIGHT/2;
        
        this.r=0;
        this.sx=1.0;
        this.sy=1.0;
        
        setBounds(0,0,960,640); //important

        this.hidden=hidden;
        this.value=data.substring(0,data.length()-1);
        this.suit=data.substring(data.length()-1);
    }

    private String suitToUni()
    {
        if (suit.equals("S")) return "\u2660";
        else if (suit.equals("H")) return "\u2665";
        else if (suit.equals("D")) return "\u2666";
        else return "\u2663";
    }

    public double get_x(){return this.x+WIDTH/2;}
    public double get_y(){return this.y+HEIGHT/2;}
    public double get_r(){return this.r;}
    public double get_scalex(){return this.sx;}
    public double get_scaley(){return this.sy;}

    //getter and setter for value and suit?
    public boolean get_hidden(){return this.hidden;}
    public boolean getHighlighted(){return this.highlight;}
    
    public void setRot(double r){
        this.r=r;
        repaint();
    }
    public void set_hidden(boolean h){this.hidden=h;}
    public void setHighlighted(boolean h){this.highlight=h;}

    public void flip() //basically animated setter for this.hidden
    {
        double prev_sx=this.sx;

        Interpolation i=new Interpolation(this,.5,20);
        i.set(Property.SCALEX,0);

        Game.delay(.25,()->{
            i.start(false);
        });
        Game.delay(.45,()->{
            this.hidden=!this.hidden;
            repaint();
            
            i.set(Property.SCALEX,prev_sx);
            i.start(true);
        });
    }
    
    public void scale(double sx,double sy)
    {
        if (sx<.01) sx=.01;
        if (sy<.01) sy=.01;

        this.sx=sx;
        this.sy=sy;
        repaint();
    }
    
    public void transform(double x,double y,double r)
    {
        this.x+=x;
        this.y+=y;
        this.r+=r;
        repaint();
    }
    
    @Override
    public void paint(Graphics graph)
    {
        Graphics2D g=(Graphics2D)graph;
        
        //apply scaling
        final int _W=(int)(WIDTH*this.sx);
        final int _H=(int)(HEIGHT*this.sy);
        
        final int cornersX=(int)(20*this.sx);
        final int cornersY=(int)(20*this.sy);
        
        //adjust coordinates to make it appear as though center point is used for scaling and positioning
        final int _X=(int)(this.x+WIDTH/2-_W/2);
        final int _Y=(int)(this.y+HEIGHT/2-_H/2);
        
        g.rotate(Math.toRadians(this.r),this.x+WIDTH/2,this.y+HEIGHT/2);
        
        //highlight test
        if (highlight)
        {
            g.setColor(new Color(225,252,71));
            g.fillRoundRect(_X-3,_Y-3,_W+6,_H+6,cornersX,cornersY);
        }
        
        
        g.setColor(Color.WHITE);
        g.fillRoundRect(_X,_Y,_W,_H,cornersX,cornersY);
        g.setColor(new Color(75,75,75));
        g.drawRoundRect(_X,_Y,_W,_H,cornersX,cornersY);
        
        if (hidden)
        {
            final int padX=(int)(8*this.sx);
            final int padY=(int)(8*this.sy);
            
            g.setColor(new Color(225,25,55));
            g.fillRoundRect(_X+padX,_Y+padY,_W-2*padX,_H-2*padY,cornersX,cornersY);

            return;
        }

        final double _F=Math.min(this.sx,this.sy);
        String val=" ";
        switch (getValue(value+suit))
        {
            case 10:
                val="10"; //removes space already there
                break;
            default:
                val+=value;
                break;
        }
        String suit=suitToUni();
        
        g.setColor((suit.equals("\u2665")||suit.equals("\u2666"))?new Color(255,0,0):new Color(75,75,75));
        g.setFont(new Font("TimesRoman",Font.PLAIN,(int)(17*_F)));
        g.drawString(val,_X+(int)(8*this.sx),_Y+(int)(22*this.sy));
        g.drawString(suit,_X+(int)(9*this.sx),_Y+(int)(40*this.sy));
        
        g.setFont(new Font("TimesRoman",Font.PLAIN,(int)(48*_F)));
        g.drawString(suit,_X+_W/2-(int)(18*this.sx),_Y+_H/2+(int)(16*this.sy));

        g.rotate(Math.toRadians(180),this.x+WIDTH/2,this.y+HEIGHT/2);
        g.setFont(new Font("TimesRoman",Font.PLAIN,(int)(17*_F)));
        g.drawString(val,_X+(int)(8*this.sx),_Y+(int)(22*this.sy));
        g.drawString(suit,_X+(int)(9*this.sx),_Y+(int)(40*this.sy));
    }

    @Override
    public String toString()
    {
        return value+suit;
    }
}
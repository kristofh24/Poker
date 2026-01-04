import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

import enums.Property;

public class Player
{
    private String name;
    private double bank;
    private double bet;
    private ArrayList<Card> hand;
    private boolean folded;
    
    private int[] pos;
    private int theta;
    
    private JLabel nameLabel;
    private JLabel betLabel;
    
    public Player(String n,double a)
    {
        name=n;
        bank=a;
        bet=0;
        hand=new ArrayList<Card>();
        folded=false;
        
        pos=new int[2];
        theta=0;
        
        nameLabel=new JLabel(name);
        nameLabel.setBounds(0,0,10*name.length(),50);
        nameLabel.setForeground(Color.WHITE);
        betLabel=new JLabel("$"+(int)bet);
        betLabel.setForeground(Color.WHITE);
    }
    
    public String getName(){return name;}
    public double getBank(){return bank;}
    public double getBet(){return bet;}
    public ArrayList<Card> getHand(){return hand;}
    public int[] getPos(){return pos;}
    public int getTheta(){return theta;}
    public JLabel getNameLabel(){return nameLabel;}
    public JLabel getBetLabel(){return betLabel;}
    public boolean getFolded(){return folded;}
    
    public void setName(String n){name=n;}
    public void setBank(double a){bank=a;}
    public void setBet(double a)
    {
        bet=a;
        betLabel.setText("$"+(int)bet);
    }
    public void setPos(int x,int y)
    {
        pos[0]=x;
        pos[1]=y;
        
        if (Game.plr_count<=6)
        {
            if (x==480)//&&y>320)
                nameLabel.setBounds(x-8*name.length()/2-88,y-26,nameLabel.getWidth(),nameLabel.getHeight());
            else
                nameLabel.setBounds(x-8*name.length()/2,y+65,nameLabel.getWidth(),nameLabel.getHeight());
        }
        else
        {
            if (x>480)
                nameLabel.setBounds(x-8*name.length()/2+88,y-26,nameLabel.getWidth(),nameLabel.getHeight());
            else
                nameLabel.setBounds(x-8*name.length()/2-88,y-26,nameLabel.getWidth(),nameLabel.getHeight());
        }
        
        betLabel.setBounds(nameLabel.getX(),nameLabel.getY()+25,150,50);
    }
    public void setTheta(int t){theta=t;}
    
    public void fold()
    {
        folded=true;
        nameLabel.setVisible(false);
        betLabel.setVisible(false);
        for (Card c:hand)
        {
            c.set_hidden(true);
            ((JFrame)c.getTopLevelAncestor()).getContentPane().setComponentZOrder(c,0); //gross
            
            Interpolation i=new Interpolation(c,1,20);
            i.set(Property.X,480+(Math.random()-.5)*100);
            i.set(Property.Y,320+(Math.random()-.5)*100);
            i.set(Property.ROT,c.get_r()+Math.random()*360);
            i.start(false);
            Game.delay(1.5,()->{
                Interpolation i2=new Interpolation(c,.5,20);
                i2.set(Property.SCALEX,0);
                i2.set(Property.SCALEY,0);
                i2.start(false);
            });
        }
    }
    public void call(double a)
    {
        bet+=a;
        bank-=a;
        betLabel.setText("$"+(int)bet);
    }
    public void raise(double to)
    {
        bank-=(to-bet);
        bet=to;
        betLabel.setText("$"+(int)bet);
    }
    
    public void addCard(Card c)
    {
        if (hand.size()>=2) return; //should never be more than 2 but just in case
        hand.add(c);
    }
    public void flipHand()
    {
        for (Card c:hand)
            c.flip();
    }
    public void clearHand()
    {
        for (Card c:hand)
            c.setVisible(false);
        
        hand.clear();
    }
    public Hand getBestHand(ArrayList<Card> cards)
    {
        ArrayList<String> h=new ArrayList<String>();
        for (Card c:hand)
            h.add(c.toString());
        for (Card c:cards)
            h.add(c.toString());
        
        return (new Evaluator(h)).getBestHand();
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
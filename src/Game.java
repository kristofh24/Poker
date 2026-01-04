import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import javax.swing.*;

import gui.*;
import enums.*;
import utils.*;

//might need to restruct this whole class a little bit...
public class Game
{
    private static Timer timer=new Timer();
    
    public static ArrayList<Player> table=new ArrayList<Player>();
    public static ArrayList<Player> folded=new ArrayList<Player>();
    public static ArrayList<Card> cards=new ArrayList<Card>();
    
    public static int plr_count=0;
    private static int plr_count_rnd=0;
    
    public static String gameState="preflop";
    private static Deck deck;
    
    private static double pot=0;
    private static double bet=0;
    
    private static int cPlayer=0;
    
    private static int passiveActions=0; //number of passive actions (fold, check, call) in a row
    
    //swing elements
    private static Container mainFrame;//=new JFrame();
    private static Container guiFrame=new Container();
    private static Container btnFrame=new Container();
    private static Container cardFrame=new Container();
    
    private static TextButton check=new TextButton("Check");
    private static TextButton raise=new TextButton("Raise");
    
    private static JLabel potLabel=new JLabel("Pot: $0");
    private static JLabel bankLabel=new JLabel("Bank: $0");
    
    private static TextBox raiseField=new TextBox(4,Restriction.NUMBERONLY);
    private static JLabel raiseFieldDeco=new JLabel("$"); //just for some tiny detail
    
    //f should be container (pass contentFrame, not mainFrame)
    public static void newGame(Container f,ArrayList<Player> plrs) //add more parameters here
    {
        mainFrame=f;
        
        check.setBounds(425,400,110,35);
        check.setBorderWidth(1);
        check.setCornerWidth(20);
        raise.setBounds(555,400,110,35);
        raise.setBorderWidth(1);
        raise.setCornerWidth(20);
        
        //temp table population for now
        /*for (int i=0;i<6;i++)
        {
            table.add(new Player("test"+i,100+25*i));
        }*/
        table=plrs;

        plr_count=table.size();
        plr_count_rnd=plr_count;
        
        /* START GAME */
        
        //init graphics
        /*mainFrame.setSize(960,640);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);*/
        
        guiFrame.setBounds(0,0,960,640);
        guiFrame.setLayout(null);
        
        btnFrame.setBounds(0,0,960,640);
        btnFrame.setLayout(null);
        guiFrame.add(btnFrame,0);
        
        cardFrame.setBounds(0,0,960,640);
        cardFrame.setLayout(null);
        
        mainFrame.add(guiFrame,0);
        mainFrame.add(cardFrame,0);
        
        guiFrame.setVisible(false);
        
        TextButton fold=new TextButton("Fold");
        fold.setBounds(295,400,110,35);
        fold.setBorderWidth(1);
        fold.setCornerWidth(20);
        fold.addOnClick(()->{
            passiveActions++;
            
            table.get(cPlayer).fold();
            guiFrame.setVisible(false);
            delay(2,()->{
                table.get(cPlayer).clearHand();
                endTurn();
            });
        });
        
        check.addOnClick(()->{
            passiveActions++;
            
            int diff=(int)(bet-table.get(cPlayer).getBet());
            if (diff>0)
            {
                if (diff>table.get(cPlayer).getBank()) diff=(int)table.get(cPlayer).getBank();
                table.get(cPlayer).call(diff);
                pot+=diff;
            }
            
            endTurn();
        });
        
        raise.addOnClick(()->{
            //more stuff here?
            
            btnFrame.setVisible(false);
            raiseField.setVisible(true);
            raiseField.setText("");
            raiseField.requestFocusInWindow();
            raiseFieldDeco.setVisible(true);
        });
        
        TextButton flip=new TextButton("Flip Cards");
        flip.setBounds(425,595,110,35);
        flip.setBorderWidth(1);
        flip.setCornerWidth(20);
        flip.addOnClick(()->{
            /*ArrayList<String> hand=new ArrayList<String>();
            for (Card c:cards)
                hand.add(c.toString());
            
            for (Card c:table.get(cPlayer).getHand())
                hand.add(c.toString());
            
            Evaluator e=new Evaluator(hand);
            e.getBestHand();*/

            btnFrame.setVisible(false);
            table.get(cPlayer).flipHand();
            delay(.65,()->{
                btnFrame.setVisible(true);

                if (!table.get(cPlayer).getHand().get(0).get_hidden()) //holy OOP
                {
                    Hand bestHand=table.get(cPlayer).getBestHand(cards);
                    if (bestHand==null) return;
                    ArrayList<String> importantCards=bestHand.getImportantCards();
                    if (importantCards==null) return;

                    ArrayList<Card> allCards=new ArrayList<Card>(); //misleading name but whatever
                    for (Card c:cards){allCards.add(c);}
                    for (Card c:table.get(cPlayer).getHand()){allCards.add(c);}

                    for (Card c:allCards)
                        for (String s:importantCards)
                            if (s.equals(c.toString()))
                                c.setHighlighted(true);
                }
            });
        });
        
        btnFrame.add(fold);
        btnFrame.add(check);
        btnFrame.add(raise);
        btnFrame.add(flip);
        
        potLabel.setBounds(400,205,160,35);
        potLabel.setForeground(Color.WHITE);
        potLabel.setFont(new Font("TimesRoman",Font.BOLD,16));
        potLabel.setVerticalAlignment(SwingConstants.CENTER);
        potLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        bankLabel.setBounds(740,10,200,35);
        bankLabel.setForeground(Color.WHITE);
        bankLabel.setVerticalAlignment(SwingConstants.CENTER);
        bankLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        cardFrame.add(potLabel); // because why not
        guiFrame.add(bankLabel);
        
        raiseField.setBounds(416,400,128,30);
        raiseField.setBorderWidth(1);
        raiseField.setCornerWidth(16);
        raiseField.addActionListener((ActionEvent e)->{
            int amt=Integer.parseInt(raiseField.getText());
            if (amt+bet>table.get(cPlayer).getBank())
            {
                raiseField.setText(""); //maybe give player some feedback?
                return;
            }
            
            plr_count_rnd=table.size()-1; //trust
            passiveActions=0;
            
            raiseField.setFocusable(false); //remove focus
            raiseField.setFocusable(true);
            
            //System.out.println(raiseField.getText());
            table.get(cPlayer).raise(bet+amt);
            
            bet+=amt;
            pot+=amt;
            
            raiseField.setVisible(false);
            raiseFieldDeco.setVisible(false);
            btnFrame.setVisible(true);
            
            endTurn();
        });
        raiseField.setVisible(false);
        
        raiseFieldDeco.setBounds(404,400,16,32);
        raiseFieldDeco.setForeground(Color.WHITE);
        raiseFieldDeco.setFont(new Font("TimesRoman",Font.BOLD,16));
        raiseFieldDeco.setVisible(false);
        
        guiFrame.add(raiseField);
        guiFrame.add(raiseFieldDeco);
        
        //set initial player positions
        for (int i=0;i<plr_count;i++)
        {
            //table.get(i).setTheta(90+(int)((360/plr_count)*i));
            guiFrame.add(table.get(i).getNameLabel());
            guiFrame.add(table.get(i).getBetLabel());
        }
        setPositions();
        
        //deal cards
        deck=new Deck();
        deck.shuffle();
        
        for (int j=0;j<2;j++)
        {
            for (int k=0;k<plr_count;k++)
            {
                int[] plrPos=table.get(k).getPos();
                int theta=table.get(k).getTheta();
                Card c=dealCard(plrPos[0]+(int)((-16+32*j)*Math.cos(Math.toRadians(theta))),
                                plrPos[1]+(int)((-16+32*j)*Math.sin(Math.toRadians(theta))),
                                theta-5+10*j,
                                .25*k+j*(.25*plr_count));

                c.set_hidden(true);
                table.get(k).addCard(c);
            }
        }
        
        delay(.25*2*plr_count+1,()->{
            guiFrame.setVisible(true);
        });
        
        //buy in
        for (Player p:table)
        {
            p.setBank(p.getBank()-10);
            pot+=10;
        }
        potLabel.setText("Pot: $"+(int)pot);
        bankLabel.setText("Bank: $"+(int)table.get(cPlayer).getBank());
    }
    
    private static void setPositions()
    {
        for (int i=0;i<plr_count;i++)
        {
            int theta=90+(int)((360/plr_count)*(i-cPlayer));
            table.get(i).setTheta(theta-90);
            table.get(i).setPos(480+(int)clamp(420*Math.cos(Math.toRadians(theta)),-360,360),
                                320+(int)(200*Math.sin(Math.toRadians(theta))));
        }
    }
    
    //deals an UNHIDDEN card to (x, y) with rotation r with a delay of d seconds
    private static Card dealCard(int x,int y,int r,double d)
    {
        String cData=deck.get_next();
        Card c=new Card(480,320,false,cData);
        //cards.add(c);
        cardFrame.add(c,0);
        
        c.setRot(r-360);
        c.scale(0,0);
        
        Interpolation i=new Interpolation(c,1,20);
        i.set(Property.X,x);
        i.set(Property.Y,y);
        i.set(Property.ROT,r);
        i.set(Property.SCALEX,.75);
        i.set(Property.SCALEY,.75);
        delay(d,()->{
            i.start(false);
        });
        
        return c;
    }
    
    private static void endTurn()
    {
        guiFrame.setVisible(false);

        for (Card c:cards) c.setHighlighted(false);
        for (Card c:table.get(cPlayer).getHand()) c.setHighlighted(false);
        
        double d=0;
        if (!table.get(cPlayer).getFolded()&&table.get(cPlayer).getHand().get(0).get_hidden()==false)
        {
            table.get(cPlayer).flipHand();
            d=1;
        }
        
        if (table.get(cPlayer).getFolded())
        {
            Player temp=table.remove(cPlayer);
            folded.add(temp);
            plr_count=table.size();
        }
        else cPlayer++;
        
        if (cPlayer>=plr_count) cPlayer=0;
        
        if (table.size()==1)
        {
            table.get(0).flipHand();
            delay(1.5,()->{
                ArrayList<Player> winners=new ArrayList<Player>();
                winners.add(table.get(0));
                endGame(winners);
            });
            return;
        }
        
        setPositions();
        
        delay(d,()->{
            for (Player p:table)
            {
                int[] plr_pos=p.getPos();
                ArrayList<Card> hand=p.getHand();
                for (int j=0;j<hand.size();j++)
                {
                    int theta=p.getTheta();
                    Interpolation i=new Interpolation(hand.get(j),1,20);
                    i.set(Property.X,plr_pos[0]+(-16+32*j)*Math.cos(Math.toRadians(theta)));//-16+32*j);
                    i.set(Property.Y,plr_pos[1]+(-16+32*j)*Math.sin(Math.toRadians(theta)));
                    i.set(Property.ROT,theta-5+10*j);
                    i.start(false);
                }
            }
            
            delay(1.5,()->{
                double d2=0;
                if (passiveActions>=plr_count_rnd)
                {
                    d2=1.5;
                    endRound();
                }
                
                delay(d2,()->{
                    System.out.println(gameState);
                    if (gameState.equals("showoff")) return;
                    
                    raise.setVisible(true); // in case last turn set it invisible
                    
                    if ((int)table.get(cPlayer).getBank()==0)
                    {
                        passiveActions++;
                        endTurn();
                        return;
                    }
                    
                    int diff=(int)(bet-table.get(cPlayer).getBet());
                    if (diff>0) 
                    {
                        if (diff>=table.get(cPlayer).getBank()) 
                        {
                            check.setText("All In");
                            raise.setVisible(false);
                        }
                        else check.setText("Call $"+(int)bet); // or $diff ?
                    }
                    else check.setText("Check");
                    
                    potLabel.setText("Pot: $"+(int)pot);
                    bankLabel.setText("Bank: $"+(int)table.get(cPlayer).getBank());
                    
                    guiFrame.setVisible(true);
                });
            });
        });
    }
    
    private static void endRound()
    {
        //deal community cards
        deck.get_next(); //burn 1 card
        switch (gameState)
        {
            case "preflop":
                gameState="flop";
                for (int j=-2;j<=0;j++)
                    cards.add(dealCard(480+(90*j),320,360,.25*(j+2)));
                
                break;
            case "flop":
                gameState="turn";
                cards.add(dealCard(480+90,320,360,0));
                
                break;
            case "turn":
                gameState="river";
                cards.add(dealCard(480+180,320,360,0));
                
                break;
            case "river":
                gameState="showoff";

                for (int i=0;i<table.size();i++)
                {
                    final Player p=table.get(i);
                    delay(1.5*i,()->{
                        p.flipHand();
                    });
                }
                
                delay(2*table.size(),()->{
                    System.out.println("lets see the lag");

                    //should work for ties...
                    ArrayList<Player> winners=new ArrayList<Player>(); //arraylist to account for ties
                    winners.add(table.get(0));

                    for (Player p:table)
                    {
                        if (winners.get(0).equals(p)) continue;

                        Hand pHand=p.getBestHand(cards);
                        Hand betterHand=Evaluator.compareHands(pHand,winners.get(0).getBestHand(cards));
                        if (betterHand==null) winners.add(p);
                        else if (betterHand.equals(pHand))
                        {
                            winners.clear();
                            winners.add(p);
                        }
                    }

                    System.out.println(winners);

                    endGame(winners);
                });
                
                break;
        }
        
        plr_count_rnd=plr_count;
        passiveActions=0;
        
        bet=0;
        for (Player p:table)
            p.setBet(0);
    }
    
    private static void endGame(ArrayList<Player> winners)
    {
        JComponent bg=new JComponent()
        {
            @Override
            public void paint(Graphics graph)
            {
                Graphics2D g=(Graphics2D)graph;

                g.setColor(new Color(0,0,0,.75f)); //higher = less transparent
                g.fillRect(-10,-10,getWidth()+20,getHeight()+20);
            }
        };
        bg.setBounds(0,0,mainFrame.getSize().width,mainFrame.getSize().height);
        cardFrame.setBounds(0,0,mainFrame.getSize().width,mainFrame.getSize().height);
        cardFrame.add(bg,0);
        bg.repaint(); //why must i do it with this one but not all the other components?

        for (int j=0;j<winners.size();j++)
        {
            ArrayList<Card> hand=winners.get(j).getHand();
            int theta=winners.get(j).getTheta()>180?360:0;
            for (int k=0;k<hand.size();k++)
            {
                Card c=hand.get(k);
                
                cardFrame.setComponentZOrder(c,0);

                int d=200;
                int s=480-(d/2)*(winners.size()-1);
                Interpolation i=new Interpolation(c,1.5,20);
                i.set(Property.X,s+(d*j)-16+32*k);
                i.set(Property.Y,240); //mess around with this
                i.set(Property.ROT,theta-5+10*k);
                i.start(false);
            }
        }

        delay(1.5,()->{
            System.out.println("show names and text and whatnot");
        });
    }
    
    //utility functions
    public static double clamp(double v,double min,double max)
    {
        return Math.max(min,Math.min(max,v));
    }

    public static void delay(double delay,Lambda func)
    {
        timer.schedule(new TimerTask() {
            @Override
            public void run(){func.call();}
        },(int)(delay*1000));
    }
}
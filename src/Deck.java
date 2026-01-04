import java.util.ArrayList;
import java.util.Random;

public class Deck
{
    private ArrayList<String> cards=new ArrayList<String>();
    
    public Deck()
    {
        for (int i=1;i<=13;i++)
        {
            String s="";
            switch (i)
            {
                case 11:
                    s="J";
                    break;
                case 12:
                    s="Q";
                    break;
                case 13:
                    s="K";
                    break;
                case 1:
                    s="A";
                    break;
                default:
                    s=Integer.toString(i);
            }

            this.cards.add(s+"S"); //spade
            this.cards.add(s+"H"); //heart
            this.cards.add(s+"D"); //diamond
            this.cards.add(s+"C"); //club
        }
    }
    
    public void shuffle()
    {
        Random r=new Random();
        
        for (int i=this.cards.size()-1;i>0;i--)
        {
            int _i=r.nextInt(i+1);
            String temp=this.cards.get(_i);
            this.cards.set(_i,this.cards.get(i));
            this.cards.set(i,temp);
        }
    }
    
    public String get_next()
    {
        String temp=this.cards.get(0);
        this.cards.remove(0);
        return temp;
    }
}
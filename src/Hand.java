import java.util.ArrayList;

import enums.Rank;
import utils.Utility;

public class Hand //a 5-card hand
{
    private static int arrFind(int[] arr,int k,boolean lr)
    {
        if (lr)
        {
            for (int i=0;i<arr.length;i++)
                if (arr[i]==k)
                    return i;
        }
        else
        {
            for (int i=arr.length-1;i>=0;i--)
                if (arr[i]==k)
                    return i;
        }

        return -1;
    }
    
    private String[] cards;
    private int[] histogram;
    private Rank ranking;

    public Hand(String[] cards)
    {
        this.cards=cards;
        sort();
        rank();
    }

    public String[] getCards(){return cards;}
    public int[] getHistogram(){return histogram;}
    public Rank getRank(){return ranking;}
    
    private void rank() // --> terrible, please refactor
    {
        Rank rank=Rank.HIGHCARD;
        
        //histogram for pairs, sets, quads
        histogram=new int[13];
        for (String s:cards)
            histogram[Card.getValue(s)-2]++;
        
        ArrayList<Integer> found=new ArrayList<Integer>();
        for (int i=0;i<13;i++)
            if (histogram[i]!=0)
                found.add(histogram[i]);

        if (found.size()==2)
        {
            if (found.indexOf(4)!=-1) rank=Rank.QUAD;
            else if (found.indexOf(3)!=-1) rank=Rank.BOAT;
        }
        else if (found.size()==3)
        {
            if (found.indexOf(3)!=-1) rank=Rank.SET;
            else if (found.indexOf(2)!=-1) rank=Rank.TWOPAIR;
        }
        else if (found.size()==4) rank=Rank.ONEPAIR;

        //check for flush
        boolean flush=true;
        for (int i=1;i<cards.length;i++)
        {
            if (!cards[i].substring(cards[i].length()-1).equals(cards[i-1].substring(cards[i-1].length()-1)))
            {
                flush=false;
                break;
            }
        }

        //check for straight
        boolean straight=false;
        if (rank==Rank.HIGHCARD)
        {
            if (Card.getValue(cards[4])-Card.getValue(cards[0])==4)
                straight=true;
            
            if (Card.getValue(cards[4])==14&&Card.getValue(cards[3])==5)
                straight=true; //wheel
        }

        //final rank adjustments
        if (straight&&flush) rank=Rank.STRAIGHTFLUSH;
        else if (straight) rank=Rank.STRAIGHT;
        else if (flush) rank=Rank.FLUSH;

        if (rank==Rank.STRAIGHTFLUSH&&Card.getValue(cards[4])==4&&Card.getValue(cards[3])==13) rank=Rank.ROYALFLUSH;

        ranking=rank;
    }

    private void sort() //really only called once in the constructor
    {
        ArrayList<String> list=new ArrayList<String>();
        for (String s:cards)
            list.add(s);

        for (int j=1;j<list.size();j++)
        {
            for (int k=j-1;k>=-1;k--)
            {
                if (k==-1||Card.getValue(list.get(k))<Card.getValue(list.get(j)))
                {
                    list.add(k+1,list.remove(j));
                    break;
                }
            }
        }

        for (int i=0;i<list.size();i++)
            cards[i]=list.get(i);
    }

    public ArrayList<String> getImportantCards()
    {
        ArrayList<String> r=new ArrayList<String>();
        switch (ranking)
        {
            case ROYALFLUSH:
            case STRAIGHTFLUSH:
            case BOAT:
            case FLUSH:
            case STRAIGHT:
                return Utility.listFromArr(cards);
            case QUAD:
            case SET:
            case ONEPAIR:
                int value=arrFind(histogram,ranking==Rank.QUAD?4:ranking==Rank.SET?3:2,true)+2;
                for (String c:cards)
                    if (Card.getValue(c)==value)
                        r.add(c);
                
                return r;
            case TWOPAIR:
                int v1=arrFind(histogram,2,true)+2;
                int v2=arrFind(histogram,2,false)+2;
                for (String c:cards)
                    if (Card.getValue(c)==v1||Card.getValue(c)==v2)
                        r.add(c);
                
                return r;
            case HIGHCARD:
                r.add(cards[4]);
                return r;
            default: return null;
        }
    }

    @Override
    public String toString()
    {
        String s="";
        for (String _s:cards)
            s+=_s+" ";
        
        return s+"   "+ranking;
    }
}
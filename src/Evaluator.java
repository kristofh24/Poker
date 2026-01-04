import java.util.ArrayList;

import enums.Rank;

//Awesome hand evaluation algorithm used here and in Hand.java:
//https://nsayer.blogspot.com/2007/07/algorithm-for-evaluating-poker-hands.html?m=1

public class Evaluator
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

    public static Hand compareHands(Hand hand1,Hand hand2) //return winning hand; returns null if tied
    {
        System.out.println(hand1+" || "+hand2);

        if (hand1.getRank()!=hand2.getRank()) return hand1.getRank().ordinal()<hand2.getRank().ordinal()?hand1:hand2;
        
        String[] c1=hand1.getCards();
        String[] c2=hand2.getCards();

        int[] h1=hand1.getHistogram();
        int[] h2=hand2.getHistogram();

        switch (hand1.getRank())
        {
            case ROYALFLUSH: return null;
            case STRAIGHTFLUSH:
            case STRAIGHT: return Card.getValue(c1[4])>Card.getValue(c2[4])?hand1:hand2;
            case QUAD:
                if (arrFind(h1,4,true)!=arrFind(h2,4,true)) return arrFind(h1,4,true)>arrFind(h2,4,true)?hand1:hand2;
                if (arrFind(h1,1,true)!=arrFind(h2,1,true)) return arrFind(h1,1,true)>arrFind(h2,1,true)?hand1:hand2;
                return null;
            case BOAT:
                if (arrFind(h1,3,true)!=arrFind(h2,3,true)) return arrFind(h1,3,true)>arrFind(h2,3,true)?hand1:hand2;
                if (arrFind(h1,2,true)!=arrFind(h2,2,true)) return arrFind(h1,2,true)>arrFind(h2,2,true)?hand1:hand2;
                return null;
            case FLUSH:
            case HIGHCARD:
                for (int i=4;i>=0;i--)
                    if (Card.getValue(c1[i])!=Card.getValue(c2[i])) return Card.getValue(c1[i])>Card.getValue(c2[i])?hand1:hand2;
                return null;
            case SET:
                if (arrFind(h1,3,true)!=arrFind(h2,3,true)) return arrFind(h1,3,true)>arrFind(h2,3,true)?hand1:hand2;
                for (int i=4;i>=0;i--)
                    if (Card.getValue(c1[i])!=Card.getValue(c2[i])) return Card.getValue(c1[i])>Card.getValue(c2[i])?hand1:hand2;
                return null;
            case TWOPAIR:
                if (arrFind(h1,2,false)!=arrFind(h2,2,false)) return arrFind(h1,2,false)>arrFind(h2,2,false)?hand1:hand2;
                if (arrFind(h1,2,true)!=arrFind(h2,2,true)) return arrFind(h1,2,true)>arrFind(h2,2,true)?hand1:hand2;
                if (arrFind(h1,1,true)!=arrFind(h2,1,true)) return arrFind(h1,1,true)>arrFind(h2,1,true)?hand1:hand2;
                return null;
            case ONEPAIR:
                if (arrFind(h1,2,true)!=arrFind(h2,2,true)) return arrFind(h1,2,true)>arrFind(h2,2,true)?hand1:hand2;
                for (int i=4;i>=0;i--)
                    if (Card.getValue(c1[i])!=Card.getValue(c2[i])) return Card.getValue(c1[i])>Card.getValue(c2[i])?hand1:hand2;
                return null;
            default: return null; //to make the compiler happy
        }
    }

    private ArrayList<String> cards;
    private ArrayList<Hand> combinations;

    public Evaluator(ArrayList<String> cards)
    {
        this.cards=cards;
    }

    private void findCombinations(int j,ArrayList<String> path)
    {
        if (path.size()==5)
        {
            String[] temp=new String[5];
            for (int i=0;i<path.size();i++)
                temp[i]=path.get(i);
            
            combinations.add(new Hand(temp));
            return;
        }

        for (int i=j;i<cards.size();i++)
        {
            path.add(cards.get(i));
            findCombinations(i+1,path);
            path.remove(path.size()-1);
        }
    }

    public Hand getBestHand()
    {
        //find all 21 combinations
        combinations=new ArrayList<Hand>();
        findCombinations(0,new ArrayList<String>());

        //get list of best hands (based only on type, not card values yet)
        int highestRank=10;
        ArrayList<Hand> bestHands=new ArrayList<Hand>();

        for (Hand c:combinations)
        {
            Rank rank=c.getRank();

            //log best hands
            if (rank.ordinal()<=highestRank)
            {
                if (rank.ordinal()<highestRank)
                    bestHands.clear();

                bestHands.add(c);
                highestRank=rank.ordinal();
            }
        }

        System.out.println("BEST HANDS: ");
        for (Hand a:bestHands)
        {
            for (String s:a.getCards())
                System.out.print(s+" ");
            System.out.print("   "+highestRank);
            System.out.println();
        }
        System.out.println(bestHands.size());

        //compare the hands and choose the best one
        if (bestHands.size()>0)
        {
            Hand bestHand=bestHands.get(0);
            for (int i=1;i<bestHands.size();i++)
                bestHand=compareHands(bestHand,bestHands.get(i));
            
            return bestHand;
        }

        return null;
    }
}
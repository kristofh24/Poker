package gui;

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class Table extends JComponent
{
    private int rows;
    private int columns;

    private Color color=Color.BLACK;
    
    private ArrayList<String> data;

    public Table(int r,int c)
    {
        rows=r;
        columns=c;

        data=new ArrayList<String>();
    }

    public ArrayList<String> getData(){return data;}

    public void setColor(Color c){color=c;}

    public void addElement(String e){addElement(e,data.size());}
    public void addElement(String e,int i)
    {
        data.add(i,e);
        repaint();
    }
    public void removeElement(int i)
    {
        data.remove(i);
        repaint();
    }

    @Override
    public void paint(Graphics graph)
    {
        Graphics2D g=(Graphics2D)graph.create();

        /*g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,getWidth(),getHeight());*/

        g.setColor(color);
        for (int i=1;i<rows;i++)
        {
            int x=i*(getWidth()/rows);//+i; //+i?
            g.drawLine(x,0,x,getHeight());
        }

        for (int i=1;i<columns;i++)
        {
            int y=i*(getHeight()/columns);//+i; //+i?
            g.drawLine(0,y,getWidth(),y);
        }

        for (int i=0;i<data.size();i++)
        {
            FontMetrics fm=getFontMetrics(getFont());
            int x=(int)((i%rows+.5)*(getWidth()/rows)-fm.stringWidth(data.get(i))/2);
            int y=(int)((i/rows+.5)*(getHeight()/columns)+fm.getHeight()/2)-2; //not the nicest

            g.drawString(data.get(i),x,y);
        }

        g.dispose();
    }
}

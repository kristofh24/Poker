import java.util.Timer;
import java.util.TimerTask;

import enums.Property;

public class Interpolation
{
    private Timer t;
    private TimerTask task;

    private Card bound; //card objects only for now
    private double[] properties;

    private int step;
    private int steps;
    private double lerp_amt;

    private int count=0;
    
    public Interpolation(Card b,double len,int step)
    {
        t=new Timer();

        bound=b;
        properties=new double[]{b.get_x(),b.get_y(),b.get_r(),b.get_scalex(),b.get_scaley()};

        this.step=step;
        steps=(int)(len*1000)/step;
        lerp_amt=5.0/steps;
    }
    public Interpolation(Card b,Interpolation i)
    {
        t=new Timer();

        bound=b;
        properties=new double[]{i.properties[0],i.properties[1],i.properties[2],i.properties[3],i.properties[4]};

        step=i.step;
        steps=i.steps;
        lerp_amt=i.lerp_amt;
    }

    public void set(Property p,double v)
    {
        switch (p)
        {
            case X:
                properties[0]=v;
                break;
            case Y:
                properties[1]=v;
                break;
            case ROT:
                properties[2]=v;
                break;
            case SCALEX:
                properties[3]=v;
                break;
            case SCALEY:
                properties[4]=v;
                break;
        }
    }

    public void start(boolean override)
    {
        if (task!=null&&override) task.cancel();

        task=new TimerTask() {
            @Override
            public void run()
            {
                count++;
                if (count>=steps+20) t.cancel(); //just add 20 extra steps for now, improve solution later

                double x=(properties[0]-bound.get_x())*lerp_amt;
                double y=(properties[1]-bound.get_y())*lerp_amt;
                double r=(properties[2]-bound.get_r())*lerp_amt;
                double sx=(properties[3]-bound.get_scalex())*lerp_amt;
                double sy=(properties[4]-bound.get_scaley())*lerp_amt;

                bound.scale(bound.get_scalex()+sx,bound.get_scaley()+sy);
                bound.transform(x,y,r);
            }
        };
        
        t.scheduleAtFixedRate(task,0,step);
    }
}
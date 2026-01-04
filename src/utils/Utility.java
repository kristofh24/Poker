package utils;

import java.util.ArrayList;

public class Utility
{
     //dimensions of canvas --> gets scaled to device dimensions
    public static final int canvasWidth=960; //canvas width
    public static final int canvasHeight=640; //canvas height

    public static <T> int arrFind(T[] arr,T obj) //ever even needed?
    {
        for (int i=0;i<arr.length;i++)
            if (arr[i].equals(obj))
                return i;

        return -1;
    }

    public static <T> ArrayList<T> listFromArr(T[] arr)
    {
        ArrayList<T> r=new ArrayList<T>();
        
        for (T v:arr)
            r.add(v);

        return r;
    }
}
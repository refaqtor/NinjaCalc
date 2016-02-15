package Utility;

/**
 * Calculator for finding a E-series resistance (standard resistance) which is closest to the user's
 * desired resistance.
 *
 * @author gbmhunter
 * @since 2016-02-15
 * @last-modified 2016-02-15
 */
public class StandardResistanceFinder {


    public enum eSeriesOptions {
        E12,
        E24,
        E48,
        E96,
        E192,
    }

    // E12 resistance array
    static Double[] e12 = new Double[]{1.0, 1.2, 1.5, 1.8, 2.2, 2.7, 3.3, 3.9, 4.7, 5.6, 6.8, 8.2, 10.0};

    // E24 resistance array
    static Double[] e24 = new Double[]{1.0, 1.1, 1.2, 1.3, 1.5, 1.6, 1.8, 2.0, 2.2, 2.4, 2.7, 3.0, 3.3, 3.6, 3.9, 4.3, 4.7, 5.1, 5.6, 6.2, 6.8, 7.5, 8.2, 9.1, 10.0};

    public static double Find(double desiredResistance, eSeriesOptions eSeries) {

        Double[] selectedRange = new Double[]{};

        // Find out what resistance series was selected
        if(eSeries == eSeriesOptions.E12)
        {
            //Log('E12 range selected.');
            selectedRange = e12;
        }
        else if(eSeries == eSeriesOptions.E24)
        {
            //Log('E24 range selected.');
            selectedRange = e24;
        }
        else if(eSeries == eSeriesOptions.E48)
        {
            //Log('E48 range selected.');
            selectedRange = BuildResArray(48);
        }
        else if(eSeries == eSeriesOptions.E96)
        {
            //Log('E96 range selected.');
            selectedRange = BuildResArray(96);
        }
        else if(eSeries == eSeriesOptions.E192)
        {
            //Log('E192 range selected.');
            selectedRange = BuildResArray(192);
        }

        Integer order = FindOrder(desiredResistance);
        Double scaledDesRes = ScaleWrtOrder(desiredResistance, order);
        System.out.println("Scaled resistance = " + scaledDesRes);
        Double closestMatch = FindClosestMatch(scaledDesRes, selectedRange);
        System.out.println("Closest match 1 = " + closestMatch);
        System.out.println("Closest match 2 = " + closestMatch*Math.pow(10, order));

        // Update percentage error
        //this.percDiff(Math.round(closestMatch.diff*100)/100);

        // Return the actual resistance
        System.out.println("Returning closest resistance = " + scaledDesRes);
        return (closestMatch*Math.pow(10, order));

    }

    /**
     * Use to build an array of E-series values based on the number of elements provided.
     * @param numElements
     * @return
     */
    public static Double[] BuildResArray(int numElements)
    {
        //array = new Array();

        Double[] array = new Double[numElements];

        // Generate array elements
        for(int i = 0; i < numElements; i++)
        {
            array[i] = (double)Math.round(Math.pow(10, i/numElements)*100.0)/100.0;
        }
        return array;
    }

    // Finds the order of magnitude of a given input variable
    // if var in range 1-10, order = 0, if var in range 10-100, order = 1, e.t.c
    public static int FindOrder(double desRes)
    {
        //Log('Desired resistance = ' + desRes);
        // Find the order of magnitude by using log()
        // (e.g. 1 = between 1-10, 2 = between 10-100, 3 - between 100-1000, e.t.c)
        Double order = Math.log10(desRes);
        //Log('Order of magnitude = ' + order);
        order = Math.floor(order);
        //Log('Floored order of magnitude = ' + order);

        return order.intValue();
    }

    public static double ScaleWrtOrder(Double desRes, Integer order)
    {
        // Scale value so it is between 1-10
        return desRes/Math.pow(10, order);
    }

    // Finds the closest array entry to the provided value
    // For computational efficiency, this function
    // assumes array values are sorted from smallest to highest
    public static double FindClosestMatch(Double val, Double[] array)
    {
        Integer i = 0;
        while(val > array[i])
        {
            i++;
        }

        System.out.println("Stopped when i = " + i);
        System.out.println("Closest lower value = " + array[i-1]);
        System.out.println("Closest higher value = " + array[i]);

        Double lowerPercDiff = ((val - array[i-1])/array[i-1])*100.0;
        System.out.println("Lower percentage diff = " + lowerPercDiff);
        Double higherPercDiff = ((val - array[i])/array[i])*100.0;
        System.out.println("Higher percentage diff = " + higherPercDiff);

        if(Math.abs(lowerPercDiff) < Math.abs(higherPercDiff))
            return array[i-1];
        else
            return array[i];
    }

}

package ngordnet;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.Collection;
import java.util.ArrayList;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {    
    
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }

    /** Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if you don't want to. */
    private NavigableSet<Integer> validYears(int startYear, int endYear) {
        NavigableSet<Integer> result  = navigableKeySet().subSet(startYear, true, endYear, true);
        return result;
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        for (Integer years : ts.validYears(startYear, endYear)) {
            this.put(years, ts.get(years));
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        for (Integer years : ts.keySet()) {
            this.put(years, ts.get(years));
        }
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (Integer year : ts.keySet()) {
            if (!containsKey(year)) {
                throw new IllegalArgumentException();
            } else {
                result.put(year, (get(year).doubleValue() / ts.get(year).doubleValue()));
            }
        }
        return result;
    }

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (Integer k : keySet()) {
            result.put(k, get(k).doubleValue());
        }
        for (Integer key : ts.keySet()) {
            if (!result.containsKey(key)) {
                result.put(key, ts.get(key).doubleValue());
            } else {
                result.put(key, (result.get(key).doubleValue() + ts.get(key).doubleValue()));
            }
        }
        return result;
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Collection<Number> result = new ArrayList<Number>();
        for (Number year : keySet()) {
            result.add(year);
        }
        return result;
    }

    /** Returns all data for this time series. 
      * Must be in the same order as years(). */
    public Collection<Number> data() {
        Collection<Number> result = new ArrayList<Number>();
        for (Number year : years()) {
            result.add(get(year));
        }
        return result;
    }
}



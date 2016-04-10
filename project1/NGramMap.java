package ngordnet;
import edu.princeton.cs.introcs.In;
import java.util.HashMap;
import java.util.Collection;

public class NGramMap {
    private In wordString;
    private In countString;
    private TimeSeries<Long> allWordsFromText;
    private HashMap<String, TimeSeries<Number>> yearlyWordAppearInText;
    private final int ENDYEAR = 2015;
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordString = new In(wordsFilename);
        countString = new In(countsFilename);
        allWordsFromText = new TimeSeries<Long>();
        yearlyWordAppearInText = new HashMap<String, TimeSeries<Number>>();
        String temp = " ";
        while (countString.hasNextLine()) {
            String line = countString.readLine();
            String[] lineList = line.split(",");
            allWordsFromText.put(Integer.parseInt(lineList[0]), Long.parseLong(lineList[1]));
        }
        while (wordString.hasNextLine()) {
            String line = wordString.readLine();
            String[] lineList = line.split("\t");
            if (lineList[0].equals(temp)) {
                yearlyWordAppearInText.get(lineList[0]).put(Integer.parseInt(lineList[1]), 
                    Long.parseLong(lineList[2]));
            } else {
                TimeSeries<Number> year = new TimeSeries<Number>();
                year.put(Integer.parseInt(lineList[1]), Long.parseLong(lineList[2]));
                yearlyWordAppearInText.put(lineList[0], year);
                temp = lineList[0];
            }
        }
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        if (yearlyWordAppearInText != null && yearlyWordAppearInText.get(word) == null 
            || yearlyWordAppearInText.get(word).get(year) == null) {
            return 0;
        }
        return yearlyWordAppearInText.get(word).get(year).intValue(); 
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord result = new YearlyRecord();
        for (String key : yearlyWordAppearInText.keySet()) {
            if (countInYear(key, year) != 0) {
                result.put(key, countInYear(key, year));
            }
        }
        return result;
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return allWordsFromText;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> result = new TimeSeries<Integer>();
        for (Integer year : yearlyWordAppearInText.get(word).keySet()) {
            if (year >= startYear && year <= endYear) {
                result.put(year, yearlyWordAppearInText.get(word).get(year).intValue());
            }
        }
        return result;
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        TimeSeries<Integer> result = new TimeSeries<Integer>();
        for (Integer year : yearlyWordAppearInText.get(word).keySet()) {
            result.put(year, yearlyWordAppearInText.get(word).get(year).intValue());
        }
        return result;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (Integer year : yearlyWordAppearInText.get(word).keySet()) {
            if (year >= startYear && year <= endYear) {
                result.put(year, yearlyWordAppearInText.get(word).get(year).doubleValue() 
                            / allWordsFromText.get(year).doubleValue());
            }
        }
        return result;
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (Integer year : yearlyWordAppearInText.get(word).keySet()) {
            result.put(year, yearlyWordAppearInText.get(word).get(year).doubleValue() 
                        / allWordsFromText.get(year).doubleValue());
        }
        return result;
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear) {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (String word : words) {
            result = weightHistory(word, startYear, endYear).plus(result);
        }
        return result;
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (String word : words) {
            result = weightHistory(word).plus(result);
        }
        return result;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp) {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (int year = startYear; year < endYear; year += 1) {
            YearlyRecord yearlyRec = getRecord(year);
            if (yearlyRec.size() > 0) {
                result.put(year, yrp.process(yearlyRec));
            }
        }
        return result;
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp)  {
        TimeSeries<Double> result = new TimeSeries<Double>();
        for (int year = 0; year < ENDYEAR; year += 1) {
            YearlyRecord yearlyRec = getRecord(year);
            if (yearlyRec.size() > 0) {
                result.put(year, yrp.process(yearlyRec));
            }
        }
        return result;
    }
}

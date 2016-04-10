package ngordnet;
import java.util.HashMap;
import java.util.Collection;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashSet;

public class YearlyRecord {
    private HashMap<String, Integer> record;
    private TreeMap<Integer, HashSet<String>> ranker;
    private TreeMap<String, Integer> ranks;
    private boolean update = true;
    private Object[] results;
    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        record = new HashMap<String, Integer>();
        ranker = new TreeMap<Integer, HashSet<String>>();
        ranks = new TreeMap<String, Integer>();
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        for (String word : otherCountMap.keySet()) {
            put(word, otherCountMap.get(word));
        }
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        if (record == null || record.get(word) == null) {
            return 0;
        }
        return record.get(word);
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        if (record.containsKey(word)) {
            HashSet<String> duplicate = ranker.get(record.get(word));
            if (!duplicate.isEmpty()) {
                duplicate.remove(word);
            }
        }
        if (ranker.containsKey(count)) {
            ranker.get(count).add(word);
        } else {
            HashSet<String> temp = new HashSet<String>();
            temp.add(word);
            ranker.put(count, temp);
        }
        record.put(word, count);
        update = false;
    }

    /** Returns the number of words recorded this year. */
    public int size() {
        return record.size();
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        ArrayList<String> result = new ArrayList<String>(); 
        for (Integer key : ranker.keySet()) {
            result.addAll(ranker.get(key));
        }
        return result;
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        ArrayList<Number> result = new ArrayList<Number>();
        for (Integer key : ranker.keySet()) {
            for (int i = 0; i < ranker.get(key).size(); i += 1) {
                result.add(key);
            }
        }
        return result;
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        if (!update) {
            results = words().toArray();
            int actualRank = results.length;
            for (Object w : results) {
                ranks.put((String) w, actualRank);
                actualRank -= 1;
            }
            update = true;
        }    
        return ranks.get(word); 
    }
}

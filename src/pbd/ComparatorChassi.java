
package pbd;


import java.util.Comparator;

public class ComparatorChassi implements Comparator<String>{
    
    public int compare(String s1, String s2){
        return s1.compareTo(s2);
    }

}

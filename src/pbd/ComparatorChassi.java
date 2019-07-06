/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbd;

import java.io.RandomAccessFile;
import java.util.Comparator;

/**
 *
 * @author Usuario
 */
public class ComparatorChassi implements Comparator<String>{
    
    public int compare(String s1, String s2){
        return s1.compareTo(s2);
    }

}

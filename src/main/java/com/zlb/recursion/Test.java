package com.zlb.recursion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * @author Yuanming Tao
 * Created on 2019/8/31
 * Description
 */
public class Test {


    public static void main(String[] args) {

        int[] aa = intersection(new int[]{1, 2, 3, 4, 5, 6, 1, 1, 1, 1, 2, 2, 6, 6, 6, 6}, new int[]{2, 2, 2, 2, 21, 2, 3, 4, 5, 6, 1, 1, 1, 1, 2, 2, 6, 6, 6, 6});
        System.out.println();
    }
    public static int[] intersection(int[] nums1, int[] nums2) {

        TreeSet<Integer> set = new TreeSet();
        for(int num: nums1)
            set.add(num);

        ArrayList<Integer> list = new ArrayList();
        for(int num: nums2){
            if(set.contains(num)){
                list.add(num);
                set.remove(num);
            }
        }

        int[] res = new int[list.size()];
        for(int i = 0 ; i < list.size() ; i ++)
            res[i] = list.get(i);
        return res;
    }
}

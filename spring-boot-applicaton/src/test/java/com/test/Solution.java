package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {



    int sum=0;
    public static void main(String[] args) {
        int[] a = new int[]{1,2,3};
        System.out.println(jump(a));
    }

    public static int jump(int[] nums) {
        Arrays.sort(nums);
        int sum=0;
        int length = nums.length;
        for(int i=length-1;i>-1;i--){
            sum = sum+nums[i];
            if(sum>length){
                return length-i;
            }
        }
        return 0;
    }

    // 创建一个全局函数来保存符合条件的集合
    List<List<String>> alist=new ArrayList<List<String>>();
    int len;
    public List<List<String>> partition(String s) {
        this.len=s.length();
        List<String> list=new ArrayList<String>();
        dfs(s,0,list);
        return alist;
    }

    // 创建一个深搜函数
    public void dfs(String str,int start,List<String> list){
        // 当start的位置位于最后一个字符上是说明前面的字符串已经分配完毕，此时经集合list添加到alist中去
        if(start>=len){
            List<String> listtemp=new ArrayList<String>(list);
            alist.add(listtemp);
        }
        // 每层要做的事情就是截取不同长度的字符
        // 如何控制该层的截取长度的增加，通过利用循环的次数增加i来控制截取的长度
        for(int i=start;i<len;i++){
            String temp=str.substring(start,i+1);
            // 判断当前截取的字符串是否是回文序列，如果不是则增加字母继续判断
            //
            if(!pd(temp)){
                continue;
            }
            // 如果是回文序列则将其加入到集合中去
            list.add(temp);
            // 然后以当前剩余的字符串长度继续进行深搜
            dfs(str,i+1,list);
            // 同样回退到上一层之前将本层的痕迹清理掉
            list.remove(list.size()-1);
        }
    }

    // 先创建一个判断该字符串是否是回文序列的方法
    public boolean pd(String str){
        int l=0;
        int r=str.length()-1;
        while(l<r){
            if(str.charAt(l)!=str.charAt(r)){
                return false;
            }
            l++;
            r--;
        }
        return true;
    }





    public int uniquePaths(int m, int n) {
        int[][] step = new int[m][n];
        //坐标m,n的路径=(m-1,n)+(m,n-1)的路径之和。类似爬楼梯问题
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                step[i][j]=step[i-1][j]+step[i][j-1];
            }
        }
        return step[m-1][n-1];
    }
}

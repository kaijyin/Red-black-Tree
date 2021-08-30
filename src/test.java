import com.sun.org.apache.regexp.internal.RE;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @auther kaij_yin@163.com
 * @create 2021-08-29-19:43
 * @Description:
 */
public class test {
   public static boolean equal(Object a,Object b){
       System.out.println("map2:"+a+" map1:"+b);
        if(a==null&&b==null)return true;
        else if(a==null||b==null)return false;
        return a.equals(b);
    }
    public static void main(String[] args) {
        RedBlackTree<Integer,Integer>map1=new RedBlackTree<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        Map<Integer,Integer>map2=new TreeMap<>();
        Integer num=5000;
        boolean pass=true;
        int c=10;
        while(c--!=0){
            for(int i=0;i<=num;i++){
                Integer key= (int)(Math.random()*1000) %100;
                System.out.println("key:"+key+" num:"+map1.get(key));
                Integer v1=map1.get(key);
                if(v1==null)v1=0;
                Integer v2=map2.get(key);
                if(v2==null)v2=0;
                map1.put(key, v1+1);
                map2.put(key, v2+1);
                Integer st=(int)(Math.random()*1000)%100;
                if(!equal(map2.get(st),map1.get(st))){
                    pass=false;
                    break;
                }
                System.out.println("map1remove:"+map1.remove(st));
                map2.remove(st);
                System.out.println("i:"+i+" size1:"+map1.size()+" size2:"+map2.size());
            }
        }
        System.out.println(pass);
    }
}

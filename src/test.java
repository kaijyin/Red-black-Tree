import RedblackTree.RedBlackTree;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @auther kaij_yin@163.com
 * @create 2021-08-29-19:43
 * @Description:
 */
public class test {
   public static boolean equal(Object a,Object b){
        if(a==null&&b==null)return true;
        else if(a==null||b==null)return false;
        return a.equals(b);
    }
    public static void main(String[] args) {
        RedBlackTree<Integer,Integer> map1=new RedBlackTree<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });
        Map<Integer,Integer>map2=new TreeMap<>();
        Integer num=5000;
        boolean pass = true;
        for (int i = 0; i <= num; i++) {
            System.out.println("now insert:" + i);
            Integer key = i;
            Integer v1 = map1.get(key);
            if (v1 == null) v1 = 0;
            Integer v2 = map2.get(key);
            if (v2 == null) v2 = 0;
            map1.put(key, v1 + 1);
            map2.put(key, v2 + 1);
            Integer rd = (int) (Math.random() * i);
            if(!equal(map1.get(rd), map2.get(rd))){
                System.out.println(map1.get(rd)+"dont match "+map2.get(rd));
                pass=false;
                break;
            }
            if (!map1.check()) {
                System.out.println("insert error!");
                pass=false;
                break;
            }
            System.out.println("now erase:"+rd);
            map2.remove(rd);
            map1.remove(rd);
            if (!map1.check()) {
                System.out.println("remove error!");
                pass=false;
                break;
            }
        }
        if(pass){
            System.out.println("all passed");
        }
    }
}

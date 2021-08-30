import java.security.Key;
import java.util.Comparator;
import java.util.zip.CheckedOutputStream;

/**
 * @auther kaij_yin@163.com
 * @create 2021-08-29-13:58
 * @Description:
 */

interface map<KeyType,ValueType>{

    boolean put(KeyType key,ValueType value);
    boolean remove(KeyType key);
    ValueType get(KeyType key);
    Integer size();
}
class RedBlackTree<KeyType,Valuetype> implements map<KeyType,Valuetype>{
//            1）每个结点要么是红的，要么是黑的。
//            2）根结点是黑的。
//            3）每个叶结点（叶结点即指树尾端NIL指针或NULL结点）是黑的。
//            4）如果一个结点是红的，那么它的俩个儿子都是黑的。
//            5）对于任一结点而言，其到叶结点树尾端NIL指针的每一条路径都包含相同数目的黑结点。

    private final boolean RED=false;
    private final boolean BLACK=true;
    private Node root;
    //所有的叶子节点以及根节点的父节点都指向一个哨兵
    private Node sentinel;
    private Integer size;
    Comparator comparator;

    @Override
    public boolean put(KeyType key,Valuetype value){
        Node node=new Node(key,value);
        node.right=node.left=node.p=sentinel;
        node.color=RED;
        return insert(node);
    }
    @Override
    public  boolean remove(KeyType key){
        Node eraseNode=new Node(key);
        Node x=root;
        while(x!=sentinel){
            if(x.compareTo(eraseNode)==0){
                break;
            }else if(x.compareTo(eraseNode)<0){
                x=x.right;
            }else x=x.left;
        }
        if(x!=sentinel){
            erase(x);
            size--;
            return true;
        }
        return false;
    }

    @Override
    public Valuetype get(KeyType key) {
        Node node=new Node(key);
        Valuetype res=null;
        Node x=root;
        while(x!=sentinel){
            if(x.compareTo(node)==0){
                res=x.value;
                break;
            }else if(x.compareTo(node)<0){
                x=x.right;
            }else x=x.left;
        }
        return res;
    }

    @Override
    public Integer size() {
        return size;
    }
    RedBlackTree(Comparator<KeyType> comparator){
        sentinel=new Node();
        root=sentinel;
        size=0;
        this.comparator=comparator;
    }
    private void RightRotate(Node x){
        Node y=x.left;
        x.left=y.right;
        y.right.p=x;
        y.p=x.p;
        if(x.p==sentinel){
            root=y;
        }else if(x.p.right==x){
            x.p.right=y;
        }else x.p.left=y;
        y.right=x;
        x.p=y;
    }
    private void LeftRotate(Node x){
        Node y=x.right;
        x.right=y.left;
        y.left.p=x;
        y.p=x.p;
        if(x.p==sentinel){
            root=y;
        }else if(x.p.left==x){
            x.p.left=y;
        }else x.p.right=y;
        y.left=x;
        x.p=y;
    }
    private Boolean insert(Node z){
        Node x=root,y=sentinel;
        while (x!=sentinel){
            y=x;
            if(x.compareTo(z)==0){
                x.value=z.value;
                return true;
            }else if(x.compareTo(z)>0){
                x=x.left;
            }else x=x.right;
        }
        size++;
        z.p =y;
        if(y==sentinel){
            z.color=BLACK;
            root=z;
            return true;
        }else if(z.compareTo(y)<0){
            y.left=z;
        }else{
            y.right=z;
        }
        z.left=sentinel;
        z.right=sentinel;
        System.out.println("Inside key:"+z.key+" parent key:"+y.key);
        InsertFixup(z);
        return true;
    }
    private void InsertFixup(Node z){
        //插入的是RED,分三种情况进行修复,直到父节点不为红,满足条件4才停止

        //修复时,当前节点以及父节点为红,祖父节点为黑,父节点兄弟节点未知
        //case 1.如果父节点的兄弟节点也为红, 把父节点和兄弟节点都标记为黑,满足条件4,5,结束
        //case 2.如果父节点的兄弟节点不为红,
        //   自己,父节点,祖父节点三点是否一线?不满足就旋转来满足
        //   满足之后,将祖父节点和父节点颜色对换,然后右旋
        //   让祖父节点所在子树,在满足条件5的情况下同时满足条件4,即没有两个红色节点相邻
        //   最后祖父节点变为红,可能也会与上层有冲突(条件4不满足),继续判断
        while(z.p.color==RED){
            //不用考虑越界,因为父节点为红,必定存在祖父(根节点为黑)
           if(z.p==z.p.p.left){
               Node y=z.p.p.right;
               if(y.color==RED){
                   z.p.color=BLACK;
                   y.color=BLACK;
                   z.p.p.color=RED;
                   z=z.p.p;
               }else {
                   if (z == z.p.right) {
                      z=z.p;
                      LeftRotate(z);
                   }
                   z.p.color=BLACK;
                   z.p.p.color=RED;
                   RightRotate(z.p.p);
               }
           }else{
               Node y=z.p.p.left;
               if(y.color==RED){
                   z.p.color=BLACK;
                   y.color=BLACK;
                   z.p.p.color=RED;
                   z=z.p.p;
               }else {
                   if (z == z.p.left) {
                       z=z.p;
                       RightRotate(z);
                   }
                   z.p.color=BLACK;
                   z.p.p.color=RED;
                   LeftRotate(z.p.p);
               }
           }
        }
        root.color=BLACK;
    }

    private Node NextMin(Node z){
        Node y=sentinel;
        while(z!=sentinel){
          y=z;
          z=z.left;
        }
        return y;
    }
    //删除操作
    private void erase(Node z){
        //删除操作
        //1.如果两个儿子都为空,则直接删除即可,不需要fix
        //2.如果有一个儿子为空,用另一个儿子来替换
        //3.如果有两个儿子,则用中序遍历最近的点来替换(我指定的右侧)
        //2替换很好理解,3又是什么原理呢?
        //因为z原本的位置我们其实不需要去改,用一个与他最接近的数去替换原位置也不会影响avl树的条件
        //替换之后,我们再去实际删除用于替换的节点,
        //由于该节点是最小的大于z的节点(右子树的最左侧的位置),来找到,所以该节点最多只有右孩子,只需要做一次2的操作即可


        Node x,y;//y:删除的点,x:y的唯一儿子
        if(z.left==sentinel||z.right==sentinel){
            y=z;
        }else {
            y=NextMin(z.right);
        }
        if(y.left!=sentinel){
            x=y.left;
        }else x=y.right;

        x.p=y.p;
        if(y.p==sentinel){
            root=x;
        }else if(y==y.p.left){
            y.p.left=x;
        }else y.p.right=x;
        //复制data
        if(y!=z){
            z.key=y.key;
            z.value=y.value;
        }
        //删除了一个黑色的节点,x为顶点的树上节点到root的黑色数目都少了1,需要调整
        if(y.color==BLACK) {
            eraseFix(x);
        }
    }
    private void eraseFix(Node x){
        //调整Fx
        //为什么要调整?
        //因为删除的y如果是黑色节点,如果y存在子树,那么子树的叶子到root的距离(经过黑色节点个数)必定-1
        //条件5不再满足,需要调整
        //找到一个红色节点,给他设置为黑色,补齐删除缺失的黑色节点
        while(x!=root&&x.color==BLACK){
            if(x==x.p.left){
                //case 1:x的兄弟节点为红,需要转换为兄弟节点为黑的情况处理,
                //如何得到呢?兄弟节点为红,兄弟节点的左节点必为黑,可以作为新的兄弟
                //父节点为黑,交换兄弟节点与父节点两个颜色,然后左旋父节点即可
                Node w=x.p.right;
                if(w.color==RED){
                    w.color=BLACK;
                    x.p.color=RED;
                    LeftRotate(x.p);
                    w=x.p.right;
                }
                //case2:此时兄弟节点已经为黑,父节点必为黑
                //如果兄弟节点两个子节点都为黑,则将兄弟节点标记为红,
                // 这样兄弟所在树的叶子节点到root路径上也少一个黑色节点,所以x移动到父节点,对整棵树再Fix
                if(w.left.color==BLACK&&w.right.color==BLACK){
                    w.color=RED;
                    x=x.p;
                }else{
                    //case3:兄弟节点的两个儿子,左红右黑,旋转为右红处理
                    if(w.right.color==BLACK) {
                        w.left.color=BLACK;
                        w.color=RED;
                        RightRotate(w);
                        w=x.p.right;
                    }
                    //case4:右红
                    w.color=x.p.color;
                    x.p.color=BLACK;
                    w.right.color=BLACK;
                    x=root;
                }
            }else{
                Node w=x.p.left;
                if(w.color==RED){
                    w.color=BLACK;
                    x.p.color=RED;
                    RightRotate(x.p);
                    w=x.p.left;
                }
                if(w.right.color==BLACK&&w.left.color==BLACK){
                    w.color=RED;
                    x=x.p;
                }else{
                    if(w.left.color==BLACK) {
                        w.right.color=BLACK;
                        w.color=RED;
                        LeftRotate(w);
                        w=x.p.left;
                    }
                    w.color=x.p.color;
                    x.p.color=BLACK;
                    w.left.color=BLACK;
                    x=root;
                }
            }
        }
        //最终修复x树缺失的一个黑色节点
        x.color=BLACK;
    }


    private class Node implements Comparable<Node> {
        public KeyType key;
        public Valuetype value;
        public Node p;
        public Node left;
        public Node right;
        public boolean color;
        Node(KeyType key,Valuetype value){
            this.key=key;this.value=value;
        }
        Node(KeyType key){
            this.key=key;
        }
        Node(){};
        @Override
        public int compareTo(Node node) {
            return comparator.compare(key,node.key);
        }
    }
}

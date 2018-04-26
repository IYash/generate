package com.person.util.simulate;

import java.util.Map;

/**
 * @author huangchangling on 2018/2/26 0026
 * 红黑树的特性:
（1）每个节点或者是黑色，或者是红色。
（2）根节点是黑色。
（3）每个叶子节点（NIL）是黑色。 [注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！]
（4）如果一个节点是红色的，则它的子节点必须是黑色的。
（5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。
 */
public class TreeMapSimulate<K,V> {

    private transient Entry<K,V> root;//根节点的标识,不参与序列化过程

    // Red-black mechanics
    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    /**
     * Node in the Tree.  Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */
    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        boolean color = BLACK;
        Entry(K k,V v,Entry<K,V> parent){
            this.key = k;
            this.value = v;
            this.parent = parent;
        }
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     */
    public V put(K k,V v){
        Entry<K,V> t = root;
        //当为第一个节点时的处理逻辑
        if(t == null){
            //构建节点并作为根节点
            Entry<K,V> entry = new Entry<>(k,v,null);
            root = entry;
            return null;
        }
        int cmp;    //key的比较结果0，大于0，小于0
        Entry<K,V> parent;
        //需要根据key是否存在,返回null或者旧值，遍历比较key,按基本的二叉树插入节点
        if(k == null) throw new NullPointerException();
        Comparable<? super K> key = (Comparable<? super K>) k;
        //查找当前节点适合插入的位置,每个代码块只表示一个逻辑，将逻辑进行拆分，便于理解和维护
        do{
            parent = t; //必须进行parent的初始化，这里就不能使用while(t!=null){}
            cmp = key.compareTo(t.key);
            if (cmp == 0)
                return t.setValue(v);
            else if (cmp < 0)
                t = t.left;
            else
                t = t.right;
        }while(t!=null);
        //在当前位置插入节点
        Entry<K,V> child = new Entry<>(k,v,parent); //当前节点和父节点建立关系
        if(cmp <0) parent.left = child;else parent.right = child; //父节点和子节点建立关系
        //调整结构树，对节点进行着色和旋转
        fixAfterInsertion(child);
        return null;
    }

    /**
     * 着色和旋转的规则，红黑树是近乎平衡的二叉树
     * @param current
     */
    private void fixAfterInsertion(Entry<K, V> current) {
        current.color = RED;  //符合特性5，减少违背特性可以减少处理的情况
        //当前节点的parent节点不是root
        while(current!=null && current!=root && !current.parent.color){

            if(parentOf(current) == leftOf(parentOf(parentOf(current)))){ //当前节点的父节点是左叶子节点
                Entry<K,V> y = rightOf(parentOf(parentOf(current)));  //当前节点的叔节点

                if(!colorOf(y)){  //该节点存在且为RED,
                    setColor(parentOf(current),BLACK);
                    setColor(y,BLACK);
                    setColor(parentOf(parentOf(current)),RED);    //着色,满足特性5
                    current = parentOf(parentOf(current));
                }else{
                    if ( current == rightOf(parentOf(current))) {
                        current = parentOf(current);
                        rotateLeft(current);
                    }
                    setColor(parentOf(current), BLACK);
                    setColor(parentOf(parentOf(current)), RED);
                    rotateRight(parentOf(parentOf(current)));
                }

            }else{  //当前节点的父节点是右叶子节点
                Entry<K,V> y = leftOf(parentOf(parentOf(current)));  //当前节点的叔节点

                if(!colorOf(y)){  //该节点存在且为RED,
                    setColor(parentOf(current),BLACK);
                    setColor(y,BLACK);
                    setColor(parentOf(parentOf(current)),RED);    //着色,满足特性5
                    current = parentOf(parentOf(current));
                }else{
                    if ( current == leftOf(parentOf(current))) {
                        current = parentOf(current);
                        rotateRight(current);
                    }
                    setColor(parentOf(current), BLACK);
                    setColor(parentOf(parentOf(current)), RED);
                    rotateLeft(parentOf(parentOf(current)));
                }
            }
        }
        root.color = BLACK;
    }
    //右旋转，将当前节点作为其左叶子节点的右节点
    private void rotateRight(Entry<K, V> p) {
        if(p != null){
            Entry<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;

        }
    }
    //左旋转，将当前节点作为其右叶子节点的左节点
    private void rotateLeft(Entry<K, V> p) {

       if(p !=null){
           Entry<K,V> r = p.right;
           p.right = r.left;   //建立p的右子节点和r的左子节点的关系
           if (r.left != null)
               r.left.parent = p;  //建立r的左子节点和p的父子关系
           r.parent = p.parent;    //建立r与父节点之间的关系
           if (p.parent == null)
               root = r;
           else if (p.parent.left == p)
               p.parent.left = r;
           else
               p.parent.right = r;
           r.left = p;
           p.parent = r;  //建立p节点和r节点的父子关系
       }
    }

    private boolean colorOf(Entry<K, V> y) {
        return y == null?BLACK:y.color;
    }

    private Entry<K,V> parentOf(Entry<K,V> current){
        return current == null?null:current.parent;
    }
    private Entry<K,V> leftOf(Entry<K,V> parent){
        return parent == null?null:parent.left;
    }
    private Entry<K,V> rightOf(Entry<K,V> parent){
        return parent == null?null:parent.right;
    }
    private <K,V> void setColor(Entry<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

}

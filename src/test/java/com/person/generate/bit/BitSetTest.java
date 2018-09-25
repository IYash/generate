package com.person.generate.bit;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

/**
 * Created by huangchangling on 2018/9/25.
 * java位图测试
 */
public class BitSetTest {

    //全量bitset
    private static BitSet allBitSet = new BitSet();
    //偶数bitset
    private static BitSet evenBitSet = new BitSet();
    //奇数bitset
    private static BitSet oddBitSet = new BitSet();
    //空bitset
    private static BitSet emptyBitSet = new BitSet();

    @Before
    public void init(){
        for(int i=0;i<3;i++) {
            allBitSet.set(i);
            if (i%2 == 0) evenBitSet.set(i);
            else oddBitSet.set(i);
        }
    }
    @Test
    public void testBit(){
        long word = allBitSet.toLongArray()[0];
        System.out.println(word);
    }
    @Test
    public void testAnd() {

    }

}

package com.person.loadbalance;

import java.util.List;

/**
 * Created by huangchangling on 2018/6/25.
 */
public interface LoadBalance<T> {
    T doSelect(List<T> providers);
}

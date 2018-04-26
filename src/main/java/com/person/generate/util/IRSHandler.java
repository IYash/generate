package com.person.generate.util;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author huangchangling on 2017/10/16 0016
 */
public interface IRSHandler<T> {

    public List<T> handle(ResultSet rs);

}

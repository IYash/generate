package com.person.script

/**
 * Created by huangchangling on 2018/7/11.
 */
class TestGroovy {
    def getTime(date){
        return date.getTime();
    }
    def getDate(time){
        return new Date(time);
    }
}

package com.person.jstorm.bolt;


import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**执行计算单词出现次数的功能
 * @author huangchangling on 2017/12/22 0022
 */
public class WordBolt extends BaseBasicBolt {

    private Map<String,Integer> wordMap;
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        wordMap = new HashMap<>();
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        System.out.println("=========prepare execute=========wordBolt"+"--------------->>>>>"+Thread.currentThread().getName());
        Integer value =1;
        String key = (String)tuple.getValue(0);
        Set<String> keySet = wordMap.keySet();
        if(keySet.contains(key)) {
            value =wordMap.get(key);
            value ++;
        }
        wordMap.put(key,value);
        for (String s:wordMap.keySet()
             ) {
            System.out.println(Thread.currentThread().getName()+"key:"+s+",value:"+wordMap.get(s));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("wordBolt"));
    }
}

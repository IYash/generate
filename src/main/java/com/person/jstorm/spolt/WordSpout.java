package com.person.jstorm.spolt;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.person.jstorm.conf.SequenceTopologyDef;

import java.util.Map;
import java.util.Random;

/**
 * @author huangchangling on 2017/12/22 0022
 */
public class WordSpout extends BaseRichSpout {

    private String[] strs;
    private int count=0;

    //构造器只会执行一次，提交topology已序列化到文件，随task分发到worker


    public WordSpout(String... strs) {
      this.strs =strs;
    }

    private SpoutOutputCollector collector;
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("FIELD"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
       if(count>strs.length-1) try {
           Thread.sleep(100000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       collector.emit(new Values(strs[count]));

       count++;
    }
}

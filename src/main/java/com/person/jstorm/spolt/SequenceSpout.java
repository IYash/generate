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
public class SequenceSpout extends BaseRichSpout {

    private String name;
    private int needCount;
    private int open;

    //构造器只会执行一次，提交topology已序列化到文件，随task分发到worker


    public SequenceSpout(String name, int needCount) {
        this.name = name;
        this.needCount = needCount;
    }

    private SpoutOutputCollector collector;
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("spout","SPOUT","Spout"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
        System.out.println(name+"============open================spout");
        open = new Random().nextInt();
        System.out.println(open+"+++++++++++++++open++++++++++++++spout");
    }

    @Override
    public void nextTuple() {
        while (SequenceTopologyDef.COUNT > needCount) {
            System.out.println(open+"+++++++++++++++open tuple++++++++++++++spout");
            collector.emit(new Values("hello", "world"));
            needCount++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

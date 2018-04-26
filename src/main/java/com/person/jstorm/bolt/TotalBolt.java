package com.person.jstorm.bolt;


import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import java.util.Map;
import java.util.Random;

/**
 * @author huangchangling on 2017/12/22 0022
 */
public class TotalBolt extends BaseBasicBolt {

    private int prepare;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        prepare = new Random().nextInt();
        System.out.println(prepare+"=========prepare=========totalBolt");
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        System.out.println(prepare+"=========prepare execute=========totalBolt");
        System.out.println(tuple.getValue(0)+"============execute=========totalBolt");
        System.out.println(tuple.getValue(1)+"============execute=========totalBolt");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("totalBolt"));
    }
}

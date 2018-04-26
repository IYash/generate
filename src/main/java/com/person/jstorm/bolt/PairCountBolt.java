package com.person.jstorm.bolt;


import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.person.jstorm.conf.SequenceTopologyDef;

import java.util.Map;
import java.util.Random;

/**
 * @author huangchangling on 2017/12/22 0022
 */
public class PairCountBolt extends BaseBasicBolt {

    private int prepare;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        prepare = new Random().nextInt();
        System.out.println(prepare+"=========prepare=========pairCountBolt");
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        System.out.println(prepare+"=========prepare execute=========pairCountBolt");
        //the checking of source source stream is required
        if(tuple.getSourceStreamId().equals(SequenceTopologyDef.CUSTOMER_STREAM_ID)){
        System.out.println(tuple.getValue(0)+"============execute=========pairCountBolt");
        basicOutputCollector.emit(new Values(SequenceTopologyDef.CUSTOMER_STREAM_ID));
        }else{
        basicOutputCollector.emit(new Values(SequenceTopologyDef.TRADE_STREAM_ID));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("pairCountBolt"));
    }
}

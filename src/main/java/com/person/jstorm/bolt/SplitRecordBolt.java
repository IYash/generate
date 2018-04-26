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
public class SplitRecordBolt extends BaseBasicBolt {

    private int prepare;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        prepare = new Random().nextInt();
        System.out.println(prepare+"=========prepare=========splitRecordBolt");
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        System.out.println(prepare+"=========prepare execute=========splitRecordBolt");
        System.out.println(tuple.getValue(0)+"============execute=========splitRecordBolt");
        System.out.println(tuple.getValue(1)+"============execute=========splitRecordBolt");

        basicOutputCollector.emit(SequenceTopologyDef.CUSTOMER_STREAM_ID,new Values(SequenceTopologyDef.CUSTOMER_STREAM_ID));
        basicOutputCollector.emit(SequenceTopologyDef.TRADE_STREAM_ID,new Values(SequenceTopologyDef.TRADE_STREAM_ID));
    }
    //outputFieldsDeclarer.declareOutputFields与basicOutputCollector.emit有默认的streamId default，二者的streamId必须一致
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream(SequenceTopologyDef.CUSTOMER_STREAM_ID,new Fields("splitRecordBolt"));
        outputFieldsDeclarer.declareStream(SequenceTopologyDef.TRADE_STREAM_ID,new Fields("splitRecordBolt"));
    }
}

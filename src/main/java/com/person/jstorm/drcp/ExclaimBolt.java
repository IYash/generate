package com.person.jstorm.drcp;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;

import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * @author huangchangling on 2018/1/2 0002
 */
public class ExclaimBolt extends BaseBasicBolt {
    @Override
    public void prepare(Map map, TopologyContext topologyContext) {

    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //DRPCTopology的对应版本
        String input = tuple.getString(1);
        basicOutputCollector.emit(new Values(tuple.getValue(0),input+"!!!"));
        //GeneralDRPCTopology的对应版本
       /* String arg = tuple.getString(0);
        Object retInfo = tuple.getValue(1);
        basicOutputCollector.emit(new Values(arg + "!!!", retInfo));*/
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("id","result"));
    }

}

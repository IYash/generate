package com.person.jstorm.topoloty;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.person.jstorm.bolt.WordBolt;
import com.person.jstorm.conf.SequenceTopologyDef;
import com.person.jstorm.spolt.WordSpout;


/**
 * fieldsGrouping test
 * new Field("")在fieldsGrouping中有重大作用
 * @author huangchangling on 2017/12/22 0022
 */
public class FieldTopology {

    static TopologyBuilder builder = new TopologyBuilder();

    public static void main(String[] args) {
        SpoutDeclarer spout = builder.setSpout(SequenceTopologyDef.SEQUENCE_SPOUT_NAME,new WordSpout("hi","cool","warm","push","pull","hello","world","hi"),1);
        BoltDeclarer totalBolt = builder.setBolt(SequenceTopologyDef.TOTAL_BOLT_NAME,new WordBolt(),3);
        totalBolt.fieldsGrouping(SequenceTopologyDef.SEQUENCE_SPOUT_NAME,new Fields("FIELD"));/*必须与spout的declare一致*/
        Config conf = new Config();
        conf.setDebug(true);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(SequenceTopologyDef.LOCAL_TOPOLOGY,conf,builder.createTopology());
    }
}

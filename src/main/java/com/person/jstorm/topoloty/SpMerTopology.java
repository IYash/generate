package com.person.jstorm.topoloty;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;
import com.person.jstorm.bolt.*;
import com.person.jstorm.conf.SequenceTopologyDef;
import com.person.jstorm.spolt.SequenceSpout;


/**
 * @author huangchangling on 2017/12/22 0022
 */
public class SpMerTopology {

    static TopologyBuilder builder = new TopologyBuilder();

    public static void main(String[] args) {
        SpoutDeclarer spout = builder.setSpout(SequenceTopologyDef.SEQUENCE_SPOUT_NAME,new SequenceSpout(SequenceTopologyDef.SEQUENCE_SPOUT_NAME,9),1);
        //different bolt different tuple
        BoltDeclarer splitBolt = builder.setBolt(SequenceTopologyDef.SPLIT_RECORD_NAME,new SplitRecordBolt(),1);
        splitBolt.localOrShuffleGrouping(SequenceTopologyDef.SEQUENCE_SPOUT_NAME);

        BoltDeclarer totalBolt = builder.setBolt(SequenceTopologyDef.TRADE_COUNT_NAME,new PairCountBolt(),1);
        totalBolt.localOrShuffleGrouping(SequenceTopologyDef.SPLIT_RECORD_NAME,/*sender name*/
                SequenceTopologyDef.TRADE_STREAM_ID)/*the stream which bolt receives tuple from sender*/;
        BoltDeclarer partBolt = builder.setBolt(SequenceTopologyDef.CUSTOMER_COUNT_NAME,new PairCountBolt(),1);
        partBolt.localOrShuffleGrouping(SequenceTopologyDef.SPLIT_RECORD_NAME,
                SequenceTopologyDef.CUSTOMER_STREAM_ID);

        //make one bolt receive tuples from more than two stream types
        BoltDeclarer mergeBolt = builder.setBolt(SequenceTopologyDef.MERGE_RECORD_NAME,new MergeRecordBolt(),1);
        mergeBolt.localOrShuffleGrouping(SequenceTopologyDef.TRADE_COUNT_NAME).localOrShuffleGrouping(SequenceTopologyDef.CUSTOMER_COUNT_NAME);

        Config conf = new Config();
        conf.setDebug(true);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(SequenceTopologyDef.TEST_TOPOLOGY,conf,builder.createTopology());
    }
}

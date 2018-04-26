package com.person.jstorm.topoloty;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;
import com.person.jstorm.bolt.PartBolt;
import com.person.jstorm.bolt.TotalBolt;
import com.person.jstorm.conf.SequenceTopologyDef;
import com.person.jstorm.spolt.SequenceSpout;



/**
 * @author huangchangling on 2017/12/22 0022
 */
public class SequenceTopology {

    static TopologyBuilder builder = new TopologyBuilder();

    public static void main(String[] args) {
        SpoutDeclarer spout = builder.setSpout(SequenceTopologyDef.SEQUENCE_SPOUT_NAME,new SequenceSpout(SequenceTopologyDef.SEQUENCE_SPOUT_NAME,8),1);
        //send the same tuple to different bolt(totalBolt&partBolt);
        BoltDeclarer totalBolt = builder.setBolt(SequenceTopologyDef.TOTAL_BOLT_NAME,new TotalBolt(),1);
        totalBolt.localOrShuffleGrouping(SequenceTopologyDef.SEQUENCE_SPOUT_NAME);
        BoltDeclarer partBolt = builder.setBolt(SequenceTopologyDef.PART_BOLT_NAME,new PartBolt(),1);
        partBolt.localOrShuffleGrouping(SequenceTopologyDef.SEQUENCE_SPOUT_NAME);

        Config conf = new Config();
        conf.setDebug(true);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(SequenceTopologyDef.LOCAL_TOPOLOGY,conf,builder.createTopology());
    }
}

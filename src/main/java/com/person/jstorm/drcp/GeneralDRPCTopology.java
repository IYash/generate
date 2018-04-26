package com.person.jstorm.drcp;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.topology.TopologyBuilder;

/**
 * @author huangchangling on 2018/1/2 0002
 */
public class GeneralDRPCTopology {

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
        LocalDRPC drpc = new LocalDRPC();

        DRPCSpout spout = new DRPCSpout("exclamation", drpc);
        builder.setSpout("drpc", spout);
        builder.setBolt("exclaim", new ExclaimBolt(), 3).noneGrouping("drpc");
        builder.setBolt("return", new ReturnResults(), 3).noneGrouping("exclaim");

        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();
        cluster.submitTopology("exclaim", conf, builder.createTopology());

        System.out.println(drpc.execute("exclamation", "aaa"));
        System.out.println(drpc.execute("exclamation", "bbb"));

        //remote模式
       /* try
        {
            StormSubmitter.submitTopology("drpc-demo", conf,builder.createTopology());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

    }
}

package com.person.jstorm.drcp;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.topology.TopologyBuilder;


/**
 * @author huangchangling on 2018/1/2 0002
 */
public class DRPCTopology {

    public static void main(String[] args) throws Exception{
        Config conf = new Config();
        conf.setDebug(true);
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("exclamation");
        builder.addBolt(new ExclaimBolt(),3);
        if (args == null || args.length == 0) {
            LocalDRPC drpc = new LocalDRPC();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("drpc-demo",conf,builder.createLocalTopology(drpc));
            //方位
        for (String word : new String[] { "hello", "goodbye","hi","cool","warm"}) {
                long start=System.currentTimeMillis();
            String result = drpc.execute("exclamation", word);
                System.err.println("============================>Result for \"" + word + "\": "
                        + result);
            System.out.println(System.currentTimeMillis()-start+"<---------------------------");
        }
        /*cluster.shutdown();
        drpc.shutdown();*/
    }else{
            StormSubmitter.submitTopology("drpc-demo", conf, builder.createRemoteTopology());
        }
    }
}

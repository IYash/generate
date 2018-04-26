package com.person.jstorm.conf;

/**
 * @author huangchangling on 2017/12/22 0022
 */
public class SequenceTopologyDef {
    //spout
    public final static String SEQUENCE_SPOUT_NAME="sequenceSpot";
    //bolt
    public final static String TOTAL_BOLT_NAME="totalBolt";
    public final static String PART_BOLT_NAME="partBolt";
    public final static String TRADE_COUNT_NAME="tradeCountBolt";
    public final static String CUSTOMER_COUNT_NAME="customerCountBolt";
    public final static String SPLIT_RECORD_NAME="splitRecordBolt";
    public final static String MERGE_RECORD_NAME="mergeRecordBolt";
    //topology
    public final static String LOCAL_TOPOLOGY="localTopology";
    public final static String TEST_TOPOLOGY="testTopology";
    //streamId
    public final static String TRADE_STREAM_ID="tradeStream";
    public final static String CUSTOMER_STREAM_ID="customerStream";

    public final static Integer COUNT=10;
}

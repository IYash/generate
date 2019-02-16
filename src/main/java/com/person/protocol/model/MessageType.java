package com.person.protocol.model;

public enum MessageType {
    BOOK_REQ(1),
    BOOK_RESP(2),
    LOGIN_REQ(3),
    LOGIN_RESP(4),
    HEARTBEAT_REQ(5),
    HEARTBEAT_RESP(6),
            ;

    private int type;
    MessageType(int type){
        this.type = type;
    }
    public byte value(){
        return (byte)type;
    }
}

package com.person.concurrent.pcdemo;

/**
 * Created by huangchangling on 2018/4/25.
 */
public class Message {

    private String message;
    private int seq;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public static  MessageBuilder messageBuilder(){
        return new MessageBuilder();
    }
    static class MessageBuilder extends Message{
        public Message buildSeq(int seq){
            super.seq = seq;
            return this;
        }
        public Message buildMessage(String message){
            super.message = message;
            return this;
        }
        public Message build(){
            return this;
        }
    }

}

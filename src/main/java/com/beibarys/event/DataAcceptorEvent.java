package com.beibarys.event;

public class DataAcceptorEvent extends AcceptorEvent{
    byte billType;

    public DataAcceptorEvent(Object source, byte billType) {
        super(source);
        this.billType = billType;
    }

    public void setBillType(byte billType){
        this.billType = billType;
    }

    public byte getBillType() {
        return billType;
    }
}
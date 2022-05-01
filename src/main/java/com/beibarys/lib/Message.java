package com.beibarys.lib;

import com.beibarys.states.Commands;

import java.util.Arrays;

public class Message {
    private byte CMD;
    private byte[] DATA;
    private boolean correctCRC;



    public Message(){}
    public Message(Commands CMD, byte[] DATA) {
        this.CMD = CMD.getCommand();
        this.DATA = DATA;
    }
    public Message(Commands CMD) {
        this.CMD = CMD.getCommand();
        this.DATA = new byte[]{};
    }

    public byte getCMD() {
        return CMD;
    }
    public void setCMD(Commands CMD) {
        this.CMD = CMD.getCommand();
    }
    public void setCMD(byte CMD){
        this.CMD = CMD;
    }

    public byte[] getDATA() {
        return DATA;
    }

    public void setDATA(byte[] DATA) {
        this.DATA = DATA;
    }

    public boolean isCorrectCRC() {
        return correctCRC;
    }

    public void setCorrectCRC(boolean correctCRC) {
        this.correctCRC = correctCRC;
    }

    @Override
    public String toString() {
        return "Message{" +
                "CMD=" + CMD +
                ", DATA=" + Arrays.toString(DATA) +
                '}';
    }
}

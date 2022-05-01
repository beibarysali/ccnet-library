package com.beibarys.lib;

import com.beibarys.comport.IBillValidator;
import com.beibarys.comport.TransportManager;
import com.beibarys.event.EventCreator;
import com.beibarys.event.EventListener;
import com.beibarys.states.Commands;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class Acceptor implements IBillValidator {
    TransportManager tm;
    ReplyParser replyParser;
    String idResponse;
    HashMap<String, BillType> billTable;
    EventListener listener;
    Scheduler scheduler;
    EventCreator eventCreator;

    public Acceptor(String portName) {
        tm = new TransportManager(portName);
        eventCreator = new EventCreator();
        replyParser = new ReplyParser(this);
        scheduler = new Scheduler();
    }

    public void setIdentificationResponse(Message message) {
        idResponse = "";
        idResponse += new String(Arrays.copyOfRange(message.getDATA(), 0, 14));//15 bytes
        idResponse += new String(Arrays.copyOfRange(message.getDATA(), 15, 27));//12 bytes
        idResponse += new String(Arrays.copyOfRange(message.getDATA(), 28, 33));//7 bytes
    }
    public String getIdResponse(){
        return idResponse;
    }

    public void setBillTable(Message message){
        billTable = new HashMap<>();
        for (int i = 0; i < 120; i++){
            if(i % 5 == 0){
                byte[] temp = Arrays.copyOfRange(message.getDATA(), i, i+5);
                BillType billType = new BillType(temp[0]+"", new String(Arrays.copyOfRange(temp, 1, 3)), temp[4]+"");
                billTable.put(i+"", billType);
            }
        }
    }

    public HashMap<String, BillType> getBillTable(){
        return billTable;
    }

    public void startPoller(){
        scheduler.go(tm, replyParser);
    }

    public void stopPoller(){
        scheduler.end();
    }

    public void setEventListener(EventListener host){
        listener = host;
    }

    public EventListener getListener() {
        return listener;
    }

    public EventCreator getEventCreator() {
        return eventCreator;
    }

    private void pollMessage(Commands cmd){
        pollMessage(cmd, new byte[0]);
    }

    private void pollMessage(Commands cmd, byte[] data){
        Message msgTo = new Message(cmd);
        msgTo.setDATA(data);
        Message msgFrom = tm.sendData(msgTo);
        replyParser.checkAnswer(msgFrom, cmd);
    }

    public void pollIdAndBillTable(){
        pollMessage(Commands.IDENTIFICATION);

        pollMessage(Commands.GET_BILL_TABLE);
    }

    @Override
    public void acceptNote() {
        pollMessage(Commands.STACK);
    }

    @Override
    public boolean connect() {
        tm.startWrapper();
        return true;
    }

    @Override
    public void disableBV() {
        pollMessage(Commands.ENABLE_BILL_TYPES, new byte[]{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});
    }

    @Override
    public void disconnect() {
        tm.stopWrapper();
    }

    @Override
    public void enableBV() {
        pollMessage(Commands.ENABLE_BILL_TYPES, new byte[]{(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});
    }

    @Override
    public String getApplication() {
        return getIdResponse();
    }

    @Override
    public String getProtokol() {
        return "CCNET";
    }

    @Override
    public void returnNote() {
        pollMessage(Commands.RETURN);
    }

    @Override
    public void downloadBin(File f) {

    }
    public void reset() {
        pollMessage(Commands.RESET);
    }
}

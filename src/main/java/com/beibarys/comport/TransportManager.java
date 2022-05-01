package com.beibarys.comport;

import com.beibarys.lib.Message;
import jssc.SerialPortException;

public class TransportManager {
    String portName;
    WrapperSerialPort wsp;

    public TransportManager(String portName){
        this.portName = portName;
        this.wsp = new WrapperSerialPort();
    }

    public synchronized Message sendData(Message message){
        wsp.sendBytesToPort(message);
        Message answ = wsp.receiveMes();

        if(answ.isCorrectCRC()){
            wsp.sendToPortACK();
            return answ;
        }

        wsp.sendToPortNAK();
        return null;
    }

    public WrapperSerialPort getWsp() {
        return wsp;
    }

    public void resetTransport(){
        if(wsp.isOpened()){
            stopWrapper();
        }
        startWrapper();
    }

    public void startWrapper(){
        wsp.start(portName);
    }
    public void stopWrapper(){
        try {
            wsp.stop();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}

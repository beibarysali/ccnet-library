package com.beibarys.comport;

import com.beibarys.lib.Converter;
import com.beibarys.lib.Message;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.Arrays;
public class WrapperSerialPort {
    private final int NON_RESPONSE = 5000;
    static final int READBUF_SIZE = 256;
    byte[] rbuf = new byte[READBUF_SIZE];
    int mReadSize = 0;
    private SerialPort serialPort;
    Converter converter = new Converter();
    final Object o = new Object();

    public void start(String portName){
        serialPort = new SerialPort(portName);
        create();
    }

    public void stop() throws SerialPortException{
        if(serialPort.isOpened()){
            serialPort.removeEventListener();
            serialPort.closePort();
            clear();
        }
    }
    public void reset(){
        if(serialPort.isOpened()){
            try {
                stop();
                clear();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
        create();
    }

    private void create(){
        try{
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
            serialPort.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if(event.isRXCHAR())
                    {
                        try {
                            if (serialPort.getInputBufferBytesCount() > 0) {
                                byte[] arrRead = serialPort.readBytes();
                                System.arraycopy(arrRead, 0, rbuf, mReadSize, arrRead.length);
                                mReadSize += arrRead.length;
                            }
                        }catch (SerialPortException e1){
                            e1.printStackTrace();
                        }
                    }
                }
            });
        }catch (SerialPortException e){
            e.printStackTrace();
        }
    }

    public void clear(){
        mReadSize = 0;
        rbuf = new byte[READBUF_SIZE];
    }

    public synchronized byte[] getBytesFromPort(){
        return rbuf;
    }

    public void sleep(long millis) {
            try {
                synchronized (o) {
                    o.wait(millis);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public synchronized void sendBytesToPort(byte[] data){
        try {
            clear();
            serialPort.writeBytes(data);
            sleep(10);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void sendBytesToPort(Message message){
        sendBytesToPort(converter.getBytesFrom(message));
    }

    public byte[] receive() {
        long start = System.currentTimeMillis();
        boolean complete = false;
        byte[] receivedData = new byte[mReadSize];
        while (!complete) {
            try {
                receivedData = Arrays.copyOfRange(getBytesFromPort(), 0, mReadSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(converter.checkCRC(receivedData)){
                return receivedData;
            }
            if (System.currentTimeMillis() - start > NON_RESPONSE) {
                complete = true;
            }
        }
        return receivedData;
    }
    public Message receiveMes(){
        byte[] answ = receive();
        if(answ.length>=5) {
            Message result = converter.getMessageFrom(answ);
            result.setCorrectCRC(false);
            if (converter.checkCRC(answ)) {
                result.setCorrectCRC(true);
            }
            return result;
        }
        else
        {
            Message mes = new Message();
            mes.setCorrectCRC(false);
            return mes;
        }
    }
    public void sendToPortNAK(){
        sendBytesToPort(converter.responeMessage(0xFF));
    }
    public void sendToPortACK(){
        sendBytesToPort(converter.responeMessage(0x00));
    }
    public boolean isOpened(){
        return serialPort.isOpened();
    }
}

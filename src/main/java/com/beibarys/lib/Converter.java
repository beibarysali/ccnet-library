package com.beibarys.lib;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;
public class Converter {
    private static final byte SYNC = 0x02;
    private static final byte ADR = 0x03;
    private static final int POLYNOMIAL = 0x08408;

    public Message getMessageFrom(byte[] bytes){
        Message message = new Message();
        byte[] data = new byte[bytes.length-5];
        if (data.length != 0) {
            int i = 3, d=0;
            while (d != data.length) {
                data[d] = bytes[i];
                i+=1;
                d+=1;
            }
        }
        message.setDATA(data);

        consoleOutputFile(bytes, false);

        return message;
    }

    private static void consoleOutputFile(byte[] data, boolean isBVFrom){
        String arrows;
        if(!isBVFrom)
            arrows = "<<<<";
        else
            arrows = ">>>>";
        try {
            FileUtils.writeStringToFile(new File("/opt/11.txt"),arrows + System.lineSeparator(), Charset.defaultCharset(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printBytesFile(data);
    }
    public static void printBytesFile(byte[] data) {
        StringBuilder sb = new StringBuilder(0);
        for (int i = 0; i < data.length; i++){
            sb.append("["+ data[i]+"]");
        }
        try {
            FileUtils.writeStringToFile(new File("/opt/11.txt"),sb.toString()+ System.lineSeparator(), Charset.defaultCharset(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void consoleOutput(byte[] data, boolean isBVFrom){
        if(!isBVFrom)
            System.out.println("<<<<");
        else
            System.out.println(">>>>");
        printBytes(data);
    }
    public static void printBytes(byte[] data) {
        for (int i = 0; i < data.length; i++){
            System.out.print("["+ data[i]+"]");
        }
        System.out.println();
    }
    public static byte[] getBytesFrom(Message message){
        int length = message.getDATA().length+6;
        byte[] bytes = new byte[length];
        bytes[0] = SYNC;
        bytes[1] = ADR;
        bytes[2] = (byte)length;
        bytes[3] = message.getCMD();

        byte[] data = message.getDATA();
        if (data.length != 0) {
            int i = 4, d=0;
            while (d != data.length) {
                bytes[i] = data[d];
                i+=1;
                d+=1;
            }
        }

        long crc = getCRC16(bytes, length - 2);
        //long temp = getCRC16Custom(bytes, length - 2);
        bytes[bytes.length - 2] = (byte) (crc & 0x00FFL);
        bytes[bytes.length - 1] = (byte) (crc >> 8);

        consoleOutputFile(bytes, true);

        return bytes;
    }

    public boolean checkCRC(byte[] bufData){
        boolean result = true;
        try {
            byte[] oldCrc = new byte[]{bufData[bufData.length - 2], bufData[bufData.length - 1]};
            byte[] newCrc = new byte[2];
            long crc = getCRC16(bufData, bufData.length - 2);
            newCrc[0] = (byte) (crc & 0x00FFL);
            newCrc[1] = (byte) (crc >> 8);
            for (int i = 0; i < 2; i++) {
                if (oldCrc[i] != newCrc[i]) {
                    result = false;
                    break;
                }
            }
        }
        catch(Exception ex)
        {
            //printBytes(bufData);
            return false;
        }
        return result;
    }


    private static long getCRC16(byte[] byteResult, int sizeData) {
        long $ = 0;
        for (int i = 0; i < sizeData; ++i) {
            $ ^= byteResult[i] & 0xFF;
            for (int j = 0; j < 8; ++j) {
                if (($ & 0x0001) != 0) {
                    $ >>= 1;
                    $ ^= 0x08408;
                } else {
                    $ >>= 1;
                }
            }
        }
        return $;
    }
    public byte[] responeMessage(int cmd){//NAK or ACK
        byte[] response = new byte[6];
        response[0] = SYNC;
        response[1] = ADR;
        response[2] = 6;
        if(cmd == 0x00)
            response[3] = 0x00;//ACK
        else
            response[3] = (byte) 0xFF;//NAK

        long crc = getCRC16(response, response.length-2);
        response[4] = (byte) (crc & 0x00FFL);
        response[5] = (byte) (crc >> 8);

        return response;
    }
}

package com.beibarys.states;

import java.util.HashMap;
import java.util.Map;

public enum Commands {
    RESET(0x30),
    GET_STATUS(0x31),
    SET_SECURITY(0x32),
    POLL(0x33),
    ENABLE_BILL_TYPES(0x34),
    STACK(0x35),
    RETURN(0x36),
    IDENTIFICATION(0x37),
    HOLD(0x38),
    SET_BARCODE_PARAMETERS(0x39),
    EXTRACT_BARCODE_DATA(0x3A),
    GET_BILL_TABLE(0x41),
    DOWNLOAD(0x50),
    GET_CRC32_OF_THE_CODE(0x51),
    REQUEST_STATISTICS(0x60);

    final private int command;
    private static final Map<Byte, Commands> lookup = new HashMap<Byte, Commands>();

    static {
        for (Commands cmd : Commands.values()) {
            lookup.put(cmd.getCommand(), cmd);
        }
    }

    public static Commands get(byte cmd){
        return lookup.get(cmd);
    }

    public byte getCommand(){
        return (byte)this.command;
    }
    Commands(int command){
        this.command = command;
    }
}
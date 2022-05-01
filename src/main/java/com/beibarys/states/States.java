package com.beibarys.states;

import java.util.HashMap;
import java.util.Map;

public enum States {
    POWER_UP(0x10),
    POWER_UP_WITH_BILL_IN_VALIDATOR(0x11),
    POWER_UP_WITH_BILL_IN_STACKER(0x12),
    INITIALIZE(0x13),
    IDLING(0x14),
    ACCEPTING(0x15),
    STACKING(0x17),
    RETURNING(0x18),
    UNIT_DISABLED(0x19),
    HOLDING(0x1A),
    DEVICE_BUSY(0x1B),
    REJECT_STATE(0x1C),//rejectState
    INSERTION(0x60),
    MAGNETIC(0x61),
    REMAINED_BILL( 0x62),
    MULTIPLYING(0x63),
    CONVEYING(0x64),
    IDENTIFICATION(0x65),
    VERIFICATION(0x66),
    OPTIC(0x67),
    INHIBIT(0x68),
    CAPACITY(0x69),
    OPERATION(0x6A),
    LENGHT(0x6C),
    UNRECOGNISED_BARCODE(0x92),
    UV(0x6D),
    INCORRECT_NUMBER_IN_BARCODE(0x93),
    UNKNOWN_BARCODE_START_SEQ(0x94),
    UNKNOWN_BARCODE_STOP_SEQ(0x95),
    FAILURE_STATE(0x47),//failureState
    STACK_MOTOR(0x50),
    TRANSPORT_MOTOR_SPEED(0x51),
    TRANSPORT_MOTOR(0x52),
    ALIGNING_MOTOR(0x53),
    INITIAL_CASSETTE_STATUS(0x54),
    OPTIC_CANAL(0x55),
    MAGNETIC_CANAL(0x56),
    CAPACITANCE_CANAL(0x5F),
    DROP_CASSETTE_FULL(0x41),//errorState
    DROP_CASSETTE_OUT_OF_POSITION(0x42),
    VALIDATOR_JAMMED(0x43),
    DROP_CASSETTE_JAMMED(0x44),
    CHEATED(0x45),
    PAUSE(0x46),
    ESCROW_POSITION(0x80),
    BILL_STACKED(0x81),
    BILL_RETURNED(0x82);

    private final int response;

    public byte getResponse(){
        return (byte) this.response;
    }
    States(int response){
        this.response = response;
    }

    private static final Map<Byte, States> lookup = new HashMap<>();

    static {
        for (States cmd : States.values()) {
            lookup.put(cmd.getResponse(), cmd);
        }
    }

    public static States get(byte cmd){
        return lookup.get(cmd);
    }
}

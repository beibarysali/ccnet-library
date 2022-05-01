package com.beibarys.lib;

import com.beibarys.event.AcceptorEvent;
import com.beibarys.event.DataAcceptorEvent;
import com.beibarys.states.*;

import static com.beibarys.states.States.*;

public class ReplyParser {
    States currentState = null;
    States previousState = null;
    Acceptor acceptor;
    boolean nonIdentificated = true;

    public ReplyParser(Acceptor acceptor){
        this.acceptor = acceptor;
    }

    public void checkPoll(Message message){
        if(message==null)
            return;
        try {
            States state = States.get(message.getDATA()[0]);
            switch(state) {
                case POWER_UP:
                    currentState = POWER_UP;
                    break;
                case POWER_UP_WITH_BILL_IN_VALIDATOR:
                    currentState = POWER_UP_WITH_BILL_IN_VALIDATOR;
                    break;
                case POWER_UP_WITH_BILL_IN_STACKER:
                    currentState = POWER_UP_WITH_BILL_IN_STACKER;
                    break;
                case INITIALIZE:
                    currentState = INITIALIZE;
                    break;
                case IDLING:
                    currentState = IDLING;
                    break;
                case ACCEPTING:
                    currentState = ACCEPTING;
                    break;
                case STACKING:
                    currentState = STACKING;
                    break;
                case RETURNING:
                    currentState = RETURNING;
                    break;
                case UNIT_DISABLED:
                    currentState = UNIT_DISABLED;
                    if(nonIdentificated){
                        acceptor.pollIdAndBillTable();
                        nonIdentificated = false;
                    }
                    break;
                case HOLDING:
                    currentState = HOLDING;
                    break;
                case DEVICE_BUSY:
                    currentState = DEVICE_BUSY;
                    break;
                case REJECT_STATE:
                    switch (States.get(message.getDATA()[1])) {
                        case INSERTION: currentState = INSERTION;
                            break;
                        case MAGNETIC: currentState = MAGNETIC;
                            break;
                        case REMAINED_BILL:  currentState = REMAINED_BILL;
                            break;
                        case MULTIPLYING: currentState = MULTIPLYING;
                            break;
                        case CONVEYING: currentState = CONVEYING;
                            break;
                        case IDENTIFICATION: currentState = IDENTIFICATION;
                            break;
                        case VERIFICATION: currentState = VERIFICATION;
                            break;
                        case OPTIC: currentState = OPTIC;
                            break;
                        case INHIBIT: currentState = INHIBIT;
                            break;
                        case CAPACITY: currentState = CAPACITY;
                            break;
                        case OPERATION: currentState = OPERATION;
                            break;
                        case LENGHT: currentState = LENGHT;
                            break;
                        case UNRECOGNISED_BARCODE: currentState = UNRECOGNISED_BARCODE;
                            break;
                        case UV: currentState = UV;
                            break;
                        case INCORRECT_NUMBER_IN_BARCODE: currentState = INCORRECT_NUMBER_IN_BARCODE;
                            break;
                        case UNKNOWN_BARCODE_START_SEQ: currentState = UNKNOWN_BARCODE_START_SEQ;
                            break;
                        case UNKNOWN_BARCODE_STOP_SEQ: currentState = UNKNOWN_BARCODE_STOP_SEQ;
                            break;
                        default: currentState = REJECT_STATE;
                            break;
                    }
                    break;
                case FAILURE_STATE:
                    switch (States.get(message.getDATA()[1])) {
                        case STACK_MOTOR:currentState = STACK_MOTOR;
                            break;
                        case TRANSPORT_MOTOR_SPEED:currentState = TRANSPORT_MOTOR_SPEED;
                            break;
                        case TRANSPORT_MOTOR:currentState = TRANSPORT_MOTOR;
                            break;
                        case ALIGNING_MOTOR:currentState = ALIGNING_MOTOR;
                            break;
                        case INITIAL_CASSETTE_STATUS:currentState = INITIAL_CASSETTE_STATUS;
                            break;
                        case OPTIC_CANAL:currentState = OPTIC_CANAL;
                            break;
                        case MAGNETIC_CANAL:currentState = MAGNETIC_CANAL;
                            break;
                        case CAPACITANCE_CANAL:currentState = CAPACITANCE_CANAL;
                            break;
                        default:currentState = FAILURE_STATE;
                            break;
                    }
                    break;
                case DROP_CASSETTE_FULL:
                    currentState = DROP_CASSETTE_FULL;
                    break;
                case DROP_CASSETTE_OUT_OF_POSITION:
                    currentState = DROP_CASSETTE_OUT_OF_POSITION;
                    break;
                case VALIDATOR_JAMMED:
                    currentState = VALIDATOR_JAMMED;
                    break;
                case DROP_CASSETTE_JAMMED:
                    currentState = DROP_CASSETTE_JAMMED;
                    break;
                case CHEATED:
                    currentState = CHEATED;
                    break;
                case PAUSE:
                    currentState = PAUSE;
                    break;
                case ESCROW_POSITION:
                    currentState = ESCROW_POSITION;
                    break;
                case BILL_STACKED:
                    currentState = BILL_STACKED;
                    break;
                case BILL_RETURNED:
                    currentState = BILL_RETURNED;
                    break;
                default:
                    break;
            }
            checkState(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkState(Message message){
        if(previousState != currentState){
            AcceptorEvent accEvent;

            if(currentState == ESCROW_POSITION || currentState == BILL_STACKED || currentState == BILL_RETURNED){
                accEvent = new DataAcceptorEvent(this, message.getDATA()[1]);
            }
            else {
                accEvent = new AcceptorEvent(this);
            }

            accEvent.setDescription(currentState + " event occured");
            acceptor.getEventCreator().fireEvent(accEvent, acceptor.getListener());
            previousState = currentState;
        }
    }

    public Object checkAnswer(Message answer, Commands cmd){
        switch(cmd) {
            case RESET:
                break;
            case GET_STATUS:
                break;
            case SET_SECURITY:
                break;
            case ENABLE_BILL_TYPES:
                break;
            case STACK:
                break;
            case RETURN:
                break;
            case IDENTIFICATION:
                acceptor.setIdentificationResponse(answer);
                break;
            case HOLD:
                break;
            case SET_BARCODE_PARAMETERS:
                break;
            case EXTRACT_BARCODE_DATA:
                break;
            case GET_BILL_TABLE:
                acceptor.setBillTable(answer);
                break;
            case DOWNLOAD:
                break;
            case GET_CRC32_OF_THE_CODE:
                break;
            case REQUEST_STATISTICS:
                break;
            default:
        }
        return null;
    }
}
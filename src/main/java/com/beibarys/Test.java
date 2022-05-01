package com.beibarys;

import com.beibarys.event.AcceptorEvent;
import com.beibarys.event.DataAcceptorEvent;
import com.beibarys.event.EventListener;
import com.beibarys.lib.Acceptor;
import com.beibarys.lib.Converter;
import com.beibarys.lib.Message;
import com.beibarys.states.Commands;

import java.util.Scanner;

public class Test implements EventListener {
    Acceptor acceptor;

    public Acceptor getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(Acceptor acceptor) {
        this.acceptor = acceptor;
    }

    public static void main(String[] args){
        Acceptor acceptor = new Acceptor("/dev/ttyUSB0");
        Test test = new Test();
        acceptor.setEventListener(test);
        test.setAcceptor(acceptor);
        acceptor.connect();
        acceptor.startPoller();
        acceptor.reset();
        boolean start = true;
        while(start){
            int number = new Scanner(System.in).nextInt();
            switch (number){
                case 1:
                    acceptor.acceptNote();
                    break;
                case 2:
                    acceptor.disableBV();
                    break;
                case 3:
                    acceptor.enableBV();
                    break;
                case 4:
                    System.out.println(acceptor.getApplication());
                    break;
                case 5:
                    acceptor.returnNote();
                    break;
                case 6:
                    System.out.println(acceptor.getProtokol());
                    break;
                case 7:
                    acceptor.reset();
                    break;
                case 8:
                    System.out.println(acceptor.getBillTable());
                    break;
                case 9:
                    start = false;
                    acceptor.stopPoller();
                    break;
                default:
                    break;
            }
        }
        acceptor.disconnect();
    }

    @Override
    public void eventOccured(AcceptorEvent event) {
        if(event instanceof DataAcceptorEvent){
            System.out.println(getAcceptor().getBillTable());
            System.out.println(((DataAcceptorEvent) event).getBillType());
        }
        System.out.println(event.getDescription());
    }
}
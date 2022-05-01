package com.beibarys.lib;

import com.beibarys.comport.TransportManager;
import com.beibarys.states.Commands;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    final private Timer timer = new Timer();
    final private Message pollMessage = new Message(Commands.POLL, new byte[]{});
    private boolean stop;

    public Scheduler(){}

    public void go(TransportManager tm, ReplyParser replyParser){
        stop = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(stop)
                        timer.cancel();
                    Message answ = tm.sendData(pollMessage);
                    replyParser.checkPoll(answ);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 100, 100);
    }
    public void end(){
        stop = true;
    }
}

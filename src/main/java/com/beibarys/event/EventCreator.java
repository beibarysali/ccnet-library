package com.beibarys.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventCreator {
    private final ExecutorService _executor = Executors.newSingleThreadExecutor();

    public EventCreator() {

    }

    public void fireEvent(final AcceptorEvent event, EventListener listener){
        this._executor.execute(new Runnable() {
            public void run() {
                listener.eventOccured(event);
            }
        });
    }
}

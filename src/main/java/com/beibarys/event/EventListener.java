package com.beibarys.event;

public interface EventListener extends java.util.EventListener {
    void eventOccured(AcceptorEvent event);
}

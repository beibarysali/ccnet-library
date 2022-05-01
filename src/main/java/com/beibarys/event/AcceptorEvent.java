package com.beibarys.event;

import java.util.EventObject;

public class AcceptorEvent extends EventObject {
    String description;

    public AcceptorEvent(Object source) {
        super(source);
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

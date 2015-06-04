package com.da.util;


public class Actor extends ActPerformer{
    final long id;
    public Actor(String name, long id) {
        super(name);this.id = id;
    }

    public long getId() {
        return id;
    }
}

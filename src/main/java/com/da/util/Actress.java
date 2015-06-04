package com.da.util;

/**
 * Created by puri on 6/4/2015.
 */
public class Actress extends ActPerformer {
    final long id;
    public Actress(String name, long id) {
        super(name);this.id = id;
    }

    public long getId() {
        return id;
    }
}

package com.hackathon.util;


import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusProvider2 {
    public static final Bus bus = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance(){
        return bus;
    }

    public BusProvider2() {

    }
}

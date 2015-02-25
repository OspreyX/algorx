package com.targetcompid.algorx;

import com.targetcompid.algorx.events.OrderBookRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shahbaz Chaudhary (shahbazc@gmail)
 */
public class ExchangeInterface {

    private final String id;
    private final Map<String,OrderBookRequest> requests = new HashMap<>();

    public ExchangeInterface(String id){
        this.id = id;
    }

    public void processRequest(OrderBookRequest request){

    }
}

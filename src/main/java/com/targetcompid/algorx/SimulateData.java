package com.targetcompid.algorx;

import com.targetcompid.algorx.events.*;
import rx.Observable;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author shahbaz
 */
public class SimulateData {
    private static DataHolder[] symbols = {new DataHolder("MSFT",30),new DataHolder("AAPL",132),new DataHolder("ORCL",45),new DataHolder("GOOG",530)};

    private static Random rand = new Random();

    public static Observable.OnSubscribe<LastSalePrice> generateLSP(){
        Timer timer = new Timer();
        Observable.OnSubscribe<LastSalePrice> subscribeFunction = (subscriber) -> {

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    DataHolder sym = symbols[rand.nextInt(4)];
                    sym.doTrade();
                    LastSalePrice event=new LastSalePrice(System.currentTimeMillis(),sym.getSymbol(),sym.getLastPrice(),sym.getLastSize(),sym.getVolume());
                    subscriber.onNext(event);
                }
            }, 1000, 500);
        };

        return subscribeFunction;
    }

    public static Observable.OnSubscribe<BBOChange> generateQuote(){
        Timer timer = new Timer();
        Observable.OnSubscribe<BBOChange> subscribeFunction = (subscriber) -> {

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    DataHolder sym = symbols[rand.nextInt(4)];
                    sym.changeQuote();
                    BBOChange event=new BBOChange(System.currentTimeMillis(),sym.getSymbol(),sym.getLastBidPrice(),sym.getLastBidSize(),sym.getLastOfferPrice(),sym.getLastOfferSize());
                    subscriber.onNext(event);
                }
            }, 1000, 250);
        };

        return subscribeFunction;
    }

    public static Observable.OnSubscribe<AlgoOrder> generateCustomerOrders(){
        long id = 0;
        Timer timer = new Timer();
        Observable.OnSubscribe<AlgoOrder> subscribeFunction = (subscriber) -> {

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    DataHolder sym = symbols[rand.nextInt(4)];
                    NewOrder order = new NewOrder(System.currentTimeMillis(),Long.toString(id),sym.getSymbol(),rand.nextInt(40000)+10000,rand.nextBoolean(),sym.getLastPrice());

                    AlgoOrder algoOrder;

                    int startMinutesFromNow = rand.nextInt(60*2);
                    int endMinutesAfterStart = rand.nextInt(60*6);
                    LocalTime start = LocalTime.now().plus(startMinutesFromNow, ChronoUnit.MINUTES);
                    LocalTime end = start.plus(endMinutesAfterStart, ChronoUnit.MINUTES);

                    double volPercent = (rand.nextInt(19)+1)/100.0;

                    if(rand.nextBoolean()) algoOrder = new TWAPOrder(start,end,order);
                    else  algoOrder = new PercentageOfVolumeOrder(start,end,volPercent,order);

                    subscriber.onNext(algoOrder);
                }
            }, 1000, 3000);
        };

        return subscribeFunction;
    }

}

class DataHolder{
    private static final Random random = new Random();

    private final String symbol;
    private double lastPrice;
    private long lastSize;
    private double lastBidPrice;
    private double lastOfferPrice;
    private long lastBidSize;
    private long lastOfferSize;
    private long volume;

    public DataHolder(String symbol, double lastPrice) {
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.lastBidPrice = lastPrice - 0.01;
        this.lastOfferPrice = lastPrice;
        this.lastBidSize = 100;
        this.lastOfferSize = 100;
        this.volume = 1000;
    }

    public void doTrade(){
        int next = random.nextInt(3);
        if(next > 1) lastPrice += 0.01;
        if(next < 1) lastPrice -= 0.01;
        lastSize = random.nextInt(500);
        volume += lastSize;
    }

    public void changeQuote(){
        double diff = (lastPrice - lastBidPrice) * 100l;
        if(diff<1) lastBidPrice = lastPrice;
        else lastBidPrice = ((double)random.nextInt((int)diff))/100.0;
        lastOfferPrice = lastBidPrice + 0.02;
        lastBidSize = random.nextInt(500)+100;
        lastOfferSize = random.nextInt(500)+100;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public long getLastSize() {
        return lastSize;
    }

    public double getLastBidPrice() {
        return lastBidPrice;
    }

    public double getLastOfferPrice() {
        return lastOfferPrice;
    }

    public long getLastBidSize() {
        return lastBidSize;
    }

    public long getLastOfferSize() {
        return lastOfferSize;
    }

    public long getVolume() {
        return volume;
    }
}

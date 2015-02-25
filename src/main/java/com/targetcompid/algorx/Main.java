package com.targetcompid.algorx;

import com.targetcompid.algorx.events.BBOChange;
import com.targetcompid.algorx.events.LastSalePrice;
import com.targetcompid.algorx.events.OrderBookResponse;
import com.targetcompid.algorx.events.AlgoOrder;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author Shahbaz Chaudhary (shahbazc@gmail)
 */
public class Main {

    public static void main(String ... args) {
        ExchangeInterface exchangeInterface = new ExchangeInterface("BATS");

        Observable<LastSalePrice> lsp = Observable.create(SimulateData.generateLSP());
        Observable<BBOChange> bidOffer = Observable.create(SimulateData.generateQuote());
        Observable<AlgoOrder> algoOrder = Observable.create(SimulateData.generateCustomerOrders());
        Observable<OrderBookResponse> exchangeResponse;

        lsp.groupBy(new Func1<LastSalePrice, LastSalePrice>() {
            @Override
            public LastSalePrice call(LastSalePrice lastSalePrice) {
                return null;
            }
        });

        lsp.subscribe(
                (incomingValue) -> System.out.println("incomingValue " + incomingValue),
                (error) -> System.out.println("Something went wrong" + ((Throwable)error).getMessage()),
                () -> System.out.println("This observable is finished")
        );

        bidOffer.subscribe(
                (incomingValue) -> System.out.println("incomingValue " + incomingValue),
                (error) -> System.out.println("Something went wrong" + ((Throwable)error).getMessage()),
                () -> System.out.println("This observable is finished")
        );

        algoOrder.subscribe(
                (incomingValue) -> System.out.println("incomingValue " + incomingValue),
                (error) -> System.out.println("Something went wrong" + ((Throwable)error).getMessage()),
                () -> System.out.println("This observable is finished")
        );
    }
}

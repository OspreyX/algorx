package com.targetcompid.algorx.events

import java.time.LocalTime

/** *
  *
  * @author Shahbaz Chaudhary (shahbazc@gmail)
  * Taken from http://falconair.github.io/2015/01/05/financial-exchange.html
  *
  */

  //Events
  abstract class OrderBookRequest
  case class NewOrder(timestamp: Long, tradeID: String, symbol: String, qty: Long, isBuy: Boolean, price: Double) extends OrderBookRequest
  case class Cancel(timestamp: Long, order: NewOrder) extends OrderBookRequest
  case class Amend(timestamp: Long, order:NewOrder, newPrice:Double, newQty:Long) extends OrderBookRequest

  abstract class OrderBookResponse
  case class Filled(timestamp: Long, price: Double, qty: Long, order: Array[NewOrder]) extends OrderBookResponse
  case class Acknowledged(timestamp: Long, request: OrderBookRequest) extends OrderBookResponse
  case class Rejected(timestamp: Long, error: String, request: OrderBookRequest) extends OrderBookResponse
  case class Canceled(timestamp: Long, reason: String, order: NewOrder) extends OrderBookResponse

  abstract class MarketDataEvent
  case class LastSalePrice(timestamp: Long, symbol: String, price: Double, qty: Long, volume: Long) extends MarketDataEvent
  case class BBOChange(timestamp: Long, symbol: String, bidPrice:Double, bidQty:Long, offerPrice:Double, offerQty:Long) extends MarketDataEvent

  abstract class AlgoOrder
  case class TWAPOrder(start: LocalTime, end: LocalTime, order:NewOrder) extends AlgoOrder
  case class PercentageOfVolumeOrder(start: LocalTime, end: LocalTime, percentOfVol: Double, order:NewOrder) extends AlgoOrder


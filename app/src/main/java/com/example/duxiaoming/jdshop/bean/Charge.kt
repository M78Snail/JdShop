package com.example.duxiaoming.jdshop.bean

data class Charge(var id: String, var `object`: String, var created: Long, var livemode: Boolean, var paid: Boolean, var refunded: Boolean,
                  var app: Any, var channel: String, var orderNo: String, var clientIp: String, var amount: Int? = null, var amountSettle: Int? = null,
                  var currency: String, var subject: String, var body: String, var timePaid: Long? = null, var timeExpire: Long? = null,
                  var timeSettle: Long? = null, var transactionNo: String, var refunds: ChargeRefundCollection, var amountRefunded: Int? = null,
                  var failureCode: String, var failureMsg: String, var metadata: Map<String, String>, var credential: Map<String, Any>, var extra: Map<String, String>,
                  var description: String

)
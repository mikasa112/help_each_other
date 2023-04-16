package org.help.protocol.model

data class Order(
    val customerUuid: String,
    val orderId: Int,
    val providerUuid: String,
    val serviceId: Int,
    val status: Int
)
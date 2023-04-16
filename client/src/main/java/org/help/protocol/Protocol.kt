package org.help.protocol

/**
 * @author Yuanan
 * @date 2023/4/15
 * @description
 */
enum class Protocol(private val data: Int) {
    CONNECT(0x01),
    CONNACK(0x02),
    MESSAGE(0x03),
    PING(0x0C),
    PONG(0x0D),
    DISCONNECT(0x0E);

    fun getData(): Int {
        //左移四位
        return this.data shl 4
    }
}
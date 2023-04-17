package org.help.protocol

import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * @author Yuanan
 * @date 2023/4/15
 * @description
 */
open class Data(
    val dataHead: ByteArray,
    var dataContent: ByteArray,
) : Serializable {


    constructor() : this(ByteArray(3), ByteArray(0))


    /**
     * 获取报文的固定头部的长度信息
     */
    fun getDataContentLen(): Int {
        // java在位运算时会把byte提升到int，所以我们只关心低八位，需先和0xFF取&,或者先提升到int在和0xFF取&
        val high = dataHead[1].toInt() and 0xFF
        val low = dataHead[2].toInt() and 0xFF
        return (high shl 8) or low
    }

    /**
     * 获取报文的协议类型
     */
    fun getProtocolType(): Protocol? {
        val byte = dataHead[0].toInt() and 0xFF
        return ProtocolType((byte and 0xF0) shr 4)
    }

    /**
     * 将dataHead和dataContent组合起来
     */
    fun getData(): ByteArray {
        val target = ByteArray(dataHead.size + dataContent.size)
        //复制dataHead到target
        System.arraycopy(dataHead, 0, target, 0, 3)
        if (dataContent.isNotEmpty()) {
            System.arraycopy(dataContent, 0, target, 3, dataContent.size)
        }
        return target
    }

    companion object {
        /**
         * 获得data的协议类型
         */
        @JvmStatic
        fun ProtocolType(data: Int): Protocol? {
            val type = when (data) {
                0x01 -> Protocol.CONNECT
                0x02 -> Protocol.CONNACK
                0x03 -> Protocol.MESSAGE
                0x0C -> Protocol.PING
                0x0D -> Protocol.PONG
                0x0E -> Protocol.DISCONNECT
                else -> null
            }
            return type
        }

        /**
         * 将Data的字节小端序
         */
        @JvmStatic
        fun GetBytes(data: Data): ByteArray {
            val byte = ByteArray(data.getData().size)
            val order = ByteBuffer.wrap(data.getData()).order(ByteOrder.LITTLE_ENDIAN)
            order.get(byte)
            return byte
        }

        @JvmStatic
        fun ConvertIntToBytes(len: Int): ByteArray {
            val data = ByteArray(2)
            data[0] = ((len shr 8) and 0xFF).toByte()
            data[1] = (len and 0xFF).toByte()
            return data
        }
    }
}
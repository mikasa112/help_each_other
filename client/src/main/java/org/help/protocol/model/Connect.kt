package org.help.protocol.model

import com.alibaba.fastjson.JSONObject
import org.help.protocol.Data
import org.help.protocol.Protocol


/**
 * @author Yuanan
 * @date 2023/4/16
 * @description
 */
class Connect : Data(dataHead = ByteArray(3), dataContent = ByteArray(0)) {
    init {
        dataHead[0] = Protocol.CONNECT.getData().toByte()
        dataContent = JSONObject.toJSONBytes("8a0aa8b7-9c39-479a-83ff-f018a4a5e7d6")
        val intToBytes = ConvertIntToBytes(dataContent.size)
        dataHead[1] = intToBytes[0]
        dataHead[2] = intToBytes[1]
    }
}
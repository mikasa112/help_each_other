package org.help.protocol.model

import org.help.protocol.Data
import org.help.protocol.Protocol


/**
 * @author Yuanan
 * @date 2023/4/16
 * @description
 */
class Ping : Data(dataHead = ByteArray(3), dataContent = ByteArray(0)) {
    init {
        dataHead[0] = Protocol.PING.getData().toByte()
    }
}
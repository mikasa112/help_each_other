package org.help.protocol

import java.io.InputStream
import java.io.OutputStream

/**
 * @author Yuanan
 * @date 2023/4/15
 * @description
 */
interface Protoc {
    fun connect(output: OutputStream)
    fun connack(input: InputStream)
    fun ping(output: OutputStream)
    fun reader(input: InputStream)
    fun disConnect(output: OutputStream)
}
package org.help.client

import com.alibaba.fastjson.JSON
import org.help.protocol.Data
import org.help.protocol.Protoc
import org.help.protocol.Protocol
import org.help.protocol.model.Connect
import org.help.protocol.model.DisConnect
import org.help.protocol.model.Order
import org.help.protocol.model.Ping
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import kotlin.concurrent.timerTask

/**
 * @author Yuanan
 * @date 2023/4/16
 * @description
 */
@Suppress("NAME_SHADOWING")
class Client(private val host: String, private val port: Int) : Protoc {
    private lateinit var socket: Socket
    private lateinit var timer: Timer
    private var heartBeatCount: Int = 0
    private var heartBeatResCount: Int = 0

    constructor() : this("127.0.0.1", 8081) {
        handle(host, port)
    }

    fun handle(host: String, port: Int) {
        heartBeatCount = 0
        heartBeatResCount = 0
        try {
            socket = Socket()
            socket.keepAlive = true
            socket.connect(InetSocketAddress(host, port))
            connect(socket.getOutputStream())
            connack(socket.getInputStream())
            ping(socket.getOutputStream())
            reader(socket.getInputStream())
        } catch (e: IOException) {
            println("headle_error:${e.message}")
            close(socket)
            handle(host, port)
        }
    }

    fun close(closeable: Closeable) {
        closeable.close()
    }


    override fun connect(output: OutputStream) {
        val ou = BufferedOutputStream(output)
        val connect = Connect()
        ou.write(Data.GetBytes(connect))
        ou.flush()
    }

    override fun connack(input: InputStream) {
        val input = BufferedInputStream(input)
        val data = Data()
        input.read(data.dataHead)
        val protocolType = data.getProtocolType()
        if (protocolType?.equals(Protocol.CONNACK) == true) {
            println("握手成功")
        }
    }

    override fun ping(output: OutputStream) {
        val ou = BufferedOutputStream(output)
        timer = Timer()
        timer.schedule(timerTask {
            heartBeatCount++
            if (heartBeatCount - heartBeatResCount >= 3) {
                timer.cancel()
                close(socket)
                println("正在重连...")
                handle(host, port)
            } else {
                val ping = Ping()
                ou.write(Data.GetBytes(ping))
                ou.flush()
            }
        }, HEARTBEAT, HEARTBEAT)
    }

    override fun reader(input: InputStream) {
        val input = BufferedInputStream(input)
        while (ALIVE) {
            val data = Data()
            input.read(data.dataHead)
            val protocolType = data.getProtocolType()
            when (protocolType) {
                Protocol.PONG -> {
                    println("服务还活着呢")
                    ++heartBeatResCount
                }

                Protocol.MESSAGE -> {
                    data.dataContent = ByteArray(data.getDataContentLen())
                    input.read(data.dataContent)
                    val order: Order = JSON.parseObject(data.dataContent, Order::class.java)
                    println("通知到客户: $order")
                }

                else -> continue
            }
        }
    }

    override fun disConnect(output: OutputStream) {
        val ou = BufferedOutputStream(output)
        ALIVE = false
        val dis = DisConnect()
        ou.write(dis.getData())
        ou.flush()
        timer.cancel()
        System.gc()
        println("client主动关闭连接")
    }

    companion object {
        val HEARTBEAT = 1000 * 40L
        var ALIVE = true

        @JvmStatic
        fun DisConnect(client: Client) {
            client.disConnect(client.socket.getOutputStream())
        }
    }
}
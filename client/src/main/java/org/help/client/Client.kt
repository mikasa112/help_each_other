package org.help.client

import org.help.protocol.Data
import org.help.protocol.Protoc
import org.help.protocol.Protocol
import org.help.protocol.model.Connect
import org.help.protocol.model.DisConnect
import org.help.protocol.model.Ping
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
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

    constructor() : this("192.168.1.115", 8081) {
        handle(host, port)
    }

    private fun handle(host: String, port: Int) {
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

    private fun close(closeable: Closeable) {
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
            when (data.getProtocolType()) {
                Protocol.PONG -> {
                    println("服务还活着呢")
                    ++heartBeatResCount
                }

                Protocol.MESSAGE -> {
                    data.dataContent = ByteArray(data.getDataContentLen())
                    input.read(data.dataContent)
                    //todo 记得在实际运用时修改
                    println("server say: ${String(data.dataContent, StandardCharsets.UTF_8)}")
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
        const val HEARTBEAT = 1000 * 30L
        var ALIVE = true

        @JvmStatic
        fun DisConnect(client: Client) {
            client.disConnect(client.socket.getOutputStream())
        }
    }
}
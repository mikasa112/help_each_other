package core

import (
	"bufio"
	"net"
	"server/internal/protocol"
	"server/pkg/conf"
	"server/pkg/logger"
)

type NetServer struct {
	addr string
}

func (s *NetServer) Start() {
	listen, err := net.Listen("tcp", s.addr)
	if err != nil {
		logger.Panicf("服务启动失败，启动地址为: {%v}, 错误为: {%v}", conf.ServerConf.Addr, err)
	}
	for {
		conn, err := listen.Accept()
		if err != nil {
			logger.Errorf("%v 连接失败，错误为: {%v}", conn.RemoteAddr(), err)
			continue
		}
		connect(conn)
		connack(conn)
		go process(conn)
	}
}

func process(conn net.Conn) {
	for {
		data := protocol.Data{}
		r := bufio.NewReader(conn)
		err := data.Unpack(r)
		if data.DataType() == protocol.PING {
			data := protocol.Data{
				DataHead:    protocol.DataHead{protocol.PONG.Bytes()},
				DataContent: nil,
			}
			w := bufio.NewWriter(conn)
			err = data.Packet(w)
		}
		if err != nil {
			ErrHandle(err, conn)
			break
		}
	}
}

// server==>client
func connack(conn net.Conn) {
	data := protocol.Data{
		DataHead:    protocol.DataHead{protocol.CONNACK.Bytes()},
		DataContent: nil,
	}
	w := bufio.NewWriter(conn)
	err := data.Packet(w)
	ErrHandle(err, conn)
	logger.Infof("握手成功，状态为: %v", data.DataType().String())
}

// client==>server
func connect(conn net.Conn) {
	data := protocol.Data{}
	r := bufio.NewReader(conn)
	logger.Infof("%v发起连接", conn.RemoteAddr().String())
	err := data.Unpack(r)
	ErrHandle(err, conn)
	if data.DataType() == protocol.CONNECT {
		logger.Infof("状态为：%v", data.DataType().String())
	} else {
		conn.Close()
	}
}

// ErrHandle 处理连接异常，如有错误直接关闭连接
func ErrHandle(err error, conn net.Conn) {
	if err != nil {
		logger.Errorf("连接以断开，错误为：%v", err)
		conn.Close()
	}
}

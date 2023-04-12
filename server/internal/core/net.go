package core

import (
	"bufio"
	"net"
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
			logger.Errorf("客户端连接失败，错误为: {%v}", err)
			continue
		}
		go process(conn)
	}
}

func process(conn net.Conn) {
	defer func() {
		err := conn.Close()
		if err != nil {
			logger.Errorf("{%v} 客户端连接关闭失败，错误为: {%v}", conn.RemoteAddr(), err)
		}
	}()
	logger.Infof("客户端: {%v} 连接成功", conn.RemoteAddr())
	for {
		connack(conn)
		//服务端响应客户端的ping请求
	}
}

func connack(conn net.Conn) {
	var buf [512]byte
	reader := bufio.NewReader(conn)
	for {
		_, _ = reader.Read(buf[0:])
	}
	//logger.Errorf("连接报文确认失败，错误为: {%v}", err)

}

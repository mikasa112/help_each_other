package core

//
//import (
//	"bufio"
//	"net"
//	"server/internal/protocol"
//	"server/pkg/conf"
//	"server/pkg/logger"
//)
//
//type NetServer struct {
//	addr string
//}
//
//func (s *NetServer) Start() {
//	listen, err := net.Listen("tcp", s.addr)
//	if err != nil {
//		logger.Panicf("服务启动失败，启动地址为: %v, 错误为: %v", conf.ServerConf.Addr, err)
//	}
//	//持续监听
//	for {
//		conn, err := listen.Accept()
//		if err != nil {
//			logger.Errorf("%v 连接失败，错误为: %v", conn.RemoteAddr(), err)
//			continue
//		}
//		//连接
//		connect(conn)
//		//握手
//		connack(conn)
//		//处理控制
//		go process(conn)
//	}
//}
//
//// client==>server
//func connect(conn net.Conn) {
//	data := protocol.Data{}
//	r := bufio.NewReader(conn)
//	logger.Infof("%v发起连接", conn.RemoteAddr().String())
//	err := data.Unpack(r)
//	if err != nil {
//		logger.Errorf("解包失败,错误为: %v", err)
//		conn.Close()
//		return
//	}
//	if data.DataType() == protocol.CONNECT {
//		//添加一个用户
//		GetContainer().Push(User{
//			Uuid: data.DataContent.String(),
//			Conn: conn,
//		})
//		logger.Infof("状态为：%v", data.DataType().String())
//	} else {
//		conn.Close()
//	}
//}
//
//// server==>client
//func connack(conn net.Conn) {
//	data := protocol.Data{
//		DataHead:    protocol.DataHead{protocol.CONNACK.Bytes()},
//		DataContent: nil,
//	}
//	w := bufio.NewWriter(conn)
//	err := data.Packet(w)
//	if err != nil {
//		logger.Errorf("封包失败,错误为: %v", err)
//		conn.Close()
//		return
//	}
//	logger.Infof("握手成功，状态为: %v", data.DataType().String())
//}
//
//// 根据报文状态去处理控制
//func process(conn net.Conn) {
//	for {
//		data := protocol.Data{}
//		r := bufio.NewReader(conn)
//		//阻塞等待解包
//		err := data.Unpack(r)
//		if err != nil {
//			logger.Errorf("解包失败,错误为: %v", err)
//			ErrHandle(conn)
//			break
//		}
//		if data.DataType() == protocol.PING {
//			data := protocol.Data{
//				DataHead:    protocol.DataHead{protocol.PONG.Bytes()},
//				DataContent: nil,
//			}
//			w := bufio.NewWriter(conn)
//			err = data.Packet(w)
//			if err != nil {
//				logger.Errorf("封包失败,错误为: %v", err)
//				ErrHandle(conn)
//				break
//			}
//			//client主动关闭连接时
//		} else if data.DataType() == protocol.DISCONNECT {
//			logger.Infof("%v以主动断开连接", conn.RemoteAddr().String())
//			conn.Close()
//			//删除这个user
//			GetContainer().Pop(User{Conn: conn})
//			//退出死循环
//			break
//		}
//	}
//}
//
//// ErrHandle 处理连接异常，如有错误直接关闭连接
//func ErrHandle(conn net.Conn) {
//	//logger.Errorf("连接以断开，错误为：%v", err)
//	conn.Close()
//	//删除这个user
//	GetContainer().Pop(User{Conn: conn})
//}

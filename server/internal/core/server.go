package core

import (
	"bufio"
	"encoding/json"
	"log"
	"runtime"
	"server/internal"
	"server/internal/protocol"
	"server/pkg/conf"
	"server/pkg/logger"
	"sync"
	"time"
)

var WG sync.WaitGroup

var Message = make(chan []byte)

func New() {
	WG.Add(1)
	server := &NetServer{addr: conf.ServerConf.Addr}
	go server.Start()
	go RedisChannel()
	logger.Infoln("服务已启动...")
	//检查服务器状态
	//go checkServer()
	distribute()
	WG.Wait()
}

// 会话分发给User
func distribute() {
	for {
		select {
		case m := <-Message:
			order := decode(m)
			//根据msg的UUID去获得conn
			conn, err := GetContainer().matchConn(order)
			if err != nil {
				//uuid的用户不存在跳过本次
				continue
			}
			lenBytes := protocol.CoverUint16ToBytes(uint16(len(m)))
			data := protocol.Data{
				DataHead:    protocol.DataHead{protocol.MESSAGE.Bytes(), lenBytes[0], lenBytes[1]},
				DataContent: m,
			}
			w := bufio.NewWriter(conn)
		flag:
			err = data.Packet(w)
			if err != nil {
				//写包失败重新写
				goto flag
			}
		}
	}

}

// 解码为order类型
func decode(data []byte) internal.Order {
	order := internal.Order{}
	err := json.Unmarshal(data, &order)
	if err != nil {
		logger.Errorf("解码失败, 错误为: %v", err)
	}
	return order
}

func checkServer() {
	ticker := time.NewTicker(10 * time.Second)
	for range ticker.C {
		container := GetContainer()
		log.Printf("容器里有: %v", container.Users)
		goroutine := runtime.NumGoroutine()
		log.Printf("当前服务器并发Goroutines为: %v", goroutine)
	}
}

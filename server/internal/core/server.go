package core

import (
	"bufio"
	"encoding/json"
	"log"
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
	logger.Infoln("服务已启动...")
	//检查容器
	go checkContainer()
	distribute()
	WG.Wait()
}

func distribute() {
	for {
		select {
		case m := <-Message:
			//todo 目前msg直接视作UUID
			msg := decode(m)
			//根据msg的UUID去获得conn
			conn, err := GetContainer().matchConn(msg)
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

// 解码为string类型 todo后续应该封装一下
func decode(data []byte) string {
	var str string
	err := json.Unmarshal(data, &str)
	if err != nil {
		logger.Errorf("解码失败, 错误为: %v", err)
	}
	return str
}

func checkContainer() {
	ticker := time.NewTicker(5 * time.Second)
	for _ = range ticker.C {
		container := GetContainer()
		for _, user := range container.Users {
			log.Printf("容器里有: %v", user.Uuid)
		}
	}
}

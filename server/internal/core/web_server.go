package core

import (
	"encoding/json"
	"github.com/gorilla/websocket"
	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/host"
	"github.com/shirou/gopsutil/v3/mem"
	"log"
	"net/http"
	"runtime"
	"server/internal/core/web"
	"server/pkg/conf"
	"server/pkg/logger"
	"time"
)

var upgrader = websocket.Upgrader{
	//跨域
	CheckOrigin: func(r *http.Request) bool {
		return true
	}}

// WebServerStart 开启Web服务
func WebServerStart() {
	http.HandleFunc(conf.WebConf.Pattern, webserver)
	err := http.ListenAndServe(conf.WebConf.Addr, nil)
	if err != nil {
		log.Panicf("Web服务启动失败，启动地址为：%v，错误为：%v", conf.WebConf.Addr, err)
	}
}

func webserver(w http.ResponseWriter, r *http.Request) {
	c, err := upgrader.Upgrade(w, r, nil)
	container := GetContainer()
	if err != nil {
		log.Panicf("WebSocket服务启动失败, 启动路径为：%v，错误为: %v",
			conf.WebConf.Pattern, err)
	}
	defer c.Close()

	for {
		msg, err := Read(c)
		user := User{Conn: c}
		if err != nil {
			container.Pop(user)
			break
		}
		switch msg.Type {
		case web.Register:
			user.Uuid = msg.Uuid
			//将client加入队列
			log.Printf("Register: %v=======>", c.RemoteAddr().String())
			container.Push(user)
		case web.Ping:
			log.Printf("Ping: %v=======>", c.RemoteAddr().String())
			PONG(c)
		}
		//给ADMIN用户主动发服务器信息
		if msg.UserType == web.ADMIN {
			go Send(user.Conn)
		}
	}
}

func PONG(c *websocket.Conn) {
	err := c.WriteMessage(websocket.TextMessage, []byte("PONG"))
	log.Printf("Pong: %v <=======", c.RemoteAddr().String())
	if err != nil {
		logger.Warnf("pong发送失败: %v", err)
	}
}

func Read(c *websocket.Conn) (*web.Message, error) {
	_, p, err := c.ReadMessage()
	if err != nil {
		logger.Errorf("连接异常: %v", err)
		return nil, err
	}
	msg := &web.Message{}
	err = json.Unmarshal(p, &msg)
	if err != nil {
		logger.Warnf("json解析异常: %v", err)
		return nil, err
	}
	return msg, nil
}

// Send 周期性发送数据
func Send(conn *websocket.Conn) {
	serverInfo := ServerInfo()
	_ = conn.WriteJSON(&serverInfo)
	t := time.NewTicker(5 * time.Second)
	defer func() {
		t.Stop()
		conn.Close()
	}()
	for range t.C {
		serverInfo := ServerInfo()
		err := conn.WriteJSON(&serverInfo)
		if err != nil {
			log.Printf("WebSocket Closed，错误为：%v", err)
			break
		}
	}
}

func ServerInfo() ServerData {
	var users []string
	for _, u := range GetContainer().Users {
		users = append(users, u.Uuid)
	}
	memory, _ := mem.VirtualMemory()
	cpuInfo, _ := cpu.Percent(1*time.Second, false)
	hostInfo, _ := host.Info()
	total := memory.Total >> 20
	used := memory.Used >> 20
	return ServerData{
		Users:       users,
		Goroutines:  runtime.NumGoroutine(),
		TotalMemory: total,
		UsedMemory:  used,
		CpuInfo:     cpuInfo[0],
		HostInfo:    hostInfo,
	}
}

type ServerData struct {
	Users       []string    `json:"users,omitempty"`
	Goroutines  int         `json:"goroutines,omitempty"`
	TotalMemory uint64      `json:"totalMemory,omitempty"`
	UsedMemory  uint64      `json:"usedMemory,omitempty"`
	CpuInfo     float64     `json:"cpuInfo,omitempty"`
	HostInfo    interface{} `json:"hostInfo,omitempty"`
}

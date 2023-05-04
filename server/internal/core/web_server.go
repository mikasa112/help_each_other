package core

import (
	"github.com/gorilla/websocket"
	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/host"
	"github.com/shirou/gopsutil/v3/mem"
	"log"
	"net/http"
	"runtime"
	"server/pkg/conf"
	"server/pkg/logger"
	"time"
)

var upgrader = websocket.Upgrader{
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
	if err != nil {
		log.Panicf("WebSocket服务启动失败, 启动路径为：%v，错误为: %v",
			conf.WebConf.Pattern, err)
	}
	defer c.Close()
	Send(c)
}

// Send 周期性发送数据
func Send(conn *websocket.Conn) {
	t := time.NewTicker(10 * time.Second)
	defer t.Stop()
	for range t.C {
		serverInfo := ServerInfo()
		err := conn.WriteJSON(&serverInfo)
		if err != nil {
			logger.Infof("WebSocket Closed，错误为：%v", err)
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

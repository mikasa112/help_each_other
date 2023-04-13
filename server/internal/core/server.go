package core

import (
	"server/pkg/conf"
	"server/pkg/logger"
	"sync"
)

var WG sync.WaitGroup

func New() {
	WG.Add(1)
	server := &NetServer{addr: conf.ServerConf.Addr}
	go server.Start()
	logger.Infoln("服务已启动...")
	WG.Wait()
}

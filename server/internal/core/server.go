package core

import (
	"server/pkg/conf"
	"sync"
)

var WG sync.WaitGroup

func New() {
	WG.Add(1)
	server := &NetServer{addr: conf.ServerConf.Addr}
	go server.Start()
	WG.Wait()
}

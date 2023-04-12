package main

import (
	"log"
	"os"
	"server/internal/protocol"
	"server/pkg/conf"
	"server/pkg/logger"
)

func init() {
	config := conf.NewConfig("./")
	config.ReadConfig()
	logger.New()
	readBanner()
}

func readBanner() {
	file, err := os.ReadFile("pkg/conf/banner.txt")
	if err != nil {
		log.Panicf("banner读取错误，错误为: %v", err)
	}
	log.Printf("\r\n%v\r\n", string(file))
}

func main() {
	bytes := []byte("😊这是一段中文的字符你们好好看看")
	d := protocol.CoverUint16ToBytes(uint16(len(bytes)))
	//core.New()
	data := protocol.Data{
		DataHead:    protocol.DataHead{protocol.PONG, d[0], d[1]},
		DataContent: bytes,
	}
	packet := data.Packet()
	log.Printf("%s", packet)

}

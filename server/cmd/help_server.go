package main

import (
	"log"
	"os"
	"server/internal/core"
	"server/pkg/cache"
	"server/pkg/conf"
	"server/pkg/logger"
)

func init() {
	config := conf.NewConfig("./")
	config.ReadConfig()
	logger.New()
	cache.RDB = cache.New()
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
	core.New()
}

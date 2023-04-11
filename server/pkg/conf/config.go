package conf

import (
	"github.com/spf13/viper"
	"log"
)

type Config struct{}

func NewConfig(path string) *Config {
	viper.SetConfigName("config")
	viper.SetConfigType("yaml")
	viper.AddConfigPath(path)
	err := viper.ReadInConfig()
	if err != nil {
		log.Panicf("日志启动失败，错误为: %v\n", err)
	}
	return &Config{}
}

// ReadConfig 读取配置
func (c *Config) ReadConfig() {
	err := viper.UnmarshalKey("server", &ServerConf)
	if err != nil {
		log.Panicf("日志读取失败，错误为: %v\n", err)
	}
}

var ServerConf *ServerConfig

type ServerConfig struct {
	Addr string
	Mode string
}

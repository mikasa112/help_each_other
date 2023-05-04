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
	err = viper.UnmarshalKey("redis", &RedisConf)
	err = viper.UnmarshalKey("web", &WebConf)
	if err != nil {
		log.Panicf("日志读取失败，错误为: %v\n", err)
	}
}

// ServerConf 服务的配置
var ServerConf *ServerConfig

// RedisConf redis的配置
var RedisConf *RedisConfig

// WebConf websocket的配置
var WebConf *WebConfig

type ServerConfig struct {
	Addr    string
	Mode    string
	Channel string
}

type RedisConfig struct {
	Addr     string
	Password string
	Db       int
}

type WebConfig struct {
	Addr      string
	Pattern   string
	CycleTime int
}

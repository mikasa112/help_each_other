package cache

import (
	"context"
	"github.com/redis/go-redis/v9"
	"server/pkg/conf"
)

var RDB Cache

type Cache struct {
	Client *redis.Client
}

func New() Cache {
	client := redis.NewClient(&redis.Options{
		Addr:     conf.RedisConf.Addr,
		Password: conf.RedisConf.Password,
		DB:       conf.RedisConf.Db,
	})
	return Cache{
		client,
	}
}

// GetString 从redis中获取string
func (cache Cache) GetString(key string) (interface{}, error) {
	data, err := cache.Client.Get(context.Background(), key).Result()
	return data, err
}

// PutString 向redis中存入string
func (cache Cache) PutString(key string, str string) error {
	err := cache.Client.Set(context.Background(), key, str, 0).Err()
	return err
}

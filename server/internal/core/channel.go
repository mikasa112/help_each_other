package core

import (
	"context"
	"github.com/redis/go-redis/v9"
	"server/pkg/cache"
	"server/pkg/conf"
	"server/pkg/logger"
)

// RedisChannel 从redis中的消息队列中阻塞获得数据
func RedisChannel() {
	ctx := context.Background()
	pubsub := cache.RDB.Client.Subscribe(ctx, conf.ServerConf.Channel)
	defer func(pubsub *redis.PubSub) {
		err := pubsub.Close()
		if err != nil {
			logger.Errorf("redis err: %v", err)
		}
	}(pubsub)
	for {
		msg, err := pubsub.ReceiveMessage(ctx)
		if err != nil {
			logger.Errorf("redis err: %v", err)
			continue
		}
		bytes := []byte(msg.Payload)
		Message <- bytes
	}

}

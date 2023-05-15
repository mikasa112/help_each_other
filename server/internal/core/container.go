package core

import (
	"errors"
	"github.com/gorilla/websocket"
	"server/internal"
	"sync"
)

type User struct {
	Uuid string
	//Conn net.Conn
	//2023/5/14修改 为了匹配websocket
	Conn *websocket.Conn
}

var container *Container
var once sync.Once

// Container 线程安全的User容器
type Container struct {
	sync.Mutex
	Users []User
}

// GetContainer 单例的容器
func GetContainer() *Container {
	once.Do(func() {
		container = &Container{
			Mutex: sync.Mutex{},
			Users: make([]User, 0),
		}
	})
	return container
}

func (c *Container) Push(user User) {
	c.Lock()
	c.Users = append(c.Users, user)
	c.Unlock()
}

func (c *Container) Pop(user User) {
	c.Lock()
	var index int
	for k, v := range c.Users {
		if user.Conn == v.Conn {
			index = k
		}
	}
	//2023/5/15 fix 这里会报一个错 暂时这么写
	if len(c.Users) > 0 {
		c.Users = append(c.Users[:index], c.Users[index+1:]...)

	}
	c.Unlock()
}

// 根据uuid获得User
func (c *Container) get(uuid string) *User {
	for _, user := range c.Users {
		if user.Uuid == uuid {
			return &user
		}
	}
	return nil
}

// MatchConn 根据uuid匹配连接
// 2023/5/14 修改 net.Conn
func (c *Container) MatchConn(order internal.Order) (*websocket.Conn, error) {
	var user *User
	switch order.Status {
	//等待顾客同意
	case 0:
		//通知顾客
		user = c.get(order.CustomerUuid)
	//正在进行
	case 1:
		//通知服务提供者
		user = c.get(order.ProviderUuid)
	//已完成
	case 2:
	//异常
	case 3:
	//取消
	case 4:
	}
	//获得顾客的uuid以便通知他
	if user != nil {
		return user.Conn, nil
	}
	return nil, errors.New(`user不存在`)
}

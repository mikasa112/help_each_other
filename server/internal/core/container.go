package core

import (
	"errors"
	"net"
	"server/internal"
	"sync"
)

type User struct {
	Uuid string
	conn net.Conn
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

func (c *Container) push(user User) {
	c.Lock()
	c.Users = append(c.Users, user)
	c.Unlock()
}

func (c *Container) pop(user User) {
	c.Lock()
	var index int
	for k, v := range c.Users {
		if v.conn == user.conn {
			index = k
		}
	}
	c.Users = append(c.Users[:index], c.Users[index+1:]...)
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

// 根据uuid匹配连接
func (c *Container) matchConn(order internal.Order) (net.Conn, error) {
	//获得顾客的uuid以便通知他
	user := c.get(order.CustomerUuid)
	if user != nil {
		return user.conn, nil
	}
	return nil, errors.New(`user不存在`)
}

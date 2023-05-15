package web

const (
	// Register 注册
	Register = iota
	//Ping 心跳
	Ping
)

const (
	ADMIN = iota
	USER
)

// Message websocket 传输协议(自定义)
type Message struct {
	Type     int    `json:"type,omitempty"`
	UserType int    `json:"userType,omitempty"`
	Uuid     string `json:"uuid,omitempty"`
	Msg      string `json:"msg,omitempty"`
}

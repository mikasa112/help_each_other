package protocol

import (
	"bufio"
	"bytes"
	"encoding/binary"
	"fmt"
)

// DataHead
// 第一个字节的高四位表示报文类型，低四位保留
// 第二个字节和第三个字节表示长度
type DataHead [3]byte
type DataContent []byte
type Data struct {
	DataHead
	DataContent
}
type ProtoType byte

const (
	// CONNECT client-->server，连接报文确认
	CONNECT ProtoType = 0x01
	// CONNACK server-->client，连接报文确认
	CONNACK ProtoType = 0x02
	// MESSAGE server-->client,服务端推送消息
	MESSAGE ProtoType = 0x03
	// PING client-->server,心跳请求
	PING ProtoType = 0x0C
	// PONG server-->client,心跳响应
	PONG ProtoType = 0x0D
	// DISCONNECT client-->server,客户端断开连接
	DISCONNECT ProtoType = 0x0E
)

// Bytes 发送数据时左移四位
func (t ProtoType) Bytes() byte {
	b := byte(t)
	return b << 4
}

func (t ProtoType) String() string {
	return fmt.Sprintf("%x", byte(t))
}

// CoverUint16ToBytes 十六位转两个八位
func CoverUint16ToBytes(l uint16) [2]byte {
	var high = byte((l >> 8) & 0xFF)
	var low = byte(l & 0xFF)
	return [2]byte{high, low}
}

// DataType 获取报文头的标志位
func (h *DataHead) DataType() ProtoType {
	bh := h[0]
	highFour := (bh & 0xF0) >> 4
	//lowFour := bh & 0x0f
	return ProtoType(highFour)
}

// DataLen 获取报文头的长度信息
func (h *DataHead) DataLen() uint16 {
	l := h[1:]
	high := uint16(l[0])
	low := uint16(l[1])
	var size = (high << 8) | low
	return size
}

type Protocol interface {
	Packet(w *bufio.Writer) error
	Unpack(r *bufio.Reader) error
}

// Packet 封包
func (d *Data) Packet(w *bufio.Writer) error {
	var pkg = new(bytes.Buffer)
	//小端
	err := binary.Write(pkg, binary.LittleEndian, &d.DataHead)
	err = binary.Write(pkg, binary.LittleEndian, &d.DataContent)
	_, err = w.Write(pkg.Bytes())
	err = w.Flush()
	if err != nil {
		return err
	}
	return nil
}

// Unpack 解包
func (d *Data) Unpack(r *bufio.Reader) error {
	//读取前三个字节为报文固定头
	head, err := r.Peek(3)
	headBuffer := bytes.NewBuffer(head)
	//小端
	err = binary.Read(headBuffer, binary.LittleEndian, &d.DataHead)
	i := make([]byte, 3+d.DataHead.DataLen())
	_, err = r.Read(i)
	d.DataContent = i[3:]
	if err != nil {
		return err
	}
	return nil
}

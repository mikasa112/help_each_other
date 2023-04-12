package protocol

// DataHead
// 第一个字节的高四位表示报文类型，低四位保留
// 第二个字节和第三个字节表示长度
type DataHead [3]byte
type DataContent []byte
type Data struct {
	DataHead
	DataContent
}

const (
	// CONNECT client-->server，连接报文确认
	CONNECT byte = 0x01
	// CONNACK server-->client，连接报文确认
	CONNACK byte = 0x02
	// PING client-->server,心跳请求
	PING byte = 0x0C
	// PONG server-->client,心跳响应
	PONG byte = 0x0D
	// DISCONNECT client-->server,客户端断开连接
	DISCONNECT byte = 0x0E
)

func CoverUint16ToBytes(l uint16) [2]byte {
	var high = byte((l >> 8) & 0xFF)
	var low = byte(l & 0xFF)
	return [2]byte{high, low}
}

// DataType 获取报文头的标志位
func (h *DataHead) DataType() byte {
	bh := h[0]
	highFour := (bh & 0xF0) >> 4
	//lowFour := bh & 0x0f
	return highFour
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
	Packet() []byte
	Unpack([]byte)
}

func (d *Data) Packet() []byte {
	//默认大小
	info := make([]byte, 0)
	info = append(info, d.DataHead[0:]...)
	info = append(info, d.DataContent[0:]...)
	return info
}

func (d *Data) Unpack(data []byte) {
	d.DataHead = DataHead(data[0:3])
	dataLen := d.DataHead.DataLen()
	d.DataContent = data[3:dataLen]
}

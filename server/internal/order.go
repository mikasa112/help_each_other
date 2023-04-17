package internal

type Order struct {
	//顾客的uuid
	CustomerUuid string `json:"customerUuid"`
	//订单uuid
	OrderId int64 `json:"orderId"`
	//服务提供者uuid
	ProviderUuid string `json:"providerUuid"`
	//服务ID
	ServiceId int64 `json:"serviceId"`
	//状态
	Status int `json:"status"`
}

<template xmlns:el-col="http://www.w3.org/1999/html">
    <div class="container">
        <div class="col">
            <div ref="cpuInfo" class="cpu"></div>
            <div ref="memoryInfo" class="memory"></div>
            <div ref="onlineUserInfo" class="onlineUser"></div>
        </div>
    </div>
</template>

<script>
import * as echarts from "echarts"
import {getCurrentUser} from "@/api/api";

export default {
    name: "HomeComponent",
    data() {
        return {
            webSocket: null,
            cpuInfo: null,
            memoryInfo: null,
            onlineUserInfo: null,
            userLen: [],
            dateList: [],
        }
    },
    methods: {
        initWebSocket() {
            //初始化weosocket
            let uri = "ws://192.168.1.113:8082";
            this.webSocket = new WebSocket(uri);
            this.webSocket.onopen = this.onOpen;
            this.webSocket.onmessage = this.onMessage;
            this.webSocket.onclose = this.onClose;
            this.webSocket.onerror = this.onError;
        },
        async onOpen() {
            console.log("websocket连接成功")
            let info = await getCurrentUser()
            const user = info.user
            const data = {
                'type': 0,
                'UserType': 0,
                'uuid': user.uuid,
            }
            //Register
            this.webSocket.send(JSON.stringify(data))
        },
        onMessage(e) {
            const serverInfo = JSON.parse(e.data)
            console.log(serverInfo)
            this.pushList(this.userLen, serverInfo.users.length)
            this.pushList(this.dateList, this.getDateNow())
            serverInfo["cpuInfo"] = Math.round(serverInfo["cpuInfo"])
            this.cpuInfo.setOption({
                title: {
                    text: 'CPU使用情况',
                    left: "center",
                    textStyle: {
                        color: '#CCC',
                        fontFamily: "Microsoft YaHei"
                    }
                },
                series: [
                    {
                        type: 'gauge',
                        progress: {
                            show: true,
                            width: 10
                        },
                        axisLine: {
                            lineStyle: {
                                width: 10
                            }
                        },
                        axisTick: {
                            show: false
                        },
                        splitLine: {
                            length: 10,
                            lineStyle: {
                                width: 2,
                                color: '#999'
                            }
                        },
                        axisLabel: {
                            distance: 20,
                            color: '#999',
                            fontSize: 10
                        },
                        anchor: {
                            show: true,
                            showAbove: true,
                            size: 20,
                            itemStyle: {
                                borderWidth: 5
                            }
                        },
                        title: {
                            show: false
                        },
                        detail: {
                            valueAnimation: true,
                            fontSize: 20,
                            offsetCenter: [0, '70%']
                        },
                        data: [
                            {
                                value: serverInfo["cpuInfo"]
                            }
                        ]
                    }
                ]
            })
            this.memoryInfo.setOption({
                tooltip: {
                    trigger: 'item'
                },
                title: {
                    text: '内存使用情况',
                    left: "center",
                    textStyle: {
                        color: '#CCC',
                        fontFamily: "Microsoft YaHei"
                    }
                },
                series: [
                    {
                        name: '单位MB',
                        type: 'pie',
                        radius: ['40%', '70%'],
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 10,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        label: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: 40,
                                fontWeight: 'bold'
                            }
                        },
                        labelLine: {
                            show: false
                        },
                        data: [
                            {value: serverInfo["totalMemory"], name: 'total'},
                            {value: serverInfo["totalMemory"] - serverInfo["usedMemory"], name: 'free'}
                        ]
                    }
                ]
            })
            this.onlineUserInfo.setOption({
                title: {
                    text: '当前在线用户数量',
                    left: "center",
                    textStyle: {
                        color: '#CCC',
                        fontFamily: "Microsoft YaHei"
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: this.dateList
                },
                yAxis: {
                    min: 0,
                    max: 20,
                    type: 'value',
                    scale: true,
                    minInterval: 1,
                },
                series: [
                    {
                        data: this.userLen,
                        type: 'line',
                        label: {
                            show: true,
                        },
                        areaStyle: {}
                    }
                ]
            })
        },
        onClose() {
            console.log("socket closed")
        },
        onError() {
            this.webSocket.close()
        },
        pushList(list, data) {
            if (list.length > 7) {
                //头删
                list.shift()
            }
            //尾插
            list.push(data)
        },
        getDateNow() {
            let date = new Date();
            let hour = date.getHours();
            let minute = date.getMinutes();
            let second = date.getSeconds();
            return hour + ':' + minute + ':' + second
        }
    },
    mounted() {
        this.initWebSocket()
        let cpuInfo = this.$refs["cpuInfo"];
        let memoryInfo = this.$refs["memoryInfo"]
        let onlineUserInfo = this.$refs["onlineUserInfo"]
        this.cpuInfo = echarts.init(cpuInfo);
        this.memoryInfo = echarts.init(memoryInfo);
        this.onlineUserInfo = echarts.init(onlineUserInfo)
    },
    destroyed() {
        this.webSocket.close()
    }
}
</script>

<style scoped>
.container {
    width: 100%;
    height: 100%;
}

.col {
    display: flex;
    background-color: white;
    width: 100%;
    height: 55%;
}

.cpu {
    padding: 0;
    margin: 0;
    width: 25%;
    height: 100%;
}

.memory {
    padding: 0;
    margin: 0;
    width: 25%;
    height: 100%;
}

.onlineUser {
    padding: 0;
    margin: 0;
    width: 50%;
    height: 100%;
}
</style>
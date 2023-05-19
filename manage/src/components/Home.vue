<template xmlns:el-col="http://www.w3.org/1999/html">
    <el-row>
        <el-col :span="5">
            <div ref="cpuInfo" class="cpu"></div>
        </el-col>
        <el-col :span="5" style="margin-left: 30px">
            <div ref="memoryInfo" class="memory"></div>
        </el-col>
        <el-col :span="12" style="margin-left: 30px">
            <div ref="goroutineInfo" class="goroutines"></div>
        </el-col>
    </el-row>
</template>

<script>
import * as echarts from "echarts"

export default {
    name: "HomeComponent",
    data() {
        return {
            webSocket: null,
            cpuInfo: null,
            memoryInfo: null,
        }
    },
    methods: {
        initWebSocket() {
            //初始化weosocket
            let uri = "ws://192.168.1.106:8082";
            this.webSocket = new WebSocket(uri);
            this.webSocket.onopen = this.onOpen;
            this.webSocket.onmessage = this.onMessage;
            this.webSocket.onclose = this.onClose;
            this.webSocket.onerror = this.onError;
        },
        onOpen() {
            console.log("websocket连接成功")
        },
        onMessage(e) {
            const serverInfo = JSON.parse(e.data)
            console.log(serverInfo)
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
        },
        onClose() {
            console.log("socket closed")
        },
        onError() {
            this.webSocket.close()
        }
    },
    mounted() {
        // this.initWebSocket()
        let cpuInfo = this.$refs["cpuInfo"];
        let memoryInfo = this.$refs["memoryInfo"]
        this.cpuInfo = echarts.init(cpuInfo);
        this.memoryInfo = echarts.init(memoryInfo);
    },
    destroyed() {
        this.webSocket.close()
    }
}
</script>

<style scoped>
.cpu {
    padding: 0;
    margin: 0;
    width: 300px;
    height: 300px;
    background-color: #fff;
}

.memory {
    padding: 0;
    margin: 0;
    width: 300px;
    height: 300px;
    background-color: #fff;
}

.goroutines {
    padding: 0;
    margin: 0;
    width: 743px;
    height: 300px;
    background-color: #fff;
}
</style>
基于腾讯地图的H5端选择地图组件(pc、移动端均可使用)

##使用说明  

- 使用前需在index.html页面引入jquery和腾讯地图的api库

``` 
<script src="https://map.qq.com/api/gljs?v=1.exp&key=yourmapkey"></script>
```


在页面中使用组件  
``` 
<Flame-chooseLocation v-model="show" mapkey="yourmapkey"></Flame-chooseLocation>
```


##属性说明  

- @property {Boolean} value = [false|true] 控制组件显示或隐藏
- @property {Boolean} exact = [false|true] 是否开启移动端精确定位，开启后请在手机端打开，pc端打开会出现长时间白屏
- @property {String} mapkey = '' 腾讯地图开放平台申请的key
- @event {Function} confirm 确认选择, 返回选择的位置信息

	
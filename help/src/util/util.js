/**
 * 计算当前位置和目标位置的距离，返回距离和距离单位
 * @param {Object} lat1 当前的纬度
 * @param {Object} lng1 当前的经度
 * @param {Object} lat2 目标的纬度
 * @param {Object} lng2 目标的经度
 */
export default function getDistance(lat1, lng1, lat2, lng2) {

	var radLat1 = rad(lat1);
	var radLat2 = rad(lat2);
	var a = radLat1 - radLat2;
	var b = rad(lng1) - rad(lng2);
	var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
		Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
	s = s * 6378.137; // EARTH_RADIUS;
	// 输出为公里
	s = Math.round(s * 10000) / 10000;
	var distance = s;
	var distance_str = "";
	if (parseInt(distance) >= 1) {
		distance_str = distance.toFixed(2) + "km";
	} else {
		distance_str = (distance * 1000).toFixed(2) + "m";
	}
	let objData = {
		distance: distance,
		distance_str: distance_str
	}
	return objData
}

function rad(d) {
	return d * Math.PI / 180.0;
}
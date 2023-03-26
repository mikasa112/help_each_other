package com.help.each.core.vo;

import com.alibaba.fastjson2.JSONObject;
import com.help.each.core.constant.Status;
import com.help.each.core.exception.BaseException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 封装响应数据
 */
@Data
@Builder
public class ApiResponse implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    private ApiResponse() {
    }

    private ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse Of(Integer code, String message, Object data) {
        return new ApiResponse(code, message, data);
    }

    /**
     * 返回成功
     *
     * @param data 鸭子
     * @return {@link ApiResponse}
     */
    public static ApiResponse OfSuccess(Object data) {
        return OfStatus(Status.OK, data);
    }

    /**
     * 返回消息
     *
     * @param message 消息
     * @return {@link ApiResponse}
     */
    public static ApiResponse OfMessage(String message) {
        return Of(Status.OK.getCode(), message, null);
    }

    public static ApiResponse OfStatus(Status status) {
        return OfStatus(status, null);
    }

    public static ApiResponse OfStatus(Status status, Object data) {
        return Of(status.getCode(), status.getMessage(), data);
    }

    public static <T extends BaseException> ApiResponse OfException(T t, Object data) {
        return Of(t.getCode(), t.getMessage(), data);
    }

    public static <T extends BaseException> ApiResponse OfException(T t) {
        return OfException(t, null);
    }


    /**
     * 输出序列化的${ApiResponse}from http response and Status
     *
     * @param response 响应
     * @param status   ${Status}
     * @throws IOException io异常
     */
    public static void PrintlnApiResponse(HttpServletResponse response, Status status) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(200);
        ApiResponse apiResponse = ApiResponse.OfStatus(status);
        String json = JSONObject.toJSONString(apiResponse);
        response.getWriter().println(json);
        response.getWriter().flush();
    }

    /**
     * 输出序列化的${ApiResponse}from http response and BaseException
     *
     * @param response 响应
     * @param e        {@code BaseException}
     * @throws IOException io异常
     */
    public static void PrintlnApiResponse(HttpServletResponse response, BaseException e) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(200);
        ApiResponse apiResponse = ApiResponse.OfException(e);
        String json = JSONObject.toJSONString(apiResponse);
        response.getWriter().println(json);
        response.getWriter().flush();
    }

    /**
     * 根据service返回的状态去选择响应
     *
     * @param bool      service返回的状态
     * @param sucMsg    消息
     * @param errStatus 失败时的状态
     */
    public static ApiResponse PrintlnApiResponse(boolean bool, String sucMsg, Status errStatus) {
        if (bool)
            return OfMessage(sucMsg);
        return OfStatus(errStatus);
    }
}

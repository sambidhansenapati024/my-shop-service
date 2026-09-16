package com.myShop.my_shop_service.dto.auth;


public class ApiResponse<T> {

    private String status;
    private int code;
    private String message;
    private T data;

    // Default constructor
    public ApiResponse() {
    }

    // Full constructor
    public ApiResponse(String status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Constructor without data
    public ApiResponse(String status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = null;
    }

    // Success response with data
    public static <T> ApiResponse<T> success(
            int code,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                "SUCCESS",
                code,
                message,
                data
        );
    }

    // Success response without data
    public static <T> ApiResponse<T> success(
            int code,
            String message
    ) {
        return new ApiResponse<>(
                "SUCCESS",
                code,
                message,
                null
        );
    }

    // Error response
    public static <T> ApiResponse<T> error(
            int code,
            String message
    ) {
        return new ApiResponse<>(
                "ERROR",
                code,
                message,
                null
        );
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.chesstournament.dto;

public class ResponseBusta<T> {

    private Integer status;
    private String messaggio;
    private T data;

    public ResponseBusta() {
    }

    public ResponseBusta(Integer status, String messaggio, T data) {
        this.status = status;
        this.messaggio = messaggio;
        this.data = data;
    }

    public static <T> ResponseBusta<T> of(String messaggio, T data) {
        return new ResponseBusta<>(null, messaggio, data);
    }

    public static <T> ResponseBusta<T> success(int status, String messaggio, T data) {
        return new ResponseBusta<>(status, messaggio, data);
    }

    public static <T> ResponseBusta<T> error(int status, String messaggio) {
        return new ResponseBusta<>(status, messaggio, null);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

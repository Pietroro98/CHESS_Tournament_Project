package com.chesstournament.dto;

public class ResponseBusta<T> {

    private String messaggio;
    private T data;

    public ResponseBusta() {
    }

    public ResponseBusta(String messaggio, T data) {
        this.messaggio = messaggio;
        this.data = data;
    }

    public static <T> ResponseBusta<T> of(String messaggio, T data) {
        return new ResponseBusta<>(messaggio, data);
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

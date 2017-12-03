package com.darkblue.rpcimpl.test;

import java.io.Serializable;

public class Message implements Serializable{
    private Long id;
    private String info;
    private Object other;

    public Message() {
    }

    public Message(Long id, String info) {
        this.id = id;
        this.info = info;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", info='" + info + '\'' +
                '}';
    }
}

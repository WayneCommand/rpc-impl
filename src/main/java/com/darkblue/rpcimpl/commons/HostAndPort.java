package com.darkblue.rpcimpl.commons;

import java.io.Serializable;

/**
 * 主机和端口
 */
public class HostAndPort implements Serializable{
    private String host;
    private short port;

    public HostAndPort() {
    }

    public HostAndPort(String host, short port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }
}

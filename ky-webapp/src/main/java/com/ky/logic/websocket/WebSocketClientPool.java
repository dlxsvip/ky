package com.ky.logic.websocket;

import org.jboss.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yl on 2017/7/20.
 * @author yl
 */
public class WebSocketClientPool {

    //连接池，用来保存每个客户端连接的对象
    private static List<ChannelHandlerContext> clientList = new ArrayList<ChannelHandlerContext>();

    public static List<ChannelHandlerContext> getClients() {
        return clientList;
    }

}

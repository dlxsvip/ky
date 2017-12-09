package com.ky.logic.websocket;

import com.ky.logic.utils.LoggerUtil;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 初始出化 webSocket 服务
 *
 * @author yl
 * @create 2016-07-20
 * @since 1.0
 */
@Component("initWebSocket")
public class InitWebSocket implements InitializingBean {

    private Integer webSocketPort;


    private String webSocketUrl;

    public static ChannelGroup recipients = new DefaultChannelGroup();

    @Override
    public void afterPropertiesSet() throws Exception {
        startWebSocketService();
    }



    private void startWebSocketService() {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new WebSocketServerPipelineFactory());

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(webSocketPort));

        LoggerUtil.debugSysLog(this.getClass().getName(), "startWebSocketService", "webSocketService:" + this.webSocketUrl);
    }


    @Autowired
    public void setWebSocketPort(@Value("${web.socket.server.port}") Integer webSocketPort) {
        this.webSocketPort = webSocketPort;
    }



    @Autowired
    public void setWebSocketUrl(@Value("${web.socket.server.url}") String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
    }

    public String getWebSocketUrl() {
        return webSocketUrl;
    }
}

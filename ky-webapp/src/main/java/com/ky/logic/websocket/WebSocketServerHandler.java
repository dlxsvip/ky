package com.ky.logic.websocket;

import com.ky.logic.common.cache.AlarmPushCache;
import com.ky.logic.utils.LoggerUtil;
import com.ky.logic.utils.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.websocketx.*;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.util.CharsetUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.setContentLength;
import static org.jboss.netty.handler.codec.http.HttpMethod.GET;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * webSocket 请求处理
 *
 * @author yl
 * @create 2016-07-20
 * @since 1.0
 */
@Component("webSocketServerHandler")
public class WebSocketServerHandler extends SimpleChannelUpstreamHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketServerHandler.class);
    public Logger log = Logger.getLogger(this.getClass());

    private WebSocketServerHandshaker handshaker;

    @Resource(name = "initWebSocket")
    private InitWebSocket initWebSocket;

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        WebSocketClientPool.getClients().add(ctx);
        log.debug("进来一个channel：" + ctx.getChannel().getId());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        WebSocketClientPool.getClients().remove(ctx);
        log.error("关掉一个channel：" + ctx.getChannel().getId());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (msg instanceof HttpRequest) {
            handleHttpRequest(ctx, (HttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }


    // 广播消息
    public Boolean sendMessageToClients(Map<String, Object> request) {
        List<ChannelHandlerContext> clientList = WebSocketClientPool.getClients();
        for (ChannelHandlerContext client : clientList) {
            JSONObject result = new JSONObject(request);
            client.getChannel().write(new TextWebSocketFrame(result.toString()));
            LoggerUtil.debugSysLog(this.getClass().getName(), "sendMessageToClients",
                    "广播消息给:" + client.getChannel().getRemoteAddress() + ",msg:" + result.toString());
        }

        return true;
    }

    // 广播消息
    public Boolean sendMessageToClients(ChannelHandlerContext client) {
        Map<String, Map<String, Object>> alarmCache = AlarmPushCache.getAlarmCache();
        Set<Map.Entry<String,Map<String, Object>>> s = alarmCache.entrySet();
        for (Map.Entry<String,Map<String, Object>> entry : s) {
            String id = entry.getKey();
            JSONObject result = new JSONObject(entry.getValue());
            client.getChannel().write(new TextWebSocketFrame(result.toString()));
            LoggerUtil.debugSysLog(this.getClass().getName(), "sendMessageToClients",
                    "广播消息给:" + client.getChannel().getRemoteAddress() + ",msg:" + result.toString());

        }

        return true;
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        // Allow only GET methods.
        if (req.getMethod() != GET) {
            sendHttpResponse(ctx, req, new DefaultHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

        // Send the demo page and favicon.ico
        if (req.getUri().equals("/")) {
            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, OK);

            ChannelBuffer content = WebSocketServerIndexPage.getContent(getWebSocketLocation(req));

            res.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");
            setContentLength(res, content.readableBytes());

            res.setContent(content);
            sendHttpResponse(ctx, req, res);
            return;
        } else if (req.getUri().equals("/favicon.ico")) {
            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, NOT_FOUND);
            sendHttpResponse(ctx, req, res);
            return;
        }

        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                this.getWebSocketLocation(req), null, false);
        this.handshaker = wsFactory.newHandshaker(req);
        if (this.handshaker == null) {
            wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.getChannel());
        } else {
            this.handshaker.handshake(ctx.getChannel(), req);
            InitWebSocket.recipients.add(ctx.getChannel());
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // Check for closing frame
        if (frame instanceof CloseWebSocketFrame) {
            this.handshaker.close(ctx.getChannel(), (CloseWebSocketFrame) frame);

            return;
        } else if (frame instanceof PingWebSocketFrame) {
            ctx.getChannel().write(new PongWebSocketFrame(frame.getBinaryData()));
            return;
        } else if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
                    .getName()));
        }

        // Send the uppercase string back.
        String request = ((TextWebSocketFrame) frame).getText();
        logger.debug(String.format("Channel %s received %s", ctx.getChannel().getId(), request));


        JSONObject obj = new JSONObject(request);
        String method = obj.getString("method");
        if (StringUtils.equals("keyList", method)) {
            sendMessageToClients(ctx);
        } else if (StringUtils.equals("test", method)) {
            String param = obj.getString("param");
            ctx.getChannel().write(new TextWebSocketFrame("服务器接收到的消息："+param));
        } else {
            ctx.getChannel().write(new TextWebSocketFrame("不支持此方法"));
        }

    }

    private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        // Generate an error page if response status code is not OK (200).
        if (res.getStatus().getCode() != 200) {
            res.setContent(ChannelBuffers.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
            setContentLength(res, res.getContent().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.getChannel().write(res);
        if (!isKeepAlive(req) || res.getStatus().getCode() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    private String getWebSocketLocation(HttpRequest req) {
        if (null == initWebSocket) {
            initWebSocket = (InitWebSocket) SpringContextUtil.getBean("initWebSocket");
        }

        return null != initWebSocket ? initWebSocket.getWebSocketUrl() : "ws://" + req.getHeader(HttpHeaders.Names.HOST) + "websocket";
    }
}
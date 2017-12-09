package com.ky.logic.common.init;

import com.ky.logic.common.job.UdpServerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by yl on 2017/8/8.
 */
@Component
public class InitUdpServer {

    private Boolean open;
    private Integer udpServerPort;

    @Autowired
    public void setOpen(@Value("${api.udp.server.open}") Boolean open) {
        this.open = open;
    }

    @Autowired
    public void setUdpServerPort(@Value("${api.udp.server.port}") Integer udpServerPort) {
        this.udpServerPort = udpServerPort;
    }

    @PostConstruct
    public void init() {
        if (open) {
            UdpServerJob udpServer = new UdpServerJob(udpServerPort);
            Thread t = new Thread(udpServer);
            t.start();
        }
    }


    @PreDestroy
    public void destroy() {

    }

}

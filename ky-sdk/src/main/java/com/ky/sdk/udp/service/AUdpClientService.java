package com.ky.sdk.udp.service;


import com.ky.sdk.udp.client.ManageUdpClient;
import com.ky.sdk.utils.UdpUtil;

/**
 * Created by yl on 2017/7/22.
 */
public class AUdpClientService {

    private ManageUdpClient client;

    public AUdpClientService(ManageUdpClient client) {
        this.client = client;
    }

    public String doSend(byte[] data) {
        return UdpUtil.INSTANCE.send(client.getIp(), client.getPort(), data);
    }

}

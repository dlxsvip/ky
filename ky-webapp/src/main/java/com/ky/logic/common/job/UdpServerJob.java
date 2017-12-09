package com.ky.logic.common.job;


import com.ky.logic.utils.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by yl on 2017/8/9.
 */
public class UdpServerJob implements Runnable {

    private Integer udpServerPort;

    public UdpServerJob(Integer udpServerPort) {
        this.udpServerPort = udpServerPort;
    }

    @Override
    public void run() {
        udpServer(udpServerPort);
    }


    public void udpServer(int serverPort) {
        try {
            //1.创建服务器端DatagramSocket,指定端口
            DatagramSocket socket = new DatagramSocket(serverPort);

            //2.创建数据报，用于接收客户端发送的数据
            byte[] data = new byte[1024];//创建字节数组，指定接收的数据包的大小。
            DatagramPacket packet = new DatagramPacket(data, data.length);


            System.out.println("****udp服务器端已经启动，等待人脸识别端发送信息****");

            //3.接收客户端发送的数据
            while (true) {
                socket.receive(packet);//此方法在接收到数据报之前会一直阻塞

                // ByteArrayOutputStream bytOs =
                //DataInputStream ds =
                //ds.readInt()

                byte[] buff = packet.getData();
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(buff));


                Thread t = new Thread(new DataProcessor(in));
                t.start();


                String info = new String(buff, "UTF-8");
                //System.out.println("Server:" + info);
                LoggerUtil.debugSysLog(UdpServerJob.class.getName(), "udp服务接收消息：", info);

                /**
                 * 向客户端响应数据
                 */
                //1.定义客户端的地址、端口号，数据
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                byte[] data2 = "Receive OK!".getBytes();
                //把数据发送给客户端
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
                socket.send(packet2);
                System.out.println("Server:Send OK!");
            }
        } catch (SocketException e) {
            System.out.println("open socket 异常");
            LoggerUtil.errorSysLog(UdpServerJob.class.getName(), "open socket 异常：", e.getMessage());
        } catch (IOException e) {
            System.out.println("接收数据 异常");
            LoggerUtil.errorSysLog(UdpServerJob.class.getName(), "接收数据 异常：", e.getMessage());
        }
    }


}

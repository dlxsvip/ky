package com.ky.sdk.utils;

import java.io.IOException;
import java.net.*;

/**
 * Created by yl on 2017/8/8.
 */
public enum UdpUtil {

    INSTANCE;


    /**
     * udp 客户端
     * 只发送 不接收（不管服务端是否在线）
     *
     * @param ip   udp服务 ip
     * @param port udp服务 端口
     * @param data 发送的消息
     * @return 是否发送成功
     */
    public String send(String ip, int port, byte[] data) {
        String result = "";
        try {
            //1.定义服务器的地址
            InetAddress address = InetAddress.getByName(ip);

            //2.创建数据报，包含发送的数据信息
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);


            //3.创建DatagramSocket 对象
            DatagramSocket socket = new DatagramSocket();

            //4.向服务器端发送数据报
            socket.send(packet);

            //4.关闭资源
            socket.close();

            return "Send OK";
        } catch (UnknownHostException e) {
            System.out.println("主机地址不可用");
        } catch (SocketException e) {
            System.out.println("建立socket 异常");
        } catch (IOException e) {
            System.out.println("发送数据 异常");
        }

        return result;
    }


    /**
     * udp 客户端
     * 发送并等待 服务端响应结果
     *
     * @param ip   udp服务 ip
     * @param port udp服务 端口
     * @param data 发送的消息
     * @return 服务端响应结果
     */
    public String sendAndReceive(String ip, int port, byte[] data) {
        String result = "";
        try {
            //1.定义服务器的地址
            InetAddress address = InetAddress.getByName(ip);

            //2.创建数据报，包含发送的数据信息
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);


            //3.创建DatagramSocket 对象
            DatagramSocket socket = new DatagramSocket();

            //4.向服务器端发送数据报
            socket.send(packet);
            System.out.println("Client:Send OK!");

            /**
             * 接收服务器端响应的数据
             */
            //1.创建数据报，用于接收服务器端响应的数据
            byte[] data2 = new byte[1024];
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
            //2.接收服务器响应的数据
            socket.receive(packet2);
            //3.读取数据
            String reply = new String(data2, 0, packet2.getLength());
            System.out.println("Client:" + reply);


            //4.关闭资源
            socket.close();

            return reply;
        } catch (UnknownHostException e) {
            System.out.println("主机地址不可用");
        } catch (SocketException e) {
            System.out.println("建立socket 异常");
        } catch (IOException e) {
            System.out.println("发送数据 异常");
        }

        return result;
    }


    /**
     * udp 服务
     *
     * @param serverPort 服务端口
     */
    public void receive(int serverPort) {
        //1.创建服务器端DatagramSocket,指定端口
        try {
            DatagramSocket socket = new DatagramSocket(serverPort);

            //2.创建数据报，用于接收客户端发送的数据
            byte[] data = new byte[1024];//创建字节数组，指定接收的数据包的大小。
            DatagramPacket packet = new DatagramPacket(data, data.length);
            //3.接收客户端发送的数据
            System.out.println("****服务器端已经启动，等待客户端发送信息");
            socket.receive(packet);//此方法在接收到数据报之前会一直阻塞

            //4.读取数据
            String info = new String(data, 0, packet.getLength());
            System.out.println("我是服务器，客户端说：" + info);


            /**
             * 向客户端响应数据
             */
            //1.定义客户端的地址、端口号，数据
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            byte[] data2 = "欢迎您！".getBytes();
            //把数据发送给客户端
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
            socket.send(packet2);
            //4.关闭socket
            socket.close();
        } catch (SocketException e) {
            System.out.println("建立socket 异常");
        } catch (IOException e) {
            System.out.println("接收数据 异常");
        }
    }

}

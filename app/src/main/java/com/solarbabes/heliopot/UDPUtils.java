package com.solarbabes.heliopot;

import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPUtils implements Runnable {
    private static String SERVER_IP;
    private static int SERVER_PORT;
    private String receive;

    public UDPUtils(String Server_IP, int Server_Port) {
        SERVER_IP = Server_IP;
        SERVER_PORT = Server_Port;
    }

    public void sendControInfo(String message1, String message2){
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] configInfo = message1.getBytes();
            InetAddress ip = InetAddress.getByName(SERVER_IP);
            DatagramPacket sendPacket = new DatagramPacket(configInfo, configInfo.length, ip ,SERVER_PORT);
            socket.send(sendPacket);

            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            packet.setData(data);
            socket.receive(packet);
            receive = new String(packet.getData(), 0, packet.getLength());
            Log.d("recv",receive);

            byte[] configInfo1 = message2.getBytes();
            DatagramPacket sendPacket1 = new DatagramPacket(configInfo1, configInfo1.length, ip ,SERVER_PORT);
            socket.send(sendPacket1);

            packet.setData(data);
            socket.receive(packet);
            receive = new String(packet.getData(), 0, packet.getLength());
            Log.d("recv",receive);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Log.d("udp","start");
    }
}
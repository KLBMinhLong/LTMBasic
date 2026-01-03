/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai6_03;

import java.net.*;
import java.util.*;

public class ChatClass {
    private static final int SERVER_PORT = 6334;
    private static Map<String, ClientInfo> clients = new HashMap<>();
    private static DatagramSocket serverSocket;
    
    // Class lưu thông tin client
    static class ClientInfo {
        InetAddress address;
        int port;
        
        ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("UDP Chat Server đang khởi động...");
        System.out.println("Server lắng nghe trên port: " + SERVER_PORT);
        
        try {
            serverSocket = new DatagramSocket(SERVER_PORT);
            byte[] receiveData = new byte[1024];
            
            while (true) {
                // Nhận packet từ client
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                
                String fullMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientAddress = receivePacket.getAddress();
                
                // Parse tin nhắn để lấy port và nội dung
                String message = fullMessage;
                int clientPort = 0;
                
                if (fullMessage.startsWith("PORT:")) {
                    int pipeIndex = fullMessage.indexOf("|");
                    String portStr = fullMessage.substring(5, pipeIndex);
                    clientPort = Integer.parseInt(portStr);
                    message = fullMessage.substring(pipeIndex + 1);
                    
                    // Lưu thông tin client
                    String clientKey = clientAddress.getHostAddress();
                    if (!clients.containsKey(clientKey) || clients.get(clientKey).port != clientPort) {
                        clients.put(clientKey, new ClientInfo(clientAddress, clientPort));
                        System.out.println("Client: " + clientKey + ":" + clientPort);
                    }
                }
                
                System.out.println("Nhận: " + message);
                
                // Broadcast tin nhắn đến tất cả client
                byte[] sendData = message.getBytes();
                for (ClientInfo client : clients.values()) {
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(
                            sendData, 
                            sendData.length, 
                            client.address, 
                            client.port
                        );
                        serverSocket.send(sendPacket);
                    } catch (Exception e) {
                        System.err.println("Lỗi khi gửi đến " + client.address.getHostAddress());
                    }
                }
                
                // Reset buffer
                receiveData = new byte[1024];
            }
        } catch (Exception e) {
            System.err.println("Lỗi server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}

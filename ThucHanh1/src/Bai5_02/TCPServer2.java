/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai5_02;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author LENOVO
 */
public class TCPServer2 {
    static final int PORT = 2222;
    private ServerSocket server = null;

    public TCPServer2() {
        try {
            server = new ServerSocket(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void action() {
        Socket socket = null;
        int i = 0;
        System.out.println("Server Listening...");
        try {
            while ((socket = server.accept()) != null) {
                new ServerThread(socket, "Client#" + i);
                System.out.printf("Thread for Client %d generating...\n", i++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TCPServer2().action();
    }
}




import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

public class TempServer3 {
    private static final int PORT = 9888;
    private static HashMap<String, Integer> temperatureData = new HashMap<>();

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            byte[] receiveData = new byte[1024];

            // Hiển thị địa chỉ IP của máy chủ
            displayServerIPAddress();

            System.out.println("UDP Server đang lắng nghe...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                // Đọc đúng độ dài gói + charset rõ ràng
                String message = new String(
                        receivePacket.getData(),
                        0,
                        receivePacket.getLength(),
                        StandardCharsets.UTF_8
                ).trim();
                //String message = new String(receivePacket.getData()).trim();
                String[] data = message.split(":");
                if (data.length == 4 && data[0].equals("Temperature")) {
                    String location = data[1];
                    int temperature = Integer.parseInt(data[2]);
                    long crc = Long.parseLong(data[3]);

                    // Kiểm tra tính toàn vẹn của gói tin
                    long calculatedCrc = Crc32_Test.getCRC32Checksum("Temperature:"+ location + ":" + temperature);
                    if (calculatedCrc == crc) {
                        String clientAddress = receivePacket.getAddress().getHostAddress();
                        temperatureData.put(location + "(" + clientAddress + ")", temperature);
                        // Kiểm tra nhiệt độ vượt ngưỡng cảnh báo
                        checkTemperatureThreshold(location, clientAddress, temperature);
                        }
                    } else {
                        System.out.println("Gói tin bị hỏng! Không thể lưu dữ liệu.");
                    }

                // Hiển thị dữ liệu nhiệt độ từ tất cả các thiết bị IoT đã kết nối
                displayTemperatureData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayServerIPAddress() {
        try {
            // Lấy địa chỉ Local IP của máy chủ
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // Lọc và tìm địa chỉ IP của giao diện Local (không phải loopback)
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        String serverIP = inetAddress.getHostAddress();
                        System.out.println("Địa chỉ IP của Server: " + serverIP);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static void displayTemperatureData() {
        System.out.println("Dữ liệu nhiệt độ:");
        for (String location : temperatureData.keySet()) {
            int temperature = temperatureData.get(location);
            System.out.println("Địa điểm " + location + ": " + temperature + " độ C");
        }
    }

    private static void checkTemperatureThreshold(String location, String clientAddress, int temperature) {
        if (temperature < 0 || temperature > 35) {
            String alertMessage = "Cảnh báo nhiệt độ vượt ngưỡng tại Địa điểm " + location + " (" + clientAddress + "): "
                    + temperature + " độ C vào lúc " + getCurrentTime();
            System.out.println(alertMessage);
            saveAlertToLogFile(alertMessage);
        }
    }

    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
    private static void saveAlertToLogFile(String alertMessage) {
        try {
            FileWriter fileWriter = new FileWriter("alerts_logs_3.txt", true);
            fileWriter.write(alertMessage + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





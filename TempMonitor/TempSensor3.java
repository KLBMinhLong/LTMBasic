

import java.net.*;
import java.util.Scanner;

public class TempSensor3 {
    private static final int PORT = 9888;
    private static String SERVER_IP; // Địa chỉ IP của máy chủ
    private static String location; // Địa điểm đo nhiệt độ

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập địa chỉ IP của máy chủ: ");
        SERVER_IP = scanner.nextLine();

        System.out.print("Nhập địa điểm đo nhiệt độ: ");
        location = scanner.nextLine();

        try {
            DatagramSocket clientSocket = new DatagramSocket();

            while (true) {
                int temperature = readTemperatureFromSensor(); // Hàm này giả lập đọc nhiệt độ từ cảm biến
                String message = "Temperature:" + location + ":" + temperature;

                // Thêm mã CRC vào gói tin
                long crc = Crc32_Test.getCRC32Checksum(message);
                message += ":" + crc;

                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        InetAddress.getByName(SERVER_IP), PORT);
                clientSocket.send(sendPacket);

                Thread.sleep(5000); // Gửi dữ liệu mỗi 5 giây
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int readTemperatureFromSensor() {
        // Giả lập đọc nhiệt độ từ cảm biến
        return (int) (Math.random() * 55) - 10;
    }
}


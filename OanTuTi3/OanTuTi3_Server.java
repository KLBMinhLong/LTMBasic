import java.io.*;
import java.net.*;
import java.util.*;

public class OanTuTi3_Server {
    private static final int PORT = 1238;
    private static final int NUM_PLAYERS = 3;
    
    private static ServerSocket serverSocket;
    private static List<PlayerHandler> players = new ArrayList<>();
    
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║   OẢN TU TÌ 3 NGƯỜI - SERVER KHỞI ĐỘNG    ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println("Đang lắng nghe trên cổng: " + PORT);
            
            displayServerIPAddress();
            
            // Chấp nhận 3 kết nối từ client và cho phép nhập tên song song
            for (int i = 0; i < NUM_PLAYERS; i++) {
                Socket socket = serverSocket.accept();
                PlayerHandler player = new PlayerHandler(socket, i + 1);
                players.add(player);
                System.out.println("✓ Người chơi " + (i + 1) + " đã kết nối! [" + socket.getInetAddress() + "]");
                
                // Gửi thông báo chào mừng ngay lập tức
                player.sendMessage("╔════════════════════════════════════════════╗");
                player.sendMessage("║        CHÀO MỪNG ĐẾN OẢN TU TÌ 3!        ║");
                player.sendMessage("╚════════════════════════════════════════════╝");
                player.sendMessage("NHẬP_TÊN");
                
                // Thông báo cho các người chơi đã kết nối trước
                if (i > 0) {
                    String joinMsg = "→ Người chơi " + (i + 1) + " đã tham gia! (Đang đợi " + (NUM_PLAYERS - i - 1) + " người nữa...)";
                    for (int j = 0; j < i; j++) {
                        players.get(j).sendMessage(joinMsg);
                    }
                }
            }
            
            // Thông báo đã đủ người
            broadcastMessage("\n✓ Đã đủ 3 người chơi! Đang chờ mọi người nhập tên...\n");
            
            // Thu thập tên từ tất cả người chơi (song song, không phụ thuộc thứ tự)
            boolean[] hasName = new boolean[NUM_PLAYERS];
            int namedPlayers = 0;
            
            // Tạo thread để đọc tên từ mỗi client độc lập
            Thread[] nameThreads = new Thread[NUM_PLAYERS];
            for (int i = 0; i < NUM_PLAYERS; i++) {
                final int playerIndex = i;
                nameThreads[i] = new Thread(() -> {
                    String name = players.get(playerIndex).readMessage();
                    
                    if (name == null || name.trim().isEmpty()) {
                        name = "Người chơi " + (playerIndex + 1);
                    }
                    
                    players.get(playerIndex).setName(name.trim());
                    
                    synchronized (hasName) {
                        hasName[playerIndex] = true;
                        int count = 0;
                        for (boolean b : hasName) {
                            if (b) count++;
                        }
                        
                        System.out.println("→ Người chơi " + (playerIndex + 1) + " đặt tên: " + name);
                        
                        // Thông báo cho các người chơi khác
                        String nameMsg = "→ " + name + " đã sẵn sàng! (" + count + "/" + NUM_PLAYERS + ")";
                        for (int j = 0; j < NUM_PLAYERS; j++) {
                            if (j != playerIndex) {
                                players.get(j).sendMessage(nameMsg);
                            } else {
                                players.get(j).sendMessage("✓ Tên của bạn: " + name);
                            }
                        }
                    }
                });
                nameThreads[i].start();
            }
            
            // Đợi tất cả thread hoàn thành
            for (Thread t : nameThreads) {
                t.join();
            }
            
            // Thông báo danh sách người chơi cho tất cả
            Thread.sleep(500); // Delay nhỏ để đảm bảo tất cả đã nhận tin
            String playerList = "\n╔════════════════════════════════════════════╗\n";
            playerList += "║         DANH SÁCH NGƯỜI CHƠI              ║\n";
            playerList += "╚════════════════════════════════════════════╝\n";
            for (int i = 0; i < NUM_PLAYERS; i++) {
                playerList += "  " + (i + 1) + ". " + players.get(i).getName() + "\n";
            }
            playerList += "\n→ Trò chơi bắt đầu!\n";
            broadcastMessage(playerList);
            System.out.println("\n" + playerList);
            
            // Vòng lặp chính của trò chơi
            int round = 1;
            while (true) {
                System.out.println("\n════════ VÁN CHƠI " + round + " ════════");
                broadcastMessage("\n════════ VÁN CHƠI " + round + " ════════");
                broadcastMessage("BẮT_ĐẦU");
                
                // Thu thập lựa chọn từ 3 người chơi
                String[] choices = new String[NUM_PLAYERS];
                for (int i = 0; i < NUM_PLAYERS; i++) {
                    String choice = players.get(i).readMessage();
                    
                    // Validate choice
                    if (choice == null) {
                        System.out.println("✗ " + players.get(i).getName() + " đã mất kết nối!");
                        cleanup();
                        return;
                    }
                    
                    choice = choice.trim().toUpperCase();
                    if (!isValidChoice(choice)) {
                        choice = "BÚA"; // Default
                    }
                    
                    choices[i] = choice;
                    System.out.println("→ " + players.get(i).getName() + " đã chọn: " + choice);
                }
                
                // Xác định kết quả
                GameResult result = determineResult(choices);
                
                // Cập nhật điểm
                for (int i = 0; i < NUM_PLAYERS; i++) {
                    players.get(i).addScore(result.scores[i]);
                }
                
                // Tạo thông báo kết quả
                String resultMessage = formatResult(choices, result);
                System.out.println("\n" + resultMessage);
                broadcastMessage(resultMessage);
                
                // Gửi bảng điểm
                String scoreBoard = getScoreBoard();
                System.out.println(scoreBoard);
                broadcastMessage(scoreBoard);
                
                // Hỏi chơi tiếp - Dừng ngay khi có người chọn N
                broadcastMessage("CHƠI_TIẾP");
                boolean continueGame = true;
                boolean[] hasResponded = new boolean[NUM_PLAYERS];
                int respondedCount = 0;
                
                // Tạo thread để đọc response từ mỗi client độc lập
                Thread[] responseThreads = new Thread[NUM_PLAYERS];
                final boolean[] stopAsking = {false};
                
                for (int i = 0; i < NUM_PLAYERS; i++) {
                    final int playerIndex = i;
                    responseThreads[i] = new Thread(() -> {
                        String response = players.get(playerIndex).readMessage();
                        
                        synchronized (stopAsking) {
                            if (stopAsking[0]) {
                                return; // Đã có người chọn N
                            }
                            
                            hasResponded[playerIndex] = true;
                            
                            if (response == null) {
                                System.out.println("→ " + players.get(playerIndex).getName() + " đã mất kết nối!");
                                String disconnectMsg = "GAME_END\n✗ " + players.get(playerIndex).getName() + " đã mất kết nối!";
                                broadcastMessage(disconnectMsg);
                                stopAsking[0] = true;
                            } else if (response.trim().equalsIgnoreCase("Y")) {
                                System.out.println("→ " + players.get(playerIndex).getName() + " muốn chơi tiếp");
                                int count = 0;
                                for (boolean b : hasResponded) if (b) count++;
                                String updateMsg = "→ " + players.get(playerIndex).getName() + " sẵn sàng! (" + count + "/" + NUM_PLAYERS + ")";
                                broadcastMessage(updateMsg);
                            } else {
                                System.out.println("→ " + players.get(playerIndex).getName() + " không muốn chơi tiếp");
                                String quitMsg = "GAME_END\n→ " + players.get(playerIndex).getName() + " đã thoát khỏi trò chơi!";
                                broadcastMessage(quitMsg);
                                stopAsking[0] = true;
                            }
                        }
                    });
                    responseThreads[i].start();
                }
                
                // Đợi tất cả thread hoàn thành hoặc có người chọn N
                for (Thread t : responseThreads) {
                    t.join();
                }
                
                // Kiểm tra có tiếp tục không
                if (stopAsking[0]) {
                    continueGame = false;
                }
                
                if (!continueGame) {
                    break;
                }
                
                round++;
            }
            
            // Kết thúc game
            String finalScore = "\n╔════════════════════════════════════════════╗\n";
            finalScore += "║            KẾT QUẢ CUỐI CÙNG              ║\n";
            finalScore += "╚════════════════════════════════════════════╝\n";
            finalScore += getScoreBoard();
            finalScore += "\nCảm ơn đã chơi! Hẹn gặp lại!";
            
            System.out.println(finalScore);
            broadcastMessage(finalScore);
            
            cleanup();
            
        } catch (IOException e) {
            System.err.println("Lỗi server: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Lỗi thread: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean isValidChoice(String choice) {
        return choice.equals("BÚA") || choice.equals("BAO") || choice.equals("KÉO");
    }
    
    private static GameResult determineResult(String[] choices) {
        GameResult result = new GameResult();
        result.scores = new double[NUM_PLAYERS];
        
        Set<String> uniqueChoices = new HashSet<>(Arrays.asList(choices));
        
        // Trường hợp 1: Cả 3 người chọn giống nhau → HÒA
        if (uniqueChoices.size() == 1) {
            result.message = "⚖ HÒA! Cả 3 người đều chọn " + choices[0];
            Arrays.fill(result.scores, 0.5);
            return result;
        }
        
        // Trường hợp 2: Có cả 3 loại BÚA, BAO, KÉO → HÒA
        if (uniqueChoices.size() == 3) {
            result.message = "⚖ HÒA! Có cả BÚA, BAO và KÉO";
            Arrays.fill(result.scores, 0.5);
            return result;
        }
        
        // Trường hợp 3: Có 2 loại khác nhau
        // Đếm số người chọn mỗi loại
        Map<String, List<Integer>> choiceMap = new HashMap<>();
        for (int i = 0; i < NUM_PLAYERS; i++) {
            choiceMap.putIfAbsent(choices[i], new ArrayList<>());
            choiceMap.get(choices[i]).add(i);
        }
        
        List<String> choiceTypes = new ArrayList<>(choiceMap.keySet());
        String choice1 = choiceTypes.get(0);
        String choice2 = choiceTypes.get(1);
        
        // Xác định choice nào thắng
        String winningChoice = getWinningChoice(choice1, choice2);
        
        List<Integer> winners = choiceMap.get(winningChoice);
        int numWinners = winners.size();
        int numLosers = NUM_PLAYERS - numWinners;
        
        // Tính điểm: người thắng chia đều điểm của người thua
        double pointsPerWinner = (double) numLosers / numWinners;
        
        StringBuilder winnerNames = new StringBuilder();
        StringBuilder loserNames = new StringBuilder();
        
        for (int i = 0; i < NUM_PLAYERS; i++) {
            if (winners.contains(i)) {
                result.scores[i] = pointsPerWinner;
                if (winnerNames.length() > 0) winnerNames.append(", ");
                winnerNames.append(players.get(i).getName());
            } else {
                result.scores[i] = 0;
                if (loserNames.length() > 0) loserNames.append(", ");
                loserNames.append(players.get(i).getName());
            }
        }
        
        result.message = "★ " + winnerNames + " THẮNG! (" + winningChoice + " > " + 
                        (winningChoice.equals(choice1) ? choice2 : choice1) + ")\n";
        result.message += "  Người thua: " + loserNames;
        
        return result;
    }
    
    private static String getWinningChoice(String c1, String c2) {
        if (c1.equals("BÚA") && c2.equals("KÉO")) return "BÚA";
        if (c1.equals("KÉO") && c2.equals("BÚA")) return "BÚA";
        if (c1.equals("KÉO") && c2.equals("BAO")) return "KÉO";
        if (c1.equals("BAO") && c2.equals("KÉO")) return "KÉO";
        if (c1.equals("BAO") && c2.equals("BÚA")) return "BAO";
        if (c1.equals("BÚA") && c2.equals("BAO")) return "BAO";
        return c1;
    }
    
    private static String formatResult(String[] choices, GameResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nLỰA CHỌN:\n");
        for (int i = 0; i < NUM_PLAYERS; i++) {
            sb.append("  ").append(players.get(i).getName())
              .append(": ").append(getChoiceEmoji(choices[i]))
              .append(" ").append(choices[i]).append("\n");
        }
        sb.append("\n").append(result.message).append("\n");
        return sb.toString();
    }
    
    private static String getChoiceEmoji(String choice) {
        switch (choice) {
            case "BÚA": return "✊";
            case "BAO": return "✋";
            case "KÉO": return "✌";
            default: return "❓";
        }
    }
    
    private static String getScoreBoard() {
        StringBuilder sb = new StringBuilder("\n┌────────────────────── BẢNG ĐIỂM ──────────────────────┐\n");
        for (int i = 0; i < NUM_PLAYERS; i++) {
            sb.append(String.format("│ %d. %-20s %10.1f điểm          │\n", 
                i + 1, players.get(i).getName(), players.get(i).getScore()));
        }
        sb.append("└────────────────────────────────────────────────────────┘\n");
        return sb.toString();
    }
    
    private static void broadcastMessage(String message) {
        for (PlayerHandler player : players) {
            player.sendMessage(message);
        }
    }
    
    private static void displayServerIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        System.out.println("Địa chỉ IP Server: " + inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Không thể lấy địa chỉ IP: " + e.getMessage());
        }
    }
    
    private static void cleanup() {
        try {
            for (PlayerHandler player : players) {
                player.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("\n✓ Server đã đóng kết nối!");
        } catch (IOException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
    
    // Inner class để quản lý mỗi người chơi
    static class PlayerHandler {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String name;
        private double score;
        private int playerNumber;
        
        public PlayerHandler(Socket socket, int playerNumber) throws IOException {
            this.socket = socket;
            this.playerNumber = playerNumber;
            this.name = "Người chơi " + playerNumber;
            this.score = 0;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }
        
        public void sendMessage(String message) {
            try {
                out.println(message);
                out.flush();
            } catch (Exception e) {
                System.err.println("Lỗi gửi tin nhắn đến " + name + ": " + e.getMessage());
            }
        }
        
        public String readMessage() {
            try {
                String message = in.readLine();
                return message;
            } catch (IOException e) {
                System.err.println("Lỗi đọc tin nhắn từ " + name + ": " + e.getMessage());
                return null;
            }
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void addScore(double points) {
            this.score += points;
        }
        
        public double getScore() {
            return score;
        }
        
        public void close() throws IOException {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        }
    }
    
    // Class để lưu kết quả game
    static class GameResult {
        String message;
        double[] scores;
    }
}

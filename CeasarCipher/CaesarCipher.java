import java.io.*;
import java.util.Scanner;

public class CaesarCipher {
    
    public static String maHoa(String text, int shift) {
        String result = "";
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            
            if (ch >= 'A' && ch <= 'Z') {
                char newChar = (char) ((ch - 'A' + shift) % 26 + 'A');
                result = result + newChar;
            }
            else if (ch >= 'a' && ch <= 'z') {
                char newChar = (char) ((ch - 'a' + shift) % 26 + 'a');
                result = result + newChar;
            }
            else {
                result = result + ch;
            }
        }
        
        return result;
    }
    
    public static String giaiMa(String text, int shift) {
        String result = "";
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            
            if (ch >= 'A' && ch <= 'Z') {
                char newChar = (char) ((ch - 'A' - shift + 26) % 26 + 'A');
                result = result + newChar;
            }
            else if (ch >= 'a' && ch <= 'z') {
                char newChar = (char) ((ch - 'a' - shift + 26) % 26 + 'a');
                result = result + newChar;
            }
            else {
                result = result + ch;
            }
        }
        
        return result;
    }
    
    public static void luuFile(String filename, String content) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(content);
            writer.close();
            System.out.println("Da luu vao file: " + filename);
        } catch (IOException e) {
            System.out.println("Loi khi luu file!");
        }
    }
    
    public static String docFile(String filename) {
        String content = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                content = content + line + "\n";
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Loi khi doc file!");
            return null;
        }
        return content.trim();
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        
        do {
            System.out.println("\n--- CHUONG TRINH MA HOA CAESAR ---");
            System.out.println("1. Ma hoa");
            System.out.println("2. Giai ma");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            
            choice = sc.nextInt();
            
            switch (choice) {
                case 1:
                    String message1 = "";
                    
                    System.out.println("1. Doc tu file");
                    System.out.println("2. Nhap tu ban phim");
                    System.out.print("Chon: ");
                    int option1 = sc.nextInt();
                    
                    if (option1 == 1) {
                        System.out.print("Nhap ten file: ");
                        sc.nextLine();
                        String filename1 = sc.nextLine();
                        message1 = docFile(filename1);
                        
                        if (message1 == null) {
                            System.out.println("Khong doc duoc file!");
                            break;
                        }
                    }
                    else if (option1 == 2) {
                        System.out.print("Nhap thong diep: ");
                        sc.nextLine();
                        message1 = sc.nextLine();
                    }
                    else {
                        System.out.println("Chon sai!");
                        break;
                    }
                    
                    System.out.print("Nhap buoc dich: ");
                    int shift1 = sc.nextInt();
                    
                    String encrypted = maHoa(message1, shift1);
                    
                    System.out.println("\nThong diep goc: " + message1);
                    System.out.println("Buoc dich: " + shift1);
                    System.out.println("Thong diep da ma hoa: " + encrypted);
                    
                    luuFile("encrypted.txt", encrypted);
                    break;
                    
                case 2:
                    String message2 = "";
                    
                    System.out.println("1. Doc tu file");
                    System.out.println("2. Nhap tu ban phim");
                    System.out.print("Chon: ");
                    int option = sc.nextInt();
                    
                    if (option == 1) {
                        System.out.print("Nhap ten file: ");
                        sc.nextLine();
                        String filename = sc.nextLine();
                        message2 = docFile(filename);
                        
                        if (message2 == null) {
                            System.out.println("Khong doc duoc file!");
                            break;
                        }
                    }
                    else if (option == 2) {
                        System.out.print("Nhap thong diep: ");
                        sc.nextLine();
                        message2 = sc.nextLine();
                    }
                    else {
                        System.out.println("Chon sai!");
                        break;
                    }
                    
                    System.out.print("Nhap buoc dich: ");
                    int shift2 = sc.nextInt();
                    
                    String decrypted = giaiMa(message2, shift2);
                    
                    System.out.println("\nThong diep da ma hoa: " + message2);
                    System.out.println("Buoc dich: " + shift2);
                    System.out.println("Thong diep goc: " + decrypted);
                    break;
                    
                case 0:
                    System.out.println("Tat chuong trinh");
                    break;
                    
                default:
                    System.out.println("Chon sai so hay chon lai tu 0 den 2");
                    break;
            }
            
        } while (choice != 0);
        
        sc.close();
    }
}

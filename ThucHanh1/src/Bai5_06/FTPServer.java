package Bai5_06;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.Scanner;
import javax.swing.JOptionPane;
import thuchanh1.DBAccess;

public class FTPServer {
    //khai bao cac hang trong giao thuc giao tiep
    public static final int DANGNHAP = 1;
    public static final int KHONGLALENH = 0;
    public static final int DANGNHAPKHONGTHANHCONG = 0;
    public static final int DANGNHAPTHANHCONG = 1;
    public static final int THOAT = 2;
    public static final int UPLOAD = 3;
    public static final int DOWNLOAD = 4;
    
    //thiet lap port giao tiep cua ung dung
    public static final int PORT = 10000;
    
    //duong dan thu muc server
    private static String serverPath = "C:/FTPServer/";
    
    //ham doi chuoi giao tiep thanh hang cho de xu ly
    public static int laLenh(String cmd) {
        if (cmd.equals("DANGNHAP"))
            return DANGNHAP;
        if (cmd.equals("UPLOAD"))
            return UPLOAD;
        if (cmd.equals("DOWNLOAD"))
            return DOWNLOAD;
        if (cmd.equals("THOAT"))
            return THOAT;
        return KHONGLALENH;
    }
    
    //ham kiem tra dang nhap tu database
    public static boolean checkLogin(String username, String password) {
        try {
            DBAccess db = new DBAccess();
            String sql = "select * from taikhoan where username='" + username + "' and password='" + password + "'";
            ResultSet rs = db.Query(sql);
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Loi kiem tra dang nhap: " + e.toString());
        }
        return false;
    }
    
    //ham tra danh sach tap tin ve client
    static void traThuMucClient(String path, PrintWriter out) {
        try {
            File dir = new File(path);
            File dsFile[];
            System.out.println("Dang doc tap tin");
            try {
                dsFile = dir.listFiles();
                System.out.println("da la ds tap tin");
                if (dsFile != null) {
                    out.println(dsFile.length);
                    for (int i = 0; i < dsFile.length; i++) {
                        String filename = dsFile[i].getName();
                        out.println(filename);
                    }
                } else {
                    out.println(0);
                }
                out.flush();
                System.out.println("da goi client");
            } catch (Exception e) {
                System.out.println("Loi doc danh sach file: " + e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public static void main(String[] args) {
        //tao thu muc server neu chua co
        File serverDir = new File(serverPath);
        if (!serverDir.exists()) {
            serverDir.mkdirs();
        }
        
        ServerSocket s;
        try {
            s = new ServerSocket(PORT);
            System.out.println("FTP Server dang chay tren port " + PORT);
            
            while (true) {
                Socket new_s = s.accept();
                System.out.println("Co client ket noi: " + new_s.getInetAddress());
                
                //nhan lenh giao tiep tu client
                boolean lap = true;
                while (lap) {
                    try {
                        String cmd;
                        Scanner sc = new Scanner(new_s.getInputStream());
                        cmd = sc.nextLine();
                        System.out.println("Nhan lenh: " + cmd);
                        
                        //dieu phoi su kien yeu cau o phia client
                        switch (laLenh(cmd)) {
                            case DANGNHAP:
                                String user = sc.nextLine();
                                String pass = sc.nextLine();
                                System.out.println("User: " + user + ", Pass: " + pass);
                                
                                PrintWriter pw = new PrintWriter(new_s.getOutputStream());
                                
                                if (checkLogin(user, pass)) {
                                    pw.println(DANGNHAPTHANHCONG);
                                    //mo thu muc len goi ve cho client
                                    File dir = new File(serverPath);
                                    File dsFile[] = dir.listFiles();
                                    if (dsFile == null) {
                                        pw.println(0);
                                    } else {
                                        pw.println(dsFile.length);
                                        for (int i = 0; i < dsFile.length; i++)
                                            pw.println(dsFile[i].getName());
                                    }
                                } else {
                                    //goi ve khong mo duoc
                                    pw.println(DANGNHAPKHONGTHANHCONG);
                                    pw.println("Dang nhap khong thanh cong");
                                }
                                pw.flush();
                                break;
                                
                            case UPLOAD:
                                System.out.println("Da vao lenh upload");
                                String fileName = sc.nextLine();
                                System.out.println("Da lay ten tap tin: " + fileName);
                                
                                try {
                                    //nhan kich thuoc file
                                    long fileSize = sc.nextLong();
                                    sc.nextLine(); // consume newline
                                    System.out.println("Kich thuoc file: " + fileSize + " bytes");
                                    
                                    String path2;
                                    //kiem tra chuoi duong dan co dau / cuoi cung hay ko?
                                    if (serverPath.lastIndexOf("/") >= serverPath.length() - 1)
                                        path2 = serverPath + fileName;
                                    else
                                        path2 = serverPath + "/" + fileName;
                                    
                                    System.out.println("Luu vao: " + path2);
                                    
                                    FileOutputStream fos = new FileOutputStream(new File(path2));
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                                    BufferedInputStream bis = new BufferedInputStream(new_s.getInputStream());
                                    
                                    byte buf[] = new byte[8192];
                                    int bytesRead;
                                    long totalReceived = 0;
                                    while (totalReceived < fileSize && (bytesRead = bis.read(buf)) != -1) {
                                        int toWrite = (int) Math.min(bytesRead, fileSize - totalReceived);
                                        bos.write(buf, 0, toWrite);
                                        totalReceived += toWrite;
                                    }
                                    System.out.println("Da nhan: " + totalReceived + " bytes");
                                    bos.flush();
                                    bos.close();
                                    fos.close();
                                    
                                    pw = new PrintWriter(new_s.getOutputStream());
                                    pw.println("DANHAN");
                                    pw.flush();
                                    
                                    //yeu cau update lai listbox o server
                                    traThuMucClient(serverPath, pw);
                                    
                                    System.out.println("Upload thanh cong");
                                } catch (Exception e) {
                                    System.out.println("Loi upload: " + e);
                                }
                                break;
                                
                            case DOWNLOAD:
                                System.out.println("Da vao lenh download");
                                String fileNameD = sc.nextLine();
                                System.out.println("Da lay ten tap tin: " + fileNameD);
                                
                                try {
                                    String cpath;
                                    //kiem tra chuoi duong dan co dau / cuoi cung hay ko?
                                    if (serverPath.lastIndexOf("/") >= serverPath.length() - 1)
                                        cpath = serverPath + fileNameD;
                                    else
                                        cpath = serverPath + "/" + fileNameD;
                                    
                                    System.out.println("Doc tu: " + cpath);
                                    
                                    //mo tap tin ra
                                    File fileToSend = new File(cpath);
                                    if (!fileToSend.exists()) {
                                        System.out.println("File khong ton tai!");
                                        pw = new PrintWriter(new_s.getOutputStream());
                                        pw.println(0); // gui kich thuoc = 0 de bao loi
                                        pw.flush();
                                        break;
                                    }
                                    
                                    //gui kich thuoc file truoc
                                    long fileSize = fileToSend.length();
                                    pw = new PrintWriter(new_s.getOutputStream());
                                    pw.println(fileSize);
                                    pw.flush();
                                    System.out.println("Da gui kich thuoc file: " + fileSize + " bytes");
                                    
                                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(cpath));
                                    BufferedOutputStream bos = new BufferedOutputStream(new_s.getOutputStream());
                                    
                                    byte buf[] = new byte[8192];
                                    int bytesRead;
                                    long totalSent = 0;
                                    while ((bytesRead = bis.read(buf)) != -1 && totalSent < fileSize) {
                                        int toWrite = (int) Math.min(bytesRead, fileSize - totalSent);
                                        bos.write(buf, 0, toWrite);
                                        totalSent += toWrite;
                                    }
                                    bos.flush();
                                    bis.close();
                                    
                                    System.out.println("da goi du lieu tap tin ve cho client: " + totalSent + " bytes");
                                    
                                    //doi nhan danh sach tap thu cua folder o server voi tinh trang moi
                                    Scanner scRequest = new Scanner(new_s.getInputStream());
                                    String cmdRequest = scRequest.nextLine();
                                    System.out.println("da nhan dap tra tu client: " + cmdRequest);
                                    
                                    if (cmdRequest.equals("DANHAN"))
                                        System.out.println("Da gui tap tin thanh cong");
                                    else
                                        System.out.println("Gui tap tin that bai");
                                        
                                } catch (Exception e) {
                                    System.out.println("Loi download: " + e);
                                }
                                break;
                                
                            case THOAT:
                                lap = false;
                                System.out.println("Client ngat ket noi");
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Loi xu ly lenh: " + e);
                        lap = false;
                    }
                }
                new_s.close();
            }
        } catch (Exception ex) {
            System.out.println("Loi server: " + ex);
        }
    }
}

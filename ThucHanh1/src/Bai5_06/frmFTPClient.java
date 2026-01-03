package Bai5_06;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class frmFTPClient extends javax.swing.JInternalFrame {

    Socket s;
    public static final int PORT = 10000;
    String path;
    JFileChooser fchPath = new JFileChooser();
    private static boolean serverStarted = false;
    
    public frmFTPClient() {
        initComponents();
        startFTPServer();
    }
    
    private void startFTPServer() {
        if (!serverStarted) {
            serverStarted = true;
            Thread serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Dang khoi dong FTP Server...");
                        FTPServer.main(new String[]{});
                    } catch (Exception e) {
                        System.out.println("Loi khoi dong server: " + e.toString());
                    }
                }
            });
            serverThread.setDaemon(true); // Server se tu dong tat khi ung dung dong
            serverThread.start();
            
            // Doi 1 giay de server khoi dong
            try {
                Thread.sleep(1000);
                System.out.println("FTP Server da san sang!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtDomain = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstClientPath = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstPath = new javax.swing.JList<>();
        btnBrowse = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        btnDownload = new javax.swing.JButton();

        jLabel1.setText("Domain:");

        txtDomain.setText("localhost");

        jLabel2.setText("User:");

        jLabel3.setText("Pass:");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jLabel4.setText("Client's Folder");

        jScrollPane1.setViewportView(lstClientPath);

        jLabel5.setText("Server Folder");

        jScrollPane2.setViewportView(lstPath);

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDomain, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(btnBrowse))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogin)
                    .addComponent(btnUpload)
                    .addComponent(btnDownload))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBrowse))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        //tao socket
        String domain = this.txtDomain.getText();
        try {
            InetAddress ia = InetAddress.getByName(domain);
            try {
                s = new Socket(ia, PORT);
                //goi user, pass len server
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                //lay du lieu tu form do su dung go vao
                String user = this.txtUser.getText();
                String pass = new String(this.txtPass.getPassword());
                String cmd = "DANGNHAP";
                pw.println(cmd);
                pw.println(user);
                pw.println(pass);
                System.out.println(cmd);
                System.out.println(user);
                System.out.println(pass);
                pw.flush();
                
                //client doi phan hoi tu server
                Scanner sc = new Scanner(s.getInputStream());
                int cmdR = sc.nextInt();
                sc.nextLine(); // consume newline
                
                if (cmdR == 1) {
                    JOptionPane.showMessageDialog(this, "Dang nhap thanh cong");
                    //luu duong dan hien tai ma list hien thi danh tap tin trong folder do.
                    DefaultListModel<String> dm = new DefaultListModel<>();
                    int n = sc.nextInt();
                    sc.nextLine(); // consume newline
                    for (int i = 0; i < n; i++) {
                        String fileName = sc.nextLine();
                        dm.addElement(fileName);
                    }
                    this.lstPath.setModel(dm);
                } else {
                    JOptionPane.showMessageDialog(this, "Dang nhap khong thanh cong");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.toString());
            }
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(this, ex.toString());
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        fchPath.setVisible(true);
        //thiet lap
        fchPath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //kiem tra form nay duoc chon nut gi?
        if (fchPath.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                path = fchPath.getSelectedFile().getCanonicalPath();
                File dir = new File(path);
                File dsFile[] = dir.listFiles();
                if (dsFile == null) {
                    JOptionPane.showMessageDialog(null, "sai duong dan!");
                } else {
                    try {
                        //luu duong dan hien tai ma list hien thi danh tap tin trong folder do.
                        DefaultListModel<String> dm = new DefaultListModel<>();
                        for (int i = 0; i < dsFile.length; i++) {
                            String filename = dsFile[i].getName();
                            dm.addElement(filename);
                        }
                        this.lstClientPath.setModel(dm);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.toString());
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.toString());
            }
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        // TODO add your handling code here:
        String fileName = (String) this.lstClientPath.getSelectedValue();
        if (fileName == null) {
            JOptionPane.showMessageDialog(this, "Vui long chon tap tin de upload!");
            return;
        }
        
        String cpath = path + "\\" + fileName;
        System.out.println(cpath);
        try {
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            //goi tin hieu lenh
            pw.println("UPLOAD");
            pw.flush();
            System.out.println("Da goi lenh upload len server");
            pw.println(fileName);
            pw.flush();
            System.out.println("Da goi ten tap tin len server");
            
            //gui kich thuoc file
            File uploadFile = new File(cpath);
            long fileSize = uploadFile.length();
            pw.println(fileSize);
            pw.flush();
            System.out.println("Da goi kich thuoc file: " + fileSize);
            
            BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
            //mo tap tin ra
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(cpath));
            //lap doc noi dung tap tin va goi lieu len server
            byte buf[] = new byte[8192];
            int bytesRead;
            long totalSent = 0;
            while ((bytesRead = bis.read(buf)) != -1 && totalSent < fileSize) {
                int toWrite = (int) Math.min(bytesRead, fileSize - totalSent);
                bos.write(buf, 0, toWrite);
                totalSent += toWrite;
            }
            System.out.println("da goi du lieu tap tin len server: " + totalSent + " bytes");
            bos.flush();
            bis.close();
            
            //doi nhan danh sach tap thu cua folder o server voi tinh trang moi
            Scanner sc = new Scanner(s.getInputStream());
            String cmd = sc.nextLine();
            System.out.println("da nhan dap tra tu server: " + cmd);
            if (cmd.equals("DANHAN"))
                JOptionPane.showMessageDialog(null, "Da gui tap tin thanh cong");
            else
                JOptionPane.showMessageDialog(null, "Gui tap tin that bai");
            
            //nhan update
            updateFolderServer();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Loi upload: " + e.toString());
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadActionPerformed
        //lay tap tin can download
        String fileName = (String) this.lstPath.getSelectedValue();
        if (fileName == null) {
            JOptionPane.showMessageDialog(this, "Vui long chon tap tin de download!");
            return;
        }
        
        System.out.println(fileName);
        try {
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            //goi tin hieu lenh
            pw.println("DOWNLOAD");
            pw.flush();
            System.out.println("Da goi lenh download len server");
            pw.println(fileName);
            pw.flush();
            
            //doi server goi kich thuoc file ve
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String fileSizeStr = br.readLine();
            long fileSize = Long.parseLong(fileSizeStr);
            
            if (fileSize == 0) {
                JOptionPane.showMessageDialog(this, "File khong ton tai tren server!");
                return;
            }
            
            System.out.println("Kich thuoc file: " + fileSize + " bytes");
            
            //doi server goi noi dung tap tin ve
            System.out.println("Doi server goi noi dung tap tin ve");
            String cpath = path + "\\" + fileName;
            FileOutputStream fos = new FileOutputStream(new File(cpath));
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = s.getInputStream();
            byte buf[] = new byte[8192];
            int bytesRead;
            long totalReceived = 0;
            while (totalReceived < fileSize && (bytesRead = is.read(buf)) != -1) {
                int toWrite = (int) Math.min(bytesRead, fileSize - totalReceived);
                bos.write(buf, 0, toWrite);
                totalReceived += toWrite;
            }
            System.out.println("Da nhan: " + totalReceived + " bytes");
            bos.flush();
            bos.close();
            fos.close();
            
            pw = new PrintWriter(s.getOutputStream());
            pw.println("DANHAN");
            pw.flush();
            
            //cap nhat lai thu muc client vua download
            this.capNhatClientFolder(cpath);
            JOptionPane.showMessageDialog(this, "Download thanh cong!");
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this, "Loi download: " + ex.toString());
        }
    }//GEN-LAST:event_btnDownloadActionPerformed

    public void updateFolderServer() {
        try {
            BufferedInputStream bi = new BufferedInputStream(s.getInputStream());
            Scanner sc = new Scanner(bi);
            DefaultListModel<String> dm = new DefaultListModel<>();
            //server goi ve so luong tap tin thu muc sau khi upload
            int n = sc.nextInt();
            sc.nextLine(); // consume newline
            System.out.println("Da nhan duoc so luong tap tin goi tu server: " + n);
            //nhan ten tung tap tin thu muc
            for (int i = 0; i < n; i++) {
                String filename = sc.nextLine();
                dm.addElement(filename);
            }
            System.out.println("Da hien thi xong danh sach tap tin");
            //hien thi len form
            this.lstPath.setModel(dm);
            //ve lai giao dien
            this.validate();
        } catch (Exception e) {
            System.out.println("Loi update folder server: " + e.toString());
        }
    }

    private void capNhatClientFolder(String cpath) {
        //mo thu muc voi duong dan path ra
        File dir = new File(path);
        File dsFile[] = dir.listFiles();
        if (dsFile == null) {
            JOptionPane.showMessageDialog(null, " Duong dan sai!");
        } else {
            try {
                //luu duong dan hien tai ma list hien thi danh tap tin trong folder do.
                DefaultListModel<String> dm = new DefaultListModel<>();
                for (int i = 0; i < dsFile.length; i++) {
                    String filename = dsFile[i].getName();
                    dm.addElement(filename);
                }
                this.lstClientPath.setModel(dm);
                this.validate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lstClientPath;
    private javax.swing.JList<String> lstPath;
    private javax.swing.JTextField txtDomain;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmFTPClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmFTPClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmFTPClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmFTPClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                javax.swing.JFrame frame = new javax.swing.JFrame("FTP Client");
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                
                // JInternalFrame can khong the add truc tiep vao JFrame
                // Phai dung JDesktopPane
                javax.swing.JDesktopPane desktopPane = new javax.swing.JDesktopPane();
                frmFTPClient client = new frmFTPClient();
                desktopPane.add(client);
                client.setVisible(true);
                client.setSize(800, 500);
                client.setLocation(0, 0);
                
                frame.setContentPane(desktopPane);
                frame.setSize(820, 550);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}

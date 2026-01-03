/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Admin
 */
public class FileTWriter extends Thread {
    private int intElement;
    private String path;

    public FileTWriter(String path) {
        this.path = path;
    }

    public void randomInt() {
        intElement = ThreadLocalRandom.current().nextInt(1, 101);
        String strInt = intElement + "";
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            fw.write(strInt);
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        randomInt();
        System.out.print(intElement + ",");
    }
}


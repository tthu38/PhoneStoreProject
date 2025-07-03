/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class NewClass {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MailService mailService = new MailService();
        String email = "hoangluu2172k4@gmail.com";
        String name = "hoàng Lưu";
        String ip = "192.168.1."; // Lấy IP client
        String time = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));

        mailService.sendLoginNotification(email, name, ip, time);

    }
}

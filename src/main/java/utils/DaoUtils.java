/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author ThienThu
 */
public class DaoUtils {
    public static String capitalizeFirstLetter(String input) {
    if (input == null || input.isEmpty()) {
        return input;
    }
    return input.substring(0, 1).toUpperCase() + input.substring(1);
}

}

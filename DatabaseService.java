package com.mycompany.pwsw_lab04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class DatabaseService {

    private Connection polacz() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab", "root", "");
            System.out.println("Połączono z bazą");
        } catch (Exception ex) {
            System.out.println("polacz() - " + ex.toString());
        }
        return connection;
    }

    private void rozlacz(Connection connection) {
        try {
            connection.close();
            System.out.println("Rozłączono z bazą");
        } catch (Exception ex) {
            System.out.println("Nie rozłączono z bazą");
        }
    }

    public void rejestruj(String imie, String nazwisko, String login, String haslo, String email) {
        boolean poprawnosc = true;
        if (imie.length() > 50 || nazwisko.length() > 50 || haslo.length() > 30 || email.length() > 50) {
            poprawnosc = false;
        }
        if (poprawnosc) {
            if (Pattern.matches("[a-zA-Z0-9]{1,}", login)) {
                try {
                    Connection connection = polacz();
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM logowanie WHERE login='" + login + "';");
                    int ile = 0;
                    while (rs.next()) {
                        ile++;
                    }
                    if (ile == 0) {
                        LocalDate localDate = LocalDate.now();
                        stmt.execute("INSERT INTO logowanie (imie,nazwisko,login,haslo,email,uprawnienia,data_rejestracji) VALUES ('" + imie + "','" + nazwisko + "','" + login + "','" + haslo + "','" + email + "','user', '" + (localDate.getYear()) + "-" + (localDate.getMonthValue()) + "-" + localDate.getDayOfMonth() + "')");
                        //System.out.println((localDate.getYear())+"-"+(localDate.getMonthValue())+"-"+localDate.getDayOfMonth());
                        JOptionPane.showMessageDialog(null, "Zarejestrowano", "Infomracja ", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Login zajęty", "Infomracja ", JOptionPane.INFORMATION_MESSAGE);
                    }
                    rozlacz(connection);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Błąd ", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Login może zawierać tylko znaki i cyfry", "Błąd ", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Niepoprawne dane!", "Błąd ", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String[] zaloguj(String login, String haslo) {
        String[] dane = new String[5];
        dane[0] = "niezalogowany";
        try {
            Connection connection = polacz();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM logowanie WHERE login='" + login + "';");
            int ile = 0;
            while (rs.next()) {
                ile++;
            }
            System.out.println(ile);
            if (ile == 1) {
                rs = stmt.executeQuery("SELECT * FROM logowanie WHERE login='" + login + "';");
                rs.next();
                if (haslo.equals(rs.getString("haslo"))) {
                    dane[0] = "zalogowany";
                    dane[1] = rs.getString("uprawnienia");
                    dane[2] = rs.getString("imie");
                    dane[3]=rs.getString("nazwisko");
                    dane[4]=rs.getString("data_rejestracji");
                }
                //dane.add(rs.getString("data_rejestracji"));
            } else {
                JOptionPane.showMessageDialog(null, "Podany login nie istnieje lub hasło jest nieprawidłowe", "Błąd ", JOptionPane.ERROR_MESSAGE);
            }
            rozlacz(connection);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Błąd podczas logowania!", "Błąd ", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.toString());
        }
        return dane;
    }
}

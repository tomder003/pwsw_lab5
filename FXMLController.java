package com.mycompany.pwsw_lab04;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;

public class FXMLController implements Initializable {

    public DatabaseService dbService;
    @FXML
    public Pane panelLogowanie;
    public Pane panelUzytkownika;
    public Pane panelRejestracja;
    public Pane panelAdministrator;
    public TextField imie;
    public TextField nazwisko;
    public TextField login;
    public TextField haslo;
    public TextField haslo2;
    public TextField email;
    public Label witajLabel;
    public TextField loginLog;
    public TextField hasloLog;
    public Label userLabel1;
    public Label userLabel2;
    public CheckBox checkBoxHaslo;
    public TextField hasloText;

    @FXML
    private void rejestracja1(ActionEvent event) {
        panelLogowanie.setVisible(false);
        panelRejestracja.setVisible(true);
    }

    @FXML
    private void zarejestruj(ActionEvent event) {
        if (haslo.getText().equals(haslo2.getText())) {
            dbService.rejestruj(imie.getText(), nazwisko.getText(), login.getText(), haslo.getText(), email.getText());
        } else {
            JOptionPane.showMessageDialog(null, "Hasła się nie zgadzają!", "Błąd ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void doLogowania(ActionEvent event) {
        panelRejestracja.setVisible(false);
        panelLogowanie.setVisible(true);
    }

    @FXML
    private void zaloguj(ActionEvent event) {
        try {
            String[] dane = dbService.zaloguj(loginLog.getText(), hasloLog.getText());
            if (dane[0].equals("zalogowany")) {
                if (dane[1].equals("user")) {
                    panelLogowanie.setVisible(false);
                    panelUzytkownika.setVisible(true);
                    witajLabel.setText("Witaj " + dane[2]);
                    userLabel1.setText("Zarejestrowano: " + dane[4]);
                    userLabel2.setText("Imię i nazwisko: " + dane[2] + " " + dane[3]);
                }
                if (dane[1].equals("admin")) {
                    panelLogowanie.setVisible(false);
                    panelAdministrator.setVisible(true);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Błąd podczas logowania!", "Błąd ", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void wyloguj1(ActionEvent event) {
        panelUzytkownika.setVisible(false);
        panelLogowanie.setVisible(true);
    }

    @FXML
    private void wyloguj2(ActionEvent event) {
        panelAdministrator.setVisible(false);
        panelLogowanie.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dbService = new DatabaseService();

        ChangeListener checkboxlistener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (checkBoxHaslo.isSelected()) {
                    hasloText.setText(hasloLog.getText());
                    hasloLog.setVisible(false);
                    hasloText.setVisible(true);
                } else {
                    hasloLog.setVisible(true);
                    hasloText.setVisible(false);
                }
            }

        };
        checkBoxHaslo.selectedProperty().addListener(checkboxlistener);
    }
}

package controllers;
import main.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.db_connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormController {

    private MainApp mainApp;

    @FXML
    private Button daftarButton;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private TextField namaField;
    
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // Handle tombol daftar
    @FXML
    private void handleDaftar() {
        String nama = namaField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Semua field harus diisi.");
            return;
        }

        String query = "INSERT INTO users (nama, username, password, email) VALUES (?, ?, MD5(?), ?)";
        try (Connection conn = db_connect.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, email);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Sukses", "Pendaftaran berhasil.");
                openLogin();  
            } else {
                showAlert("Error", "Pendaftaran gagal.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Tidak dapat mengakses database.");
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (verifikasiLogin(username, password)) {
            showAlert("Sukses", "Login berhasil, selamat datang " + username + "!");
            openDashboardAnggota();  
        } else {
            showAlert("Error", "Username atau password salah.");
        }
    }

    private boolean verifikasiLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = MD5(?)";
        try (Connection conn = db_connect.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Terjadi kesalahan pada database.");
            return false;
        }
    }

    private void openLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) daftarButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Tidak dapat membuka halaman login.");
        }
    }

    private void openDashboardAnggota() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardAnggota.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Anggota");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Tidak dapat membuka halaman dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

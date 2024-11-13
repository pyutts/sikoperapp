package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import services.db_connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {
    
    // Inner class User
    public static class User {
        private Integer id;
        private String nama;      
        private String username;
        private String email;
        private String password;
        private String alamat;    

        public User(Integer id, String name, String username, String email, String password, String address) {
            this.id = id;
            this.nama = name;
            this.username = username;
            this.email = email;
            this.password = password;
            this.alamat = address;
        }

        // Getters
        public Integer getId() { return id; }
        public String getNama() { return nama; }         
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getAlamat() { return alamat; }    

        // Setters
        public void setId(Integer id) { this.id = id; }
        public void setNama(String nama) { this.nama = nama; }
        public void setUsername(String username) { this.username = username; }
        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
        public void setAlamat(String alamat) { this.alamat = alamat; }
    }
    
    @FXML
    private TableView<User> tableView;
    
    @FXML
    private TableColumn<User, Integer> columnID;
    
    @FXML
    private TableColumn<User, String> columnNama;    
    
    @FXML
    private TableColumn<User, String> columnUsername;
    
    @FXML
    private TableColumn<User, String> columnEmail;
    
    @FXML
    private TableColumn<User, String> columnPassword;
    
    @FXML
    private TableColumn<User, String> columnAlamat;  

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Menggunakan lambda untuk menghindari masalah dengan PropertyValueFactory
        columnID.setCellValueFactory(data -> 
            new SimpleObjectProperty<>(data.getValue().getId()));
        
        columnNama.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getNama()));
        
        columnUsername.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getUsername()));
        
        columnEmail.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getEmail()));
        
        columnPassword.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getPassword()));
        
        columnAlamat.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getAlamat()));

        // Set items ke TableView
        tableView.setItems(userList);
        
        // Load data dari database
        loadUserData();
    }

    private void loadUserData() {
        String query = "SELECT * FROM users";
        try (Connection conn = db_connect.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Bersihkan list yang ada
            userList.clear();
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("address")
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database Error", "Terjadi kesalahan saat mengambil data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method untuk refresh data
    @FXML
    private void refreshData() {
        loadUserData();
    }
}
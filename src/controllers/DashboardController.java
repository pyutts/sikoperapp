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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class DashboardController {
    
    public static class User {
        private Integer id;
        private String nama;      
        private String username;
        private String email;
        private String password;
        private String alamat;    

        public User (Integer id, String name, String username, String email, String password, String address) {
            this.id = id;
            this.nama = name;
            this.username = username;
            this.email = email;
            this.password = password;
            this.alamat = address;
        }

        public Integer getId() { return id; }
        public String getNama() { return nama; }         
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getAlamat() { return alamat; }    
        public void setId(Integer id) { this.id = id; }
        public void setNama(String nama) { this.nama = nama; }
        public void setUsername(String username) { this.username = username; }
        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
        public void setAlamat(String alamat) { this.alamat = alamat; }
    }
    
    // memanggil tombol simpanan
    @FXML
    private void goToSimpanan() {
        try {
      
            Parent simpananView = FXMLLoader.load(getClass().getResource("/views/DashboardSimpanan.fxml"));
            Scene scene = new Scene(simpananView);
            
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    //memanggil tombol anggota
    @FXML
    private void goToAnggota() {
        try {

            Parent anggotaView = FXMLLoader.load(getClass().getResource("/views/DashboardAnggota.fxml"));
            Scene scene = new Scene(anggotaView);

            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private TableColumn<User, String> columnAlamat;  

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnID.setCellValueFactory(data -> 
            new SimpleObjectProperty<>(data.getValue().getId()));
        
        columnNama.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getNama()));
        
        columnUsername.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getUsername()));
        
        columnEmail.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getEmail()));
        
        
        columnAlamat.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getAlamat()));

        tableView.setItems(userList);
        
        loadUserData();
    }

    private void loadUserData() {
        String query = "SELECT * FROM users";
        try (Connection conn = db_connect.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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

    @FXML
    private void refreshData() {
        loadUserData();
    }
}
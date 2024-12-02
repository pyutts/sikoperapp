package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controllers.FormController;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SIKOPER - Sistem Koperasi");
        
        // Buka halaman login sebagai tampilan awal
        openLoginForm(); 
    }

    public void openLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormLogin.fxml"));
            Parent root = loader.load();

            // Atur controller di bagian Form Controller
            FormController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRegisterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormDaftar.fxml"));
            Parent root = loader.load();

            // stage atau windows untuk form daftar
            Stage registerStage = new Stage();
            registerStage.setTitle("Form Daftar");
            registerStage.setScene(new Scene(root));
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

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
import java.time.LocalDate;

public class DashboardSimpananController {

    public static class Simpanan {
        private Integer id;
        private Integer member_id;
        private String nama;
        private LocalDate tgl_simpan;
        private Double jumlah_simpan;
        private String transaksi_type;

        public Simpanan(Integer id, Integer member_id, String nama, LocalDate tgl_simpan, Double jumlah_simpan, String transaksi_type) {
            this.id = id;
            this.member_id = member_id;
            this.nama = nama;
            this.tgl_simpan = tgl_simpan;
            this.jumlah_simpan = jumlah_simpan;
            this.transaksi_type = transaksi_type;
        }

        public Integer getId() { return id; }
        public Integer getMemberId() { return member_id; }
        public String getNama() { return nama; }
        public LocalDate getTglSimpan() { return tgl_simpan; }
        public Double getJumlahSimpan() { return jumlah_simpan; }
        public String getTransaksiType() { return transaksi_type; }
        public void setId(Integer id) { this.id = id; }
        public void setMemberId(Integer member_id) { this.member_id = member_id; }
        public void setNama(String nama) { this.nama = nama; }
        public void setTglSimpan(LocalDate tgl_simpan) { this.tgl_simpan = tgl_simpan; }
        public void setJumlahSimpan(Double jumlah_simpan) { this.jumlah_simpan = jumlah_simpan ; }
        public void setTransaksiType(String transaksi_type) { this.transaksi_type = transaksi_type; }
    }

    @FXML
    private TableView<Simpanan> tableView;

    @FXML
    private TableColumn<Simpanan, Integer> columnID;

    @FXML
    private TableColumn<Simpanan, String> columnNama;

    @FXML
    private TableColumn<Simpanan, LocalDate> columnTglSimpanan;

    @FXML
    private TableColumn<Simpanan, Double> columnJmlSimpanan;

    @FXML
    private TableColumn<Simpanan, String> columnTransaksi;

    private final ObservableList<Simpanan> simpananList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnID.setCellValueFactory(data -> 
            new SimpleObjectProperty<>(data.getValue().getId()));

        columnNama.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getNama()));

        columnTglSimpanan.setCellValueFactory(data -> 
            new SimpleObjectProperty<>(data.getValue().getTglSimpan()));

        columnJmlSimpanan.setCellValueFactory(data -> 
            new SimpleObjectProperty<>(data.getValue().getJumlahSimpan()));

        columnTransaksi.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getTransaksiType()));

        tableView.setItems(simpananList);

        loadSimpananData();
    }

    private void loadSimpananData() {
        String query = "SELECT s.id, s.member_id, s.transaksi_date AS tgl_simpan, s.jumlah AS jumlah_simpan, " +
                       "s.transaksi_type, u.name AS member_name " +
                       "FROM transaksi s JOIN users u ON s.member_id = u.id";

        try (Connection conn = db_connect.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            simpananList.clear();

            while (rs.next()) {
                Simpanan simpanan = new Simpanan(
                    rs.getInt("id"),
                    rs.getInt("member_id"),
                    rs.getString("member_name"),
                    rs.getDate("tgl_simpan").toLocalDate(),
                    rs.getDouble("jumlah_simpan"),
                    rs.getString("transaksi_type")
                );
                simpananList.add(simpanan);
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
        loadSimpananData();
    }
}

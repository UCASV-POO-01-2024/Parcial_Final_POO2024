package insanos.parcialfinal;

import insanos.parcialfinal.Sistema;
import insanos.parcialfinal.Transaccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReporteAController {
    private Sistema sistema;
    @FXML
    private TextField idCliente;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFinal;
    @FXML
    private Button mostrarReporteA;
    @FXML
    private TableView<Transaccion> TablaConsultaA;
    @FXML
    private TableColumn<Transaccion, Integer> colId;
    @FXML
    private TableColumn<Transaccion, LocalDateTime> colFecha;
    @FXML
    private TableColumn<Transaccion, BigDecimal> colMonto;
    @FXML
    private TableColumn<Transaccion, String> colDescripcion;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("IDtransaccion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        mostrarReporteA.setOnAction(event -> {
            try {
                MostrarReporteA();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Transaccion> CrearReporteA() throws IOException {
        List<Transaccion> transacciones = new ArrayList<>();
        try {
            int id = Integer.parseInt(idCliente.getText());
            LocalDate fechaInicial = fechaInicio.getValue();
            LocalDate fechaFin = fechaFinal.getValue();

            String query = "SELECT t.id, t.fecha, t.precio, t.descripcion "
                    + "FROM Transaccion t "
                    + "JOIN Tarjeta tar ON t.id_tarjeta = tar.id "
                    + "JOIN Cliente c ON tar.id_cliente = c.id "
                    + "WHERE c.id = ? "
                    + "AND t.fecha BETWEEN ? AND ? "
                    + "ORDER BY t.fecha";

            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, id);
                stmt.setDate(2, Date.valueOf(fechaInicial));
                stmt.setDate(3, Date.valueOf(fechaFin));

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Transaccion transaccion = new Transaccion(
                            rs.getInt("id"),
                            rs.getTimestamp("fecha").toLocalDateTime(),
                            rs.getBigDecimal("precio"),
                            rs.getString("descripcion"),
                            null
                    );
                    transacciones.add(transaccion);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e);
            }
        } catch (NumberFormatException e) {
            System.err.println("El ID del cliente no es válido: " + idCliente.getText());
        }

        return transacciones;
    }

    private void MostrarReporteA() throws IOException {
        List<Transaccion> transacciones = CrearReporteA();
        ObservableList<Transaccion> transaccionesData = FXCollections.observableArrayList(transacciones);
        TablaConsultaA.setItems(transaccionesData);


        guardarInformeEnArchivo(transacciones);
    }

    private void guardarInformeEnArchivo(List<Transaccion> transacciones) {
        File directorio = new File("Reportes");
        if (!directorio.exists()) {
            directorio.mkdir();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        File archivo = new File(directorio, "reporte A" + timestamp + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (Transaccion transaccion : transacciones) {
                writer.write("ID: " + transaccion.getIDtransaccion() + ", ");
                writer.write("Fecha: " + transaccion.getFecha() + ", ");
                writer.write("Monto: " + transaccion.getCantidad() + ", ");
                writer.write("Descripcion: " + transaccion.getDescripcion());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }
}

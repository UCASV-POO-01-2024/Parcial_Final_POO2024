package insanos.parcialfinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReporteBController {
    private Sistema sistema;

    @FXML
    private TextField idCliente;
    @FXML
    private TextField mes;
    @FXML
    private TextField año;
    @FXML
    private Button mostrarReporteB;
    @FXML
    private Label totalGastado;

    @FXML
    public void initialize() {
        mostrarReporteB.setOnAction(event -> {
            try {
                MostrarReporteB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private BigDecimal CrearReporteB() throws IOException {
        BigDecimal total = BigDecimal.ZERO;
        try {
            int id = Integer.parseInt(idCliente.getText());
            int mesIngresado = Integer.parseInt(mes.getText());
            int añoIngresado = Integer.parseInt(año.getText());

            String query = "SELECT SUM(t.precio) as totalGastado "
                    + "FROM Transaccion t "
                    + "JOIN Tarjeta tar ON t.id_tarjeta = tar.id "
                    + "JOIN Cliente c ON tar.id_cliente = c.id "
                    + "WHERE c.id = ? "
                    + "AND YEAR(t.fecha) = ? "
                    + "AND MONTH(t.fecha) = ?";

            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, id);
                stmt.setInt(2, añoIngresado);
                stmt.setInt(3, mesIngresado);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    total = rs.getBigDecimal("totalGastado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e);
            }
        } catch (NumberFormatException e) {
            System.err.println("El ID del cliente, mes o año no es valido.");
        }

        return total;
    }

    private void MostrarReporteB() throws IOException {
        BigDecimal total = CrearReporteB();
        totalGastado.setText(total != null ? total.toString() : "0.00");

        guardarInformeEnArchivo(total);
    }

    private void guardarInformeEnArchivo(BigDecimal total) {
        File directorio = new File("Reportes");
        if (!directorio.exists()) {
            directorio.mkdir();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        File archivo = new File(directorio, "Reporte B" + timestamp + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("ID Cliente: " + idCliente.getText());
            writer.newLine();
            writer.write("Mes: " + mes.getText());
            writer.newLine();
            writer.write("Año: " + año.getText());
            writer.newLine();
            writer.write("Total Gastado: " + (total != null ? total.toString() : "0.00"));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }
}
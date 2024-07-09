package insanos.parcialfinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteCController {

    @FXML
    private TextField idCliente;
    @FXML
    private Button mostrarTarjetas;
    @FXML
    private Label tarjetasCredito;
    @FXML
    private Label tarjetasDebito;
    private Sistema sistema;

    @FXML
    public void initialize() {
        mostrarTarjetas.setOnAction(event -> {
            try {
                MostrarTarjetas();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<String> obtenerTarjetasPorTipo(int clienteId, String tipoTarjeta) throws IOException {
        List<String> tarjetas = new ArrayList<>();
        String query = "SELECT num_tarjeta FROM Tarjeta WHERE id_cliente = ? AND tipo_tarjeta = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clienteId);
            stmt.setString(2, tipoTarjeta);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Censurar el número de tarjeta dejando solo los últimos 4 dígitos
                String numTarjeta = rs.getString("num_tarjeta");
                String numTarjetaCensurado = "**** **** **** " + numTarjeta.substring(numTarjeta.length() - 4);
                tarjetas.add(numTarjetaCensurado);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e);
        }

        return tarjetas;
    }

    private void MostrarTarjetas() throws IOException {
        try {
            int clienteId = Integer.parseInt(idCliente.getText());

            List<String> tarjetasCreditoList = obtenerTarjetasPorTipo(clienteId, "credito");
            List<String> tarjetasDebitoList = obtenerTarjetasPorTipo(clienteId, "debito");

            tarjetasCredito.setText(tarjetasCreditoList.isEmpty() ? "N/A" : String.join(", ", tarjetasCreditoList));
            tarjetasDebito.setText(tarjetasDebitoList.isEmpty() ? "N/A" : String.join(", ", tarjetasDebitoList));

        } catch (NumberFormatException e) {
            System.err.println("El ID del cliente no es válido: " + idCliente.getText());
        }
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }
}

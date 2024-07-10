package insanos.parcialfinal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteDController {
    private Sistema sistema;

    @FXML
    private TableView<Cliente> tableViewClientes;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colApellido;
    @FXML
    private TableColumn<Cliente, Integer> colCantidadCompras;
    @FXML
    private TableColumn<Cliente, Double> colTotalGastado;
    @FXML
    private TextField txtFacilitador;

    private ObservableList<Cliente> listaClientes;

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCantidadCompras.setCellValueFactory(new PropertyValueFactory<>("cantidadCompras"));
        colTotalGastado.setCellValueFactory(new PropertyValueFactory<>("totalGastado"));

        listaClientes = FXCollections.observableArrayList();
        tableViewClientes.setItems(listaClientes);
    }

    @FXML
    private void Mostrar() {
        String facilitador = txtFacilitador.getText();
        cargarDatos(facilitador);
    }

    private void cargarDatos(String facilitador) {
        String sql = "SELECT " +
                "    c.id AS id_cliente, " +
                "    c.nombre AS nombre_cliente, " +
                "    c.apellido AS apellido_cliente, " +
                "    COUNT(t.id) AS cantidad_compras, " +
                "    SUM(t.precio) AS total_gastado " +
                "FROM " +
                "    Cliente c " +
                "JOIN " +
                "    Tarjeta ta ON c.id = ta.id_cliente " +
                "JOIN " +
                "    Transaccion t ON ta.id = t.id_tarjeta " +
                "WHERE " +
                "    ta.facilitador = ? " +
                "GROUP BY " +
                "    c.id, c.nombre, c.apellido " +
                "ORDER BY " +
                "    total_gastado DESC";

        listaClientes.clear();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, facilitador);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre_cliente"),
                        rs.getString("apellido_cliente"),
                        rs.getInt("cantidad_compras"),
                        rs.getDouble("total_gastado")
                );
                listaClientes.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    public static class Cliente {
        private final int id;
        private final String nombre;
        private final String apellido;
        private final int cantidadCompras;
        private final double totalGastado;

        public Cliente(int id, String nombre, String apellido, int cantidadCompras, double totalGastado) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.cantidadCompras = cantidadCompras;
            this.totalGastado = totalGastado;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public int getCantidadCompras() {
            return cantidadCompras;
        }

        public double getTotalGastado() {
            return totalGastado;
        }
    }
    public void Volver(){
        try {
            sistema.mostrarAdminScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

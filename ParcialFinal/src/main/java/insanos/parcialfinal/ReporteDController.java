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
    private Sistema sistema; // 00087023 Instancia del sistema para acceder a sus métodos y propiedades.

    @FXML
    private TableView<Cliente> tableViewClientes; // 00087023 TableView para mostrar los clientes.
    @FXML
    private TableColumn<Cliente, Integer> colId; // 00087023 Columna para el ID del cliente.
    @FXML
    private TableColumn<Cliente, String> colNombre; // 00087023 Columna para el nombre del cliente.
    @FXML
    private TableColumn<Cliente, String> colApellido; // 00087023 Columna para el apellido del cliente.
    @FXML
    private TableColumn<Cliente, Integer> colCantidadCompras; // 00087023 Columna para la cantidad de compras del cliente.
    @FXML
    private TableColumn<Cliente, Double> colTotalGastado; // 00087023 Columna para el total gastado por el cliente.
    @FXML
    private TextField txtFacilitador; // 00087023 Campo de texto para ingresar el facilitador de tarjeta.

    private ObservableList<Cliente> listaClientes; // 00087023 Lista observable de clientes para la TableView.

    // 00087023 Método inicializado al cargar el controlador.
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id")); // 00087023 Asocia la propiedad 'id' de Cliente con la columna 'colId'.
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre")); // 00087023 Asocia la propiedad 'nombre' de Cliente con la columna 'colNombre'.
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido")); // 00087023 Asocia la propiedad 'apellido' de Cliente con la columna 'colApellido'.
        colCantidadCompras.setCellValueFactory(new PropertyValueFactory<>("cantidadCompras")); // 00087023 Asocia la propiedad 'cantidadCompras' de Cliente con la columna 'colCantidadCompras'.
        colTotalGastado.setCellValueFactory(new PropertyValueFactory<>("totalGastado")); // 00087023 Asocia la propiedad 'totalGastado' de Cliente con la columna 'colTotalGastado'.

        listaClientes = FXCollections.observableArrayList(); // 00087023 Inicializa la lista observable de clientes.
        tableViewClientes.setItems(listaClientes); // 00087023 Asigna la lista de clientes a la TableView.
    }

    // 00087023 Método llamado al presionar el botón de mostrar reporte.
    @FXML
    private void Mostrar() {
        String facilitador = txtFacilitador.getText(); // 00087023 Obtiene el facilitador de tarjeta ingresado.
        cargarDatos(facilitador); // 00087023 Llama al método para cargar los datos según el facilitador.
    }

    // 00087023 Método para cargar los datos desde la base de datos según el facilitador.
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

        listaClientes.clear(); // 00087023 Limpia la lista de clientes antes de cargar nuevos datos.

        try (Connection conn = Database.getConnection(); // 00087023 Establece la conexión a la base de datos.
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, facilitador); // 00087023 Establece el valor del facilitador en la consulta preparada.

            ResultSet rs = stmt.executeQuery(); // 00087023 Ejecuta la consulta SQL y obtiene el resultado.

            // 00087023 Itera sobre el resultado y crea objetos Cliente con la información obtenida.
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre_cliente"),
                        rs.getString("apellido_cliente"),
                        rs.getInt("cantidad_compras"),
                        rs.getDouble("total_gastado")
                );
                listaClientes.add(cliente); // 00087023 Agrega el cliente a la lista observable.
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 00087023 Maneja las excepciones SQL imprimiendo el error.
        }
    }

    // 00087023 Método para establecer la instancia del sistema.
    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    // 00087023 Clase interna para representar la información de un cliente.
    public static class Cliente {
        private final int id; // 00087023 ID del cliente.
        private final String nombre; // 00087023 Nombre del cliente.
        private final String apellido; // 00087023 Apellido del cliente.
        private final int cantidadCompras; // 00087023 Cantidad de compras realizadas por el cliente.
        private final double totalGastado; // 00087023 Total gastado por el cliente.

        // 00087023 Constructor para inicializar los atributos del cliente.
        public Cliente(int id, String nombre, String apellido, int cantidadCompras, double totalGastado) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.cantidadCompras = cantidadCompras;
            this.totalGastado = totalGastado;
        }

        // 00087023 Métodos getter para obtener los valores de los atributos.
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
            sistema.mostrarAdminScreen(); //Funcion para volver a la pantalla de Admin
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

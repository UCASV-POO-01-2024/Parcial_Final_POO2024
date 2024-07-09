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

public class ReporteDController {
    private Sistema sistema; // 00087023 Esta línea define una variable para almacenar la referencia al sistema, permitiendo el acceso a sus métodos y propiedades en todo el controlador.
    @FXML
    private TextField facilitadorTarjeta; // 00087023 Esta línea define un campo de texto que permitirá al usuario ingresar el nombre del facilitador de la tarjeta (por ejemplo, Visa o Mastercard).
    @FXML
    private Button mostrarReporteD; // 00087023 Esta línea define un botón que, al ser presionado, desencadenará la generación y visualización del reporte D basado en el facilitador de tarjeta.
    @FXML
    private TableView<ClienteCompraInfo> tablaReporteD; // 00087023 Esta línea define una tabla que se utilizará para mostrar los datos del reporte D, organizados en filas y columnas.
    @FXML
    private TableColumn<ClienteCompraInfo, String> colCliente; // 00087023 Esta línea define una columna de la tabla que mostrará los nombres de los clientes que realizaron transacciones con el facilitador especificado.
    @FXML
    private TableColumn<ClienteCompraInfo, Integer> colCantidadCompras; // 00087023 Esta línea define una columna de la tabla que mostrará la cantidad de compras realizadas por cada cliente.
    @FXML
    private TableColumn<ClienteCompraInfo, BigDecimal> colTotalGastado; // 00087023 Esta línea define una columna de la tabla que mostrará el total de dinero gastado por cada cliente en sus transacciones.

    @FXML
    public void initialize() {
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente")); // 00087023 Configura la columna 'colCliente' para mostrar la propiedad 'nombreCliente' de los objetos ClienteCompraInfo en la tabla.
        colCantidadCompras.setCellValueFactory(new PropertyValueFactory<>("cantidadCompras")); // 00087023 Configura la columna 'colCantidadCompras' para mostrar la propiedad 'cantidadCompras' de los objetos ClienteCompraInfo en la tabla.
        colTotalGastado.setCellValueFactory(new PropertyValueFactory<>("totalGastado")); // 00087023 Configura la columna 'colTotalGastado' para mostrar la propiedad 'totalGastado' de los objetos ClienteCompraInfo en la tabla.

        mostrarReporteD.setOnAction(event -> { // 00087023 Establece un manejador de eventos para el botón 'mostrarReporteD' que ejecuta el método mostrarClientes al hacer clic.
            try {
                mostrarClientes(); // 00087023 Llama al método mostrarClientes para generar y mostrar el reporte D basado en el facilitador de tarjeta ingresado.
            } catch (IOException e) {
                e.printStackTrace(); // 00087023 Imprime la pila de errores en la consola si ocurre una excepción de entrada/salida durante la ejecución del método mostrarClientes.
            }
        });
    }

    private void mostrarClientes() throws IOException { // 00087023 Método que genera y muestra el reporte D obteniendo los datos de los clientes y guardándolos en un archivo.
        try {
            String facilitador = facilitadorTarjeta.getText(); // 00087023 Obtiene el texto ingresado por el usuario en el campo de texto 'facilitadorTarjeta'.
            List<ClienteCompraInfo> clientesInfo = obtenerClientesPorFacilitador(facilitador); // 00087023 Llama al método obtenerClientesPorFacilitador para obtener la información de los clientes según el facilitador.
            ObservableList<ClienteCompraInfo> clientesData = FXCollections.observableArrayList(clientesInfo); // 00087023 Convierte la lista de ClienteCompraInfo en una lista observable para actualizar la tabla.
            tablaReporteD.setItems(clientesData); // 00087023 Establece los datos de la tabla 'tablaReporteD' con los elementos de la lista observable 'clientesData'.
            guardarInformeEnArchivo(facilitador, clientesInfo); // 00087023 Llama al método guardarInformeEnArchivo para guardar los datos del reporte en un archivo de texto.
        } catch (Exception e) {
            System.err.println("Error al procesar el reporte: " + e.getMessage()); // 00087023 Imprime un mensaje de error en la consola si ocurre una excepción durante la ejecución del método mostrarClientes.
        }
    }

    private List<ClienteCompraInfo> obtenerClientesPorFacilitador(String facilitador) { // 00087023 Método que obtiene los datos de los clientes que realizaron transacciones con el facilitador especificado.
        List<ClienteCompraInfo> clientesInfo = new ArrayList<>(); // 00087023 Crea una lista para almacenar la información de los clientes que realizaron transacciones con el facilitador especificado.
        String query = "SELECT c.nombre, COUNT(t.id) AS cantidad_compras, SUM(t.monto) AS total_gastado " +
                "FROM Cliente c " +
                "JOIN Transaccion t ON c.id = t.id_cliente " +
                "JOIN Tarjeta ta ON t.id_tarjeta = ta.id " +
                "WHERE ta.facilitador = ? " +
                "GROUP BY c.nombre"; // 00087023 Define una consulta SQL para obtener el nombre del cliente, la cantidad de compras y el total gastado por cada cliente según el facilitador.

        try (Connection conn = Database.getConnection(); // 00087023 Obtiene una conexión a la base de datos utilizando el método getConnection de la clase Database.
             PreparedStatement stmt = conn.prepareStatement(query)) { // 00087023 Prepara la consulta SQL utilizando la conexión obtenida.
            stmt.setString(1, facilitador); // 00087023 Establece el parámetro de la consulta con el facilitador ingresado por el usuario.
            ResultSet rs = stmt.executeQuery(); // 00087023 Ejecuta la consulta SQL y obtiene los resultados en un ResultSet.
            while (rs.next()) { // 00087023 Itera sobre los resultados de la consulta SQL.
                String nombreCliente = rs.getString("nombre"); // 00087023 Obtiene el nombre del cliente de la columna 'nombre' del ResultSet.
                int cantidadCompras = rs.getInt("cantidad_compras"); // 00087023 Obtiene la cantidad de compras de la columna 'cantidad_compras' del ResultSet.
                BigDecimal totalGastado = rs.getBigDecimal("total_gastado"); // 00087023 Obtiene el total gastado de la columna 'total_gastado' del ResultSet.
                clientesInfo.add(new ClienteCompraInfo(nombreCliente, cantidadCompras, totalGastado)); // 00087023 Crea un nuevo objeto ClienteCompraInfo con los datos obtenidos y lo agrega a la lista clientesInfo.
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e); // 00087023 Lanza una excepción en tiempo de ejecución con un mensaje de error si ocurre una SQLException durante la ejecución de la consulta.
        }
        return clientesInfo; // 00087023 Devuelve la lista de ClienteCompraInfo con la información de los clientes que realizaron transacciones con el facilitador especificado.
    }

    private void guardarInformeEnArchivo(String facilitador, List<ClienteCompraInfo> clientesInfo) { // 00087023 Método que guarda los datos del reporte D en un archivo de texto.
        File directorio = new File("Reportes"); // 00087023 Crea un objeto File que representa el directorio 'Reportes'.
        if (!directorio.exists()) { // 00087023 Verifica si el directorio 'Reportes' no existe.
            directorio.mkdir(); // 00087023 Crea el directorio 'Reportes' si no existe.
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"); // 00087023 Define un formato para la fecha y hora actuales.
        String timestamp = LocalDateTime.now().format(formatter); // 00087023 Obtiene la fecha y hora actuales formateadas según el patrón definido.
        File archivo = new File(directorio, "Reporte D - " + timestamp + ".txt"); // 00087023 Crea un archivo de texto en el directorio 'Reportes' con un nombre que incluye la fecha y hora actuales.

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) { // 00087023 Crea un BufferedWriter para escribir en el archivo de texto creado.
            writer.write("Facilitador de Tarjeta: " + facilitador); // 00087023 Escribe el nombre del facilitador de tarjeta en el archivo.
            writer.newLine(); // 00087023 Escribe una nueva línea en el archivo.
            for (ClienteCompraInfo info : clientesInfo) { // 00087023 Itera sobre la lista de ClienteCompraInfo.
                writer.write("Cliente: " + info.getNombreCliente() + ", Compras: " + info.getCantidadCompras() + ", Total Gastado: $" + info.getTotalGastado()); // 00087023 Escribe la información de cada cliente en el archivo.
                writer.newLine(); // 00087023 Escribe una nueva línea en el archivo.
            }
        } catch (IOException e) {
            e.printStackTrace(); // 00087023 Imprime la pila de errores en la consola si ocurre una excepción de entrada/salida durante la escritura en el archivo.
        }
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema; // 00087023 Establece la referencia al sistema, permitiendo el acceso a sus métodos y propiedades desde el controlador.
    }
}

// 00087023 Clase que representa la información de las compras de un cliente.
class ClienteCompraInfo {
    private String nombreCliente; // 00087023 Define una variable para almacenar el nombre del cliente.
    private int cantidadCompras; // 00087023 Define una variable para almacenar la cantidad de compras realizadas por el cliente.
    private BigDecimal totalGastado; // 00087023 Define una variable para almacenar el total de dinero gastado por el cliente.

    public ClienteCompraInfo(String nombreCliente, int cantidadCompras, BigDecimal totalGastado) {
        this.nombreCliente = nombreCliente; // 00087023 Establece el nombre del cliente en el constructor.
        this.cantidadCompras = cantidadCompras; // 00087023 Establece la cantidad de compras en el constructor.
        this.totalGastado = totalGastado; // 00087023 Establece el total gastado en el constructor.
    }

    public String getNombreCliente() {
        return nombreCliente; // 00087023 Devuelve el nombre del cliente.
    }

    public int getCantidadCompras() {
        return cantidadCompras; // 00087023 Devuelve la cantidad de compras realizadas por el cliente.
    }

    public BigDecimal getTotalGastado() {
        return totalGastado; // 00087023 Devuelve el total de dinero gastado por el cliente.
    }
}

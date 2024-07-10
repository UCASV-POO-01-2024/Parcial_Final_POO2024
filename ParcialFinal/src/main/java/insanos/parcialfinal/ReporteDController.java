package insanos.parcialfinal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import java.util.ArrayList;
import java.util.List;
public class ReporteDController {

    @FXML
    private TextField facilitadorTarjeta; //00087023 Campo de texto para ingresar el nombre del facilitador de la tarjeta.

    @FXML
    private Button mostrarReporteD; // 00087023 Botón para mostrar el reporte D.

    private Sistema sistema; //00087023 Instancia de la clase Sistema para acceder a sus métodos y propiedades.
    
    @FXML
    public void initialize() {
        mostrarReporteD.setOnAction(event -> {
            try {
                MostrarReporteD(); //00087023 Llama al método para mostrar el reporte D cuando se presiona el botón.
            } catch (IOException e) {
                e.printStackTrace(); //00087023 Maneja la excepción imprimiendo el error en la consola.
            }
        });
    }
    private List<insanos.parcialfinal.ClienteCompraInfo> CrearReporteD() throws IOException {
        List<insanos.parcialfinal.ClienteCompraInfo> clientesInfo = new ArrayList<>(); //00087023 Lista para almacenar la información de los clientes.

        String facilitador = facilitadorTarjeta.getText(); //00087023 Obtiene el texto ingresado en el campo facilitadorTarjeta.

        //00087023 Consulta SQL para obtener clientes, cantidad de compras y total gastado por un facilitador específico.
        String query = "SELECT c.nombre, COUNT(t.id) AS cantidad_compras, SUM(t.precio) AS total_gastado " +
                "FROM Cliente c " +
                "JOIN Tarjeta tar ON c.id = tar.id_cliente " +
                "JOIN Transaccion t ON tar.id = t.id_tarjeta " +
                "WHERE tar.facilitador = ? " +
                "GROUP BY c.nombre";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, facilitador); //00087023 Establece el valor del facilitador en la consulta preparada.
            ResultSet rs = stmt.executeQuery(); //00087023 Ejecuta la consulta y obtiene los resultados.

            //00087023 Itera sobre los resultados y añade la información de cada cliente a la lista.
            while (rs.next()) {
                String nombreCliente = rs.getString("nombre"); //00087023 Obtiene el nombre del cliente del resultado.
                int cantidadCompras = rs.getInt("cantidad_compras"); //00087023 Obtiene la cantidad de compras del resultado.
                BigDecimal totalGastado = rs.getBigDecimal("total_gastado"); //00087023 Obtiene el total gastado del resultado.

                clientesInfo.add(new insanos.parcialfinal.ClienteCompraInfo(nombreCliente, cantidadCompras, totalGastado)); //00087023 Añade la información del cliente a la lista.
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e); //00087023 Maneja las excepciones SQL lanzando un RuntimeException con un mensaje descriptivo.
        }

        return clientesInfo; //00087023 Devuelve la lista de información de los clientes.
    }

    
    private void MostrarReporteD() throws IOException {
        List<insanos.parcialfinal.ClienteCompraInfo> clientesInfo = CrearReporteD(); //00087023 Crea el reporte obteniendo la información de los clientes.
        guardarInformeEnArchivo(clientesInfo); //00087023 Guarda el informe en un archivo.
    }

    
    private void guardarInformeEnArchivo(List<insanos.parcialfinal.ClienteCompraInfo> clientesInfo) {
        File directorio = new File("Reportes"); //00087023 Define el directorio donde se guardarán los reportes.
        if (!directorio.exists()) {
            directorio.mkdir(); //00087023 Crea el directorio si no existe.
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"); //00087023 Define el formato de la fecha y hora.
        String timestamp = LocalDateTime.now().format(formatter); //00087023 Obtiene la fecha y hora actual en el formato definido.
        File archivo = new File(directorio, "Reporte D_" + timestamp + ".txt"); //00087023 Define el nombre del archivo de reporte con el timestamp.

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("Facilitador de Tarjeta: " + facilitadorTarjeta.getText()); //00087023 Escribe el nombre del facilitador de la tarjeta en el archivo.
            writer.newLine(); //00087023 Escribe una nueva línea en el archivo.

            //00087023 Escribe la información de cada cliente en el archivo.
            for (insanos.parcialfinal.ClienteCompraInfo info : clientesInfo) {
                writer.write("Cliente: " + info.getNombreCliente() + ", Compras: " + info.getCantidadCompras() + ", Total Gastado: $" + info.getTotalGastado());
                writer.newLine(); //00087023 Escribe una nueva línea en el archivo.
            }
        } catch (IOException e) {
            e.printStackTrace(); //00087023 Maneja la excepción imprimiendo el error en la consola.
        }
    }
    public void setSistema(Sistema sistema) {
        this.sistema = sistema; //00087023Establece la referencia al sistema.
    }
}
class ClienteCompraInfo {
    private String nombreCliente; //00087023 Nombre del cliente.
    private int cantidadCompras; //00087023 Cantidad de compras realizadas por el cliente.
    private BigDecimal totalGastado; //00087023 Total de dinero gastado por el cliente.
    public ClienteCompraInfo(String nombreCliente, int cantidadCompras, BigDecimal totalGastado) {
        this.nombreCliente = nombreCliente; //00087023 Establece el nombre del cliente.
        this.cantidadCompras = cantidadCompras; //00087023 Establece la cantidad de compras.
        this.totalGastado = totalGastado; //00087023 Establece el total gastado.
    }
    public String getNombreCliente() {
        return nombreCliente; //00087023 Devuelve el nombre del cliente.
    }
    
    public int getCantidadCompras() {
        return cantidadCompras; //00087023 Devuelve la cantidad de compras.
    }
    public BigDecimal getTotalGastado() {
        return totalGastado; //00087023 Devuelve el total gastado.
    }
}


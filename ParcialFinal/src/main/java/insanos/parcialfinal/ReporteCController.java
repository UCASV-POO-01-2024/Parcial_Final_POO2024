package insanos.parcialfinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReporteCController {

    @FXML
    private TextField idCliente; //00092322 Campo de texto para ingresar el ID del cliente
    @FXML
    private Button mostrarTarjetas; //00092322 Botón para mostrar las tarjetas del cliente
    @FXML
    private Label tarjetasCredito; //00092322 Etiqueta para mostrar las tarjetas de crédito del cliente
    @FXML
    private Label tarjetasDebito; //00092322 Etiqueta para mostrar las tarjetas de débito del cliente
    @FXML
    private Button volver; //00092322 Botón para volver a la pantalla anterior
    private Sistema sistema; //00092322 Referencia al sistema principal

    @FXML
    public void initialize() { //00092322 Método de inicialización del controlador
        mostrarTarjetas.setOnAction(event -> { //00092322 Asigna un manejador de eventos al botón mostrarTarjetas
            try {
                MostrarTarjetas(); //00092322 Llama al método para mostrar las tarjetas
            } catch (IOException e) { //00092322 Captura posibles excepciones de entrada/salida
                e.printStackTrace(); //00092322 Imprime la pila de errores en caso de excepción
            }
        });
        volver.setOnAction(e -> { //00092322 Asigna un manejador de eventos al botón volver
            try {
                sistema.mostrarAdminScreen(); //00092322 Llama al método para mostrar la pantalla de administración
            } catch (IOException ex) { //00092322 Captura posibles excepciones de entrada/salida
                throw new RuntimeException(ex); //00092322 Lanza una excepción en caso de error
            }
        });
    }

    private List<String> obtenerTarjetasPorTipo(int clienteId, String tipoTarjeta) throws IOException { //00092322 Método para obtener tarjetas por tipo
        List<String> tarjetas = new ArrayList<>(); //00092322 Lista para almacenar las tarjetas
        String query = "SELECT num_tarjeta FROM Tarjeta WHERE id_cliente = ? AND tipo_tarjeta = ?"; //00092322 Consulta SQL para obtener tarjetas

        try (Connection conn = Database.getConnection(); //00092322 Obtiene una conexión a la base de datos
             PreparedStatement stmt = conn.prepareStatement(query)) { //00092322 Prepara la consulta SQL

            stmt.setInt(1, clienteId); //00092322 Establece el ID del cliente en la consulta
            stmt.setString(2, tipoTarjeta); //00092322 Establece el tipo de tarjeta en la consulta

            ResultSet rs = stmt.executeQuery(); //00092322 Ejecuta la consulta y obtiene los resultados
            while (rs.next()) { //00092322 Itera sobre los resultados de la consulta

                String numTarjeta = rs.getString("num_tarjeta"); //00092322 Obtiene el número de tarjeta
                String numTarjetaCensurado = "**** **** **** " + numTarjeta.substring(numTarjeta.length() - 4); //00092322 Censura el número de tarjeta
                tarjetas.add(numTarjetaCensurado); //00092322 Añade la tarjeta censurada a la lista
            }

        } catch (SQLException e) { //00092322 Captura posibles excepciones SQL
            throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e); //00092322 Lanza una excepción en caso de error
        }

        return tarjetas; //00092322 Devuelve la lista de tarjetas
    }

    private void MostrarTarjetas() throws IOException { //00092322 Método para mostrar las tarjetas
        try {
            int clienteId = Integer.parseInt(idCliente.getText()); //00092322 Obtiene y convierte el ID del cliente del campo de texto

            List<String> tarjetasCreditoList = obtenerTarjetasPorTipo(clienteId, "credito"); //00092322 Obtiene la lista de tarjetas de crédito
            List<String> tarjetasDebitoList = obtenerTarjetasPorTipo(clienteId, "debito"); //00092322 Obtiene la lista de tarjetas de débito

            tarjetasCredito.setText(tarjetasCreditoList.isEmpty() ? "N/A" : String.join(", ", tarjetasCreditoList)); //00092322 Actualiza la etiqueta de tarjetas de crédito
            tarjetasDebito.setText(tarjetasDebitoList.isEmpty() ? "N/A" : String.join(", ", tarjetasDebitoList)); //00092322 Actualiza la etiqueta de tarjetas de débito

            guardarInformeEnArchivo(clienteId, tarjetasCreditoList, tarjetasDebitoList); //00092322 Llama al método para guardar el informe en un archivo

        } catch (NumberFormatException e) { //00092322 Captura posibles excepciones de formato de número
            System.err.println("El ID del cliente no es válido: " + idCliente.getText()); //00092322 Imprime un mensaje de error en caso de excepción
        }
    }

    private void guardarInformeEnArchivo(int clienteId, List<String> tarjetasCredito, List<String> tarjetasDebito) { //00092322 Método para guardar el informe en un archivo
        File directorio = new File("Reportes"); //00092322 Crea un objeto File para el directorio de reportes
        if (!directorio.exists()) { //00092322 Verifica si el directorio no existe
            directorio.mkdir(); //00092322 Crea el directorio si no existe
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"); //00092322 Formateador de fecha y hora
        String timestamp = LocalDateTime.now().format(formatter); //00092322 Obtiene la fecha y hora actual formateada
        File archivo = new File(directorio, "Reporte C" + timestamp + ".txt"); //00092322 Crea un archivo para el informe con un nombre basado en la fecha y hora

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) { //00092322 Crea un BufferedWriter para escribir en el archivo
            writer.write("ID Cliente: " + clienteId); //00092322 Escribe el ID del cliente en el archivo
            writer.newLine(); //00092322 Añade una nueva línea
            writer.write("Tarjetas de Crédito: " + (tarjetasCredito.isEmpty() ? "N/A" : String.join(", ", tarjetasCredito))); //00092322 Escribe las tarjetas de crédito en el archivo
            writer.newLine(); //00092322 Añade una nueva línea
            writer.write("Tarjetas de Débito: " + (tarjetasDebito.isEmpty() ? "N/A" : String.join(", ", tarjetasDebito))); //00092322 Escribe las tarjetas de débito en el archivo
            writer.newLine(); //00092322 Añade una nueva línea
        } catch (IOException e) { //00092322 Captura posibles excepciones de entrada/salida
            e.printStackTrace(); //00092322 Imprime la pila de errores en caso de excepción
        }
    }

    public void setSistema(Sistema sistema) { //00092322 Método para establecer la referencia al sistema principal
        this.sistema = sistema; //00092322 Asigna el sistema principal al campo `sistema`
    }
}

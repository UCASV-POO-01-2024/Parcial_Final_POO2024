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
    private Sistema sistema; //00092322 Referencia al sistema principal
    @FXML
    private TextField idCliente; //00092322 Campo de texto para ingresar el ID del cliente
    @FXML
    private DatePicker fechaInicio; //00092322 Selector de fecha para la fecha de inicio
    @FXML
    private DatePicker fechaFinal; //00092322 Selector de fecha para la fecha final
    @FXML
    private Button mostrarReporteA; //00092322 Botón para mostrar el reporte
    @FXML
    private Button volver; //00092322 Botón para volver a la pantalla anterior
    @FXML
    private TableView<Transaccion> TablaConsultaA; //00092322 Tabla para mostrar las transacciones
    @FXML
    private TableColumn<Transaccion, Integer> colId; //00092322 Columna para el ID de la transacción
    @FXML
    private TableColumn<Transaccion, LocalDateTime> colFecha; //00092322 Columna para la fecha de la transacción
    @FXML
    private TableColumn<Transaccion, BigDecimal> colMonto; //00092322 Columna para el monto de la transacción
    @FXML
    private TableColumn<Transaccion, String> colDescripcion; //00092322 Columna para la descripción de la transacción

    @FXML
    public void initialize() { //00092322 Método de inicialización del controlador
        colId.setCellValueFactory(new PropertyValueFactory<>("IDtransaccion")); //00092322 Asocia la columna ID con el atributo IDtransaccion de la clase Transaccion
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha")); //00092322 Asocia la columna Fecha con el atributo fecha de la clase Transaccion
        colMonto.setCellValueFactory(new PropertyValueFactory<>("cantidad")); //00092322 Asocia la columna Monto con el atributo cantidad de la clase Transaccion
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion")); //00092322 Asocia la columna Descripción con el atributo descripcion de la clase Transaccion

        mostrarReporteA.setOnAction(event -> { //00092322 Asigna un manejador de eventos al botón mostrarReporteA
            try {
                MostrarReporteA(); //00092322 Llama al método para mostrar el reporte
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

    private List<Transaccion> CrearReporteA() throws IOException { //00092322 Método para crear el reporte A
        List<Transaccion> transacciones = new ArrayList<>(); //00092322 Lista para almacenar las transacciones
        try {
            int id = Integer.parseInt(idCliente.getText()); //00092322 Obtiene y convierte el ID del cliente del campo de texto
            LocalDate fechaInicial = fechaInicio.getValue(); //00092322 Obtiene la fecha inicial del selector de fecha
            LocalDate fechaFin = fechaFinal.getValue(); //00092322 Obtiene la fecha final del selector de fecha

            String query = "SELECT t.id, t.fecha, t.precio, t.descripcion " //00092322 Consulta SQL para obtener las transacciones
                    + "FROM Transaccion t "
                    + "JOIN Tarjeta tar ON t.id_tarjeta = tar.id "
                    + "JOIN Cliente c ON tar.id_cliente = c.id "
                    + "WHERE c.id = ? "
                    + "AND t.fecha BETWEEN ? AND ? "
                    + "ORDER BY t.fecha";

            try (Connection conn = Database.getConnection(); //00092322 Obtiene una conexión a la base de datos
                 PreparedStatement stmt = conn.prepareStatement(query)) { //00092322 Prepara la consulta SQL

                stmt.setInt(1, id); //00092322 Establece el ID del cliente en la consulta
                stmt.setDate(2, Date.valueOf(fechaInicial)); //00092322 Establece la fecha inicial en la consulta
                stmt.setDate(3, Date.valueOf(fechaFin)); //00092322 Establece la fecha final en la consulta

                ResultSet rs = stmt.executeQuery(); //00092322 Ejecuta la consulta y obtiene los resultados
                while (rs.next()) { //00092322 Itera sobre los resultados de la consulta
                    Transaccion transaccion = new Transaccion( //00092322 Crea un objeto Transaccion
                            rs.getInt("id"), //00092322 Establece el ID de la transacción
                            rs.getTimestamp("fecha").toLocalDateTime(), //00092322 Establece la fecha de la transacción
                            rs.getBigDecimal("precio"), //00092322 Establece el monto de la transacción
                            rs.getString("descripcion"), //00092322 Establece la descripción de la transacción
                            null //00092322 Establece el número de tarjeta como nulo
                    );
                    transacciones.add(transaccion); //00092322 Añade la transacción a la lista
                }
            } catch (SQLException e) { //00092322 Captura posibles excepciones SQL
                throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e); //00092322 Lanza una excepción en caso de error
            }
        } catch (NumberFormatException e) { //00092322 Captura posibles excepciones de formato de número
            System.err.println("El ID del cliente no es válido: " + idCliente.getText()); //00092322 Imprime un mensaje de error en caso de excepción
        }

        return transacciones; //00092322 Devuelve la lista de transacciones
    }

    private void MostrarReporteA() throws IOException { //00092322 Método para mostrar el reporte A
        List<Transaccion> transacciones = CrearReporteA(); //00092322 Crea el reporte A
        ObservableList<Transaccion> transaccionesData = FXCollections.observableArrayList(transacciones); //00092322 Convierte la lista de transacciones a un ObservableList
        TablaConsultaA.setItems(transaccionesData); //00092322 Establece los datos en la tabla

        guardarInformeEnArchivo(transacciones); //00092322 Llama al método para guardar el informe en un archivo
    }

    private void guardarInformeEnArchivo(List<Transaccion> transacciones) { //00092322 Método para guardar el informe en un archivo
        File directorio = new File("Reportes"); //00092322 Crea un objeto File para el directorio de reportes
        if (!directorio.exists()) { //00092322 Verifica si el directorio no existe
            directorio.mkdir(); //00092322 Crea el directorio si no existe
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"); //00092322 Formateador de fecha y hora
        String timestamp = LocalDateTime.now().format(formatter); //00092322 Obtiene la fecha y hora actual formateada
        File archivo = new File(directorio, "reporte A" + timestamp + ".txt"); //00092322 Crea un archivo para el informe con un nombre basado en la fecha y hora

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) { //00092322 Crea un BufferedWriter para escribir en el archivo
            for (Transaccion transaccion : transacciones) { //00092322 Itera sobre las transacciones
                writer.write("ID: " + transaccion.getIDtransaccion() + ", "); //00092322 Escribe el ID de la transacción en el archivo
                writer.write("Fecha: " + transaccion.getFecha() + ", "); //00092322 Escribe la fecha de la transacción en el archivo
                writer.write("Monto: " + transaccion.getCantidad() + ", "); //00092322 Escribe el monto de la transacción en el archivo
                writer.write("Descripcion: " + transaccion.getDescripcion()); //00092322 Escribe la descripción de la transacción en el archivo
                writer.newLine(); //00092322 Añade una nueva línea
            }
        } catch (IOException e) { //00092322 Captura posibles excepciones de entrada/salida
            e.printStackTrace(); //00092322 Imprime la pila de errores en caso de excepción
        }
    }

    public void setSistema(Sistema sistema) { //00092322 Método para establecer la referencia al sistema principal
        this.sistema = sistema; //00092322 Asigna el sistema principal al campo `sistema`
    }
}

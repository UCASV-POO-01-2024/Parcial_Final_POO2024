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

public class ReporteBController { //00099022 Declara la clase controladora para el Reporte B
    private Sistema sistema; //00099022 Variable para manejar la logica del sistema

    @FXML // Indica que los siguientes elementos estan vinculados a la vista FXML
    private TextField idCliente; //00099022 Campo de texto para ingresar el ID del cliente
    @FXML
    private TextField mes; //00099022 Campo de texto para ingresar el mes
    @FXML
    private TextField año; //00099022 Campo de texto para ingresar el año
    @FXML
    private Button mostrarReporteB; //00099022 Boton para mostrar el reporte
    @FXML
    private Label totalGastado; //00099022 Etiqueta para mostrar el total gastado

    @FXML
    public void initialize() { //00099022 Metodo que se ejecuta al inicializar el controlador
        mostrarReporteB.setOnAction(event -> { //00099022 Define accion al hacer clic en el boton
            try {
                MostrarReporteB(); //00099022 Llama al metodo para mostrar el reporte
            } catch (IOException e) { //00099022 Captura y maneja excepciones de entrada/salida
                e.printStackTrace(); //00099022 Imprime el seguimiento de la pila de errores
            }
        });
    }

    private BigDecimal CrearReporteB() throws IOException { //00099022 Metodo para crear el reporte B
        BigDecimal total = BigDecimal.ZERO; //00099022 Inicializa el total en 0
        try {
            int id = Integer.parseInt(idCliente.getText()); //00099022 Convierte el ID del cliente a entero
            int mesIngresado = Integer.parseInt(mes.getText()); //00099022 Convierte el mes a entero
            int añoIngresado = Integer.parseInt(año.getText()); //00099022 Convierte el año a entero

            String query = "SELECT SUM(t.precio) as totalGastado " //00099022 Consulta SQL para sumar precios
                    + "FROM Transaccion t " //00099022 Selecciona de la tabla Transaccion
                    + "JOIN Tarjeta tar ON t.id_tarjeta = tar.id " //00099022 Une con la tabla Tarjeta
                    + "JOIN Cliente c ON tar.id_cliente = c.id " //00099022 Une con la tabla Cliente
                    + "WHERE c.id = ? " //00099022 Filtra por ID de cliente
                    + "AND YEAR(t.fecha) = ? " //00099022 Filtra por año de la transaccion
                    + "AND MONTH(t.fecha) = ?"; //00099022 Filtra por mes de la transaccion

            try (Connection conn = Database.getConnection(); //00099022 Obtiene conexion a la base de datos
                 PreparedStatement stmt = conn.prepareStatement(query)) { //00099022 Prepara la consulta SQL

                stmt.setInt(1, id); //00099022 Establece el primer parametro de la consulta
                stmt.setInt(2, añoIngresado); //00099022 Establece el segundo parametro de la consulta
                stmt.setInt(3, mesIngresado); //00099022 Establece el tercer parametro de la consulta

                ResultSet rs = stmt.executeQuery(); //00099022 Ejecuta la consulta y obtiene resultados
                if (rs.next()) { //00099022 Si hay resultados
                    total = rs.getBigDecimal("totalGastado"); //00099022 Asigna el total gastado al resultado
                }
            } catch (SQLException e) { //00099022 Captura y maneja excepciones SQL
                throw new RuntimeException("Error al ejecutar la consulta: " + e.getMessage(), e); //00099022 tira excepcion con mensaje
            }
        } catch (NumberFormatException e) { //00099022 Captura y maneja excepciones de formato numerico
            System.err.println("El ID del cliente, mes o año no es valido."); //00099022 Imprime mensaje de error
        }

        return total; //00099022 Retorna el total gastado
    }

    private void MostrarReporteB() throws IOException { //00099022 Metodo para mostrar el reporte B
        BigDecimal total = CrearReporteB(); //00099022 Crea el reporte y obtiene el total gastado
        totalGastado.setText(total != null ? total.toString() : "0.00"); //00099022 Muestra el total en la etiqueta

        guardarInformeEnArchivo(total); //00099022 Guarda el informe en un archivo
    }

    private void guardarInformeEnArchivo(BigDecimal total) { //00099022 Metodo para guardar el informe en un archivo
        File directorio = new File("Reportes"); //00099022 Crea un objeto File para el directorio Reportes
        if (!directorio.exists()) { //00099022 Si el directorio no existe
            directorio.mkdir(); //00099022 Crea el directorio
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"); //00099022 Define el formato para la fecha y hora
        String timestamp = LocalDateTime.now().format(formatter); //00099022 Obtiene la fecha y hora actual formateada
        File archivo = new File(directorio, "Reporte B" + timestamp + ".txt"); //00099022 Crea un archivo con el nombre Reporte B y la fecha y hora actual

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) { //00099022 Crea un BufferedWriter para escribir en el archivo
            writer.write("ID Cliente: " + idCliente.getText()); //00099022 Escribe el ID del cliente en el archivo
            writer.newLine(); //00099022 Añade una nueva línea
            writer.write("Mes: " + mes.getText()); //00099022 Escribe el mes en el archivo
            writer.newLine(); //00099022 Añade una nueva línea
            writer.write("Año: " + año.getText()); //00099022 Escribe el año en el archivo
            writer.newLine(); //00099022 Añade una nueva línea
            writer.write("Total Gastado: " + (total != null ? total.toString() : "0.00")); //00099022 Escribe el total gastado en el archivo
            writer.newLine(); //00099022 Añade una nueva línea
        } catch (IOException e) { //00099022 Captura y maneja excepciones de entrada/salida
            e.printStackTrace(); //00099022 Imprime el seguimiento de la pila de errores
        }
    }

    public void setSistema(Sistema sistema) { //00099022 Metodo para establecer el sistema
        this.sistema = sistema; //00099022 Asigna el sistema a la variable de instancia
    }
}
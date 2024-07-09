package insanos.parcialfinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminController {

    //00092322 Botones para cada reporte
    @FXML
    private Button ReporteA; //00092322 Botón para el Reporte A
    @FXML
    private Button ReporteB; //00092322 Botón para el Reporte B
    @FXML
    private Button ReporteC; //00092322 Botón para el Reporte C
    @FXML
    private Button ReporteD; //00092322 Botón para el Reporte D
    @FXML
    private TextField idCliente; //00092322 Donde el usuario ingresa el ID del cliente
    @FXML
    private DatePicker fechaInicio; //00092322 DatePicker para seleccionar la fecha de inicio
    @FXML
    private DatePicker fechaFinal; //00092322 DatePicker para seleccionar la fecha final
    @FXML
    private Button salir; //00092322 Botón para salir
    private Sistema sistema; //00092322 Referencia a la clase Sistema

    public AdminController() { //00092322 Constructor vacío de AdminController
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema; //00092322 Asigna la instancia del sistema al controlador
    }

    @FXML
    private void initialize() { //00092322 Método para inicializar el controlador
        ReporteA.setOnAction(e -> { //00092322 Define la acción al presionar el botón ReporteA
            try {
                sistema.mostrarReporteA(); //00092322 Llama al método del sistema para mostrar el Reporte A
            } catch (IOException ex) {
                ex.printStackTrace(); //00092322 Imprime la traza de la excepción
                throw new RuntimeException(ex); //00092322 Lanza una RuntimeException con la causa original
            }
        });

        ReporteB.setOnAction(e -> { //00092322 Define la acción al presionar el botón ReporteB
            try {
                sistema.mostrarReporteB(); //00092322 Llama al método del sistema para mostrar el Reporte B
            } catch (IOException ex) {
                throw new RuntimeException(ex); //00092322 Lanza una RuntimeException con la causa original
            }
        });

        ReporteC.setOnAction(e -> { //00092322 Define la acción al presionar el botón ReporteC
            try {
                sistema.mostrarReporteC(); //00092322 Llama al método del sistema para mostrar el Reporte C
            } catch (IOException ex) {
                throw new RuntimeException(ex); //00092322 Lanza una RuntimeException con la causa original
            }
        });

        ReporteD.setOnAction(e -> { //00092322 Define la acción al presionar el botón ReporteD
            try {
                sistema.mostrarReporteD(); //00092322 Llama al método del sistema para mostrar el Reporte D
            } catch (IOException ex) {
                throw new RuntimeException(ex); //00092322 Lanza una RuntimeException con la causa original
            }
        });

        salir.setOnAction(e -> { //00092322 Define la acción al presionar el botón Salir
            try {
                sistema.mostrarLogin(); //00092322 Llama al método del sistema para mostrar la pantalla de login
            } catch (IOException ex) {
                throw new RuntimeException(ex); //00092322 Lanza una RuntimeException con la causa original
            }
        });
    }
}

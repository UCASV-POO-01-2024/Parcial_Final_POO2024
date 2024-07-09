package insanos.parcialfinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    @FXML
    private Button ReporteA;
    @FXML
    private Button ReporteB;
    @FXML
    private Button ReporteC;
    @FXML
    private Button ReporteD;
    @FXML
    private TextField idCliente;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFinal;
    private Sistema sistema;

    public AdminController() {
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    @FXML
    private void initialize() {
        ReporteA.setOnAction(e -> {
            try {
                    sistema.mostrarReporteA();

            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        ReporteB.setOnAction(e -> {
            try {
                sistema.mostrarReporteB();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ReporteC.setOnAction(e -> {
            try {
                sistema.mostrarReporteC();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ReporteD.setOnAction(e -> {
            try {
                sistema.mostrarReporteD();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}

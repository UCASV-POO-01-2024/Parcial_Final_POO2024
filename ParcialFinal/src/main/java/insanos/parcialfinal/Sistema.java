package insanos.parcialfinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Sistema extends Application {
    private Stage stage; //00092322 Variable de instancia para el escenario principal

    public static void main(String[] args) { //00092322 Método principal que lanza la aplicación
        launch(args); //00092322 Lanza la aplicación JavaFX
    }

    @Override
    public void start(Stage stage) throws IOException { //00092322 Método de inicio de la aplicación
        this.stage = stage; //00092322 Asigna el escenario a la variable de instancia
        mostrarLogin(); //00092322 Muestra la pantalla de inicio de sesión
    }

    public void mostrarLogin() throws IOException { //00092322 Método para mostrar la pantalla de inicio de sesión
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/Login.fxml")); //00092322 Carga el archivo FXML del login
        Parent root = loader.load(); //00092322 Carga el contenido del archivo FXML

        LoginController controller = loader.getController(); //00092322 Obtiene el controlador asociado al archivo FXML
        controller.setSistema(this); //00092322 Establece el sistema en el controlador

        Scene scene = new Scene(root); //00092322 Crea una nueva escena con el contenido cargado
        stage.setTitle("Sistema"); //00092322 Establece el título del escenario
        stage.setScene(scene); //00092322 Establece la escena en el escenario
        stage.show(); //00092322 Muestra el escenario
    }

    public void mostrarReporteA() throws IOException { //00092322 Método para mostrar el reporte A
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteA.fxml")); //00092322 Carga el archivo FXML del reporte A
        Parent root = loader.load(); //00092322 Carga el contenido del archivo FXML
        ReporteAController controller = loader.getController(); //00092322 Obtiene el controlador asociado al archivo FXML
        controller.setSistema(this); //00092322 Establece el sistema en el controlador
        Scene scene = new Scene(root); //00092322 Crea una nueva escena con el contenido cargado
        stage.setScene(scene); //00092322 Establece la escena en el escenario
        stage.show(); //00092322 Muestra el escenario
    }

    public void mostrarReporteB() throws IOException { //00092322 Método para mostrar el reporte B
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteB.fxml")); //00092322 Carga el archivo FXML del reporte B
        Parent root = loader.load(); //00092322 Carga el contenido del archivo FXML
        ReporteBController controller = loader.getController(); //00092322 Obtiene el controlador asociado al archivo FXML
        controller.setSistema(this); //00092322 Establece el sistema en el controlador
        Scene scene = new Scene(root); //00092322 Crea una nueva escena con el contenido cargado
        stage.setScene(scene); //00092322 Establece la escena en el escenario
        stage.show(); //00092322 Muestra el escenario
    }

    public void mostrarReporteC() throws IOException { //00092322 Método para mostrar el reporte C
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteC.fxml")); //00092322 Carga el archivo FXML del reporte C
        Parent root = loader.load(); //00092322 Carga el contenido del archivo FXML
        ReporteCController controller = loader.getController(); //00092322 Obtiene el controlador asociado al archivo FXML
        controller.setSistema(this); //00092322 Establece el sistema en el controlador
        Scene scene = new Scene(root); //00092322 Crea una nueva escena con el contenido cargado
        stage.setScene(scene); //00092322 Establece la escena en el escenario
        stage.show(); //00092322 Muestra el escenario
    }

    public void mostrarReporteD() throws IOException { //00092322 Método para mostrar el reporte D
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteD.fxml")); //00092322 Carga el archivo FXML del reporte D
        Parent root = loader.load(); //00092322 Carga el contenido del archivo FXML
        ReporteDController controller = loader.getController(); //00092322 Obtiene el controlador asociado al archivo FXML
        controller.setSistema(this); //00092322 Establece el sistema en el controlador
        Scene scene = new Scene(root); //00092322 Crea una nueva escena con el contenido cargado
        stage.setScene(scene); //00092322 Establece la escena en el escenario
        stage.show(); //00092322 Muestra el escenario
    }

    public void mostrarAdminScreen() throws IOException { //00092322 Método para mostrar la pantalla de administración
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/AdminScreen.fxml")); //00092322 Carga el archivo FXML de la pantalla de administración
        Parent root = loader.load(); //00092322 Carga el contenido del archivo FXML
        AdminController controller = loader.getController(); //00092322 Obtiene el controlador asociado al archivo FXML
        controller.setSistema(this); //00092322 Establece el sistema en el controlador
        Scene scene = new Scene(root); //00092322 Crea una nueva escena con el contenido cargado
        stage.setScene(scene); //00092322 Establece la escena en el escenario
        stage.show(); //00092322 Muestra el escenario
    }

    public void setStage(Stage stage) { //00092322 Método para establecer el escenario
        this.stage = stage; //00092322 Asigna el escenario a la variable de instancia
    }
}

package insanos.parcialfinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Sistema extends Application {
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        mostrarLogin();
    }

    private void mostrarLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/Login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setSistema(this);

        Scene scene = new Scene(root);
        stage.setTitle("Sistema");
        stage.setScene(scene);
        stage.show();
    }

    public void mostrarReporteA() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteA.fxml"));
        Parent root = loader.load();
        ReporteAController controller = loader.getController();
        controller.setSistema(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void mostrarReporteB() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteB.fxml"));
        Parent root = loader.load();
        ReporteBController controller = loader.getController();
        controller.setSistema(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void mostrarReporteC() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteC.fxml"));
        Parent root = loader.load();
        ReporteCController controller = loader.getController();
        controller.setSistema(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

     public void mostrarReporteD() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insanos/parcialfinal/ReporteD.fxml"));
        Parent root = loader.load();
        ReporteDController controller = loader.getController();
        controller.setSistema(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

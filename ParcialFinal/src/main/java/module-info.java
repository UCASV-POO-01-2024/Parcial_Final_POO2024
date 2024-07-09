module insanos.parcialfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens insanos.parcialfinal to javafx.fxml;
    exports insanos.parcialfinal;
}
package insanos.parcialfinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LoginController { //00191223 Declara la clase LoginController que maneja la lógica del inicio de sesión y el registro de transacciones.

    private Sistema sistema;

    public LoginController() {
    }

    public LoginController(Sistema sistema) {
        this.sistema = sistema;
    }

    public GridPane gridPane;

    @FXML
    private TextField campoDeNombre; //00191223 Campo de texto para el nombre de usuario.

    @FXML
    private PasswordField campoDeContraseña; //00191223 Campo de texto para la contraseña.

    @FXML
    private TextField campoDeNombreTransaccion; //00191223 Campo de texto para el nombre de la transacción.

    @FXML
    private TextField campoDePrecioTransaccion; //00191223 Campo de texto para el precio de la transacción.

    @FXML
    private TextField campoDeDescripcionTransaccion; //00191223 Campo de texto para la descripción de la transacción.

    @FXML
    private TextField campoDeIDTransaccion; //00191223 Campo de texto para el ID de la tarjeta utilizada en la transacción.

    @FXML
    private void BotonInicioSesion() { //00191223 Método que maneja la acción del botón de inicio de sesión.
        String username = campoDeNombre.getText(); //00191223 Obtiene el nombre de usuario del campo de texto.
        String password = campoDeContraseña.getText(); //00191223 Obtiene la contraseña del campo de texto.
        UserType userType = Auntenticador(username, password); //00191223 Autentica al usuario con el nombre y contraseña obtenidos.

        if (userType != null) { //00191223 Revisa que el campo del usuario no este vacio
            showAlert(Alert.AlertType.INFORMATION, "Login Exitoso", "Bienvenido " + username); //00191223 si el inicio es correcto muestra una pantallita de login exitoso
            CargarPantalla(userType); //00191223 Dependiendo que usaurio reciba muestra la pantalla necesaria
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Fallido", "Usuario o contraseña incorrectos"); //00191223 Si la autenticación falla muestra un mensaje de error
        }
    }

    @FXML
    private void BotonParaRegistrarTransaccion() { //00191223 Método que maneja la acción del botón de registrar transacción.
        String nombre = campoDeNombreTransaccion.getText(); //00191223 Obtiene el nombre de la transacción del campo de texto.
        double precio = Double.parseDouble(campoDePrecioTransaccion.getText()); //00191223 Obtiene el precio de la transacción y lo convierte a double.
        String descripcion = campoDeDescripcionTransaccion.getText(); //00191223 Obtiene la descripción de la transacción del campo de texto.
        int idTarjeta = Integer.parseInt(campoDeIDTransaccion.getText()); //00191223 Obtiene el ID de la tarjeta y lo convierte a entero.
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //00191223 Obtiene la fecha y hora actual

        try { //00191223 Intenta registrar la transacción.
            Transacciones.registerTransaction(nombre, precio, descripcion, idTarjeta, fecha); //00191223 le pasa los atributos para la transaccion
            showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "La transacción ha sido registrada con éxito.");  //00191223 Pantallitaque muestra un mensaje de exito
            LimpiarCasillas(); //00191223 llamada a la funcion para limpiar las casillas
        } catch (SQLException e) { e.printStackTrace();  //00191223 Si ocurre una excepción SQL, muestra un mensaje de error.
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar la transacción."); //00191223 Pantallita de error si falla
        }
    }
    @FXML
    private void BotonCerrarSesion() { //00191223 Funcion para el boton de cerrar sesion de ambos usuarios
        try { //00191223 Intenta abrir de nuevo la pantalla de login
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml"))); //00191223 Carga el fxml de login
            Stage stage = (Stage) campoDeNombreTransaccion.getScene().getWindow(); //00191223 Obtiene la ventana actual donde está ubicado el botón de cerrar sesión
            stage.setScene(new Scene(root)); //00191223 Establece la nueva escena de login
        } catch (IOException e) {e.printStackTrace();  //00191223 Manejo de excepciones en caso de que ocurra un error al cargar el FXML
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla de inicio de sesión."); //00191223 Mensaje que tira en caso de error
        }
    }

    private UserType Auntenticador(String username, String password) { //00191223 Método para autenticar al usuario.
        if ("admin".equals(username) && "1234".equals(password)) {
            return UserType.ADMIN; //00191223 Si el nombre de usuario y la contraseña son correctos para el admin
        } else if ("cliente".equals(username) && "12345".equals(password)) {
            return UserType.CLIENTE;//00191223 Si el nombre de usuario y la contraseña son correctos para el cliente
        }
        return null; //00191223 Si no coinciden
    }

    private void CargarPantalla(UserType userType) { //00191223 Método para cargar la pantalla del usuario según su tipo.
        String fxmlFile = switch (userType) { //00191223 Selecciona el archivo FXML correspondiente según el tipo de usuario.
            case ADMIN -> "AdminScreen.fxml"; //00191223 en el caso de Admin muestra pantalla Admin
            case CLIENTE -> "ClienteScreen.fxml"; //00191223 en el caso de Cliente muestra pantalla Cliente
        };

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            Parent root = loader.load();

            if (userType == UserType.ADMIN) {
                AdminController adminController = loader.getController();
                adminController.setSistema(sistema);
            }

            Stage stage = (Stage) campoDeNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla de usuario.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { //00191223 Método para mostrar una alerta.
        Alert alert = new Alert(alertType); //00191223 Crea una nueva alerta con el tipo especificado.
        alert.setTitle(title); //00191223 Establece el título de la alerta.
        alert.setHeaderText(null); //00191223 Establece el encabezado de la alerta como null.
        alert.setContentText(message); //00191223 Establece el contenido de la alerta.
        alert.showAndWait(); //00191223 Muestra la alerta y espera a que el usuario la cierre.
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    private enum UserType { //00191223 Define un enumerador para los tipos de usuario.
        ADMIN, //00191223 Tipo de usuario administrador.
        CLIENTE //00191223 Tipo de usuario cliente.
    }

    private void LimpiarCasillas() { //00191223 Método para limpiar los campos de texto de la transacción.
        campoDeNombreTransaccion.setText("");//00191223 Limpia el campo de texto del nombre de la transacción.
        campoDePrecioTransaccion.setText("");//00191223 Limpia el campo de texto del precio de la transacción.
        campoDeDescripcionTransaccion.setText("");//00191223 Limpia el campo de texto de la descripción de la transacción.
        campoDeIDTransaccion.setText("");//00191223 Limpia el campo de texto del ID de la tarjeta.
    }


}
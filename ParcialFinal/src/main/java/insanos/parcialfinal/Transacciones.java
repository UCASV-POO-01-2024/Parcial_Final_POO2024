package insanos.parcialfinal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transacciones { //00191223 Declara la clase Transacciones que contiene métodos para interactuar con la base de datos.
    public static void registerTransaction(String nombre, double precio, String descripcion, int idTarjeta, String fecha) throws SQLException  //00191223 Método estático para registrar una transacción
    {
        String sql = "INSERT INTO Transaccion (nombre, precio, descripcion, id_tarjeta, fecha) VALUES (?, ?, ?, ?, ?)";
        //00191223 Define la consulta SQL para insertar una nueva transacción

        try (Connection conn = Database.getConnection(); //00191223 Obtiene una conexión a la base de datos
             PreparedStatement stmt = conn.prepareStatement(sql)) //00191223 Prepara la declaración SQL para su ejecución.
        {
            stmt.setString(1, nombre); //00191223 Establece el primer parámetro de la consulta SQL con el valor del nombre.
            stmt.setDouble(2, precio); //00191223 // Establece el segundo parámetro de la consulta SQL con el valor del precio.
            stmt.setString(3, descripcion); //00191223 // Establece el tercer parámetro de la consulta SQL con el valor de la descripción.
            stmt.setInt(4, idTarjeta); //00191223 // Establece el cuarto parámetro de la consulta SQL con el valor del id de la tarjeta.
            stmt.setString(5, fecha); //00191223 // Establece el quinto parámetro de la consulta SQL con el valor de la fecha.
            stmt.executeUpdate(); //00191223 Ejecuta la consulta SQL de actualización.
        }
    }
}
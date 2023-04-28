package jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jdbc.modelo.Reservas;

public class ReservaDAO {
	final private Connection con;

	public ReservaDAO(Connection con) {
		this.con = con;
	}

	public void guardar(Reservas reserva) {
		try { PreparedStatement statement = con.prepareStatement(
				"INSERT INTO RESERVAS (FECHA_ENTRADA, FECHA_SALIDA, VALOR, FORMA_PAGO)" 
						+ "VALUES(?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
		try (statement) {
				ejecutaRegistro (reserva,  statement);
			} 
		
		}catch (SQLException e) {
			throw new RuntimeException (e);
	}	
}	

private void ejecutaRegistro(Reservas reserva, PreparedStatement statement)
		throws SQLException {
	statement.setDate(1, reserva.getFechaEntrada());
	statement.setDate(2, reserva.getFechaSalida());
	statement.setString(3, reserva.getValor());
	statement.setString(4, reserva.getFormaPago());
	statement.execute();
	
	final ResultSet resultSet = statement.getGeneratedKeys();
	try (resultSet){
		while (resultSet.next()) {
		reserva.setId(resultSet.getInt(1));
		System.out.println(String.format("Fue insertada la reserva %s", reserva));
		}
	}	
}

public List<Reservas> listarReservas() {
	List<Reservas> resultado = new ArrayList<>();
	try {
		final PreparedStatement statement = con.prepareStatement("SELECT ID, FECHA_ENTRADA, FECHA_SALIDA, VALOR, FORMA_PAGO FROM RESERVAS");
		try (statement) {
			statement.execute();

			final ResultSet resultset = statement.getResultSet();
			try(resultset){
			while (resultset.next()) {
				Reservas fila = new Reservas(resultset.getInt("ID"),
											resultset.getDate("FECHA_ENTRADA"),
											resultset.getDate("FECHA_SALIDA"),
											resultset.getString("VALOR"),
											resultset.getString("FORMA_PAGO"));			
				resultado.add(fila);
			}
		}
	}
			return resultado;
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
}


public List<Reservas> listarReservasId(int id) {
	List<Reservas> resultado = new ArrayList<>();
	try {
		final PreparedStatement statement = con.prepareStatement("SELECT ID, FECHA_ENTRADA, FECHA_SALIDA,"
				+ " VALOR, FORMA_PAGO FROM RESERVAS WHERE ID = ?");
		try (statement) {
			statement.setInt(1, id);
			statement.execute();

			final ResultSet resultset = statement.getResultSet();
			try(resultset){
			while (resultset.next()) {
				Reservas fila = new Reservas(resultset.getInt("ID"),
											resultset.getDate("FECHA_ENTRADA"),
											resultset.getDate("FECHA_SALIDA"),
											resultset.getString("VALOR"),
											resultset.getString("FORMA_PAGO"));			
				resultado.add(fila);
			}
		}
	}
			return resultado;
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
}

public int modificarReserva (Date fechaEntrada, Date fechaSalida, String valor, String formaPago, int id) {
	try {
		final PreparedStatement statement = con.prepareStatement(
				"UPDATE RESERVAS SET FECHA_ENTRADA = ? ,"
				+ " FECHA_SALIDA = ? , VALOR = ? , FORMA_PAGO = ?  WHERE ID = ?");
		try (statement) {
			statement.setDate(1, fechaEntrada );
			statement.setDate(2, fechaSalida );
			statement.setString(3, valor);
			statement.setString(4, formaPago);
			statement.setInt(5, id);
			statement.execute();

			int updateCount = statement.getUpdateCount();

			return updateCount;
		}
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}

}

public int eliminarReserva(Integer id) {
	try {
		Statement stat = con.createStatement();
		stat.execute("SET FOREIGN_KEY_CHECKS=0");
		final PreparedStatement statement = con.prepareStatement("DELETE FROM RESERVAS WHERE ID = ?");
		try (statement) {
			statement.setInt(1, id);
			statement.execute();
		stat.execute("SET FOREIGN_KEY_CHECKS=1");
			int updateCount = statement.getUpdateCount();

			return updateCount;
		}
	} catch (SQLException e) {
		throw new RuntimeException(e);
	}
}
	}


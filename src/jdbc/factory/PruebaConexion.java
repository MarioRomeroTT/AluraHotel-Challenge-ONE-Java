	package jdbc.factory;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

	public static void main(String[] args)  {
		
		ConnectionFactory conectionFactory = new ConnectionFactory();
		
		Connection conexion = conectionFactory.recuperarConexion();
		
		System.out.println("Conexion OK, se procede a cerrar la conexion");
		
		try {
			conexion.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		
		
		
		
		
	} 
	
	

}

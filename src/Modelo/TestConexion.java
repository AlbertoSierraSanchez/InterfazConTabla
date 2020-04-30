package Modelo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

	public class TestConexion {
		private String url= "";
		private   String user = "";
		private String pwd = "";
		private   String usr = "";
		private   Connection conexion;
	public TestConexion()  {

		
		
		Properties propiedades = new Properties();
		InputStream entrada = null;
		try {
			File miFichero = new File("src/Modelo/datos.ini");
			if (miFichero.exists()){
				entrada = new FileInputStream(miFichero);
				propiedades.load(entrada);
				url=propiedades.getProperty("url");
				user=propiedades.getProperty("user");
				pwd=propiedades.getProperty("pwd");
				usr=propiedades.getProperty("usr");
			}

			else
				System.out.println("Fichero no encontrado");
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally {
			if (entrada != null) {
				try {
					entrada.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conexion = DriverManager.getConnection(url, user, pwd);

			if(conexion.isClosed())
				System.out.println("Fallo en Conexión con la Base de Datos");


		}catch (Exception e) {
			System.out.println("ERROR en conexión con ORACLE");
			e.printStackTrace();
		}
	}
	public ObservableList<Persona> MostrarTabla() throws SQLException{

		
		ObservableList<Persona> listaPersonas =  FXCollections.observableArrayList();

		//Preparo la conexión para ejecutar sentencias SQL de tipo update
		Statement stm = conexion.createStatement();

		// Preparo la sentencia SQL CrearTablaPersonas
		String selectsql = "SELECT NOMBRE,APELLIDO,EMAIL,SEXO,ESTADO FROM "+usr+".PERSON2";

		//ejecuto la sentencia
		try{
			ResultSet resultado = stm.executeQuery(selectsql);

			while(resultado.next()){
				String nombre=resultado.getString(1);
				String apellido=resultado.getString(2);
				String email=resultado.getString(3);
				char sexo=resultado.getString(4).charAt(0);
				
				boolean estado = false;
				
				if(resultado.getString(5).equals("true")){
					estado=true;
				}
				
				Persona persona= new Persona(nombre, apellido, email, sexo, estado);
				listaPersonas.add(persona);
			}

		}catch(SQLException sqle){

			
			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			System.out.println(codeErrorSQL);
		}
		

		return listaPersonas;
	}
	public void GuardarDatos(String nombre, String apellido, String email, char sexo ,boolean estado) throws SQLException{

			//Preparo la conexión para ejecutar sentencias SQL de tipo update
		
		String auxcasado =  " False " ;
		
		if (estado ==  true )
			auxcasado =  " True " ;

			// Preparo la sentencia SQL
			String insertsql = "INSERT INTO "+usr+".PERSON2 VALUES (?,?,?,?,?)";
			
			PreparedStatement pstmt = conexion.prepareStatement(insertsql);
			
			pstmt.setString(1, nombre);
			pstmt.setString(2, apellido);
			pstmt.setString(3, email);
			pstmt.setString(4, Character.toString(sexo));
			pstmt.setString(5, auxcasado);
			
			
			
			//ejecuto la sentencia
		try{
			
				int resultado = pstmt.executeUpdate();
			
				if(resultado != 1)
					System.out.println("Error en la inserción " + resultado);
			
		}catch(SQLException sqle){
			
			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0, pos);
			if(codeErrorSQL.equals("ORA-00001") )
				System.out.println("Ese correo ya existe!!!");
			
			else if(codeErrorSQL.equals("ORA-00001"))
				
				System.out.println("El correo debe contener un máximo de 20 caracteres");
			else
				System.out.println("Problemas de Inserción: " + sqle);
		}

	}
	public void BorrarDatos(String email) throws SQLException{
		//Preparo la conexión para ejecutar sentencias SQL de tipo update
	
		
		// Preparo la sentencia SQL
		String insertsql = "DELETE FROM "+usr+".PERSON2 WHERE EMAIL = ?";
		
		PreparedStatement pstmt = conexion.prepareStatement(insertsql);
		
		pstmt.setString(1, email);
		//ejecuto la sentencia
		try{
			int resultado = pstmt.executeUpdate();

			if(resultado !=1)
				System.out.println("Error en la inserción " + resultado);
		}catch(SQLException sqle){
			
			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0, pos);
			
			if(codeErrorSQL.equals("ORA-00001") )
				System.out.println("la tabla PERSON2 ya estaba creada!!!");
			else
				System.out.println("Ha habido algún problema con  Oracle al hacer el borrado de tabla");
		}
		
	}
    public void ActualizarDatos(Persona persona1,Persona persona2) throws SQLException{

    	//Preparo la conexión para ejecutar sentencias SQL de tipo update
    	
		
		// Preparo la sentencia SQL
		String updatesql = "UPDATE "+usr+".PERSON2 SET NOMBRE=?, APELLIDO =?, EMAIL =?, SEXO =?, ESTADO =? WHERE EMAIL=?";		
		//ejecuto la sentencia
		PreparedStatement pstmt = conexion.prepareStatement(updatesql);
		
		pstmt.setString(1, persona2.getNombre());
		pstmt.setString(2, persona2.getApellido());
		pstmt.setString(3, persona2.getEmail());
		pstmt.setString(4, Character.toString(persona2.getSexo()));
		
		String auxcasado =  " False " ;
		if (persona2.isEstado() ==  true )
				auxcasado =  " True " ;
		
		pstmt.setString(5, auxcasado);
		pstmt.setString(6, persona1.getEmail());
		
		
	try{
			int resultado = pstmt.executeUpdate();
		
			if(resultado != 1)
				System.out.println("Error en la modificación " + resultado);
		
	}catch(SQLException sqle){
		System.out.println(sqle);
		int pos = sqle.getMessage().indexOf(":");
		String codeErrorSQL = sqle.getMessage().substring(0, pos);
		if(codeErrorSQL.equals("ORA-00955") )
			System.out.println("la tabla PERSON2 ya estaba creada!!!");
		else
			System.out.println("Ha habido algún problema con  Oracle al hacer la modificación de campo");
	}
    }

    public ObservableList<Persona> FiltrarApellido(String apellido2) throws SQLException{

    	ObservableList<Persona> listaPersonas =  FXCollections.observableArrayList();
    	
    	//Preparo la conexión para ejecutar sentencias SQL de tipo update
    			Statement stm = conexion.createStatement();
    			
    			// Preparo la sentencia SQL CrearTablaPersonas
    			String selectsql = "SELECT * FROM "+usr+".PERSON2 WHERE APELLIDO = '" + apellido2 + "'";
    			PreparedStatement pstmt = conexion.prepareStatement(selectsql);
    			
    			pstmt.setString(1, apellido2);
    			//ejecuto la sentencia
    			try{
    				ResultSet resultado = stm.executeQuery(selectsql);

    				while(resultado.next()){
    					String nombre=resultado.getString(1);
    					String apellido=resultado.getString(2);
    					String email=resultado.getString(3);
    					char sexo=resultado.getString(4).charAt(0);
       					boolean estado = false;
    					
    					if(resultado.getString(5).equals("true")){
    						estado=true;
    					}
    					
    					Persona persona= new Persona(nombre, apellido, email, sexo, estado);
    					listaPersonas.add(persona);
    				}

    			}catch(SQLException sqle){

    				
    				int pos = sqle.getMessage().indexOf(":");
    				String codeErrorSQL = sqle.getMessage().substring(0,pos);

    				System.out.println(codeErrorSQL);
    			}
    	
		return listaPersonas;
    	
    }
    
}

package Vista;



import java.sql.SQLException;
import java.util.Optional;

import Modelo.Persona;
import Modelo.TestConexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controladora {

		@FXML
	   private Button Guardar;
		@FXML
	   private Button Borrar;
		@FXML
	   private Button Eliminar;
	   @FXML
	   private CheckBox Estadock;
	   @FXML
	   private RadioButton HombreBotton;
	   @FXML
	   private RadioButton MujerBotton;
	   @FXML
	   private ToggleGroup Sexo;

	   @FXML
	   private TextField NombreTXT;
	   
	   @FXML
	   private TextField ApellidoTXT;
	   
	   @FXML
	   private TextField EmailTXT;

	   @FXML
	   private TableView<Persona> Tabla;
	   @FXML
		private TableColumn<Persona,String> ColName;
	   @FXML
		private TableColumn<Persona,String> ColApe;
	   @FXML
		private TableColumn<Persona,String> ColEmail;
	   @FXML
		private TableColumn<Persona,Character> ColSexo;
	   
	   @FXML
		private TableColumn<Persona,Boolean> ColEstado;
	   
	   @FXML
	   private Button Buscar;
	   
	   @FXML
	   private TextField FiltroTXT;
	   
	   TestConexion con;
	   
	   private ObservableList<Persona> data = FXCollections.observableArrayList();	
	   private ObservableList<Persona> data2 = FXCollections.observableArrayList();
	   private ObservableList<Persona> ListaFiltrado = FXCollections.observableArrayList();
	   
	   private boolean edicion=false;
	   private int indiceEdicion=0;
		
	public void initialize() throws SQLException{
			
			
			con = new TestConexion();
			
			data=con.MostrarTabla();
			
			Tabla.setItems(this.data);

			ColName.setCellValueFactory(new PropertyValueFactory<Persona,String>("Nombre"));
			ColApe.setCellValueFactory(new PropertyValueFactory<Persona,String>("Apellido"));
			ColEmail.setCellValueFactory(new PropertyValueFactory<Persona,String>("Email"));
			ColSexo.setCellValueFactory(new PropertyValueFactory<Persona,Character>("Sexo"));
			ColEstado.setCellValueFactory(new PropertyValueFactory<Persona,Boolean>("Estado"));

		}

	public void Guardar(ActionEvent event) throws SQLException{
		
		
		boolean casado = Estadock.isSelected();
		char sexo;

		if(HombreBotton.isSelected())
			sexo = 'H';
		else
			sexo = 'M';

		// Añadir un chequeo de campos vacíos o de validación de formato como el email
		
		if(NombreTXT.getText().equals("") || ApellidoTXT.getText().equals("") || EmailTXT.getText().equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!!!");
			alert.setHeaderText("Observa que hayas introducido todos los datos");
			alert.setContentText("¡No se pueden grabar campos vacíos!");
			alert.showAndWait();
		}else{

			Persona nuevo1 = new Persona(NombreTXT.getText(),ApellidoTXT.getText(),EmailTXT.getText(),sexo,casado);
			data2.add(nuevo1);
			
			if(edicion == true){
				Persona editada = data.get(indiceEdicion);
				editada.setNombre(NombreTXT.getText());
				editada.setApellido(ApellidoTXT.getText());
				editada.setEmail(EmailTXT.getText());
				editada.setEstado(casado);
				editada.setSexo(sexo);
				data.set(indiceEdicion, editada);
				con.ActualizarDatos(data2.get(0), editada);
				
				Alert alerta = new Alert ( AlertType.INFORMATION ); 
			   	alerta . setTitle ( "Información" ); 
			   	alerta . setHeaderText (null); 
			   	alerta . setContentText ("¡Campo Actualizado!");  
			   	alerta . showAndWait();
	
			}else{
				
				Persona nuevo2 = new Persona(NombreTXT.getText(),ApellidoTXT.getText(),EmailTXT.getText(),sexo,casado);
				con.GuardarDatos(NombreTXT.getText(),ApellidoTXT.getText(),EmailTXT.getText(),sexo,casado);
				
				boolean esta = false;
				
				for(Persona a : data){
					if(a.getEmail().equals(nuevo2.getEmail())){
						esta=true;
					}

				}
					if(esta==true){
						Alert alerta = new Alert ( AlertType.INFORMATION ); 
					   	alerta . setTitle ( "Información" ); 
					   	alerta . setHeaderText (null); 
					   	alerta . setContentText ("¡El email ya existe!");  
					   	alerta . showAndWait();
					}else{
						
						data.add(nuevo2);
						Alert alerta = new Alert ( AlertType.INFORMATION ); 
					   	alerta . setTitle ( "Información" ); 
					   	alerta . setHeaderText (null); 
					   	alerta . setContentText ("¡Campo guardado!");  
					   	alerta . showAndWait();
					}
			}

		}
				
						
				}	

	public void Borrar(){

	       this.NombreTXT.setText("");
	       this.ApellidoTXT.setText("");
	       this.EmailTXT.setText("");
	       this.Estadock.setSelected(false);
	       	indiceEdicion=0;
	       	edicion=false;
	       	
	       	
	       	
	       	

	}
	
	public void Eliminar(ActionEvent event) throws SQLException{
	
		int index = Tabla.getSelectionModel().getSelectedIndex();

		if(index>=0){
			
			Persona seleccionada = Tabla.getSelectionModel().getSelectedItem();

					Alert alert = new Alert(AlertType.CONFIRMATION);
			       alert.setTitle("Borrando...");
			       alert.setHeaderText("Desea Borrar a " + seleccionada.getNombre() +" "+ seleccionada.getApellido());
			      
			       Optional <ButtonType> result = alert.showAndWait ();
			       
			      if (result.get () == ButtonType.OK){
			    	  
			    	  	con.BorrarDatos(seleccionada.getEmail());
			    	   	data.remove(seleccionada);
						
			    	   	Alert alerta = new Alert ( AlertType.INFORMATION ); 
			    	   	alerta . setTitle ( "Información" ); 
			    	   	alerta . setHeaderText (null); 
			    	   	alerta . setContentText ( "¡Eliminado!" );  
			    	   	alerta . showAndWait ();
			    	   	
			    	   	
			    	   	
			       }

		}else{
			
				Alert alert = new Alert(AlertType.ERROR);
		       alert.setTitle("Error !");
		       alert.setHeaderText("Seleccione una fila...");
		       alert.showAndWait();
		}
		
		
		Borrar();
		
		
	}
	
	public void Editar() throws SQLException{
		
		int index = Tabla.getSelectionModel().getSelectedIndex();
		
		if(index >=0){
			
			edicion=true;
			indiceEdicion=index;
			
			Persona seleccionada = Tabla.getSelectionModel().getSelectedItem();
			
			
			
			NombreTXT.setText(seleccionada.getNombre());
			ApellidoTXT.setText(seleccionada.getApellido());
			EmailTXT.setText(seleccionada.getEmail());
			Estadock.setSelected(false);

			if(seleccionada.getSexo()=='H'){
				
				HombreBotton.setSelected(true);
				
			}else{
				
				MujerBotton.setSelected(true);
			}
			
		}
		
		
		
	}
	
	public void Buscar() throws SQLException{
		
		
		if(FiltroTXT.getText().length()==0){
			
			initialize();
		
		}else{
			
			ListaFiltrado=con.FiltrarApellido(FiltroTXT.getText());
			Tabla.setItems(ListaFiltrado);
			if(ListaFiltrado.size()!=0){
				Alert alerta = new Alert ( AlertType.INFORMATION ); 
			   	alerta . setTitle ( "Información" ); 
			   	alerta . setHeaderText (null); 
			   	alerta . setContentText ("¡Estos son todas las personas con apellido : " +FiltroTXT.getText() + " !");  
			   	alerta . showAndWait();
			}else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!!!");
				alert.setHeaderText("Observa que hayas introducido correctamente los datos");
				alert.setContentText("¡No existen usuarios con este apellido :" + FiltroTXT.getText()+"!");
				alert.showAndWait();
			}
			
		}


	}
	
	
	
}

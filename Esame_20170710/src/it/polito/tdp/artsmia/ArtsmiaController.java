/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		model.creaGrafo();
		
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {
		if(model.verifica(Integer.parseInt(txtObjectId.getText()))!=null) {
			txtResult.appendText("L'identificativo che hai scelto è corretto. L'opera corrispondente è:\n "+model.verifica(Integer.parseInt(txtObjectId.getText())).toString()+"\n");
			txtResult.appendText("La componente connessa ha dimensione: "+model.calcolaComponenteConnessa(Integer.parseInt(txtObjectId.getText())));
			int id=Integer.parseInt(txtObjectId.getText());
			int dimensione=model.calcolaComponenteConnessa(id);
			List<Integer> valori=new LinkedList<Integer>();
			for(int i=2; i<dimensione; i++) {
				valori.add(i);
			}
			if(dimensione>1) {
				boxLUN.getItems().addAll(valori);
			} else {
				txtResult.appendText("L'opera che hai scelto ha una componente connessa di dimensione 1. Selezionane un'altra.");
			}
		} else {
			txtResult.appendText("L'opera che hai inserito non è presente nel nostro DataBase.\n");
		}
		
	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		model.cercaOggetti(boxLUN.getValue(), Integer.parseInt(txtObjectId.getText()));
	txtResult.appendText("Cammino trovato: \n"+model.getCamminoBest().toString()+ "\nPeso totale: "+model.getMaxCammino());
	
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}
	
	public void setModel(Model model) {
    	this.model = model;
    	
    }
}

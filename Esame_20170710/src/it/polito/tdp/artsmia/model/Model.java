package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;


public class Model {
	
	private List<ArtObject> opere;
	private SimpleWeightedGraph <ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap=new HashMap<Integer, ArtObject>();
	private List<ArtObject> camminoBest;
	private int maxCammino;
	
	public Model() {
		this.dao=new ArtsmiaDAO();
	}
	
	public void caricaOpere(){
		opere=dao.listObjects(idMap);
	}
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		caricaOpere();
		for(ArtObject o: opere) {
			grafo.addVertex(o);
			System.out.println(o.toString());
		}
		/*for(ArtObject a1:grafo.vertexSet()) {
			for(ArtObject a2: grafo.vertexSet()) {
				if(dao.esibizioni(a1, a2)>0 && !grafo.containsEdge(a1, a2) && !a1.equals(a2)) {
					Graphs.addEdgeWithVertices(grafo, a1, a2, dao.esibizioni(a1, a2));
					System.out.println(a1.toString()+" "+a2.toString());
				}
			}
		}*/
		
		for(ArtObject o: opere) {
			List<EsibizioniComuni> e=dao.esibizioni(idMap, o);
			for(EsibizioniComuni es: e) {
				Graphs.addEdgeWithVertices(grafo, o, es.getA2(), es.getCount());
				System.out.println(o.toString()+" "+es.getA2().toString()+" "+es.getCount()+"\n");
			}
			
			
		}
		System.out.format("Grafo creato: %d vertici, %d archi\n", grafo.vertexSet().size(), grafo.edgeSet().size());
		
	}

	public List<ArtObject> getOpere() {
		return opere;
	}

	public void setOpere(List<ArtObject> opere) {
		this.opere = opere;
	}

	public SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	public ArtObject verifica(int id) {
		if(idMap.containsKey(id)) {
			return idMap.get(id);
		} else return null;
	}
	
	public int calcolaComponenteConnessa(int idObj) {
		//trova il vertice di partenza
		ArtObject start = idMap.get(idObj);
		
		//visita il grafo
		Set<ArtObject> visitati = new HashSet<>();
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> dfv=new DepthFirstIterator<>(this.grafo, start);
		while(dfv.hasNext()) {
			visitati.add(dfv.next());
		}
		//conta gli elementi
		return visitati.size();
		
		
	}
	public List<ArtObject> cercaOggetti(int dimensione, int id){
		//preparo le variabili utili alla ricorsione
		List<ArtObject> parziale = new ArrayList<ArtObject>();
		this.camminoBest=new ArrayList<ArtObject>();
		maxCammino=0;
		ArtObject iniziale = idMap.get(id);
		
		//itero a livello 0
		parziale.add(iniziale);
		cerca(1, parziale, 0, dimensione);
		parziale.remove(0);
		return camminoBest;
		
		
	}

	private void cerca(int i, List<ArtObject> parziale, int peso, int dimensione) {
		//condizione di terminazione
		if(i==dimensione) {
			if(peso>=maxCammino) {
				maxCammino=peso;
				camminoBest = new ArrayList<>(parziale); //CLONO PARZIALE!!!
			}
		}
		
		ArtObject ultimo = parziale.get(parziale.size()-1);
		List <ArtObject> vicini = Graphs.neighborListOf(grafo, ultimo);
		for(ArtObject a: vicini) {
			if(!parziale.contains(a) && a.getClassification() != null && ultimo.getClassification().equals(a.getClassification())) {
				parziale.add(a);
				peso+=grafo.getEdgeWeight(grafo.getEdge(a, ultimo));
				cerca(i+1, parziale, peso, dimensione);
				peso-=grafo.getEdgeWeight(grafo.getEdge(a, ultimo));
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}

	public List<ArtObject> getCamminoBest() {
		Collections.sort(camminoBest);
		return camminoBest;
	}

	public void setCamminoBest(List<ArtObject> camminoBest) {
		this.camminoBest = camminoBest;
	}

	public int getMaxCammino() {
		return maxCammino;
	}

	public void setMaxCammino(int maxCammino) {
		this.maxCammino = maxCammino;
	}
	

}

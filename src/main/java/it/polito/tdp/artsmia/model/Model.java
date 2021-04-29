package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer,ArtObject> idMap; // per non interrogare troppe volte il db
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer,ArtObject>();
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungere i vertici
		//1. -> Recupero tutti gli ArtObject dal db
		//2. -> Li inserisco come vertici 
		 dao.listObjects(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aggiungere gli archi
		//APPROCCIO 1: DOPPIO CICLO "FOR" ANNIDATO -> funzionante solo se il NUMERO DI VERTICI è MOLTO BASSO
		//IN QUESTO CASO SI UTILIZZA UN TEMPO TROPPO ALTO
		
		/*for (ArtObject a1:this.grafo.vertexSet()) {
			for(ArtObject a2:this.grafo.vertexSet()) {
				if(!a1.equals(a2)&& !this.grafo.containsEdge(a1,a2)) {
					//devo collegare a1 ad a2?
					int peso = dao.getPeso(a1,a2);
					if (peso > 0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}*/
		
		//APPROCCIO 3
		for(Adiacenza a:dao.getAdiacenze()) {
				Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		
		System.out.println("Numero di vertici: " + grafo.vertexSet().size());
		System.out.println("Numero di archi: " + grafo.edgeSet().size());		
		
	}

}

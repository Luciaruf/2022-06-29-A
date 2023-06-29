package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private List<Album> allAlbums;
	private SimpleDirectedWeightedGraph<Album,DefaultWeightedEdge> graph;
	private ItunesDAO dao;
	private List<Album> bestPath;
	private int bestScore;
	
	public Model() {
		super();
		this.allAlbums = new ArrayList<>();
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao = new ItunesDAO();
	}
	
	public List<Album> getPath(Album source,Album target,int threshold){
		List<Album> parziale = new ArrayList<>();
		this.bestPath = new ArrayList<>();
		this.bestScore = 0;
		parziale.add(source);
		
		ricorsione(parziale,target,threshold);
		
		return this.bestPath;
	}
	
	private void ricorsione(List<Album> parziale, Album target, int threshold) {
		Album current = parziale.get(parziale.size()-1);
		if(current.equals(target)) {// condizione di uscita current è uguale a target
			// se questa è vero controllo se questa soluzione è migliore della best che ho salvato
			if(getScore(parziale)>this.bestScore) {
				this.bestScore = getScore(parziale);
				this.bestPath = new ArrayList<>(parziale); // facendo la new mi creo una nuova copia che evito di sovrascrivere
			}
			return; //arrivato al target	
		}
		
		//continuo ad aggiungere elementi in parziale
		List<Album> successors = Graphs.successorListOf(this.graph, current);
		
		for(Album a : successors) {
			if(this.graph.getEdgeWeight(this.graph.getEdge(current, a))>= threshold) {
				parziale.add(a);
				ricorsione(parziale,target,threshold);
				parziale.remove(a);
			
			}
		}
	}

	private int getScore(List<Album> parziale) {
		int score = 0;
		Album source = parziale.get(0);
		
		for(Album a: parziale.subList(1, parziale.size()-1)) {
			if(getBilancio(a)>getBilancio(source)) 
				score += 1;
		}
		return score;
	}

	public List<BilancioAlbum> getAdiacenti(Album root){
		List<Album>successori = new ArrayList<>(Graphs.successorListOf(this.graph, root));
		
		List<BilancioAlbum> bilanciosuccessori = new ArrayList<>();
		
		for(Album a: successori) {
			bilanciosuccessori.add(new BilancioAlbum(a,getBilancio(a)));
		}
		
		Collections.sort(bilanciosuccessori);
		
		return bilanciosuccessori;
	}
	
	public void buildGraph(int n) {
		clearGraph();
		loadNodes(n);
		
		Graphs.addAllVertices(this.graph, this.allAlbums);
		
		
		
		for(Album a1: allAlbums) {
			for(Album a2 : allAlbums) {
				int peso = a1.getNumSongs()-a2.getNumSongs();
				
				if(peso > 0) {
					//orientato da meno canzoni a più canzoni da a2 a a1
					// a2 vertice di source
					Graphs.addEdgeWithVertices(this.graph, a2, a1,peso);
				}
			}
		}
		System.out.println(this.graph.vertexSet().size());
		System.out.println(this.graph.edgeSet().size());
		
	}
	
	private int getBilancio(Album a) {
		int bilancio = 0;
		
		List<DefaultWeightedEdge> edgesINDEfaultWeightedEdges = new ArrayList<>(this.graph.incomingEdgesOf(a)); //tutti gli edge che entrano in a
		List<DefaultWeightedEdge> edgesOUTDEfaultWeightedEdges = new ArrayList<>(this.graph.outgoingEdgesOf(a));
		
		// cicla su tutti gli edge entranti e somma il peso
		for(DefaultWeightedEdge e: edgesINDEfaultWeightedEdges) 
			bilancio += this.graph.getEdgeWeight(e);
		
		for(DefaultWeightedEdge e1: edgesOUTDEfaultWeightedEdges) 
			bilancio-= this.graph.getEdgeWeight(e1);
		
		
		return bilancio;	
	}
	
	public List<Album> getVertices(){
		
		List<Album> allVertices = new ArrayList<>(this.graph.vertexSet());
		Collections.sort(allVertices);
		
		return allVertices;
	}
	
	private void clearGraph() {
		this.allAlbums = new ArrayList<>();
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
	}

	public void loadNodes(int n) {

		if(this.allAlbums.isEmpty()) {
			this.allAlbums = dao.getFilterendAlbums(n);
		}
		
	}

	public int getNumVertices() {
		// TODO Auto-generated method stub
		return this.graph.vertexSet().size();
	}

	public int getNumEdges() {
		// TODO Auto-generated method stub
		return this.graph.edgeSet().size();
	}
	
	
}

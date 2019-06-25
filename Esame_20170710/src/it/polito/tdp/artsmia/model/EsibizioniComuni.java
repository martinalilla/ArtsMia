package it.polito.tdp.artsmia.model;

public class EsibizioniComuni {
	private ArtObject a1;
	private ArtObject a2;
	private int count;
	public EsibizioniComuni(ArtObject a1, ArtObject a2, int count) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.count = count;
	}
	public ArtObject getA1() {
		return a1;
	}
	public void setA1(ArtObject a1) {
		this.a1 = a1;
	}
	public ArtObject getA2() {
		return a2;
	}
	public void setA2(ArtObject a2) {
		this.a2 = a2;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	

}

package it.polito.tdp.itunes.model;

import java.util.Objects;

public class BilancioAlbum implements Comparable<BilancioAlbum>{
	private Album a;
	private Integer bilancio;
	
	public BilancioAlbum(Album a, Integer bilancio) {
		super();
		this.a = a;
		this.bilancio = bilancio;
	}
	public Album getA() {
		return a;
	}
	public void setA(Album a) {
		this.a = a;
	}
	public Integer getBilancio() {
		return bilancio;
	}
	public void setBilancio(Integer bilancio) {
		this.bilancio = bilancio;
	}
	@Override
	public int hashCode() {
		return Objects.hash(a, bilancio);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BilancioAlbum other = (BilancioAlbum) obj;
		return Objects.equals(a, other.a) && Objects.equals(bilancio, other.bilancio);
	}
	@Override
	public int compareTo(BilancioAlbum ba) {
		// TODO Auto-generated method stub
		return this.bilancio.compareTo(ba.getBilancio());
	}
	@Override
	public String toString() {
		return "a=" + a + ", bilancio=" + bilancio;
	}
	
	

}

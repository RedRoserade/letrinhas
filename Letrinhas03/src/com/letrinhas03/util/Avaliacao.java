/*
 * Esta classe serve para calcular todos os possiveis par�metros de avalia��o dos testes realizados
 * @author D�rio
 */

package com.letrinhas03.util;

public class Avaliacao {
	private int totalDePalavras,TotalDeSinaisPontuacao;
	private int plvErradas, pontua, vacil, fragment, silabs, repeti;

	public Avaliacao(int totalDePalavras, int TotalDeSinaisPontuacao){
		this.totalDePalavras= totalDePalavras;
		this.TotalDeSinaisPontuacao= TotalDeSinaisPontuacao;
	}
	
	public void decPalErrada() {
		if (plvErradas != 0){
			plvErradas--;
		}
	}
	
	public void incPalErrada() {
		if(plvErradas< totalDePalavras){
			plvErradas++;
		}
	}

	public int getPlvErradas() {
		return plvErradas;
	}

	public void decPontua() {
		if (pontua != 0){
			pontua--;
		}
	}
	
	public void incPontua() {
		pontua++;
	}

	public int getPontua() {
		return pontua;
	}

	public void decVacil() {
		if (vacil != 0)
			vacil--;
	}
	
	public void incVacil() {
		vacil++;
	}
	
	public int getVacil() {
		return vacil;
	}

	public void decFragment() {
		if (fragment != 0)
			fragment--;
	}
	
	public void incFragment() {
		fragment++;
	}
	
	public int getFragment() {
		return fragment;
	}

	public void decSilabs() {
		if (silabs != 0)
			silabs--;
	}
	
	public void incSilabs() {
		silabs++;
	}
	
	public int getSilabs() {
		return silabs;
	}

	public void decRepeti() {
		if (repeti != 0)
			repeti--;
	}
	
	public void incRepeti() {
		repeti++;
	}
	
	public int getRepeti() {
		return repeti;
	}

	public String calcula(int minutos, int segundos){
		String resultado="==========Avalia��o============\n" 
				+"Tempo de Leitura (em segundos): "+(minutos*60 + segundos)+"\n";
		resultado += "Palavras lidas por minuto (plm): "+ PLM(minutos, segundos)+"\n";
		resultado += "Palavras corretamente lidas (pcl): "+ palavrasCertas() +"\n";
		resultado += "Precis�o de Leitura (PL): "+ PL() +"\n";
		resultado += "Velocidade de leitura (VL): "+ VL(minutos,segundos) +"\n";
		resultado += "Expressividade: "+ Expressividade()+"\n";
		resultado += "Ritmo: "+ Ritmo()+"\n\n";
		resultado += "===============================\n"
				+"Detalhes\n"
				+"===============================\n"
				+"Tempo: "+minutos+":"+segundos+"\n"
				+"Total de Palavras no texto: "+totalDePalavras+"\n"
				+"Total de sinais de pontua��o no texto: "+TotalDeSinaisPontuacao+"\n"
				+"Palavras lidas incorretamente: "+ plvErradas+"\n"
				+"Sinais de pontua��o desrespeitados: "+pontua+"\n"
				+"Vacila��es: "+vacil+"\n"
				+"Fragmenta��es: "+fragment+"\n"
				+"Silaba��es: "+silabs+"\n"
				+"Repeti��es: "+repeti+"\n";
		
		return resultado;
	}
	
	/**
	 * palavras lidas por minuto
	 * @author D�rio
	 */
	public float PLM(int minutos, int segundos){
		minutos = minutos + (segundos/60);
		return  (totalDePalavras / minutos)*60;
	}
	
	public int palavrasCertas(){
		return  totalDePalavras-plvErradas;
	}
	
	/**
	 * precis�o na leitura
	 * @author D�rio
	 */
	public float PL(){
		return (palavrasCertas()/totalDePalavras)*100;
	}
	
	/**
	 * velocidade de leitura
	 * @author D�rio
	 */
	public float VL(int minutos, int segundos){
		minutos = minutos + (segundos/60);
		int palavrasCertas = totalDePalavras - plvErradas;
		return  (palavrasCertas / minutos)*60;
	}
		
	/**
	 * calculo do n� todal de sinais de pontua��o menos os sinais de pontua��o desrespeitados
	 * @author D�rio
	 */
	public int Expressividade(){
		return TotalDeSinaisPontuacao - pontua;
	}
	
	/**
	 * calculo do n� total de palavras menos o total de falhas(repeti��es, vacila��es, siliba��es e fragmenta��es)
	 * @author D�rio
	 */
	public int Ritmo(){
		return totalDePalavras - (fragment + vacil + silabs + repeti);
	}
	
}

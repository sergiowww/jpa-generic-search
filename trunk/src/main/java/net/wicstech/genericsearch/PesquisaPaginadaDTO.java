/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software � confidencial e propriedade da Conab.
 * N�o � permitida sua distribui��o ou divulga��o do seu conte�do sem
 * expressa autoriza��o da Conab.
 * Este arquivo cont�m informa��es propriet�rias.
 */
package net.wicstech.genericsearch;

import java.io.Serializable;

/**
 * Dados para paginar a listagem.
 * 
 * @author sergio.oliveira
 * 
 */
public class PesquisaPaginadaDTO implements Serializable {

	private static final long serialVersionUID = 8474328976965592450L;

	/**
	 * Primeiro registro da p�gina.
	 */
	private long first;

	/**
	 * Quantidade de registros a trazer.
	 */
	private long count;
	/**
	 * Propriedade para ordenar.
	 */
	private String sortProperty;

	/**
	 * Dire��o do ordenamento.
	 */
	private boolean ascending;

	public PesquisaPaginadaDTO(long first, long count) {
		super();
		this.first = first;
		this.count = count;
	}

	/**
	 * @return the first
	 */
	public long getFirst() {
		return first;
	}

	/**
	 * @param first
	 *            the first to set
	 */
	public void setFirst(long first) {
		this.first = first;
	}

	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(long count) {
		this.count = count;
	}

	/**
	 * @return the sortProperty
	 */
	public String getSortProperty() {
		return sortProperty;
	}

	/**
	 * @param sortProperty
	 *            the sortProperty to set
	 */
	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending
	 *            the ascending to set
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

}

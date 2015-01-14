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
public class PagedSearchNavigation implements Serializable {

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

	/**
	 * 
	 * Construtor da consulta paginada, o primeiro registro e o count com o m�ximo de registros.
	 *
	 * @param first
	 * @param count
	 */
	public PagedSearchNavigation(long first, long count) {
		super();
		this.first = first;
		this.count = count;
	}

	public long getFirst() {
		return first;
	}

	public void setFirst(long first) {
		this.first = first;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

}

/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
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
	 * Primeiro registro da página.
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
	 * Direção do ordenamento.
	 */
	private boolean ascending;

	/**
	 * 
	 * Construtor da consulta paginada, o primeiro registro e o count com o máximo de registros.
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

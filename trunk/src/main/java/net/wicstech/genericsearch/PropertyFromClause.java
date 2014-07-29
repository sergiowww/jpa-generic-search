/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
 */
package net.wicstech.genericsearch;

import javax.persistence.criteria.From;

/**
 * Informações do nó fonte.
 * 
 * @author Sergio
 * 
 */
class PropertyFromClause {
	private From<Object, Object> currentFrom;
	private String nestedPropertiesToGet;

	/**
	 * @param currentFrom
	 * @param nestedPropertiesToGet
	 */
	public PropertyFromClause(From<Object, Object> currentFrom, String nestedPropertiesToGet) {
		super();
		this.currentFrom = currentFrom;
		this.nestedPropertiesToGet = nestedPropertiesToGet;
	}

	/**
	 * @return the currentFrom
	 */
	public From<Object, Object> getCurrentFrom() {
		return this.currentFrom;
	}

	/**
	 * @param currentFrom
	 *            the currentFrom to set
	 */
	public void setCurrentFrom(From<Object, Object> currentFrom) {
		this.currentFrom = currentFrom;
	}

	/**
	 * @return the nestedPropertiesToGet
	 */
	public String getNestedPropertiesToGet() {
		return this.nestedPropertiesToGet;
	}

	/**
	 * @param nestedPropertiesToGet
	 *            the nestedPropertiesToGet to set
	 */
	public void setNestedPropertiesToGet(String nestedPropertiesToGet) {
		this.nestedPropertiesToGet = nestedPropertiesToGet;
	}
}
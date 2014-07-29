/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software � confidencial e propriedade da Conab.
 * N�o � permitida sua distribui��o ou divulga��o do seu conte�do sem
 * expressa autoriza��o da Conab.
 * Este arquivo cont�m informa��es propriet�rias.
 */
package net.wicstech.genericsearch;

import javax.persistence.criteria.From;

/**
 * Informa��es do n� fonte.
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
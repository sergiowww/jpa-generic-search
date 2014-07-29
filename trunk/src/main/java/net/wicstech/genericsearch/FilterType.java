/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software � confidencial e propriedade da Conab.
 * N�o � permitida sua distribui��o ou divulga��o do seu conte�do sem
 * expressa autoriza��o da Conab.
 * Este arquivo cont�m informa��es propriet�rias.
 */
package net.wicstech.genericsearch;

/**
 * Tipos de filtro.
 * 
 * @author Sergio
 * 
 */
public enum FilterType {
	ILIKE,

	EQUALS,

	GREATER_THAN_OR_EQUAL,

	LESS_THAN_OR_EQUAL,

	/**
	 * Rastreia as propriedades anotadas dentro deste campo.
	 */
	SCAN_FILTERS_INSIDE_THIS
}

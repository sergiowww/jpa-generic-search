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
	/**
	 * Equals outro par�metro.
	 */
	EQUALS,

	/**
	 * Not equals um determinado par�metro.
	 */
	NOT_EQUALS,

	/**
	 * Maior que ou igual.
	 */
	GREATER_THAN_OR_EQUAL,

	/**
	 * Realiza um like case insensitive.
	 */
	ILIKE,

	/**
	 * Menor que ou igual.
	 */
	LESS_THAN_OR_EQUAL,

	/**
	 * Realiza um like com padr�o exatamente igual ao passado pelo par�metro.
	 */
	LIKE_EXACT,

	/**
	 * Rastreia as propriedades anotadas dentro deste campo.
	 */
	SCAN_FILTERS_INSIDE_THIS
}

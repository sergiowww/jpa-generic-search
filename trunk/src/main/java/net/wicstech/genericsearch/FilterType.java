/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
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
	 * Realiza um like case insensitive.
	 */
	ILIKE,

	/**
	 * Equals outro parâmetro.
	 */
	EQUALS,

	/**
	 * Maior que ou igual.
	 */
	GREATER_THAN_OR_EQUAL,

	/**
	 * Menor que ou igual.
	 */
	LESS_THAN_OR_EQUAL,

	/**
	 * Rastreia as propriedades anotadas dentro deste campo.
	 */
	SCAN_FILTERS_INSIDE_THIS
}

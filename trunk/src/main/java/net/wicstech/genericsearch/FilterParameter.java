/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software � confidencial e propriedade da Conab.
 * N�o � permitida sua distribui��o ou divulga��o do seu conte�do sem
 * expressa autoriza��o da Conab.
 * Este arquivo cont�m informa��es propriet�rias.
 */
package net.wicstech.genericsearch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que o valor deste campo ser� utilizado como um filtro.
 * 
 * @author Sergio
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FilterParameter {

	/**
	 * Tipo do filtro que ser� aplicado.
	 * 
	 * @return
	 */
	FilterType value();

	/**
	 * Este representa a propriedade da entidade, que dever� ser usada para montar a restri��o. Se esta n�o
	 * estiver presente, o valor da entidade � buscado do {@link FilterParameter#property()} ou do pr�prio
	 * nome do campo que esta anota��o est� colocada.
	 * 
	 * @return
	 */
	String[] entityProperty() default {};

	/**
	 * Colocar distinct no resultado quando esse crit�rio for selecionado.
	 * 
	 * @return
	 */
	boolean distinct() default false;

}

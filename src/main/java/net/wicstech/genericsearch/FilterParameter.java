/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
 */
package net.wicstech.genericsearch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que o valor deste campo será utilizado como um filtro.
 * 
 * @author Sergio
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FilterParameter {

	/**
	 * Tipo do filtro que será aplicado.
	 * 
	 * @return
	 */
	FilterType value();

	/**
	 * Este representa a propriedade da entidade, que deverá ser usada para montar a restrição. Se esta não
	 * estiver presente, o valor da entidade é buscado do {@link FilterParameter#property()} ou do próprio
	 * nome do campo que esta anotação está colocada.
	 * 
	 * @return
	 */
	String[] entityProperty() default {};

	/**
	 * Colocar distinct no resultado quando esse critério for selecionado.
	 * 
	 * @return
	 */
	boolean distinct() default false;

}

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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Classe base para DAOs.
 *
 * @author sergio.oliveira
 *
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractDao implements Serializable {

	private static final long serialVersionUID = 1907838897198727224L;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * EntityManager.
	 *
	 * @return
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * Criteria builder.
	 *
	 * @see EntityManager#getCriteriaBuilder()
	 * @return
	 */
	protected CriteriaBuilder criteriaBuilder() {
		return this.entityManager.getCriteriaBuilder();
	}

	/**
	 * ILike predicate.
	 *
	 * @param pathSelecion
	 * @param value
	 * @return
	 */
	protected final Predicate ilikePredicate(final Path<String> pathSelecion, final String value) {
		final CriteriaBuilder cb = criteriaBuilder();
		return cb.like(cb.lower(pathSelecion), '%' + value.toLowerCase() + '%');
	}

	/**
	 * Like exact predicate.
	 *
	 * @param pathSelecion
	 * @param pattern
	 * @return
	 */
	protected final Predicate likeExactPredicate(final Path<String> pathSelecion, final String pattern) {
		final CriteriaBuilder cb = criteriaBuilder();
		return cb.like(cb.lower(pathSelecion), pattern);
	}

	/**
	 * Listar uma tupla como instâncias da classe targetClass.
	 *
	 * @param resultList
	 * @param targetClass
	 * @return
	 */
	protected final <O> List<O> listTupleAs(final List<Tuple> resultList, final Class<O> targetClass) {
		final List<O> retorno = new ArrayList<O>();

		for (final Tuple tuple : resultList) {

			final O target = BeanUtils.instantiate(targetClass);
			final BeanWrapper wrapperTarget = PropertyAccessorFactory.forBeanPropertyAccess(target);
			wrapperTarget.setAutoGrowNestedPaths(true);
			final List<TupleElement<?>> elements = tuple.getElements();
			for (final TupleElement<?> tupleElement : elements) {
				final String property = tupleElement.getAlias();
				final Object value = tuple.get(tupleElement.getAlias());
				wrapperTarget.setPropertyValue(property, value);
			}
			retorno.add(target);
		}
		return retorno;
	}
}

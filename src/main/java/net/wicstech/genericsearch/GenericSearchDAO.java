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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * Dao para busca criterizada.
 *
 * @author sergio.oliveira
 *
 */
@Repository("genericSearchDAO")
@SuppressWarnings("unchecked")
public class GenericSearchDAO extends AbstractDao {

	private static final long serialVersionUID = 2858834138761219160L;

	/**
	 * Retornar um único resultado.
	 *
	 * @param entityClass
	 * @param parametros
	 * @return
	 */
	public <T extends Serializable> T getSingleResult(final Class<T> entityClass, final Serializable parametros) {
		// Verificar se existe mais de um registro e tratar como não registro único.
		final List<T> resultado = list(entityClass, parametros, new PagedSearchNavigation(NumberUtils.INTEGER_ZERO, 2));
		if (CollectionUtils.isEmpty(resultado)) {
			return null;
		} else if (resultado.size() > NumberUtils.INTEGER_ONE) {
			throw new NonUniqueResultException("A consulta não retornou um resultado único: " + resultado.size());
		}
		return resultado.get(NumberUtils.INTEGER_ZERO);
	}

	/**
	 * Listar todas as ocorrências sem paginação.
	 *
	 * @param entityClass
	 * @param parametros
	 * @return
	 */
	public <T extends Serializable> List<T> list(final Class<T> entityClass, final Serializable parametros) {
		return list(entityClass, parametros, new PagedSearchNavigation(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
	}

	/**
	 * Listar resultado com paginação de resultados.
	 *
	 * @param entityClass
	 * @param parametros
	 * @param pesquisa
	 * @return
	 */
	public <T extends Serializable> List<T> list(final Class<T> entityClass, final Serializable parametros, final PagedSearchNavigation pesquisa) {

		final CriteriaBuilder cb = criteriaBuilder();
		CriteriaQuery<?> criteria;

		final boolean modoSeletivo = parametros.getClass().isAnnotationPresent(SelectFields.class);
		if (modoSeletivo) {
			criteria = cb.createTupleQuery();
		} else {
			criteria = cb.createQuery(entityClass);
		}

		final Root<T> from = criteria.from(entityClass);

		processFilters(parametros, entityClass, criteria, from);

		processSorting(pesquisa, entityClass, criteria, from);
		final long count = pesquisa.getCount();
		final long first = pesquisa.getFirst();
		if (modoSeletivo) {
			processSelections(parametros, entityClass, criteria, from);
			return listTuple((CriteriaQuery<Tuple>) criteria, first, count);
		}
		final TypedQuery<T> query = (TypedQuery<T>) createQuery(criteria, first, count);
		return query.getResultList();
	}

	/**
	 * Contar a quantidade de registros de uma query.
	 *
	 * @param entityClass
	 * @param parametros
	 * @return
	 */
	public <T extends Serializable> long size(final Class<T> entityClass, final Serializable parametros) {
		final CriteriaBuilder cb = criteriaBuilder();
		final CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		final Root<?> from = criteria.from(entityClass);
		processFilters(parametros, entityClass, criteria, from);
		if (criteria.isDistinct()) {
			criteria.distinct(false);
			criteria.select(cb.countDistinct(from));
		} else {
			criteria.select(cb.count(from));
		}
		return getSingleResultAs(criteria);
	}

	/**
	 * Processar as seleções da consulta.
	 *
	 * @param searchObject
	 * @param entityType
	 * @param criteria
	 * @param from
	 */
	protected void processSelections(final Serializable searchObject, final Class<?> entityType, final CriteriaQuery<?> criteria, final Root<?> from) {
		final SelectFields selectFieldsAnnotation = searchObject.getClass().getAnnotation(SelectFields.class);
		final List<Selection<?>> selecoes = new ArrayList<Selection<?>>();
		selectFields(entityType, from, selectFieldsAnnotation, selecoes);

		criteria.multiselect(selecoes);
	}

	/**
	 * Selecionar campos da consulta.
	 *
	 * @param entityType
	 * @param from
	 * @param selectFieldsAnnotation
	 * @param selecoes
	 */
	protected void selectFields(final Class<?> entityType, final Root<?> from, final SelectFields selectFieldsAnnotation, final List<Selection<?>> selecoes) {
		final String[] selectFields = selectFieldsAnnotation.value();
		for (final String nestedProperties : selectFields) {
			final FieldMetadata metadata = newFieldMetadata(entityType, from, nestedProperties);
			final Selection<Object> selection = metadata.getPath(nestedProperties).alias(nestedProperties);
			selecoes.add(selection);
		}
	}

	/**
	 * Criar query.
	 *
	 * @param query
	 * @param first
	 * @param count
	 * @return
	 */
	private <T> TypedQuery<T> createQuery(final CriteriaQuery<T> query, final long first, final long count) {
		final TypedQuery<T> typedQuery = getEntityManager().createQuery(query);
		if (count > 0) {
			typedQuery.setFirstResult((int) first);
			typedQuery.setMaxResults((int) count);
		}
		return typedQuery;
	}

	/**
	 * Criar os filtros de restrição.
	 *
	 * @param path
	 * @param filterValue
	 * @param filterType
	 * @return
	 */
	private <T> Predicate filterRestriction(final Path<T> path, final Object filterValue, final FilterType filterType) {
		switch (filterType) {
			case NOT_EQUALS:
				return criteriaBuilder().notEqual(path, filterValue);
			case EQUALS:
				return criteriaBuilder().equal(path, filterValue);
			case ILIKE:
				return ilikePredicate((Path<String>) path, (String) filterValue);
			case LIKE_EXACT:
				return likeExactPredicate((Path<String>) path, (String) filterValue);
			case GREATER_THAN_OR_EQUAL:
				return criteriaBuilder().greaterThanOrEqualTo((Path<Date>) path, (Date) filterValue);
			case LESS_THAN_OR_EQUAL:
				return criteriaBuilder().lessThanOrEqualTo((Path<Date>) path, (Date) filterValue);
			default:
				throw new IllegalArgumentException("FilterType " + filterType + " não reconhecido!");
		}
	}

	/**
	 * Recuperar o caminho da propriedade de uma entidade.
	 *
	 * @param entityType
	 *
	 * @param field
	 * @param filterParameter
	 * @param parentEntityPath
	 *
	 * @return
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	private String[] getEntityPropertyPath(final Class<?> entityType, final Field field, final FilterParameter filterParameter, final String[] parentEntityPath) {
		if (field.getType() == entityType) {
			return null;
		}
		String[] propertyPath = filterParameter.entityProperty();

		if (ArrayUtils.isEmpty(propertyPath)) {
			propertyPath = new String[] {field.getName()};
		}

		if (ArrayUtils.isNotEmpty(parentEntityPath)) {
			final String parentProperty = parentEntityPath[NumberUtils.INTEGER_ZERO];
			for (int i = 0; i < propertyPath.length; i++) {
				propertyPath[i] = new StringBuilder(parentProperty).append(FieldMetadata.PONTO).append(propertyPath[i]).toString();
			}
		}
		return propertyPath;
	}

	/**
	 * Buscar metadados para serem filtrados na consulta.
	 *
	 * @param searchObject
	 * @param entityType
	 * @param from
	 * @param parentEntityPath
	 * @return
	 */
	private List<FieldMetadata> getFieldMetadata(final Serializable searchObject, final Class<?> entityType, final Root<?> from, final String[] parentEntityPath) {
		final List<FieldMetadata> fieldMetadata = new ArrayList<FieldMetadata>();
		final Class<? extends Serializable> searchObjectClass = searchObject.getClass();
		final List<Field> fields = getFields(searchObjectClass);
		final ConfigurablePropertyAccessor wrapper = PropertyAccessorFactory.forDirectFieldAccess(searchObject);
		for (final Field field : fields) {
			if (field.isAnnotationPresent(FilterParameter.class)) {
				final Object propertyValue = wrapper.getPropertyValue(field.getName());
				// CHECKSTYLE:OFF
				if (propertyValue != null) {
					final FilterParameter filterParameter = field.getAnnotation(FilterParameter.class);
					final String[] entityPropertyPath = getEntityPropertyPath(entityType, field, filterParameter, parentEntityPath);

					if (FilterType.SCAN_FILTERS_INSIDE_THIS.equals(filterParameter.value())) {
						if (propertyValue instanceof Serializable) {
							fieldMetadata.addAll(getFieldMetadata((Serializable) propertyValue, entityType, from, entityPropertyPath));
						} else {
							throw new IllegalArgumentException("O campo " + field.getName() + " não é serializable, deveria ser!");
						}
					} else {
						final FieldMetadata metadados = newFieldMetadata(entityType, from, entityPropertyPath);
						metadados.setSearchObjectFieldName(field.getName());
						metadados.setFilterParameter(filterParameter);
						metadados.setWrapper(wrapper);
						fieldMetadata.add(metadados);
					}
				}
				// CHECKSTYLE:ON
			}
		}
		return fieldMetadata;
	}

	private List<Field> getFields(Class<?> searchObjectClass) {
		final List<Field> fields = new ArrayList<Field>();
		while (searchObjectClass != Object.class) {
			fields.addAll(Arrays.asList(searchObjectClass.getDeclaredFields()));
			searchObjectClass = searchObjectClass.getSuperclass();
		}

		return fields;
	}

	/**
	 * Lista o resultado do criteria query como um único resultado.
	 *
	 * @param <Y>
	 * @param query
	 * @return
	 */
	private <Y> Y getSingleResultAs(final CriteriaQuery<Y> query) {
		final TypedQuery<Y> typedQuery = getEntityManager().createQuery(query);
		return typedQuery.getSingleResult();
	}

	/**
	 * Listar resultados com paginação.
	 *
	 * @param query
	 * @param pesquisaDTO
	 * @return
	 */
	private <I, O> List<O> listTuple(final CriteriaQuery<Tuple> query, final long first, final long count) {

		final TypedQuery<Tuple> typedQuery = createQuery(query, first, count);

		final List<Tuple> resultList = typedQuery.getResultList();
		final Class<O> targetClass = (Class<O>) query.getRoots().iterator().next().getJavaType();
		return listTupleAs(resultList, targetClass);
	}

	/**
	 * Criar campo metadata.
	 *
	 * @param entityType
	 * @param from
	 * @param entityPropertyPath
	 * @return
	 */
	private <T> FieldMetadata newFieldMetadata(final Class<T> entityType, final Root<?> from, final String... entityPropertyPath) {
		final Metamodel metamodel = getEntityManager().getMetamodel();
		return new FieldMetadata(metamodel, entityType, from, entityPropertyPath);
	}

	/**
	 * Processar os filtros.
	 *
	 * @param searchObject
	 * @param entityType
	 * @param criteria
	 * @param from
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	private void processFilters(final Serializable searchObject, final Class<?> entityType, final CriteriaQuery<?> criteria, final Root<?> from) {

		final List<FieldMetadata> metadados = getFieldMetadata(searchObject, entityType, from, null);
		final List<Predicate> predicates = new ArrayList<Predicate>();
		for (final FieldMetadata fieldMetadata : metadados) {
			final String[] entityProperty = fieldMetadata.getEntityProperty();
			final List<Predicate> orPredicates = new ArrayList<Predicate>();
			for (final String property : entityProperty) {
				if (fieldMetadata.isDistinct()) {
					criteria.distinct(fieldMetadata.isDistinct());
				}
				final Object filterValue = fieldMetadata.getPropertyValue();
				final Path<Object> restrictionProperty = fieldMetadata.getPath(property);
				final Predicate predicate = filterRestriction(restrictionProperty, filterValue, fieldMetadata.getFilterParameter().value());
				orPredicates.add(predicate);
			}
			if (orPredicates.size() > NumberUtils.INTEGER_ONE) {
				predicates.add(criteriaBuilder().or(toArray(orPredicates)));
			} else {
				predicates.addAll(orPredicates);
			}
		}
		if (!predicates.isEmpty()) {
			criteria.where(toArray(predicates));
		}
	}

	/**
	 * Acrescentar a ordenação do resultado.
	 *
	 * @param pesquisa
	 * @param entityType
	 * @param criteria
	 * @param from
	 */
	private <T extends Serializable> void processSorting(final PagedSearchNavigation pesquisa, final Class<T> entityType, final CriteriaQuery<?> criteria, final Root<T> from) {
		if (StringUtils.isNotBlank(pesquisa.getSortProperty())) {
			final CriteriaBuilder cb = criteriaBuilder();

			final FieldMetadata metadata = newFieldMetadata(entityType, from, pesquisa.getSortProperty());
			metadata.setJoinType(JoinType.LEFT);
			final Expression<?> expression = metadata.getPath(pesquisa.getSortProperty());
			// CHECKSTYLE:OFF
			criteria.orderBy(pesquisa.isAscending() ? cb.asc(expression) : cb.desc(expression));
			// CHECKSTYLE:ON
		}
	}

	/**
	 * Transformar o resultado em array.
	 *
	 * @param predicates
	 * @return
	 */
	private Predicate[] toArray(final List<Predicate> predicates) {
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}

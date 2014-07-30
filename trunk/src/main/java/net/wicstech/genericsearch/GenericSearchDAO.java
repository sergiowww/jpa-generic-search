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
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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
	public <T extends Serializable> T getSingleResult(Class<T> entityClass, Serializable parametros) {
		List<T> resultado = list(entityClass, parametros, new PesquisaPaginadaDTO(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ONE));
		if (resultado.isEmpty()) {
			return null;
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
	public <T extends Serializable> List<T> list(Class<T> entityClass, Serializable parametros) {
		return list(entityClass, parametros, new PesquisaPaginadaDTO(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
	}

	/**
	 * Listar resultado com paginação de resultados.
	 * 
	 * @param entityClass
	 * @param parametros
	 * @param pesquisa
	 * @return
	 */
	public <T extends Serializable> List<T> list(Class<T> entityClass, Serializable parametros, PesquisaPaginadaDTO pesquisa) {

		CriteriaBuilder cb = criteriaBuilder();
		CriteriaQuery<?> criteria;

		boolean modoSeletivo = parametros.getClass().isAnnotationPresent(SelectFields.class);
		if (modoSeletivo) {
			criteria = cb.createTupleQuery();
		} else {
			criteria = cb.createQuery(entityClass);
		}

		Root<T> from = criteria.from(entityClass);

		processFilters(parametros, entityClass, criteria, from);

		processSorting(pesquisa, entityClass, criteria, from);
		long count = pesquisa.getCount();
		long first = pesquisa.getFirst();
		if (modoSeletivo) {
			processSelections(parametros, entityClass, criteria, from);
			return listTuple((CriteriaQuery<Tuple>) criteria, first, count);
		}
		TypedQuery<T> query = (TypedQuery<T>) createQuery(criteria, first, count);
		return query.getResultList();
	}

	/**
	 * Acrescentar a ordenação do resultado.
	 * 
	 * @param pesquisa
	 * @param entityType
	 * @param criteria
	 * @param from
	 */
	private <T extends Serializable> void processSorting(PesquisaPaginadaDTO pesquisa, Class<T> entityType, CriteriaQuery<?> criteria, Root<T> from) {
		if (StringUtils.isNotBlank(pesquisa.getSortProperty())) {
			CriteriaBuilder cb = criteriaBuilder();

			FieldMetadata metadata = newFieldMetadata(entityType, from, pesquisa.getSortProperty());
			Expression<?> expression = metadata.getPath(pesquisa.getSortProperty());
			// CHECKSTYLE:OFF
			criteria.orderBy(pesquisa.isAscending() ? cb.asc(expression) : cb.desc(expression));
			// CHECKSTYLE:ON
		}
	}

	/**
	 * Listar resultados com paginação.
	 * 
	 * @param query
	 * @param pesquisaDTO
	 * @return
	 */
	private <I, O> List<O> listTuple(CriteriaQuery<Tuple> query, long first, long count) {

		TypedQuery<Tuple> typedQuery = createQuery(query, first, count);

		List<Tuple> resultList = typedQuery.getResultList();
		Class<O> targetClass = (Class<O>) query.getRoots().iterator().next().getJavaType();
		return listTupleAs(resultList, targetClass);
	}

	private <T> TypedQuery<T> createQuery(CriteriaQuery<T> query, long first, long count) {
		TypedQuery<T> typedQuery = getEntityManager().createQuery(query);
		if (count > 0) {
			typedQuery.setFirstResult((int) first);
			typedQuery.setMaxResults((int) count);
		}
		return typedQuery;
	}

	/**
	 * Processar as seleções da consulta.
	 * 
	 * @param searchObject
	 * @param entityType
	 * @param criteria
	 * @param from
	 */
	protected void processSelections(Serializable searchObject, Class<?> entityType, CriteriaQuery<?> criteria, Root<?> from) {
		SelectFields selectFieldsAnnotation = searchObject.getClass().getAnnotation(SelectFields.class);
		List<Selection<?>> selecoes = new ArrayList<Selection<?>>();
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
	protected void selectFields(Class<?> entityType, Root<?> from, SelectFields selectFieldsAnnotation, List<Selection<?>> selecoes) {
		String[] selectFields = selectFieldsAnnotation.value();
		for (String nestedProperties : selectFields) {
			FieldMetadata metadata = newFieldMetadata(entityType, from, nestedProperties);
			Selection<Object> selection = metadata.getPath(nestedProperties).alias(nestedProperties);
			selecoes.add(selection);
		}
	}

	/**
	 * Processar os filtros.
	 * 
	 * @param searchObject
	 * @param entityType
	 * @param criteria
	 * @param from
	 */
	private void processFilters(Serializable searchObject, Class<?> entityType, CriteriaQuery<?> criteria, Root<?> from) {

		List<FieldMetadata> metadados = getFieldMetadata(searchObject, entityType, from, null);
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (FieldMetadata fieldMetadata : metadados) {
			String[] entityProperty = fieldMetadata.getEntityProperty();
			List<Predicate> orPredicates = new ArrayList<Predicate>();
			for (String property : entityProperty) {
				if (fieldMetadata.isDistinct()) {
					criteria.distinct(fieldMetadata.isDistinct());
				}
				Object filterValue = fieldMetadata.getPropertyValue();
				Path<Object> restrictionProperty = fieldMetadata.getPath(property);
				Predicate predicate = filterRestriction(restrictionProperty, filterValue, fieldMetadata.getFilterParameter().value());
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

	private Predicate[] toArray(List<Predicate> predicates) {
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private <T> Predicate filterRestriction(Path<T> path, Object filterValue, FilterType filterType) {

		if (FilterType.ILIKE.equals(filterType)) {
			return ilikePredicate((Path<String>) path, (String) filterValue);
		}
		if (FilterType.LESS_THAN_OR_EQUAL.equals(filterType)) {
			return criteriaBuilder().lessThanOrEqualTo((Path<Date>) path, (Date) filterValue);
		}
		if (FilterType.GREATER_THAN_OR_EQUAL.equals(filterType)) {
			return criteriaBuilder().greaterThanOrEqualTo((Path<Date>) path, (Date) filterValue);
		}
		if (FilterType.EQUALS.equals(filterType)) {
			return criteriaBuilder().equal(path, filterValue);
		}
		throw new IllegalArgumentException("FilterType " + filterType + " não reconhecido!");
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
	private List<FieldMetadata> getFieldMetadata(Serializable searchObject, Class<?> entityType, Root<?> from, String[] parentEntityPath) {
		List<FieldMetadata> fieldMetadata = new ArrayList<FieldMetadata>();
		Class<? extends Serializable> searchObjectClass = searchObject.getClass();
		Field[] fields = searchObjectClass.getDeclaredFields();
		ConfigurablePropertyAccessor wrapper = PropertyAccessorFactory.forDirectFieldAccess(searchObject);
		for (Field field : fields) {
			if (field.isAnnotationPresent(FilterParameter.class)) {
				Object propertyValue = wrapper.getPropertyValue(field.getName());
				if (propertyValue != null) {
					FilterParameter filterParameter = field.getAnnotation(FilterParameter.class);
					String[] entityPropertyPath = getEntityPropertyPath(field, filterParameter, parentEntityPath);

					if (FilterType.SCAN_FILTERS_INSIDE_THIS.equals(filterParameter.value())) {
						if (propertyValue instanceof Serializable) {
							fieldMetadata.addAll(getFieldMetadata((Serializable) propertyValue, entityType, from, entityPropertyPath));
						} else {
							throw new IllegalArgumentException("O campo " + field.getName() + " não é serializable, deveria ser!");
						}
					} else {
						FieldMetadata metadados = newFieldMetadata(entityType, from, entityPropertyPath);
						metadados.setSearchObjectFieldName(field.getName());
						metadados.setFilterParameter(filterParameter);
						metadados.setWrapper(wrapper);
						fieldMetadata.add(metadados);
					}
				}
			}
		}
		return fieldMetadata;
	}

	private <T> FieldMetadata newFieldMetadata(Class<T> entityType, Root<?> from, String... entityPropertyPath) {
		Metamodel metamodel = getEntityManager().getMetamodel();
		return new FieldMetadata(metamodel, entityType, from, entityPropertyPath);
	}

	private String[] getEntityPropertyPath(Field field, FilterParameter filterParameter, String[] parentEntityPath) {
		String[] propertyPath = filterParameter.entityProperty();
		if (ArrayUtils.isEmpty(propertyPath)) {
			propertyPath = new String[] {field.getName()};
		}

		if (ArrayUtils.isNotEmpty(parentEntityPath)) {
			String parentProperty = parentEntityPath[NumberUtils.INTEGER_ZERO];
			for (int i = 0; i < propertyPath.length; i++) {
				propertyPath[i] = parentProperty + FieldMetadata.PONTO + propertyPath[i];
			}
		}
		return propertyPath;
	}

	public <T extends Serializable> long size(Class<T> entityClass, Serializable parametros) {
		CriteriaBuilder cb = criteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<?> from = criteria.from(entityClass);
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
	 * Lista o resultado do criteria query como um único resultado.
	 * 
	 * @param <Y>
	 * @param query
	 * @return
	 */
	private <Y> Y getSingleResultAs(CriteriaQuery<Y> query) {
		TypedQuery<Y> typedQuery = getEntityManager().createQuery(query);
		return typedQuery.getSingleResult();
	}

}

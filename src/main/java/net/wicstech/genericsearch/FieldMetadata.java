/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
 */
package net.wicstech.genericsearch;

import java.util.Set;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;

/**
 * Metadados de pesquisa.
 * 
 * @author sergio.oliveira
 *
 */
class FieldMetadata {
	public static final String PONTO = ".";
	private static final String SPLIT_NO_PONTO = "\\" + PONTO;

	private final Class<?> entityType;
	private final Root<?> from;
	private final String[] entityProperty;
	private FilterParameter filterParameter;
	private String searchObjectFieldName;
	private final Metamodel metamodel;
	private JoinType joinType = JoinType.INNER;

	private ConfigurablePropertyAccessor wrapper;

	public FieldMetadata(Metamodel metamodel, Class<?> entityType, Root<?> from, String... entityProperty) {
		super();
		this.entityProperty = entityProperty;
		this.entityType = entityType;
		this.from = from;
		this.metamodel = metamodel;
	}

	@SuppressWarnings("PMD.ConfusingTernary")
	private PropertyFromClause getPropertyFrom(String propertyPath) {
		// CHECKSTYLE:OFF
		@SuppressWarnings("unchecked")
		From<Object, Object> currentFrom = (From<Object, Object>) from;
		// CHECKSTYLE:ON
		String[] properties = propertyPath.split(SPLIT_NO_PONTO);
		Class<?> currentEntityType = entityType;
		int ultimoIndice = properties.length - NumberUtils.INTEGER_ONE;
		for (int indice = 0; indice < properties.length; indice++) {
			String property = properties[indice];
			EntityType<?> entity = metamodel.entity(currentEntityType);
			Attribute<?, ?> attribute = entity.getAttribute(property);

			boolean isCollection = attribute.isCollection();
			if (attribute.getPersistentAttributeType().equals(Attribute.PersistentAttributeType.EMBEDDED)) {
				property = StringUtils.join(properties, PONTO, ArrayUtils.indexOf(properties, property), properties.length);
				// CHECKSTYLE:OFF
				indice = ultimoIndice;
			}

			// CHECKSTYLE:OFF
			if (indice != ultimoIndice) {
				currentFrom = createJoin(property, currentFrom);
				currentEntityType = getTypeOrListType(attribute);
			} else if (isCollection) {
				return new PropertyFromClause(createJoin(property, currentFrom), null);
			} else {
				return new PropertyFromClause(currentFrom, property);
			}
			// CHECKSTYLE:ON
		}
		throw new IllegalArgumentException("Nenhuma propriedade informada");
	}

	public <Y> Path<Y> getPath(String propertyPath) {
		return this.<Y> getPath(getPropertyFrom(propertyPath));
	}

	// CHECKSTYLE:OFF
	@SuppressWarnings("unchecked")
	// CHECKSTYLE:ON
	private <Y> Path<Y> getPath(PropertyFromClause sourceInformation) {
		String nestedPropertiesToGet = sourceInformation.getNestedPropertiesToGet();
		if (nestedPropertiesToGet == null) {
			return (Path<Y>) sourceInformation.getCurrentFrom();
		}
		From<Object, Object> currentFrom = sourceInformation.getCurrentFrom();
		if (StringUtils.contains(nestedPropertiesToGet, PONTO)) {
			String[] properties = nestedPropertiesToGet.split(SPLIT_NO_PONTO);
			Path<Y> path = null;
			for (String property : properties) {
				if (path == null) {
					path = currentFrom.get(property);
				} else {
					path = path.get(property);
				}
			}
			return path;
		}
		return currentFrom.get(nestedPropertiesToGet);
	}

	/**
	 * Criar um join ou utilizar um que já foi criado.
	 * 
	 * @param propertyName
	 * @param currentFrom
	 * @return
	 */
	// CHECKSTYLE:OFF
	@SuppressWarnings({"unchecked", "PMD.UnusedPrivateMethod"})
	// CHECKSTYLE:ON
	private From<Object, Object> createJoin(String propertyName, From<Object, Object> currentFrom) {
		Set<Join<Object, ?>> joins = currentFrom.getJoins();
		for (Join<Object, ?> join : joins) {
			if (propertyName.equals(join.getAttribute().getName())) {
				return (From<Object, Object>) join;
			}
		}
		return currentFrom.join(propertyName, joinType);
	}

	/**
	 * Retorna o próprio tipo ou o tipo da lista.
	 * 
	 * @param attribute
	 * @param propertyType
	 * @return
	 */
	private Class<?> getTypeOrListType(Attribute<?, ?> attribute) {
		if (attribute.isCollection()) {
			PluralAttribute<?, ?, ?> plural = (PluralAttribute<?, ?, ?>) attribute;

			return plural.getElementType().getJavaType();
		}
		return attribute.getJavaType();
	}

	@SuppressWarnings("PMD.MethodReturnsInternalArray")
	public String[] getEntityProperty() {
		return entityProperty;
	}

	public FilterParameter getFilterParameter() {
		return filterParameter;
	}

	public void setFilterParameter(FilterParameter filterParameter) {
		this.filterParameter = filterParameter;
	}

	public boolean isDistinct() {
		return filterParameter.distinct();
	}

	public void setSearchObjectFieldName(String searchObjectFieldName) {
		this.searchObjectFieldName = searchObjectFieldName;
	}

	public void setWrapper(ConfigurablePropertyAccessor wrapper) {
		this.wrapper = wrapper;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public Object getPropertyValue() {
		return wrapper.getPropertyValue(searchObjectFieldName);
	}

	/**
	 * Informações do nó fonte.
	 * 
	 * @author Sergio
	 * 
	 */
	private class PropertyFromClause {
		private final From<Object, Object> currentFrom;
		private final String nestedPropertiesToGet;

		/**
		 * Construtor.
		 * 
		 * @param currentFrom
		 * @param nestedPropertiesToGet
		 */
		public PropertyFromClause(From<Object, Object> currentFrom, String nestedPropertiesToGet) {
			super();
			this.currentFrom = currentFrom;
			this.nestedPropertiesToGet = nestedPropertiesToGet;
		}

		public From<Object, Object> getCurrentFrom() {
			return this.currentFrom;
		}

		public String getNestedPropertiesToGet() {
			return this.nestedPropertiesToGet;
		}

	}
}

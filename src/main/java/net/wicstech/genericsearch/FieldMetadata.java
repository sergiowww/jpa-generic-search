package net.wicstech.genericsearch;

import java.util.Set;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
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
	private boolean distinct;
	private FilterParameter filterParameter;
	private String searchObjectFieldName;
	private Metamodel metamodel;
	private ConfigurablePropertyAccessor wrapper;

	public FieldMetadata(Metamodel metamodel, Class<?> entityType, Root<?> from, String... entityProperty) {
		super();
		this.entityProperty = entityProperty;
		this.entityType = entityType;
		this.from = from;
		this.metamodel = metamodel;
	}

	@SuppressWarnings("unchecked")
	private PropertyFromClause getPropertyFrom(String propertyPath) {
		From<Object, Object> currentFrom = (From<Object, Object>) from;
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
				indice = ultimoIndice;
			}

			if (indice != ultimoIndice) {
				currentFrom = createJoin(property, currentFrom);
				if (isCollection && !distinct) {
					distinct = true;
				}
				if (NumberUtils.INTEGER_ONE.equals(properties.length)) {
					return new PropertyFromClause(currentFrom, property);
				}
				currentEntityType = getTypeOrListType(attribute);
			} else {
				return new PropertyFromClause(currentFrom, property);
			}
		}
		return null;
	}

	public <Y> Path<Y> getPath(String propertyPath) {
		return this.<Y> getPath(getPropertyFrom(propertyPath));
	}

	private <Y> Path<Y> getPath(PropertyFromClause sourceInformation) {
		String nestedPropertiesToGet = sourceInformation.getNestedPropertiesToGet();
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
	@SuppressWarnings("unchecked")
	private From<Object, Object> createJoin(String propertyName, From<Object, Object> currentFrom) {
		Set<Join<Object, ?>> joins = currentFrom.getJoins();
		for (Join<Object, ?> join : joins) {
			if (propertyName.equals(join.getAttribute().getName())) {
				return (From<Object, Object>) join;
			}
		}
		return currentFrom.join(propertyName);
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
		return distinct;
	}

	public void setSearchObjectFieldName(String searchObjectFieldName) {
		this.searchObjectFieldName = searchObjectFieldName;
	}

	public void setWrapper(ConfigurablePropertyAccessor wrapper) {
		this.wrapper = wrapper;
	}

	public Object getPropertyValue() {
		return wrapper.getPropertyValue(searchObjectFieldName);
	}

}

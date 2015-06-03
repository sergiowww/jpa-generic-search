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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.wicstech.genericsearch.entidadestest.SituacaoCorretorCorretora;

import org.springframework.transaction.annotation.Transactional;

/**
 * Dao genérico para gravar objetos no banco de dados.
 * 
 * @author sergio.oliveira
 * 
 */
public class TestDbDAO {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Salvar objetos no banco de aceitação.
	 * 
	 * @param entities
	 */
	@Transactional
	public void salvar(Serializable... entities) {
		for (Serializable entity : entities) {
			entityManager.persist(entity);
			entityManager.flush();
		}
	}

	/**
	 * Salvar objetos no banco de dados.
	 * 
	 * @param entities
	 */
	@Transactional
	public <T extends Serializable> void salvar(List<T> entities) {
		for (Serializable entity : entities) {
			entityManager.persist(entity);
			entityManager.flush();
		}
	}

	/**
	 * Busca pelo identificador.
	 * 
	 * @param entityClass
	 * @param primaryKey
	 * @return
	 */
	public <T> T findById(Class<T> entityClass, Object primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}

	/**
	 * Listar todas as ocorrências da entidade informada.
	 * 
	 * @param class1
	 * @return
	 */
	public <T> List<T> getAll(Class<T> class1) {
		TypedQuery<T> query = entityManager.createQuery("from " + class1.getSimpleName(), class1);
		return query.getResultList();

	}

	/**
	 * Listar situação.
	 * 
	 * @param situacaoDescricao
	 * @return
	 */
	public SituacaoCorretorCorretora getSituacaoByDescricao(String situacaoDescricao) {
		String queryString = "from " + SituacaoCorretorCorretora.class.getSimpleName() + " as sit where sit.descricao = :situacaoDescricao";
		TypedQuery<SituacaoCorretorCorretora> query = entityManager.createQuery(queryString, SituacaoCorretorCorretora.class);
		query.setParameter("situacaoDescricao", situacaoDescricao);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}

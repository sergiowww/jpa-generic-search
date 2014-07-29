/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software � confidencial e propriedade da Conab.
 * N�o � permitida sua distribui��o ou divulga��o do seu conte�do sem
 * expressa autoriza��o da Conab.
 * Este arquivo cont�m informa��es propriet�rias.
 */
package net.wicstech.genericsearch.entidadestest;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;
import net.wicstech.genericsearch.SelectFields;

/**
 * The persistent class for the tb_situacao_corretor_corretora database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_SITUACAO_CORRETOR_CORRETORA)
@SelectFields({"descricao", "id"})
@Access(AccessType.FIELD)
public class SituacaoCorretorCorretora implements Serializable {

	private static final long serialVersionUID = -7807803082021876951L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	@FilterParameter(FilterType.EQUALS)
	private String descricao;

	@Column(nullable = true)
	private String tipoSituacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoSituacao() {
		return tipoSituacao;
	}

	public void setTipoSituacao(String tipoSituacao) {
		this.tipoSituacao = tipoSituacao;
	}
}
/*
 * Copyright (c) Companhia Nacional de Abastecimento - Conab
 *
 * Este software é confidencial e propriedade da Conab.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem
 * expressa autorização da Conab.
 * Este arquivo contém informações proprietárias.
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

/**
 * The persistent class for the tb_tipo_documento database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_TIPO_DOCUMENTO)
@Access(AccessType.FIELD)
public class TipoDocumento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_tipo_documento", unique = true, nullable = false)
	@FilterParameter(FilterType.EQUALS)
	private Long id;

	@FilterParameter(FilterType.ILIKE)
	@Column(length = 100)
	private String nome;

	@FilterParameter(FilterType.ILIKE)
	@Column(length = 500)
	private String descricao;

	@FilterParameter(FilterType.EQUALS)
	@Column(name = "ind_possui_validade")
	private Boolean possuiValidade;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getPossuiValidade() {
		return possuiValidade;
	}

	public void setPossuiValidade(Boolean possuiValidade) {
		this.possuiValidade = possuiValidade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TipoDocumento)) {
			return false;
		}
		TipoDocumento other = (TipoDocumento) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
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
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;

/**
 * The persistent class for the tb_corretor database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_CORRETOR)
@Access(AccessType.FIELD)
public class Corretor implements Serializable {

	private static final long serialVersionUID = 8823321133795052999L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_corretor", unique = true, nullable = false)
	@FilterParameter(FilterType.EQUALS)
	private Long id;

	@Column(nullable = false, unique = true, length = 11)
	@FilterParameter(FilterType.EQUALS)
	private String cpf;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_termino_suspensao")
	private Date dataTerminoSuspensao;

	// bi-directional many-to-one association to SituacaoCorretor
	// @ManyToOne
	@ManyToOne
	@JoinColumn(name = "id_situacao_corretor", nullable = false)
	@FilterParameter(FilterType.EQUALS)
	private SituacaoCorretorCorretora situacaoCorretorCorretora;

	public Long getId() {
		return this.id;
	}

	public void setId(Long idCorretor) {
		this.id = idCorretor;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataTerminoSuspensao() {
		return this.dataTerminoSuspensao;
	}

	public void setDataTerminoSuspensao(Date dataTerminoSuspensao) {
		this.dataTerminoSuspensao = dataTerminoSuspensao;
	}

	public SituacaoCorretorCorretora getSituacaoCorretorCorretora() {
		return this.situacaoCorretorCorretora;
	}

	public void setSituacaoCorretorCorretora(SituacaoCorretorCorretora situacaoCorretorCorretora) {
		this.situacaoCorretorCorretora = situacaoCorretorCorretora;
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
		if (!(obj instanceof Corretor)) {
			return false;
		}
		Corretor other = (Corretor) obj;
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
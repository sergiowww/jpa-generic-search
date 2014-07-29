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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;

/**
 * The persistent class for the tb_bolsa_corretora_corretor database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_BOLSA_CORRETORA_CORRETOR)
@Access(AccessType.FIELD)
public class BolsaCorretoraCorretor implements Serializable {

	private static final long serialVersionUID = -1857370723501024850L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_bolsa_corretora_corretor")
	private Long id;

	// bi-directional many-to-one association to Corretor
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_corretor", nullable = false)
	@FilterParameter(value = FilterType.SCAN_FILTERS_INSIDE_THIS)
	private Corretor corretor;

	// bi-directional many-to-one association to Corretora
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_corretora", nullable = false)
	@FilterParameter(value = FilterType.SCAN_FILTERS_INSIDE_THIS)
	private Corretora corretora;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Corretor getCorretor() {
		return this.corretor;
	}

	public void setCorretor(Corretor corretor) {
		this.corretor = corretor;
	}

	public Corretora getCorretora() {
		return this.corretora;
	}

	public void setCorretora(Corretora corretora) {
		this.corretora = corretora;
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
		if (!(obj instanceof BolsaCorretoraCorretor)) {
			return false;
		}
		BolsaCorretoraCorretor other = (BolsaCorretoraCorretor) obj;
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
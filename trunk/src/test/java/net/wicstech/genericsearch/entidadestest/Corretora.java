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
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;

/**
 * The persistent class for the tb_corretora database table.
 *
 */
@Entity
@Table(name = Tabelas.TABELA_CORRETORA)
@Access(AccessType.FIELD)
public class Corretora implements Serializable {

	private static final long serialVersionUID = 7648574683264712564L;

	@Column(nullable = false, unique = true, length = 14)
	@FilterParameter(FilterType.EQUALS)
	private String cnpj;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_termino_suspensao")
	private Date dataTerminoSuspensao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_corretora", unique = true, nullable = false)
	@FilterParameter(FilterType.EQUALS)
	private Long id;

	// bi-directional many-to-one association to BolsaCorretoraCorretor
	@OneToMany(mappedBy = "corretora", cascade = CascadeType.ALL, orphanRemoval = true)
	// TODO implementar a busca por coleções: @FilterParameter(value = FilterType.EQUALS, property =
	// {"listaBolsaCorretoraCorretor"})
	private List<BolsaCorretoraCorretor> listaBolsaCorretoraCorretor;

	@Column(name = "nome_responsavel", length = 100)
	@FilterParameter(FilterType.LIKE_EXACT)
	private String nomeResponsavel;

	// bi-directional many-to-one association to SituacaoCorretora
	@ManyToOne
	@JoinColumn(name = "id_situacao_corretora", nullable = false)
	private SituacaoCorretorCorretora situacaoCorretorCorretora;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Corretora)) {
			return false;
		}
		final Corretora other = (Corretora) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public String getCnpj() {
		return this.cnpj;
	}

	public Date getDataTerminoSuspensao() {
		return this.dataTerminoSuspensao;
	}

	public Long getId() {
		return this.id;
	}

	public List<BolsaCorretoraCorretor> getListaBolsaCorretoraCorretor() {
		return this.listaBolsaCorretoraCorretor;
	}

	public String getNomeResponsavel() {
		return this.nomeResponsavel;
	}

	public SituacaoCorretorCorretora getSituacaoCorretorCorretora() {
		return this.situacaoCorretorCorretora;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	public void setCnpj(final String cnpj) {
		this.cnpj = cnpj;
	}

	public void setDataTerminoSuspensao(final Date dataTerminoSuspensao) {
		this.dataTerminoSuspensao = dataTerminoSuspensao;
	}

	public void setId(final Long idCorretora) {
		this.id = idCorretora;
	}

	public void setListaBolsaCorretoraCorretor(final List<BolsaCorretoraCorretor> listaBolsaCorretoraCorretor) {
		this.listaBolsaCorretoraCorretor = listaBolsaCorretoraCorretor;
	}

	public void setNomeResponsavel(final String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public void setSituacaoCorretorCorretora(final SituacaoCorretorCorretora situacaoCorretorCorretora) {
		this.situacaoCorretorCorretora = situacaoCorretorCorretora;
	}

}
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_corretora", unique = true, nullable = false)
	@FilterParameter(FilterType.EQUALS)
	private Long id;

	@Column(nullable = false, unique = true, length = 14)
	@FilterParameter(FilterType.EQUALS)
	private String cnpj;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_termino_suspensao")
	private Date dataTerminoSuspensao;

	// bi-directional many-to-one association to SituacaoCorretora
	@ManyToOne
	@JoinColumn(name = "id_situacao_corretora", nullable = false)
	private SituacaoCorretorCorretora situacaoCorretorCorretora;

	@Column(name = "nome_responsavel", length = 100)
	private String nomeResponsavel;

	// bi-directional many-to-one association to BolsaCorretoraCorretor
	@OneToMany(mappedBy = "corretora", cascade = CascadeType.ALL, orphanRemoval = true)
	// TODO implementar a busca por coleções: @FilterParameter(value = FilterType.EQUALS, property =
	// {"listaBolsaCorretoraCorretor"})
	private List<BolsaCorretoraCorretor> listaBolsaCorretoraCorretor;

	public Long getId() {
		return this.id;
	}

	public void setId(Long idCorretora) {
		this.id = idCorretora;
	}

	public String getCnpj() {
		return this.cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
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

	public String getNomeResponsavel() {
		return this.nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public List<BolsaCorretoraCorretor> getListaBolsaCorretoraCorretor() {
		return this.listaBolsaCorretoraCorretor;
	}

	public void setListaBolsaCorretoraCorretor(List<BolsaCorretoraCorretor> listaBolsaCorretoraCorretor) {
		this.listaBolsaCorretoraCorretor = listaBolsaCorretoraCorretor;
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
		if (!(obj instanceof Corretora)) {
			return false;
		}
		Corretora other = (Corretora) obj;
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
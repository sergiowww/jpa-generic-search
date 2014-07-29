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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;

/**
 * The persistent class for the tb_edital database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_EDITAL)
@Access(AccessType.FIELD)
public class Edital implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@FilterParameter(FilterType.EQUALS)
	@Column(unique = true, nullable = false, name = "id_edital")
	private Long id;

	@Column(nullable = false, length = 4, name = "ano")
	@FilterParameter(FilterType.EQUALS)
	private Integer ano;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false, name = "data_leilao")
	private Date dataLeilao;

	@Column(name = "numero_reserva")
	@FilterParameter(FilterType.EQUALS)
	private Long numeroReserva;

	@FilterParameter(FilterType.EQUALS)
	@Column(nullable = false, name = "numero")
	private Integer numero;

	public Long getId() {
		return this.id;
	}

	public void setId(Long idEdital) {
		this.id = idEdital;
	}

	public Date getDataLeilao() {
		return this.dataLeilao;
	}

	public void setDataLeilao(Date dataLeilao) {
		this.dataLeilao = dataLeilao;
	}

	// CHECKSTYLE:ON

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Edital)) {
			return false;
		}
		Edital other = (Edital) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Long getNumeroReserva() {
		return numeroReserva;
	}

	public void setNumeroReserva(Long numeroReserva) {
		this.numeroReserva = numeroReserva;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
}

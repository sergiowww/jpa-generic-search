package net.wicstech.genericsearch.entidadestest;

import static javax.persistence.AccessType.PROPERTY;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the dip_disponibilidade_prof database table.
 * 
 */
@Embeddable
@Access(PROPERTY)
public class DisponibilidadePK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private Profissional profissional;
	private Date inicio;
	private Integer diaSemana;

	@Temporal(TemporalType.TIME)
	@Column(name = "DIP_HR_INICIO", unique = true, nullable = false)
	public Date getInicio() {
		return this.inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	// bi-directional many-to-one association to ProProfissional
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "PRO_ID", nullable = false, insertable = false, updatable = false)
	public Profissional getProfissional() {
		return this.profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	@Column(name = "DIP_NU_DIA_SEMANA", unique = true, nullable = false)
	public Integer getDiaSemana() {
		return this.diaSemana;
	}

	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diaSemana == null) ? 0 : diaSemana.hashCode());
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
		result = prime * result + ((profissional == null) ? 0 : profissional.hashCode());
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
		if (!(obj instanceof DisponibilidadePK)) {
			return false;
		}
		DisponibilidadePK other = (DisponibilidadePK) obj;
		if (diaSemana == null) {
			if (other.diaSemana != null) {
				return false;
			}
		} else if (!diaSemana.equals(other.diaSemana)) {
			return false;
		}
		if (inicio == null) {
			if (other.inicio != null) {
				return false;
			}
		} else if (!inicio.equals(other.inicio)) {
			return false;
		}
		if (profissional == null) {
			if (other.profissional != null) {
				return false;
			}
		} else if (!profissional.equals(other.profissional)) {
			return false;
		}
		return true;
	}

}
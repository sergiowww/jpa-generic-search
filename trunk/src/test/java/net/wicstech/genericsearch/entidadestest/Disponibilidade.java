package net.wicstech.genericsearch.entidadestest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the dip_disponibilidade_prof database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_DISPONIBILIDADE)
@Access(AccessType.PROPERTY)
public class Disponibilidade implements Serializable {
	private static final long serialVersionUID = 1L;
	private DisponibilidadePK id;
	private Date fim;

	/**
	 * Indica se esta disponibilidade está contida na lista.
	 */
	private boolean inserida = true;

	/**
	 * Construtor com data início e fim.
	 * 
	 * @param inicio
	 * @param fim
	 */
	public Disponibilidade(Date inicio, Date fim, Integer diaSemama) {
		super();
		setInicio(inicio);
		setFim(fim);
		setDiaSemana(diaSemama);
	}

	/**
	 * Construtor padrão.
	 */
	public Disponibilidade() {
		super();
	}

	@EmbeddedId
	public DisponibilidadePK getId() {
		return this.id;
	}

	public void setId(DisponibilidadePK id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "DIP_HR_FIM", unique = true, nullable = false)
	public Date getFim() {
		return this.fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	/**
	 * @return the profissional
	 */
	@Transient
	public Profissional getProfissional() {
		if (id != null) {
			return id.getProfissional();
		}
		return null;
	}

	/**
	 * @param profissional
	 *            the profissional to set
	 */
	public void setProfissional(Profissional profissional) {
		if (id == null) {
			id = new DisponibilidadePK();
		}
		id.setProfissional(profissional);
	}

	/**
	 * @return the inicio
	 */
	@Transient
	public Date getInicio() {
		if (id != null) {
			return id.getInicio();
		}
		return null;
	}

	/**
	 * @param inicio
	 *            the inicio to set
	 */
	public void setInicio(Date inicio) {
		if (id == null) {
			id = new DisponibilidadePK();
		}
		id.setInicio(inicio);
	}

	/**
	 * @return the diaSemana
	 */
	@Transient
	public Integer getDiaSemana() {
		if (id != null) {
			return id.getDiaSemana();
		}
		return null;
	}

	/**
	 * @param diaSemana
	 *            the diaSemana to set
	 */
	public void setDiaSemana(Integer diaSemana) {
		if (id == null) {
			id = new DisponibilidadePK();
		}
		this.id.setDiaSemana(diaSemana);
	}

	/**
	 * @return the inserida
	 */
	@Transient
	public boolean isInserida() {
		return inserida;
	}

	/**
	 * @param inserida
	 *            the inserida to set
	 */
	public void setInserida(boolean inserida) {
		this.inserida = inserida;
	}

}
package net.wicstech.genericsearch.entidadestest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * The persistent class for the dis_disciplina database table.
 * 
 */
@Entity
@Table(name = Tabelas.TABELA_DISCIPLINA)
@Access(AccessType.PROPERTY)
public class Disciplina implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String descricao;
	private List<Profissional> profissionais;

	public Disciplina() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DIS_ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "DIS_NM", nullable = false, length = 30)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String nome) {
		this.descricao = nome;
	}

	/**
	 * Adicionar um profissional.
	 * 
	 * @param profissional
	 */
	void addProfissional(Profissional profissional) {
		if (profissionais == null) {
			profissionais = new ArrayList<Profissional>();
		}
		profissionais.add(profissional);
	}

	/**
	 * @return the profissionais
	 */
	@ManyToMany(mappedBy = "disciplinas")
	public List<Profissional> getProfissionais() {
		return profissionais;
	}

	/**
	 * @param profissionais
	 *            the profissionais to set
	 */
	public void setProfissionais(List<Profissional> profissionais) {
		this.profissionais = profissionais;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return descricao;
	}

}
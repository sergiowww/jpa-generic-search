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
import javax.persistence.Transient;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;

@Entity
@Table(name = Tabelas.TABELA_ENTIDADE_TIPO)
@Access(AccessType.FIELD)
public class EntidadeTipo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_entidade", unique = true, nullable = false)
	private Long id;

	@Column(length = 100)
	private String nome;

	@Transient
	@FilterParameter(value = FilterType.NOT_EQUALS, entityProperty = "nome")
	private String nomeExclude;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeExclude() {
		return nomeExclude;
	}

	public void setNomeExclude(String nomeExclude) {
		this.nomeExclude = nomeExclude;
	}
}

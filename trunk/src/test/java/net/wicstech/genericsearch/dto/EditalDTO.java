package net.wicstech.genericsearch.dto;

import java.io.Serializable;
import java.util.Date;

import net.wicstech.genericsearch.FilterParameter;
import net.wicstech.genericsearch.FilterType;
import net.wicstech.genericsearch.entidadestest.Edital;

/**
 * Entidade de teste.
 * 
 * @author sergio.oliveira
 *
 */
public class EditalDTO implements Serializable {
	private static final long serialVersionUID = 5833280243718768405L;

	@FilterParameter(value = FilterType.GREATER_THAN_OR_EQUAL, entityProperty = "dataLeilao")
	private Date dataInicio;

	@FilterParameter(value = FilterType.LESS_THAN_OR_EQUAL, entityProperty = "dataLeilao")
	private Date dataFim;

	@FilterParameter(FilterType.SCAN_FILTERS_INSIDE_THIS)
	private Edital edital;

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim
	 *            the dataFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Edital getEdital() {
		return edital;
	}

	public void setEdital(Edital edital) {
		this.edital = edital;
	}

}

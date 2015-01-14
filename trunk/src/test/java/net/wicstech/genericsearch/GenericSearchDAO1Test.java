package net.wicstech.genericsearch;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.wicstech.genericsearch.dto.EditalDTO;
import net.wicstech.genericsearch.entidadestest.Edital;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

public class GenericSearchDAO1Test extends AbstractJUnitTest {

	@Before
	public void setUp() throws Exception {
		limparTabelas();
		final Edital edital1 = new Edital();
		edital1.setAno(2010);
		edital1.setDataLeilao(getDate("05/10/2005"));
		edital1.setNumero(2);
		edital1.setNumeroReserva(20l);

		final Edital edital2 = new Edital();
		edital2.setAno(2011);
		edital2.setDataLeilao(getDate("07/10/2005"));
		edital2.setNumero(4);
		edital2.setNumeroReserva(40l);
		this.dao.salvar(edital1, edital2);

	}

	@Test
	@Rollback(true)
	public void testScanProperties() {
		EditalDTO dto = new EditalDTO();
		Edital editalPesquisa = new Edital();
		editalPesquisa.setAno(2010);
		dto.setEdital(editalPesquisa);
		assertEquals(1, genericSearchDAO.size(Edital.class, dto));
		List<Edital> resultado = genericSearchDAO.list(Edital.class, dto);
		assertEquals(1, resultado.size());
		Edital edital = resultado.get(0);
		assertEquals(editalPesquisa.getAno(), edital.getAno());
	}

	@Test
	@Rollback(true)
	public void testScanProperties2() {
		EditalDTO dto = new EditalDTO();
		dto.setDataInicio(getDate("07/10/2005"));
		dto.setDataFim(getDate("10/10/2005"));
		Edital editalPesquisa = new Edital();
		editalPesquisa.setAno(2010);
		dto.setEdital(editalPesquisa);
		assertEquals(0, genericSearchDAO.size(Edital.class, dto));
		List<Edital> resultado = genericSearchDAO.list(Edital.class, dto);
		assertEquals(0, resultado.size());
	}

	/**
	 * Intervalo que não exite na base.
	 * 
	 */
	@Test
	@Rollback(true)
	public void testPesquisaIntervalo3() {
		EditalDTO dto = new EditalDTO();
		dto.setDataInicio(getDate("01/10/2005"));
		dto.setDataFim(getDate("03/10/2005"));
		List<Edital> resultado = this.genericSearchDAO.list(Edital.class, dto);
		assertEquals(0, this.genericSearchDAO.size(Edital.class, dto));
		assertEquals(0, resultado.size());
	}

	@Test
	@Rollback(true)
	public void testPesquisaIntervalo2() {
		EditalDTO dto = new EditalDTO();
		dto.setDataInicio(getDate("04/10/2005"));
		List<Edital> resultado = this.genericSearchDAO.list(Edital.class, dto);
		assertEquals(2, this.genericSearchDAO.size(Edital.class, dto));
		assertEquals(2, resultado.size());
	}

	@Test
	@Rollback(true)
	public void testPesquisaIntervalo1() {
		EditalDTO dto = new EditalDTO();
		dto.setDataInicio(getDate("06/10/2005"));
		dto.setDataFim(getDate("08/10/2005"));

		List<Edital> resultado = this.genericSearchDAO.list(Edital.class, dto);
		assertEquals(1, this.genericSearchDAO.size(Edital.class, dto));
		assertEquals(1, resultado.size());
		final Edital editalConferir = resultado.get(0);
		assertEquals(getDate("07/10/2005"), editalConferir.getDataLeilao());
		assertEquals(Integer.valueOf(4), editalConferir.getNumero());
	}

}

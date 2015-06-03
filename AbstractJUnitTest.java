package net.wicstech.genericsearch;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.wicstech.genericsearch.entidadestest.Tabelas;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration("/GenericSearchDAOTest.xml")
@Ignore
public class AbstractJUnitTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final String FORMATO_DD_MM_YYYY = "dd/MM/yyyy";
	private static final String FORMATO_HH_MM = "HH:mm";
	@Autowired
	protected GenericSearchDAO genericSearchDAO;
	@Autowired
	protected TestDbDAO dao;

	protected void limparTabelas() throws Exception {
		final Field[] fields = Tabelas.class.getDeclaredFields();

		for (final Field field : fields) {
			final Object campo = field.get(null);

			deleteFromTables(ObjectUtils.toString(campo));
		}
	}

	protected Date getTime(final String dateString) {
		return parseDate(dateString, FORMATO_HH_MM);
	}

	protected Date getDate(final String dateString) {
		return parseDate(dateString, FORMATO_DD_MM_YYYY);
	}

	private Date parseDate(final String dateString, final String format) {
		final SimpleDateFormat sdf = new SimpleDateFormat(format);

		try {
			return sdf.parse(dateString);
		} catch (final ParseException e) {
			return null;
		}
	}

}

package org.letter.metrics.collector.jmx;



import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SafeNameTest {
	@Test
	public void testSafeName() {
		List<Object[]> kv = Arrays.asList(new Object[][] {
				{ "test_test", "test-test" }, { "test_test", "test-_test" }, { "test_test", "test-_-test" },
				{ "_", "-_-"}, { "", "" }, { null, null }, { "_", "---" },
				{ "test", "test" },
				{ "_001", "001" },
				// A very long string
				{ "_asetstjlk_testkljsek_tesktjsekrslk_testkljsetkl_tkesjtk_sljtslkjetesslelse_lktsjetlkesltel_kesjltelksjetkl_tesktjksjltse_sljteslselkselse_tsjetlksetklsjekl_slkfjrtlskek_",
						"$asetstjlk_$testkljsek_$tesktjsekrslk_$testkljsetkl_$tkesjtk_$sljtslkjetesslelse_$lktsjetlkesltel_$kesjltelksjetkl_$tesktjksjltse_$sljteslselkselse_$tsjetlksetklsjekl_$slkfjrtlskek___" },
				{ "test_swedish_chars_", "test_swedish_chars_åäö" },
				{ "test_test", "test@test" },
				{ "test_test", "test;test" },
				{ "test:test", "test:test" },
		});
		for (Object[] item:kv){
			String safeName = JmxCollector.safeName((String) item[0]);
			//assertEquals(item[1], safeName);
		}

	}
}

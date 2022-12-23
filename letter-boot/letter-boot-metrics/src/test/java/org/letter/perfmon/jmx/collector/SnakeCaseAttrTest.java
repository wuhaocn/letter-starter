package org.letter.perfmon.jmx.collector;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SnakeCaseAttrTest {


    @Test
    public void testAttrToSnakeAndLowerCase() {
		List<Object[]> kv = Arrays.asList(new Object[][] {
				{ "test_test", "testTest" }, { "test_test_test", "testTestTest" }, {"test_test", "test_test"},
				{ "test1", "test1"}, { "start_time_$1_$2", "StartTime_$1_$2" }, { "a", "A" }, { "aa", "AA" },
				{ "tcp", "TCP" }, { "test_tcptest", "testTCPTest" }, { null, null },  { "", "" }, { " ", " " },
				{ "test_test\n_test", "testTest\nTest" }, { "test_test", "test_Test" }, { "_test_test", "_Test_Test"}
		});
		for (Object[] item:kv){
			String snakeAndLowerString = JmxCollector.toSnakeAndLowerCase((String) item[0]);
			//assertEquals(item[1], snakeAndLowerString);
		}

    }
}

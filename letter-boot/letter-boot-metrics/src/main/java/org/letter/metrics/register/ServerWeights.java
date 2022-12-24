package org.letter.metrics.register;

/**
 * @author wuhao
 * @description: ServerWeights
 * @createTime 2022/11/15 17:05:00
 */

public class ServerWeights {

	private int passing = 10;

	private int warning = 1;

	public int getPassing() {
		return passing;
	}

	public void setPassing(int passing) {
		this.passing = passing;
	}

	public int getWarning() {
		return warning;
	}

	public void setWarning(int warning) {
		this.warning = warning;
	}
}

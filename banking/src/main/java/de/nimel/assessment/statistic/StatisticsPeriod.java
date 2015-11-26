package de.nimel.assessment.statistic;

public enum StatisticsPeriod {

	DAY(86400000L), QUARTER(2629743830L), YEAR(31556926000L);

	private final long milliseconds;

	private StatisticsPeriod(final long milliseconds) {
		this.milliseconds = milliseconds;
	}

	public long getMilliseconds() {
		return milliseconds;
	}
}

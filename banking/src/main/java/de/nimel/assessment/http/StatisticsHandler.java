package de.nimel.assessment.http;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.nimel.assessment.statistic.Statistics;
import de.nimel.assessment.statistic.StatisticsPeriod;
import de.nimel.assessment.statistic.filter.DummyFilter;
import de.nimel.assessment.statistic.filter.Filter;
import de.nimel.assessment.statistic.filter.PeriodFilter;
import de.nimel.assessment.statistic.filter.StateFilter;
import de.nimel.assessment.transaction.TransactionState;

@Service
@Path("stats")
@Produces("text/plain")
public final class StatisticsHandler {

	private static final Filter PENDING_STATE_FILTER = new StateFilter(
			TransactionState.PENDING);

	private static final Filter DONE_STATE_FILTER = new StateFilter(
			TransactionState.DONE);

	private static final Filter CANCELLED_STATE_FILTER = new StateFilter(
			TransactionState.CANCELLED);

	private static final Filter QUARTERLY_FILTER = new PeriodFilter(
			StatisticsPeriod.QUARTER);

	private static final Filter DAILY_FILTER = new PeriodFilter(
			StatisticsPeriod.DAY);

	private static final Filter YEARLY_FILTER = new PeriodFilter(
			StatisticsPeriod.YEAR);

	private static final Filter DUMMY_FILTER = new DummyFilter();

	private Statistics stats;

	@Autowired
	public void setStatistics(final Statistics stats) {
		this.stats = stats;
	}

	@GET
	@Path("pendingpercentage")
	public String getTransactionPercentageInPendingState() {
		return Integer.toString(stats
				.getTransactionPercentage(PENDING_STATE_FILTER));
	}

	@GET
	@Path("donepercentage")
	public String getTransactionPercentageInDoneState() {
		return Integer.toString(stats
				.getTransactionPercentage(DONE_STATE_FILTER));
	}

	@GET
	@Path("cancelledpercentage")
	public String getTransactionPercentageInCancelledState() {
		return Integer.toString(stats
				.getTransactionPercentage(CANCELLED_STATE_FILTER));
	}

	@GET
	@Path("quarterlycount")
	public String getTransactionCountForQuarter() {
		return Integer.toString(stats.getTransactionCount(QUARTERLY_FILTER));
	}

	@GET
	@Path("yearlycount")
	public String getTransactionCountForYear() {
		return Integer.toString(stats.getTransactionCount(YEARLY_FILTER));
	}

	@GET
	@Path("dailycount")
	public String getTransactionCountForDay() {
		return Integer.toString(stats.getTransactionCount(DAILY_FILTER));
	}

	@GET
	@Path("totalcount")
	public String getTotalTransactionCount() {
		return Integer.toString(stats.getTransactionCount(DUMMY_FILTER));
	}
}

package net.tarilabs.joind_ex042015;

import java.util.Map;

public class TalkRecordStats extends TalkRecord {
	private long anonCnt = 0;
	private long namedCnt = 0;
	private double anonAvg = 0d;
	private double namedAvg = 0d;
	private double totalAvg = 0d;
	
	public TalkRecordStats(TalkRecord origin, Map<Boolean, Long> anonOrNot, Map<Boolean, Double> anonPartAvg, Double totalAvg) {
		super(origin.getId(), origin.getTalk_title(), origin.getH_avgerage_rating(), origin.getH_comment_count(), origin.getH_starred_count());
		anonCnt = anonOrNot.get(true);
		namedCnt = anonOrNot.get(false);
		anonAvg = anonPartAvg.get(true);
		namedAvg = anonPartAvg.get(false);
		this.totalAvg = totalAvg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TalkRecordStats [anonCnt=");
		builder.append(anonCnt);
		builder.append(", namedCnt=");
		builder.append(namedCnt);
		builder.append(", anonAvg=");
		builder.append(anonAvg);
		builder.append(", namedAvg=");
		builder.append(namedAvg);
		builder.append(", totalAvg=");
		builder.append(totalAvg);
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getH_avgerage_rating()=");
		builder.append(getH_avgerage_rating());
		builder.append(", getH_comment_count()=");
		builder.append(getH_comment_count());
		builder.append(", getH_starred_count()=");
		builder.append(getH_starred_count());
		builder.append("]");
		return builder.toString();
	}
	
	public String toCvsLine() {
		StringBuilder builder = new StringBuilder();
		builder.append(getId());
		builder.append(",");
		builder.append(getH_avgerage_rating());
		builder.append(",");
		builder.append(getH_comment_count());
		builder.append(",");
		builder.append(getH_starred_count());
		builder.append(",");
		builder.append(anonCnt);
		builder.append(",");
		builder.append(namedCnt);
		builder.append(",");
		builder.append(anonAvg);
		builder.append(",");
		builder.append(namedAvg);
		builder.append(",");
		builder.append(totalAvg);
		return builder.toString();
	}
}

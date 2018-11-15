package kr.ac.jbnu.se.tetris;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingList implements Serializable {
	private static final long serialVersionUID = 2L;

	public static final String FILENAME = "ranking.dat";

	private ArrayList<RankingScore> rank = new ArrayList<>();

	public List<RankingScore> getList(int length) {
		length = Math.min(length, this.rank.size());
		return this.rank.subList(0, length);
	}

	public void update() {
		Collections.sort(this.rank, new RankingComparator());
	}

	public void insert(String name, long score) {
		this.rank.add(new RankingScore(name, score));
		update();
	}
}

class RankingComparator implements Comparator<RankingScore> {
	@Override
	public int compare(RankingScore a, RankingScore b) {
		return b.compareTo(a);
	}
}
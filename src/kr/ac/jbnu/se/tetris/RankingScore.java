package kr.ac.jbnu.se.tetris;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class RankingScore implements Serializable {
	private String name;

	private long score;

	private long time;

	public RankingScore() {
	}

	RankingScore(String name, long score2) {
		this.name = name;
		this.score = score2;
		this.time = new Date().getTime();
	}

	public String getName() {
		return this.name;
	}

	public long getScore() {
		return this.score;
	}

	public Date getDate() {
		return new Date(this.time);
	}

	public int compareTo(RankingScore other) {
		if (this.score == other.score) {
			if (this.time == other.time) {
				return this.name.compareTo(other.name);
			}
			return this.time < other.time ? 1 : -1;
		}
		return this.score > other.score ? 1 : -1;
	}

	/**
	 * Serialize this instance.
	 * 
	 * @param out
	 *            Target to which this instance is written.
	 * @throws IOException
	 *             Thrown if exception occurs during serialization.
	 */
	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.writeUTF(this.name);
		out.writeLong(this.score);
		out.writeLong(this.time);
	}

	/**
	 * Deserialize this instance from input stream.
	 * 
	 * @param in
	 *            Input Stream from which this instance is to be deserialized.
	 * @throws IOException
	 *             Thrown if error occurs in deserialization.
	 * @throws ClassNotFoundException
	 *             Thrown if expected class is not found.
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.name = in.readUTF();
		this.score = in.readLong();
		this.time = in.readLong();
	}
}

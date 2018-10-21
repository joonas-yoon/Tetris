package kr.ac.jbnu.se.tetris;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class KeyBind implements Serializable {

	private int keyCode;

	public KeyBind(int newKeyCode) {
		set(newKeyCode);
	}

	public void set(int newKeyCode) {
		this.keyCode = newKeyCode;
	}

	public int getCode() {
		return this.keyCode;
	}

	public String getText() {
		return KeyEvent.getKeyText(this.keyCode);
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
		out.writeInt(keyCode);
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
		this.keyCode = in.readInt();
	}

	private void readObjectNoData() throws ObjectStreamException {
		throw new InvalidObjectException("Stream data required");
	}
}

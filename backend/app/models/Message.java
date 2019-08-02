package models;

/**
 * Formatador de mensagens de retorno para a interface
 * @param <T>
 */
public class Message<T> {

	private String message;

	private T data;

	public Message(String text) {
		this.message = text;
	}

	public Message(String text, T data) {
		this(text);
		this.withData(data);
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Message withData(T data) {
		this.data = data;
		return this;
	}

}
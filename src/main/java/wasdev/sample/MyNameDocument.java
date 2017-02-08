package wasdev.sample;

public class MyNameDocument {

	private String userName = null;

	public MyNameDocument(String userName) {
		this.setUserName(userName);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
package quiet.util;

public class Person {

	private String id;
	private String name;
	private String sex;
	private String wh;
	private String pid;
	

	public Person(String id, String name, String sex, String wh, String pid) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.wh = wh;
		this.pid = pid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWh() {
		return wh;
	}

	public void setWh(String wh) {
		this.wh = wh;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
}

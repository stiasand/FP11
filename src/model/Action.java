package model;

public interface Action {
	public void performClientAction();
	public void performServerAction();
	public String toXML();
	public Action fromXML(String xml);
}

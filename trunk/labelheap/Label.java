package labelheap;

public class Label {

	private String label;

	public Label() {
		label = null;
	}

	public String getLabel() {
		return label;
	}

	public Label setLabel(String label) {
		this.label = label;
		return this;
	}
	
	public void print()
	{
		System.out.println( label );
	}

}

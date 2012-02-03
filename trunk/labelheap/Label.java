package labelheap;

public class Label {

	private String label;

	public Label() {
		label = null;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * @param label the new label to be set in the Label object
	 * @return the Label object
	 */
	public Label setLabel(String label) {
		this.label = label;
		return this;
	}
	
	public void print()
	{
		System.out.println( label );
	}

	/**
	 * @return the length of the label as an int
	 */
	public int getLength() {
		return label.length();
	}

}

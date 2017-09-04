package com.cogito.erm.compliance.compliancecheck.model;

public class ExcelCellData {

	private int rowNum = -1;
	private int colNum = -1;
	private Object value = null;
	
	private String formula = null;
	boolean isSpecialCell = false;
	boolean setStyle = false;

	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public boolean isSpecialCell() {
		return isSpecialCell;
	}
	public void setSpecialCell(boolean isSpecialCell) {
		this.isSpecialCell = isSpecialCell;
	}
	public boolean isSetStyle() {
		return setStyle;
	}
	public void setSetStyle(boolean setStyle) {
		this.setStyle = setStyle;
	}
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getColNum() {
		return colNum;
	}
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	
}

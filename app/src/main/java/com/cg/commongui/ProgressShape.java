

package com.cg.commongui;
public interface ProgressShape {
	public enum ShapeType {
		CIRCLE, ARC, LINE, SEGMENT_CIRCLE;
	}
	public ShapeType setType(ShapeType type);
}

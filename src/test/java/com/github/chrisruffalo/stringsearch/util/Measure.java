package com.github.chrisruffalo.stringsearch.util;

import com.google.caliper.memory.ObjectGraphMeasurer;
import com.google.caliper.memory.ObjectGraphMeasurer.Footprint;
import com.google.common.collect.ImmutableMultiset;

public class Measure {
	
	private enum ClassSize {
		
		LONG(long.class, 8),
		INT(int.class, 4),
		BYTE(byte.class, 1),
		BOOLEAN(boolean.class, 1),
		CHAR(char.class, 2),
		SHORT(short.class, 2),
		FLOAT(float.class, 4),
		DOUBLE(double.class, 8);
		;
		
		private Class<?> type;
		
		private int bytes;
		
		private ClassSize(Class<?> type, int bytes) {
			this.type = type;
			this.bytes = bytes;
		}
		
		public int bytes() {
			return this.bytes;
		}
		
		public static ClassSize forType(Class<?> given) {
			for(ClassSize size : ClassSize.values()) {
				if(size.type.equals(given)) {
					return size;
				}
			}
			System.out.println("could not get size for: " + given);
			return null;
		}
		
	}
	
	private static final int REFERENCE_SIZE = 4;
	private static final int HEADER_SIZE = 16;

	public static long size(Object object) {
		
		Footprint footprint = ObjectGraphMeasurer.measure(object);
		
		long refsSize = Measure.REFERENCE_SIZE * footprint.getNonNullReferences();
		long nullRefSize = Measure.REFERENCE_SIZE * footprint.getNullReferences();
		long objectsSize = Measure.HEADER_SIZE * footprint.getObjects();
		long size = refsSize + objectsSize + nullRefSize;
		
		System.out.println("Object: " + object.getClass().getName());
		System.out.println("=====================================================");
		System.out.println("References (" + footprint.getNonNullReferences() + "): " + Measure.prettyBytes(refsSize));
		System.out.println("Null References (" + footprint.getNullReferences() + "): " + Measure.prettyBytes(nullRefSize));
		System.out.println("Objects (" + footprint.getObjects() + "): " + Measure.prettyBytes(objectsSize));
				
		ImmutableMultiset<Class<?>> primitives = footprint.getPrimitives();
		for(Class<?> primitive : primitives.elementSet()) {
			ClassSize classSize = ClassSize.forType(primitive);
			int count = primitives.count(primitive);
			int primSize = (classSize.bytes() * count);
			size += primSize;
			
			System.out.println("Primitives of type '" + primitive.getName() + "' (" + count + "): " + Measure.prettyBytes(primSize));
		}
		System.out.println("=====================================================");
		System.out.println("Total: " + Measure.prettyBytes(size));
		System.out.println("=====================================================\n");
		
		return size;
	}

	public static String prettyBytes(long bytes) {
		return Measure.prettyBytes(bytes, true);
	}
	
	public static String prettyBytes(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}

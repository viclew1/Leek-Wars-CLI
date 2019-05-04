package com.leek.wars.bot.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum BodyHelper {

	INSTANCE;

	public String generateBody(Object toWrite) {
		List<BodyElement> fieldsToWrite = getBodyElements(toWrite);
		Iterator<BodyElement> it = fieldsToWrite.iterator();
		StringBuilder body = new StringBuilder();
		while (it.hasNext()) {
			BodyElement element = it.next();
			String label = element.getLabel();
			String value = element.getValue();
			if (value == null || "".equals(value)) {
				continue;
			}
			body.append(label + "=" + value);
			if (it.hasNext()) {
				body.append("&");
			}
		}
		return body.toString();
	}

	private List<BodyElement> getBodyElements(Object toWrite) {
		Class<?> refClass = toWrite.getClass();
		List<Field> allFields = getAllDeclaredFields(refClass);
		List<BodyElement> elements = new ArrayList<>();
		for (Field f : allFields) {
			BodyMember bodyMember = f.getDeclaredAnnotation(BodyMember.class);
			if (bodyMember == null) {
				continue;
			}
			String elementName = getElementName(f, bodyMember);
			String elementValue = getElementValue(f, toWrite);
			elements.add(new BodyElement(elementName, elementValue));
		}
		return elements;
	}


	private String getElementName(Field field, BodyMember bodyMember) {
		if ("".equals(bodyMember.value())) {
			return field.getName();
		}
		return bodyMember.value();
	}

	private String getElementValue(Field field, Object refObj) {
		try {
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			Object val = field.get(refObj);
			field.setAccessible(accessible);
			if (val == null) {
				return "";
			}
			return val.toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return "";
		}
	}

	private List<Field> getAllDeclaredFields(Class<?> refClass) {
		List<Field> fields = new ArrayList<>();
		if (refClass.getSuperclass() != null) {
			fields.addAll(getAllDeclaredFields(refClass.getSuperclass()));
		}
		for (Field f : refClass.getDeclaredFields()) {
			fields.add(f);
		}
		return fields;
	}

	private class BodyElement {

		private final String label;
		private final String value;

		private BodyElement(String label, String value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public String getValue() {
			return value;
		}

	}

}

package com.cg.commonclass;

public class UpperCaseParse {

	public static void main(String[] arg) {

		System.out.println(UpperCaseParse.captionTextForUpperCaseString("UNO"));

	}

	public static String captionTextForUpperCaseString(String s) {

		String[] camelCaseWords = s.split("(?=[A-Z])");
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < camelCaseWords.length; i++) {

			buffer.append(camelCaseWords[i] + " ");
		}

		return buffer.toString().substring(0, buffer.length() - 1);
	}

}

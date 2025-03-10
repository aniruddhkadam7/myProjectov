package com.raybiztech.projectmanagement.invoice.utility;

public class NumberToWordUSCurrency {

	private static final String[] specialNames = { "", " Thousand", " Million",
			" Billion", " Trillion", " Quadrillion", " Quintillion" };

	private static final String[] tensNames = { "", " Ten", " Twenty",
			" Thirty", " Forty", " Fifty", " Sixty", " Seventy", " Eighty",
			" Ninety" };

	private static final String[] numNames = { "", " One", " Two", " Three",
			" Four", " Five", " Six", " Seven", " Eight", " Nine", " Ten",
			" Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen",
			" Sixteen", " Seventeen", " Eighteen", " Nineteen" };

	private String convertLessThanOneThousand(int number) {
		String current;

		if (number % 100 < 20) {
			current = numNames[number % 100];
			number /= 100;
		} else {
			current = numNames[number % 10];
			number /= 10;

			current = tensNames[number % 10] + current;
			number /= 10;
		}
		if (number == 0)
			return current;
		return numNames[number] + " Hundred" + current;
	}

	public String convert(Long number) {

		if (number == 0) {
			return "zero";
		}

		String prefix = "";

		if (number < 0) {
			number = -number;
			prefix = "negative";
		}

		String current = "";
		int place = 0;

		do {
			int n = (int) (number % 1000);
			if (n != 0) {
				String s = convertLessThanOneThousand(n);
				current = s + specialNames[place] + current;
			}
			place++;
			number /= 1000;
		} while (number > 0);

		return (prefix + current).trim();
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * NumberToWord obj = new NumberToWord();
	 * System.out.println("This is old us------> " +
	 * EnglishNumberstoWords.convert(100000000000000L));
	 * System.out.println("This is new US ------ " +
	 * obj.convert(1000000000000000L));
	 * System.out.println("This is old Indian ------>" + new
	 * NumberToWordConversion().convertNumberToWords(990000000));
	 * 
	 * }
	 */
}

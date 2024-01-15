package com.thinkbox.validation.identity;

public class SocialInsuranceNumber {
	public boolean isValid(String sin) throws Exception {
		if (sin.length() != 9) {
			return false;
		}
		int sumOfEvenDigits = 0;
		int sumOfOddDigits = 0;
		int lastNumericDigit = 0; // This is the last digit on the right side
		int sumofDigits = 0;

		// start from the right most, leave check digit
		int loc = 1;
		for (int pos = sin.length() - 1; pos >= 1; pos--) {
			if (loc % 2 == 0) {
				sumOfEvenDigits += Integer.parseInt(sin.substring(pos - 1, pos));
			} else {
				sumOfOddDigits += sum(Integer.parseInt(sin.substring(pos - 1, pos)) * 2);
			}
			loc++;
		}
		lastNumericDigit = Integer.parseInt(sin.substring(sin.length() - 1, sin.length()));
		sumofDigits = sumOfEvenDigits + sumOfOddDigits + lastNumericDigit;
		return (sumofDigits % 10 == 0) ? true: false;
	}

	private int sum(int num) {
		if (num < 10) {
			return num;			
		}
		if (num >= 10) {
			int sum = num / 10 + num % 10;
			if (sum < 10) {
				return sum;
			} else {
				return sum(sum);
			}
		}
		return -1;
	}
}

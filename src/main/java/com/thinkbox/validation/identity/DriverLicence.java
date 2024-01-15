package com.thinkbox.validation.identity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class DriverLicence {
	private static final String STRING_PROVINCE_ONTARIO = "ON";
	private static final String STRING_PROVINCE_QUEBEC = "QC";
	private static final String STRING_PROVINCE_NOVA_SCOTIA = "NS";
	private static final String STRING_PROVINCE_NEWFOUNDLAND_AND_LABRADOR = "NL";
	private static final String STRING_PROVINCE_NEWFOUNDLAND = "NF";
	private static final String STRING_RESULT_PASS = "PASS";
	private static final String STRING_RESULT_FAIL = "FAIL";
	private static final String STRING_RESULT_NA = "NA";

	private static HashMap<String, String> mapONCode;
	private static HashMap<String, String> mapQCCode;
	private static HashMap<String, String> mapSoundexCode;

	private static final Pattern namePattern = Pattern.compile("[a-zA-Z]+");

	private DriverLicence() {
	}

	static {
		mapONCode = new HashMap<>();
		/*
		 * A 0 B,C,D 1 E 2 F,G,H 3 I,J,K 4 L,M,N 5 O 6 P,Q,R 7 S,T 8 U,V,W,X,Y,Z 9
		 */
		mapONCode.put("A", "0");
		mapONCode.put("B", "1");
		mapONCode.put("C", "1");
		mapONCode.put("D", "1");
		mapONCode.put("E", "2");
		mapONCode.put("F", "3");
		mapONCode.put("G", "3");
		mapONCode.put("H", "3");
		mapONCode.put("I", "4");
		mapONCode.put("J", "4");
		mapONCode.put("K", "4");
		mapONCode.put("L", "5");
		mapONCode.put("M", "5");
		mapONCode.put("N", "5");
		mapONCode.put("O", "6");
		mapONCode.put("P", "7");
		mapONCode.put("Q", "7");
		mapONCode.put("R", "7");
		mapONCode.put("S", "8");
		mapONCode.put("T", "8");
		mapONCode.put("U", "9");
		mapONCode.put("V", "9");
		mapONCode.put("W", "9");
		mapONCode.put("X", "9");
		mapONCode.put("Y", "9");
		mapONCode.put("Z", "9");

		mapQCCode = new HashMap<>();
		/*
		 * A,B 1 C 2 D,E,E(with accent) F 3, G,H,I 4 J,K,L 5 M,N,O 6 P,Q,R 7 S,T 8
		 * U,V,W,X,Y,Z 9
		 */

		mapQCCode.put("A", "1");
		mapQCCode.put("B", "1");
		mapQCCode.put("C", "2");
		mapQCCode.put("D", "3");
		mapQCCode.put("E", "3");
		mapQCCode.put("F", "3");
		mapQCCode.put("G", "4");
		mapQCCode.put("H", "4");
		mapQCCode.put("I", "4");
		mapQCCode.put("J", "5");
		mapQCCode.put("K", "5");
		mapQCCode.put("L", "5");
		mapQCCode.put("M", "6");
		mapQCCode.put("N", "6");
		mapQCCode.put("O", "6");
		mapQCCode.put("P", "7");
		mapQCCode.put("Q", "7");
		mapQCCode.put("R", "7");
		mapQCCode.put("S", "8");
		mapQCCode.put("T", "8");
		mapQCCode.put("U", "9");
		mapQCCode.put("V", "9");
		mapQCCode.put("W", "9");
		mapQCCode.put("X", "9");
		mapQCCode.put("Y", "9");
		mapQCCode.put("Z", "9");

		mapSoundexCode = new HashMap<>();
		/*
		 * B,F,P,V 1 C,G,J,K,Q,S,X,Z 2 D,T 3 L 4 M,N 5 R 6 else ""
		 */
		mapSoundexCode.put("B", "1");
		mapSoundexCode.put("F", "1");
		mapSoundexCode.put("P", "1");
		mapSoundexCode.put("V", "1");

		mapSoundexCode.put("C", "2");
		mapSoundexCode.put("G", "2");
		mapSoundexCode.put("J", "2");
		mapSoundexCode.put("K", "2");
		mapSoundexCode.put("Q", "2");
		mapSoundexCode.put("S", "2");
		mapSoundexCode.put("X", "2");
		mapSoundexCode.put("Z", "2");

		mapSoundexCode.put("D", "3");
		mapSoundexCode.put("T", "3");

		mapSoundexCode.put("L", "4");

		mapSoundexCode.put("M", "5");
		mapSoundexCode.put("N", "5");

		mapSoundexCode.put("A", "_");
		mapSoundexCode.put("E", "_");
		mapSoundexCode.put("H", "_");
		mapSoundexCode.put("I", "_");
		mapSoundexCode.put("O", "_");
		mapSoundexCode.put("U", "_");
		mapSoundexCode.put("W", "_");
		mapSoundexCode.put("Y", "_");
	}

	private static String getNthLetter(String str, int index) {
		if (str == null || index < 1 || str.length() < index) {
			return null;
		}
		return str.substring(index - 1, index);
	}

	private static String getDOBForDDMMYYFormat(String dob) {
		// YYY-MM-DD to ddMMyy
		return dob.substring(8, 10) + dob.substring(5, 7) + dob.substring(2, 4);
	}

	private static String getDOBForYYMMDDFormat(String dob) {
		// YYYY-MM-DD to yyMMdd
		return dob.substring(2, 4) + dob.substring(5, 7) + dob.substring(8, 10);
	}

	private static boolean validateInputParameters(String provinceCode, String driverLicense, String firstName,
			String lastName, String dob) {
		Date dobDate = null;
		try {
			dobDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
		} catch (Exception e) {
			// can't parse dob string to date object
		}
		return provinceCode != null && provinceCode.trim().length() > 0 && driverLicense != null
				&& driverLicense.trim().length() > 0 && firstName != null && firstName.trim().length() > 0
				&& lastName != null && lastName.trim().length() > 0 && dobDate != null;
	}

	private static boolean validateCommonBasicRule(int driverLicenseLength, String driverLicense, String firstName,
			String lastName) {
		// DL first character should be a letter and equals to the lastname's first
		// character
		// DL should be driverLicenseLength characters long
		// DL following characters should be numeric only
		return driverLicense != null && driverLicense.trim().length() > 0 && Character.isLetter(driverLicense.charAt(0))
				&& driverLicense.charAt(0) == lastName.charAt(0) && driverLicense.length() == driverLicenseLength
				&& StringUtils.isNumeric(driverLicense.substring(1)) && isValidName(firstName) && isValidName(lastName);
	}

	private static String removeConsecutives(String characters) {
		String firstRound = removeConsecutiveSameValues(characters, 0);
		return removeConsecutiveSameValues(firstRound, 0);
	}

	private static String removeConsecutiveSameValues(String characters, int index) {
		if (index >= characters.length())
			return characters;

		char c = characters.charAt(index);
		String doubleCons = "" + c + c;

		if (characters.contains(doubleCons)) {
			return removeConsecutiveSameValues(characters.replaceAll(doubleCons, "" + c), index);
		}
		return removeConsecutiveSameValues(characters, index + 1);
	}

	private static boolean isValidName(String name) {
		Matcher matcher = namePattern.matcher(name);
		return matcher.matches();
	}

	private static String removeNonCharacters(String text) {
		String treaded = StringUtils.deleteWhitespace(text);
		treaded = StringUtils.remove(treaded, "-");
		treaded = StringUtils.remove(treaded, "'");
		treaded = StringUtils.remove(treaded, ".");
		treaded = StringUtils.remove(treaded, ",");

		return treaded;

	}

	public static boolean validateNewfoundlandDriverLicense(String driverLicense, String firstName, String lastName,
			String dob) {
		if (!validateCommonBasicRule(10, driverLicense, firstName, lastName)) {
			return false;
		}
		String dobFromDL = driverLicense.substring(1, 7);
		String dobFormat = getDOBForYYMMDDFormat(dob);

		return dobFormat.equals(dobFromDL);
	}

	public static boolean validateNovaSoctiaDriverLicense(String driverLicense, String firstName, String lastName,
			String dob) {
		// The last 9 digits are fixed length, so the DL should be at least contain 9
		// characters
		// DL last 9 characters should be numeric only
		if (driverLicense.length() < 9 || !StringUtils.isNumeric(StringUtils.reverse(driverLicense).substring(0, 9))
				|| !isValidName(firstName) || !isValidName(lastName))
			return false;

		String userNameFromDL = StringUtils.reverse(StringUtils.reverse(driverLicense).substring(9));
		if (!((lastName.length() >= 5 && userNameFromDL.trim().equals(lastName.substring(0, 5))
				|| lastName.length() < 5 && userNameFromDL.trim().equals(lastName)))) {
			return false;
		}

		String dobFromDL = StringUtils.reverse(StringUtils.reverse(driverLicense).substring(3, 9));
		String dobFormat = getDOBForDDMMYYFormat(dob);

		return dobFromDL.equals(dobFormat);
	}

	public static boolean validateQuebecDriverLicense(String driverLicense, String firstName, String lastName,
			String dob) {
		if (!validateCommonBasicRule(13, driverLicense, firstName, lastName)) {
			return false;
		}

		// validate first 5 characters
		String dlStr = driverLicense.substring(0, 5);

		String nameStr = StringUtils.EMPTY;
		String found;

		for (int i = 0; i < lastName.length(); i++) {
			found = mapSoundexCode.get(lastName.substring(i, i + 1));
			nameStr += (found == null ? StringUtils.EMPTY : found);
		}

		// remove any consecutive same value counts
		nameStr = removeConsecutives(nameStr);
		nameStr = nameStr.replaceAll("_", StringUtils.EMPTY);

		// handle the lastname's first letter
		String firstLetter = getNthLetter(lastName, 1);
		String firstLetterNumber = mapSoundexCode.get(firstLetter);

		if (StringUtils.isEmpty(nameStr)) {
			// If the name is made of vowels only like 'AA', nameStr will be empty
			nameStr = lastName.substring(0, 1);
		} else {
			if (firstLetterNumber.equals(nameStr.substring(0, 1))) {
				// Last name starts with one or many consecutive same values
				nameStr = firstLetter + nameStr.substring(1);
			} else {
				// last name doesn't start with consecutive same values (start with AEHIOUWY)
				nameStr = firstLetter + nameStr;
			}
		}

		// keep only the first four characters
		nameStr = (nameStr.length() > 4 ? nameStr.substring(0, 4) : nameStr);

		if (nameStr.length() < 4) {
			for (int j = nameStr.length(); j < 4; j++) {
				nameStr += "0";
			}
		}

		String st = mapQCCode.get(getNthLetter(firstName, 1));
		nameStr += (st == null ? StringUtils.EMPTY : st);
		if (!dlStr.equals(nameStr)) {
			return false;
		}

		// validate DOB
		String dobFromDL = driverLicense.substring(5, 11);
		String dobFormat = getDOBForDDMMYYFormat(dob);
		return dobFromDL.equals(dobFormat);
	}

	public static boolean validateOntarioDriverLicense(String driverLicense, String firstName, String lastName,
			String dob) {
		if (!validateCommonBasicRule(15, driverLicense, firstName, lastName)) {
			return false;
		}
		// first digit in DL equals to lastname's second letter's digit
		// if the second letter is a special character like in O'Larry, then take the
		// next valid character
		String codeVal = mapONCode.get(getNthLetter(lastName, 2));
		if (codeVal == null) {
			for (int index = 3; index <= lastName.length(); index++) {
				codeVal = mapONCode.get(getNthLetter(lastName, index));
				if (codeVal != null)
					break;
			}
		}

		// if still null, then set to ""
		if (codeVal == null)
			codeVal = StringUtils.EMPTY;

		if (!driverLicense.substring(1, 2).equals(codeVal))
			return false;

		// validate DOB
		String dobFromDL = driverLicense.substring(driverLicense.length() - 6);
		String regularMale = getDOBForYYMMDDFormat(dob);
		String dobFormatOnMaleTwin = regularMale.substring(0, 2) + (Integer.parseInt(regularMale.substring(2, 4)) + 12)
				+ regularMale.substring(4);
		String regularFemale = regularMale.substring(0, 2) + (Integer.parseInt(regularMale.substring(2, 4)) + 50)
				+ regularMale.substring(4);
		String dobFormatOnFemaleTwin = regularMale.substring(0, 2)
				+ (Integer.parseInt(regularMale.substring(2, 4)) + 62) + regularMale.substring(4);

		return (dobFromDL.equals(regularMale) || dobFromDL.equals(regularFemale)
				|| dobFromDL.equals(dobFormatOnMaleTwin) || dobFromDL.equals(dobFormatOnFemaleTwin));
	}

	public static String validateDriverLicense(String provinceCode, String driverLicense, String firstName,
			String lastName, String dob) {
		if (!validateInputParameters(provinceCode, driverLicense, firstName, lastName, dob)) {
			return STRING_RESULT_NA;
		}

		provinceCode = provinceCode.toUpperCase();
		driverLicense = StringUtils.stripAccents(driverLicense.toUpperCase());
		firstName = removeNonCharacters(StringUtils.stripAccents(firstName.toUpperCase()));
		lastName = removeNonCharacters(StringUtils.stripAccents(lastName.toUpperCase()));

		if (STRING_PROVINCE_ONTARIO.equals(provinceCode)) {
			return validateOntarioDriverLicense(driverLicense, firstName, lastName, dob) ? STRING_RESULT_PASS
					: STRING_RESULT_FAIL;
		} else if (STRING_PROVINCE_QUEBEC.equals(provinceCode)) {
			return validateQuebecDriverLicense(driverLicense, firstName, lastName, dob) ? STRING_RESULT_PASS
					: STRING_RESULT_FAIL;
		} else if (STRING_PROVINCE_NOVA_SCOTIA.equals(provinceCode)) {
			return validateNovaSoctiaDriverLicense(driverLicense, firstName, lastName, dob) ? STRING_RESULT_PASS
					: STRING_RESULT_FAIL;
		} else if (STRING_PROVINCE_NEWFOUNDLAND_AND_LABRADOR.equals(provinceCode)
				|| STRING_PROVINCE_NEWFOUNDLAND.equals(provinceCode)) {
			return validateNewfoundlandDriverLicense(driverLicense, firstName, lastName, dob) ? STRING_RESULT_PASS
					: STRING_RESULT_FAIL;
		} else {
			return STRING_RESULT_NA;
		}
	}
}

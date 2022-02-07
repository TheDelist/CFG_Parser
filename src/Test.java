import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class Test {

	public static boolean isTextValid(String textToCheck) {
		return !textToCheck.chars().filter(Character::isUpperCase).findAny().isEmpty();

	}

	public static ArrayList<Integer> UpperCaseIndex(String textToCheck) { 
		ArrayList<Integer> sum = new ArrayList<Integer>();
		char[] ar = textToCheck.toCharArray();
		for (int i = 0; i < ar.length; i++) {
			if (Character.isUpperCase(ar[i])) {
				sum.add(i);
			}
		}
		return sum;
	}

	public static ArrayList<Integer> LowerCaseIndex(String textToCheck) {
		ArrayList<Integer> sum = new ArrayList<Integer>();
		char[] ar = textToCheck.toCharArray();
		for (int i = 0; i < ar.length; i++) {
			if (Character.isLowerCase(ar[i])) {
				sum.add(i);
			}
		}
		return sum;
	}

	public static boolean checkTerminals(ArrayList<Grammer> rules, String str) {
		for (Grammer grammer : rules) {

			for (int i = 0; i < grammer.terminals.size(); i++) {
				if (grammer.terminals.get(i).value.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String derivationCheck(String derivationString, ArrayList<String> nonterminalWritten) {
		String newDerivation = "";

		char[] derCharArray = derivationString.toCharArray();
		for (int k = 0; k < derCharArray.length; k++) {
			for (int m = 0; m < nonterminalWritten.size(); m++) {
				if (nonterminalWritten.get(m).equals(String.valueOf(derCharArray[k]))) {
					derCharArray[k] = ' ';
				}
			}
		}

		for (int k = 0; k < derCharArray.length; k++) {
			if (derCharArray[k] != ' ') {
				newDerivation = newDerivation + String.valueOf(derCharArray[k]);
			}
		}
		return newDerivation;
	}

	public static void main(String[] args) {
		
		// Reading file
		try {
			File myObj = new File("CFG.txt");
			Scanner myReader = new Scanner(myObj);
			ArrayList<Grammer> rules = new ArrayList<Grammer>();
			ArrayList<Grammer> rulesSave = new ArrayList<Grammer>();
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] dataArray = data.split(" ");
				for (int i = 0; i < dataArray.length; i++) {
					String[] dataParts = dataArray[i].split(">");
					rules.add(new Grammer(dataParts[0]));
					rulesSave.add(new Grammer(dataParts[0]));
					if (dataParts[1].contains("|")) {
						String[] array = dataParts[1].split("\\|");
						for (int j = 0; j < array.length; j++) {
							rules.get(i).terminals.add(new Terminal(array[j]));
							rulesSave.get(i).terminals.add(new Terminal(array[j]));
						}
						Collections.sort(rules.get(i).terminals,
								(a, b) -> Integer.compare(a.value.length(), b.value.length()));
					}
				}
			}
			myReader.close();

			ArrayList<LinkedList<String>> paths = new ArrayList<LinkedList<String>>();

			for (int row = 0; row < rules.size(); row++) { // Get minumum paths

				LinkedList<String> q = new LinkedList<String>();
				int index = row;
				q.add(rules.get(row).nonTerminal);
				while (true) {

					for (int i = 0; i < rules.size(); i++) {

						if (rules.get(index).terminals.get(0).value.equals(rules.get(i).nonTerminal)) {
							q.add(rules.get(i).nonTerminal);
							rules.get(index).terminals.get(0).isTaken = true;
							index = i;

							break;
						} else if (i == rules.size() - 1) {
							q.add(rules.get(index).terminals.get(0).value);
							rules.get(index).terminals.get(0).isTaken = true;
						}

					}
					if (!q.getLast().equals(q.getLast().toUpperCase())) {
						paths.add(q);
						break;

					}
				}
			}
			// Deleting 
			for (int i = 0; i < rules.size(); i++) {
				for (int j = 0; j < rules.get(i).terminals.size(); j++) {
					if (rules.get(i).terminals.get(j).isTaken) {
						rules.get(i).terminals.remove(j);
					}
				}
			}

			for (int k = 0; k < rules.size(); k++) {

				LinkedList<String> q = new LinkedList<String>();
				q.add(rules.get(k).nonTerminal);

				int index = k;

				for (int i = 0; i < rules.size(); i++) { // Finding all possible paths

					String str3 = rules.get(i).terminals.get(0).value;

					if (rules.get(index).terminals.get(0).value.contains(rules.get(i).nonTerminal)) {
						q.add(rules.get(index).terminals.get(0).value);
						rules.get(index).terminals.get(0).isTaken = true;

						for (int j = 0; j < rules.get(i).terminals.get(0).value.length(); j++) {
							String str = rules.get(index).terminals.get(0).value.substring(j, j + 1); 
																										

							for (int l = 0; l < paths.size(); l++) { // Path checking
								if (str.equals(paths.get(l).getFirst())) {
									for (int m = 0; m < paths.get(l).size(); m++) {

										if (paths.get(l).get(m).length() > 1) {

											String head;
											String middle;
											String tail;
											ArrayList<Integer> upp = UpperCaseIndex(
													rules.get(index).terminals.get(0).value);
											ArrayList<Integer> low = LowerCaseIndex(
													rules.get(index).terminals.get(0).value);
											head = rules.get(index).terminals.get(0).value.substring(0, upp.get(0));

											if (!low.isEmpty()) {
												head = head.replace(rules.get(index).terminals.get(0).value
														.substring(low.get(0), upp.get(0)), "");
											}

											if (upp.size() == 1) {
												middle = rules.get(index).terminals.get(0).value.substring(upp.get(0),
														upp.get(0) + 1);
												tail = rules.get(index).terminals.get(0).value.substring(upp.get(0) + 1,
														rules.get(index).terminals.get(0).value.length());
											} else {
												middle = rules.get(index).terminals.get(0).value.substring(upp.get(0),
														upp.get(upp.size() - 1) + 1);
												tail = rules.get(index).terminals.get(0).value.substring(
														upp.get(upp.size() - 1) + 1,
														rules.get(index).terminals.get(0).value.length());
											}
											middle = paths.get(l).get(m);
											rules.get(index).terminals.get(0).value = head + middle + tail;

										} else {
											rules.get(index).terminals.get(
													0).value = rules.get(index).terminals.get(0).value.replaceFirst(
															rules.get(index).terminals.get(0).value.substring(j, j + 1),
															paths.get(l).get(m));
										}

										if (!q.contains(rules.get(index).terminals.get(0).value)) {
											q.add(rules.get(index).terminals.get(0).value);
										}

										if (isTextValid(q.getLast()) == false) {
											paths.add(q);

											q = new LinkedList<String>();

										}

									}
								}

							}

						}
						rules.get(i).terminals.get(0).value = str3;
						if (!q.isEmpty() && !paths.contains(q))
							paths.add(q);

					}
					if (q.isEmpty()) {
						break;
					}

					if (!q.isEmpty()) {
						if (!q.getLast().equals(q.getLast().toUpperCase())) {

							System.out.println();
							break;

						}
					}
				}

			}
			Scanner sc = new Scanner(System.in); // Take input from the user
			System.out.print("Please enter an input: ");
			String input = sc.nextLine();
			ArrayList<ArrayList<String>> derivationList = new ArrayList<ArrayList<String>>();

			System.out.print("\n---------Derivation---------");
			int inputLengthBackup = 0;
			boolean isBelongToCFG = false;
			while (true) {
				boolean flag = false;
				ArrayList<String> strList = new ArrayList<String>();
				for (int j = 0; j < input.length(); j++) {
					for (int k = j; k <= input.length(); k++) {
						strList.add(input.substring(j, k));
					}
				}
				if (inputLengthBackup == input.length()) {
					isBelongToCFG = true;
					break;
				}
				inputLengthBackup = input.length();

				strList.sort(Comparator.comparingInt(String::length).reversed());

				for (int j = 0; j < strList.size(); j++) { // Find the substring of input for back-tracking
					for (int j2 = 0; j2 < paths.size(); j2++) {
						for (int k = 0; k < paths.get(j2).size(); k++) {

							StringBuilder input1 = new StringBuilder();
							// append a string into StringBuilder input1
							input1.append(input);
							// reverse StringBuilder input1
							input1.reverse();

							if (paths.get(j2).get(k).equals(strList.get(j))) {

								String str2 = strList.get(j);
								String str = input;

								ArrayList<String> pathList = new ArrayList<String>();
								ArrayList<String> parseList = new ArrayList<String>();
								for (int m = k; m >= 0; m--) {
									pathList.add(paths.get(j2).get(m));

									str = str.replace(str2, paths.get(j2).get(m));
									str2 = paths.get(j2).get(m);

									parseList.add(str);
								}

								input = input.replace(strList.get(j), pathList.get(pathList.size() - 1));
								flag = true;
								derivationList.add(parseList);

								break;

							} else if (paths.get(j2).get(k).equals(input1.toString())) { // if the reverse of input
																							// equals
								String revPath = "";
								ArrayList<String> pathList = new ArrayList<String>();
								ArrayList<String> parseList = new ArrayList<String>();
								String str2 = strList.get(j);
								String str = input;
								for (int m = k; m >= 0; m--) {
									StringBuilder reversePath = new StringBuilder();

									reversePath.append(paths.get(j2).get(m));

									reversePath.reverse();

									pathList.add(reversePath.toString());

									str = str.replace(str2, reversePath.toString());
									str2 = reversePath.toString();

									revPath = reversePath.toString();
									parseList.add(str);
								}

								input = input.replace(strList.get(j), revPath);
								flag = true;
								derivationList.add(parseList);

								break;
							}
						}
						if (flag) {
							break;
						}
					}
					if (flag) {
						break;
					}
				}

				if (!checkTerminals(rulesSave, input) && input.length() == 1) {
					break;
				}

			} // end of while
			if (isBelongToCFG) {
				System.out.println("\nInput string can not be generated for this grammer!");
				System.exit(0);
			}
			System.out.println();
			String str = "";
			for (int i = derivationList.size() - 1; i >= 0; i--) {
				for (int l = derivationList.get(i).size() - 1; l >= 0; l--) {

					if (!derivationList.get(i).get(l).equals(str)) {
						System.out.println("=> " + derivationList.get(i).get(l));
					}
					str = derivationList.get(i).get(l);
				}
			}

			System.out.println("\n--------Parsing Tree--------"); // Printing of parsing tree according to nonterminals

			String lastStep = "";
			int spaceCount = 15;

			for (int i = derivationList.size() - 1; i >= 0; i--) {
				for (int l = derivationList.get(i).size() - 1; l >= 0; l--) {

					char[] derivationArray = derivationList.get(i).get(l).toCharArray();
					char[] lastStepArray = lastStep.toCharArray();
					for (int k2 = 0; k2 < spaceCount; k2++) {
						System.out.print(" ");
					}
					for (int k = 0; k < derivationArray.length; k++) {

						if (lastStep.length() < derivationArray.length) {

							System.out.print(derivationArray[k] + " ");

						} else if (lastStepArray[k] != derivationArray[k]) {

							System.out.println();

							for (int k2 = 0; k2 < spaceCount; k2++) {
								System.out.print(" ");
							}
							if (k == 2) {

								System.out.print("|");
							}
							for (int m = 0; m < k; m++) {
								System.out.print("  ");
							}

							System.out.print(derivationArray[k]);
							if (k == 1) {
								for (int m = 0; m < k; m++) {
									System.out.print("   ");
								}
								System.out.print("|");
							}

						}

					}
					System.out.println();
					lastStep = derivationList.get(i).get(l);

				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}

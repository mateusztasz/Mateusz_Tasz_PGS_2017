package tasz.mateusz.TextManipulation;

import java.util.Scanner;

/**
 * Class for text work.
 */
public class Text {
    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     * @return r Returns number from 0 to 1 - how good two strings are similar.
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }

        return (longerLength - levenshteinDistance(longer, shorter)) / (double) longerLength;
    }

    /**
     * Calculates difference between strings using LavenStein algorithm.
     *
     * @param s1 first string
     * @param s2 second string
     * @return How similar are whose s1 and s2
     */
    private static int levenshteinDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }


    public static boolean makeSure(String typed, String desired) {
        System.out.println("You typed a command >>" + typed + "<<");
        System.out.println("Did you mean? >>" + desired + "<<");
        System.out.println("Confirm >>[Y]/[y] <<");
        System.out.println("Decline >>[anything] <<");
        System.out.print("$>");
        Scanner scan = new Scanner(System.in); //obiekt do odebrania danych od użytkownika

        String command = scan.nextLine().trim().toLowerCase();

        return command.equals("y");

    }
}


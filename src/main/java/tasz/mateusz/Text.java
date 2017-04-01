package tasz.mateusz;

import java.util.Scanner;

/**
 * Created by Mateusz on 01.04.2017.
 */
public class Text {
    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
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



    public static int levenshteinDistance(String s1, String s2) {
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


    public static boolean makeSure(String typed, String desired){
        System.out.println("You typed a command >>"+typed+"<<");
        System.out.println("Did you mean? >>"+desired+"<<");
        System.out.println("Answer precisely >>[Y]/[N]<<");
        Scanner scan = new Scanner(System.in); //obiekt do odebrania danych od użytkownika

        String command = scan.nextLine().trim().toLowerCase();
        if(command.equals("y")) return true;

        return false;
    }
}

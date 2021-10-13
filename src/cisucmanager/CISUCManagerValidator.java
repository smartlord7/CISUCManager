package cisucmanager;

import java.util.Calendar;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sancho Amaral Simoes, 2019217590
 * Universidade de Coimbra, Licenciatura em Engenharia Informática
 * Programação Orientada a Objetos, 2º ano, 1º semestre, 2020/2021
 *
 * Class that holds some primitives to perform validation over the text input
 * files used in CISUCManager.
 */
public class CISUCManagerValidator {

    // region Constants
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@dei.uc.pt$",
            PHONE_NUMBER_REGEX = "^\\d{9}$",
            NAME_REGEX = "^[\\p{IsLatin}\\p{Zs}]+$";
    public static final int MIN_YEAR = 1900,
            MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final Function<Integer, Boolean> POSITIVE_NUMBER = e -> e > 0;
    public static final Function<Integer, Boolean> VALID_YEAR = e -> e > MIN_YEAR && e <= MAX_YEAR;
    // endregion Constants

    // region Constructors
    public CISUCManagerValidator() {
    }
    // endregion Constructors

    // region Public Methods
    /**
     * Function that determines if a string matches a certain regex.
     *
     * @param regex regex to be matched in the string.
     * @param toValidate the string to be validated.
     * @return true - if the string matches the given regex. false - otherwise
     */
    public boolean isInvalid(String regex, String toValidate) {
        Pattern pattern = Pattern.compile(toValidate);
        Matcher matcher = pattern.matcher(regex);
        return !matcher.find();
    }

    /**
     * Function that determines if a string is valid given a certain validation
     * function.
     *
     * @param validationFunction the validation function to be applied on the
     * string.
     * @param toValidate the string to be validated.
     * @return true - if the string is valid. false - otherwise
     */
    public boolean isInvalid(Function<Integer, Boolean> validationFunction, int toValidate) {
        return !validationFunction.apply(toValidate);
    }

    /**
     * Function that performs the ISBN algorithm over a string to check if it is
     * a valid ISBN.
     *
     * @param isbn the string to be validated.
     * @return true - if the string is a valid ISBN. false - otherwise
     */
    public boolean isInvalidISBN(String isbn) {
        if (isbn.length() == 10) {
            return true;
        }

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (digit * (i + 1));
        }
        return !(sum % 11 == 0);
    }
    // endregion Public Methods
}

package br.com.jacto.cloverindustryauth;

// Define uma classe ValidateException que herda de RuntimeException
public class ValidationException extends RuntimeException {

    // Construtor que recebe uma mensagem de erro e passa para o construtor da classe pai (RuntimeException)
    public ValidationException(String message) {
        super(message);
    }
}

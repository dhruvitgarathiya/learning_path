package 9_18_valuesource;

public class Test {
    private final Service service = new Service();

    @ParameterizedTest
    @ValueSource(strings = {
         "password123",
        "admin1234",
        "helloWorld",
        "JavaSpring"
    })
     void shouldAcceptValidPasswords(String password) {

        assertTrue(passwordService.isValid(password));
}
}
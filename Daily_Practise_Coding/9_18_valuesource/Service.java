@Service
public class PasswordService {

    public boolean isValid(String password) {
        return password != null
                && password.length() >= 8;
    }
}
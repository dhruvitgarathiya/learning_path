@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Dhruvit");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertEquals("Dhruvit", result.getName());
    }
}
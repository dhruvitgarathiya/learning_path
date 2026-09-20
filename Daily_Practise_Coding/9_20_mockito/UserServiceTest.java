@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


@Test
void shouldReturnUserWhenUserExists() {

    User user = new User();

    user.setId(1L);
    user.setName("Dhruvit");
    user.setEmail("dhruvit@gmail.com");

    when(userRepository.findById(1L))
            .thenReturn(Optional.of(user));

    User result = userService.getUser(1L);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Dhruvit", result.getName());
    assertEquals(
            "dhruvit@gmail.com",
            result.getEmail());

    verify(userRepository)
            .findById(1L);
}
}
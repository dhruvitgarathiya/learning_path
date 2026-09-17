package Daily_Practise_Coding.9_17_ParameterizeuSource;

@WebMvcTest(UserController.class)
public class TestController {
    
    @AutoWired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
        "1,200",
        "3,200"
        "-1,400"
    })
    void shouldReturnCorrectStatus(
        long id,
        int expectedStatus
    ) throws Exception{
        mockMvc.perFrom(
            get("/users/{id}",id)
        ).andExcept(
            status().is(exceptedStatus)
        );
    }
}

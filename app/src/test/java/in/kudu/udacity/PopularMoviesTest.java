package in.kudu.udacity;

import static org.mockito.Mockito.*;

/**
 * Created by gowrishg on 18/1/16.
 */
@RunWith(AndroidJUnit4.class)
@RunWith(MockitoJUnit4Runner.class)
public class PopularMovie {

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertThat(EmailValidator.isValidEmail("name@email.com"), is(true));

        // mock creation
        List mockedList = mock(List.class);

    }

}

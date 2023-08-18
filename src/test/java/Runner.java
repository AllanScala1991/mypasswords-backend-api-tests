import com.mypasswords.user.CreateNewUser;
import com.mypasswords.user.DeleteUser;
import com.mypasswords.user.UpdateUser;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   CreateNewUser.class,
   DeleteUser.class,
   UpdateUser.class
})
public class Runner {
}

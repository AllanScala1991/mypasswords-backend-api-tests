import com.mypasswords.login.Login;
import com.mypasswords.password.CreateNewPassword;
import com.mypasswords.password.GetAllPasswordsByUserId;
import com.mypasswords.password.GetPasswordByTitle;
import com.mypasswords.password.UpdatePasswordById;
import com.mypasswords.user.CreateNewUser;
import com.mypasswords.user.DeleteUser;
import com.mypasswords.user.UpdateUser;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   CreateNewUser.class,
   DeleteUser.class,
   UpdateUser.class,
   Login.class,
   CreateNewPassword.class,
   GetAllPasswordsByUserId.class,
   GetPasswordByTitle.class,
   UpdatePasswordById.class
})
public class Runner {
}

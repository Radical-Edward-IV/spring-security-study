package edward.iv.restapi.view;

import edward.iv.restapi.security.JwtTokenProvider;
import edward.iv.restapi.security.UserPrincipal;
import edward.iv.restapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ViewController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @GetMapping("/home")
    public String getIndexView() { return "home"; }

    @GetMapping("/signin-view")
    public String getSignInView() {
        return "sign-in";
    }

    @GetMapping("/about-view")
    public String getAboutView() {
        return "about";
    }
}

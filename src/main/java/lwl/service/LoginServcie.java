package lwl.service;

import lwl.domain.ResponseResult;
import lwl.domain.User;

public interface LoginServcie {
    ResponseResult login(User user);

    ResponseResult logout();
}

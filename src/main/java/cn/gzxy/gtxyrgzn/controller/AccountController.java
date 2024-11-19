package cn.gzxy.gtxyrgzn.controller;

import cn.gzxy.gtxyrgzn.data.AccountDto;
import cn.gzxy.gtxyrgzn.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {
    final AccountService accountService;

    @GetMapping("/account")
    public Object account() {
        return null;
    }

    @PostMapping("/login")
    public Object login(@RequestBody AccountDto.Login login) {
        AccountDto.Account account = accountService.login(login);
        if (account == null) return Map.of("code", 500, "message", "登录失败");

        return account;
    }

    @PostMapping("/register")
    public Object register(@RequestBody AccountDto.Register register) {
        AccountDto.Account account = accountService.register(register);
        if (account == null) return Map.of("code", 500, "message", "注册失败");

        return account;
    }
}

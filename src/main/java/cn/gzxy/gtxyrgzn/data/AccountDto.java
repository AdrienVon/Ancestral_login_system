package cn.gzxy.gtxyrgzn.data;

public class AccountDto {
    public record Register(String username, String password) {
    }

    public record Login(String username, String password) {
    }

    public record Account(String username, String password) {
    }
}

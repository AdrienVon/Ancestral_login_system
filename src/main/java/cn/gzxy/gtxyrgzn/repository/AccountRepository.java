package cn.gzxy.gtxyrgzn.repository;

import cn.gzxy.gtxyrgzn.model.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
    @NotNull <S extends Account> S save(@NotNull S account);

    @NotNull
    Account findByUsername(String username);
}

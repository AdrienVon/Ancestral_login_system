package cn.gzxy.gtxyrgzn.model;

import java.util.List;

public record Account(
        String id,
        String avatar,
        String username,
        String password,
        List<String> roles,
        List<String> permissions
) {
}

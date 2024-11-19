package cn.gzxy.gtxyrgzn.model;

import java.util.List;

public record RunLog(
        String id,
        String imageId,
        List<String> logs
) {
}

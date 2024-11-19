package cn.gzxy.gtxyrgzn.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RunningService {
    public void running(String id) throws IOException {
        Runtime.getRuntime().exec("winver");
    }
}

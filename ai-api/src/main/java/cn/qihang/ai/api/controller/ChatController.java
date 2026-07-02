package cn.qihang.ai.api.controller;

import cn.qihang.ai.common.AjaxResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody ChatRequest request) {
        String response = chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
        return AjaxResult.success(response);
    }

    @GetMapping("/ping")
    public AjaxResult ping() {
        return AjaxResult.success("启航AI企业业务平台服务运行中");
    }

    @Data
    public static class ChatRequest {
        private String sessionId;
        private String message;
        private Long shopId;
    }
}
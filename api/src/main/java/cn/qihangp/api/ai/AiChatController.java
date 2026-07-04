package cn.qihangerp.api.ai;

import cn.qihangerp.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * AI 对话接口。
 * /api/ai/ping  健康检查
 * /api/ai/chat  流式对话(SSE)
 * /api/ai/chat/sync  同步对话
 */
@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    @Autowired
    private AiChatService chatService;

    @GetMapping("/ping")
    public AjaxResult ping() {
        return AjaxResult.success("启航AI ERP服务运行中");
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        if (message == null || message.isBlank()) {
            return Flux.just("请输入问题");
        }
        return chatService.stream(message);
    }

    @PostMapping("/chat/sync")
    public AjaxResult chatSync(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        if (message == null || message.isBlank()) {
            return AjaxResult.error("message 不能为空");
        }
        return AjaxResult.success(chatService.chat(message));
    }
}

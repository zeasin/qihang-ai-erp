package cn.qihang.ai.infra.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 模型路由器 - 根据配置动态选择AI模型
 * 支持 DeepSeek（默认）、Ollama（本地）、通义千问等
 */
@Slf4j
@Component
public class ModelRouter {

    private final ChatClient defaultClient;

    public ModelRouter(ChatClient defaultClient) {
        this.defaultClient = defaultClient;
    }

    public ChatClient getClient() {
        // TODO: 从数据库读取用户配置的模型选择
        // 支持动态切换 deepseek / ollama / qwen 等
        return defaultClient;
    }
}
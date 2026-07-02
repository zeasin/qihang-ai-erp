package cn.qihang.ai.infra.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "spring.ai.openai.api-key")
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                        你是启航AI企业业务平台的智能助手，擅长处理企业销售业务问题。
                        你可以查询订单、商品、库存、客户等信息，并进行分析和建议。
                        请用专业、简洁的中文回答。对于操作类请求，请先确认再执行。
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(
                        MessageWindowChatMemory.builder().maxMessages(10).build()
                ).build())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.ai.openai.api-key")
    public ChatClient analysisChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("你是一个专业的企业数据分析师，请基于数据给出精确的洞察和建议。")
                .build();
    }
}
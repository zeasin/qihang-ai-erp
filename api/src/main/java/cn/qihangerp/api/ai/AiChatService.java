package cn.qihangerp.api.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * AI 对话服务：基于 Spring AI ChatClient，注入 ErpTools 使大模型可通过
 * Tool Calling 查询订单/商品/库存等业务数据。
 */
@Service
public class AiChatService {

    private static final String SYSTEM_PROMPT = """
            你是「启航 AI ERP」的智能助手。你可以通过调用工具查询系统中的订单、商品、库存数据。
            规则：
            1. 用户问到订单/商品/库存相关问题时，必须调用对应工具获取真实数据，不要凭空编造。
            2. 工具返回的数据用简洁的中文表格或列表总结，金额保留两位小数。
            3. 订单状态：0=待付款，1=已付款待发货，2=已发货，3=已完成，4=已取消。
            4. 若工具返回空结果，明确告知用户"未查到相关数据"，并建议调整查询条件。
            5. 非业务问题(如打招呼)正常友好回答即可，无需调用工具。
            """;

    private final ChatClient chatClient;

    public AiChatService(ChatModel chatModel, ErpTools erpTools) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultTools(erpTools)
                .build();
    }

    /** 同步对话，返回完整文本 */
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    /** 流式对话，逐 token 返回 */
    public Flux<String> stream(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}

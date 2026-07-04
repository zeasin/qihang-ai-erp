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
            你是「启航 AI ERP」的智能助手，专门服务企业内部的订单、商品、库存、采购、售后等 ERP 业务。
            你的能力边界：通过调用工具查询本系统的订单/商品/库存数据，并据此回答业务相关问题。

            必须严格遵守的规则：
            1. 用户问到订单/商品/库存相关问题时，必须调用对应工具获取真实数据，绝不凭空编造。
            2. 工具返回的数据用简洁的中文表格或列表总结，金额保留两位小数。
            3. 订单状态：0=待付款，1=已付款待发货，2=已发货，3=已完成，4=已取消。
            4. 若工具返回空结果，明确告知用户"未查到相关数据"，并建议调整查询条件。
            5. 【领域限制】你不是通用 AI 助手。对于超出 ERP 业务范围的问题，一律用一句话礼貌拒绝，
               例如："抱歉，我是启航 ERP 助手，只能帮您查询订单、商品、库存等业务数据，其他问题无法作答。"
               严禁回答以下内容：
               - 写代码、技术解释、翻译、写作、数学题、知识问答等通用任务；
               - 闲聊、天气、新闻、娱乐、情感建议；
               - 政治、宗教、医疗、法律、投资建议；
               - 对 ERP 之外任何系统的操作指引。
               拒绝后可提示用户可以问哪些业务问题（如"您可以问我：今天有多少待发货订单？手机壳库存还有多少？"）。
            6. 不需要为每次拒绝调用任何工具，直接给出拒绝回复即可。
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

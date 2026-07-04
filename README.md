# 启航 AI ERP (Qihang AI ERP)

> AI 驱动的新一代企业资源计划系统 —— 用自然语言查订单、查商品、查库存、查报表。

## 🎯 功能简介

### AI 能力

| 能力 | 说明 |
|------|------|
| 🔍 **订单查询** | 按订单号、收货人、状态、时间范围查订单及明细 |
| 📦 **商品查询** | 按名称查商品信息、SKU、价格 |
| 📊 **库存查询** | 查 SKU 库存、按仓库库存、低库存预警 |
| 📈 **NL2SQL 报表** | 自然语言转 SQL，自动做聚合统计、排名、趋势分析 |
| ❌ **领域限制** | 仅限 ERP 业务问题，非业务问题礼貌拒绝 |

### 业务模块（CRUD 已打通）

| 模块 | 说明 |
|------|------|
| 📦 订单管理 | 电商/线下订单、付款、发货、订单明细 |
| 📦 商品管理 | 商品库、SKU、分类、品牌 |
| 📊 库存管理 | SKU 库存、库存调整、出入库日志 |
| 🛒 采购管理 | 采购单、采购入库 |
| 🚚 发货管理 | 出库、发货、物流公司 |
| ↩️ 售后管理 | 退款、退货入库、审批 |
| 🏪 渠道管理 | 店铺、平台、商户、区域 |
| 🏬 基础数据 | 仓库、供应商、物流公司 |
| 🔐 系统管理 | 用户、角色、菜单、字典、配置、定时任务、开放鉴权 |

### 🤖 AI 能做什么（示例问题）

**订单相关**
- "查一下订单号含 DD2024 的订单"
- "今天有多少待发货订单？"
- "待发货订单还有多少金额？"
- "上个月订单总额是多少？"
- "最近30天每天的订单量趋势"
- "哪个省的下单量最多？"

**商品相关**
- "手机壳有哪些SKU？"
- "照明分类下有哪些商品？"
- "上个月各品类销售额排名"
- "哪个品牌卖得最好？"

**库存相关**
- "手机壳还有多少库存？"
- "库存低于10的有哪些商品？"
- "各仓库的库存总值是多少？"
- "最近一周的库存变动记录"

**采购相关**
- "未完成的采购单有哪些？"
- "各供应商的采购金额统计"
- "采购订单完成率"

**售后相关**
- "待处理的售后单有多少？"
- "退款原因分布"

### ❌ AI 不能做什么

这不是通用 AI 助手，以下问题会礼貌拒绝：

- 写代码、技术解释、翻译
- 闲聊、天气、新闻、娱乐
- 政治、宗教、医疗、法律建议
- 任何不在 ERP 业务范围内的问题

---

## 🏗️ 系统架构

```
┌──────────────────────────────────────────────┐
│            前端 / 外部调用方                   │
└──────────────────────────────────────────────┘
                    │ HTTP / SSE
┌──────────────────────────────────────────────┐
│  api 模块 (Spring Boot 4.1)                  │
│  /api/erp-api/*  业务 REST 接口              │
│  /api/sys-api/*   系统管理接口               │
│  /api/ai/*        AI 对话接口（SSE 流式）    │
├──────────────────────────────────────────────┤
│  service 模块 · 业务逻辑（MyBatis-Plus）     │
├──────────────────────────────────────────────┤
│  AI 编排 · ChatClient + ErpTools (@Tool)     │
├──────────────────────────────────────────────┤
│  mapper / model / common / security          │
├──────────────────────────────────────────────┤
│  PostgreSQL · Redis                          │
└──────────────────────────────────────────────┘
```

## 🛠️ 技术栈

| 组件 | 选型 |
|------|------|
| 后端框架 | Spring Boot 4.1.0 |
| AI 框架 | Spring AI 2.0.0（DeepSeek 接入） |
| AI 模型 | DeepSeek Chat（兼容 OpenAI 协议） |
| ORM | MyBatis-Plus 3.5.16 |
| 数据库 | PostgreSQL 16+ |
| 缓存 | Redis 7+ |
| 安全 | Spring Security + JWT |
| API 文档 | SpringDoc OpenAPI 2.3 |

## 📁 项目模块

实际 Maven 模块（与早期设计文档中规划的 `ai-common / ai-agent / ai-rag` 等不同，目前采用扁平结构，AI 能力直接内嵌于 `api` 模块）：

```
qihang-ai-erp/
├── common/      # 公共工具：AjaxResult、PageQuery、PageResult
├── model/       # 实体：OOrder、OGoods、OGoodsSkuStock 等 37 个
├── mapper/      # MyBatis-Plus Mapper 接口
├── security/    # Spring Security 配置、JWT、登录服务
├── service/     # 业务 Service（19 个）：订单、商品、库存、采购…
└── api/         # Web 入口 + Controller + AI 对话 + ErpTools
    └── src/main/java/cn/qihangp/api/
        ├── controller/      # 业务 Controller（22 个）
        └── ai/              # AI 对话：AiChatController、AiChatService、ErpTools
```

## 🚀 环境要求

| 依赖 | 版本 | 用途 |
|------|------|------|
| Java | 17+（推荐 21） | 后端运行 |
| Maven | 3.8+ | 后端构建 |
| PostgreSQL | 16+ | 主数据库 |
| Redis | 7+ | 缓存 / 会话 |
| DeepSeek API Key | — | AI 模型调用 |

---

## 📦 中间件准备

### PostgreSQL

```bash
docker run -d \
  --name qihang-postgres \
  -e POSTGRES_DB=qihang_ai_platform \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16
```

| 参数 | 值 |
|------|-----|
| JDBC URL | `jdbc:postgresql://127.0.0.1:5432/qihang_ai_platform` |
| 用户名 / 密码 | postgres / postgres |

数据库初始化脚本位于 `docs/sql/`，按文件顺序执行即可。

### Redis

```bash
docker run -d --name qihang-redis -p 6379:6379 redis:7
```

---

## 🔧 后端运行

### 1. 配置 DeepSeek API Key

```bash
# Linux / macOS
export DEEPSEEK_API_KEY=sk-your-deepseek-api-key
```

或直接填写到 `api/src/main/resources/application-dev.yml`：

```yaml
spring:
  ai:
    deepseek:
      api-key: sk-your-deepseek-api-key
```

### 2. 编译启动

```bash
mvn clean install -DskipTests
mvn spring-boot:run -pl api -Dspring-boot.run.profiles=dev
```

### 3. 验证

```bash
# 健康检查
curl http://localhost:8099/api/ai/ping

# AI 对话（流式 SSE）
curl -N -X POST http://localhost:8099/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"查一下订单号含 DD 的订单"}'
```

### 核心配置（`api/src/main/resources/application.yml`）

```yaml
server:
  port: 8099

spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/qihang_ai_platform
    username: postgres
    password: postgres
  data:
    redis:
      host: 127.0.0.1
      port: 6379
  ai:
    deepseek:
      api-key: ${DEEPSEEK_API_KEY:}
      chat:
        model: deepseek-chat
        temperature: 0.7
```

---

## 🧪 API 接口

### AI 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/ai/ping` | 健康检查 |
| POST | `/api/ai/chat` | AI 对话（SSE 流式返回，逐 token 推送） |
| POST | `/api/ai/chat/sync` | AI 对话（同步返回完整结果） |

请求体：

```json
{ "message": "查一下手机壳的库存" }
```

**AI 回答流程**：
1. 收到用户问题 → AI 判断需要哪些数据
2. 按需调用内置工具查询真实数据：
   - `queryOrders` / `getOrderById` — 查订单
   - `queryGoods` — 查商品
   - `queryStock` — 查库存
   - `queryBySql` — 执行只读 SQL 做聚合统计（NL2SQL）
3. AI 根据工具返回的结果组织中文回答

**技术实现**：Spring AI 2.0 Tool Calling + DeepSeek Chat，工具返回真实数据后 AI 自然语言总结输出。

### 业务接口（部分）

| 路径前缀 | 说明 |
|----------|------|
| `/api/erp-api/order` | 订单 CRUD / 付款 |
| `/api/erp-api/goods` | 商品 CRUD |
| `/api/erp-api/stock` | 库存查询 / 调整 / 日志 |
| `/api/erp-api/purchase` | 采购单 / 入库 |
| `/api/erp-api/delivery` | 出库 / 发货 |
| `/api/erp-api/after-sale` | 售后 |
| `/api/sys-api/*` | 系统 / 渠道 / 基础数据 |
| `/api-docs` `/swagger-ui.html` | OpenAPI 文档 |

---

## 📌 版本规划

| 版本 | 状态 | 内容 |
|------|------|------|
| v1.0 | ✅ 已完成 | ERP CRUD + AI 对话（Tool Calling 查订单/商品/库存 + NL2SQL 报表） |
| v1.1 | 🔜 计划 | 对话记忆（多轮上下文）、对话面板优化 |
| v2.0 | 🗓️ 规划 | 智能预警 Agent、主动推送 |
| v3.0 | 🗓️ 规划 | RAG 知识库（PGVector）、MCP 协议 |

详见 [docs/TODO.md](docs/TODO.md) 与 [docs/AI-NATIVE-ERP-DESIGN.md](docs/AI-NATIVE-ERP-DESIGN.md)。

# 启航AI企业业务平台 (Qihang AI Platform)

> **企业级AI原生业务平台** —— 以AI为第一驱动力，重塑企业业务管理方式。

## 📸 界面预览

| 首页 | AI对话 | 智能看板 |
|------|--------|---------|
| 快捷搜索 + 功能入口 | 自然语言交互操作 | 数据指标可视化 |

## 🎯 功能简介

| 功能 | 说明 |
|------|------|
| 🤖 **AI智能对话** | 自然语言查订单、查库存、销售分析，对话即操作 |
| 📊 **智能看板** | 今日订单、销售额、库存预警等关键指标一览 |
| 📦 **订单管理** | 电商订单、线下订单、全渠道聚合管理 |
| 📦 **商品管理** | 商品库、SKU管理、分类品牌 |
| 📊 **库存管理** | 多仓库存、出入库、预警提醒 |
| 🧠 **NL2SQL** | 自然语言自动转SQL查询，零门槛数据分析 |
| 📚 **RAG知识库** | 文档向量化存储，智能检索增强问答 |
| 🔌 **MCP协议** | Model Context Protocol，AI可编程接口开放 |
| 🔗 **开放集成** | OpenAPI对接老ERP系统，数据平滑迁移 |

## 🏗️ 系统架构

```
┌──────────────────────────────────────────────────┐
│                 前端层 (Vue 3 + TS)               │
│   AI对话面板  │  智能看板  │  全局搜索  │  管理页  │
├──────────────────────────────────────────────────┤
│                 API层 (Spring Boot 4.1)          │
│   /api/ai/chat  │  /api/ai/ping  │  SSE推送     │
├──────────────────────────────────────────────────┤
│               AI Agent 编排层                     │
│   订单Agent  │  库存Agent  │  分析Agent          │
├──────────────────────────────────────────────────┤
│          AI 核心能力层                            │
│   NL2SQL  │  RAG知识库  │  预测引擎  │  Embedding│
├──────────────────────────────────────────────────┤
│               数据层                              │
│   PostgreSQL  │  PGVector  │  Redis             │
└──────────────────────────────────────────────────┘
```

## 🛠️ 技术栈

| 组件 | 选型 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 4.1.0 |
| AI框架 | Spring AI | 2.0.0 |
| 数据库 | PostgreSQL | 16+ |
| 向量数据库 | PGVector（PostgreSQL插件） | — |
| 缓存 | Redis | 7+ |
| 前端框架 | Vue 3 + TypeScript | 3.4+ |
| UI组件库 | Element Plus | 2.6+ |
| 构建工具 | Vite | 5.4+ |
| AI模型 | DeepSeek Chat（云端） | — |
| 协议 | MCP（Model Context Protocol） | — |

## 📁 项目模块

```
qihang-ai-platform/
├── ai-common/          # 公共模块：工具类、统一返回、异常体系
├── ai-infrastructure/  # AI基础设施：ChatClient、模型路由、向量库
├── ai-agent/           # AI Agent编排：Agent注册、工具定义、工作流
├── ai-nl2sql/          # NL2SQL引擎：自然语言转SQL查询
├── ai-rag/             # RAG知识库：文档管理、向量检索、增强问答
├── ai-business/        # 业务核心：订单、商品、库存、客户模型
├── ai-integration/     # 集成层：对接老ERP OpenAPI、MCP协议
├── ai-api/             # API层：Controller、安全配置、应用入口
├── ai-mcp-server/      # MCP Server：对外暴露AI Tool能力
└── ai-frontend/        # 前端：Vue 3 + TypeScript + Element Plus
```

## 🚀 环境要求

| 依赖 | 版本要求 | 用途 |
|------|---------|------|
| Java | 17+（推荐 21） | 后端运行 |
| Node.js | 20+ | 前端构建运行 |
| Maven | 3.8+ | 后端构建 |
| Docker | 可选 | 快速启动 PostgreSQL / Redis |
| PostgreSQL | 16+ | 主数据库 |
| Redis | 7+ | 缓存 / 会话管理 |

---

## 📦 Docker 环境准备

### PostgreSQL（主数据库 + PGVector）

```bash
docker run -d \
  --name qihang-postgres \
  -e POSTGRES_DB=qihang_ai_platform \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16
```

**数据库连接参数：**

| 参数 | 值 |
|------|-----|
| 主机 | localhost |
| 端口 | 5432 |
| 数据库名 | qihang_ai_platform |
| 用户名 | postgres |
| 密码 | postgres |
| JDBC URL | `jdbc:postgresql://127.0.0.1:5432/qihang_ai_platform` |

> **注意：** 如需使用PGVector向量功能，需进入容器安装插件：
> ```bash
> docker exec -it qihang-postgres psql -U postgres -d qihang_ai_platform -c "CREATE EXTENSION IF NOT EXISTS vector;"
> ```

### Redis（缓存）

```bash
docker run -d \
  --name qihang-redis \
  -p 6379:6379 \
  redis:7
```

**Redis连接参数：**

| 参数 | 值 |
|------|-----|
| 主机 | localhost |
| 端口 | 6379 |
| 数据库 | 0 |

### 验证容器状态

```bash
docker ps
# 预期输出：qihang-postgres 和 qihang-redis 均为 Up 状态
```

---

## 🔧 后端运行

### 1️⃣ 配置 API Key

本平台默认使用 **DeepSeek Chat** 作为AI模型（兼容OpenAI协议），需要设置API Key：

```bash
# Linux / macOS
export DEEPSEEK_API_KEY=sk-your-deepseek-api-key

# Windows (CMD)
set DEEPSEEK_API_KEY=sk-your-deepseek-api-key

# Windows (PowerShell)
$env:DEEPSEEK_API_KEY="sk-your-deepseek-api-key"
```

> **💡 小贴士：** 也可以直接在 `ai-api/src/main/resources/application-dev.yml` 中填写：
> ```yaml
> spring:
>   ai:
>     openai:
>       api-key: sk-your-deepseek-api-key
> ```

### 2️⃣ 编译打包

```bash
# 在项目根目录执行
mvn clean install -DskipTests
```

### 3️⃣ 启动后端服务

```bash
# 方式一：Maven 直接启动（推荐）
mvn spring-boot:run -pl ai-api -Dspring-boot.run.profiles=dev

# 方式二：先打包再运行
cd ai-api
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4️⃣ 验证后端

```bash
curl http://localhost:8099/api/ai/ping
```

**预期返回：**
```json
{"code":200,"data":"启航AI企业业务平台服务运行中","msg":"操作成功"}
```

### 5️⃣ 后端配置说明

核心配置位于 `ai-api/src/main/resources/application.yml`：

```yaml
server:
  port: 8099                    # 服务端口

spring:
  datasource:                   # PostgreSQL 数据库
    url: jdbc:postgresql://127.0.0.1:5432/qihang_ai_platform
    username: postgres
    password: postgres
  data:
    redis:                      # Redis 缓存
      host: 127.0.0.1
      port: 6379
  ai:
    openai:
      base-url: https://api.deepseek.com/v1   # AI模型地址
      api-key: ${DEEPSEEK_API_KEY:}           # API Key（环境变量）
      chat:
        options:
          model: deepseek-chat                # 模型名称
          temperature: 0.7
          max-tokens: 4096
```

---

## 🖥️ 前端运行

### 1️⃣ 安装依赖

```bash
cd ai-frontend
npm install
```

### 2️⃣ 启动开发服务器

```bash
npm run dev
```

### 3️⃣ 访问前端

打开浏览器访问：**http://localhost:3000**

### 4️⃣ 前端配置说明

前端通过 Vite 代理转发 API 请求到后端，配置在 `ai-frontend/vite.config.ts`：

```typescript
export default defineConfig({
  server: {
    port: 3000,                    // 前端开发服务器端口
    proxy: {
      '/api': {                    // API请求代理
        target: 'http://localhost:8099',  // 后端地址
        changeOrigin: true,
      },
    },
  },
})
```

### 5️⃣ 前端页面

| 路由 | 页面 | 功能 |
|------|------|------|
| `/` | 首页 | 快捷搜索 + 功能入口卡片 |
| `/chat` | AI对话 | 自然语言与AI交互，查数据、做分析 |
| `/dashboard` | 智能看板 | 关键指标可视化展示 |

---

## ⚡ 快速启动（一键指南）

```bash
# 1. 启动数据库（Docker）
docker run -d --name qihang-postgres -e POSTGRES_DB=qihang_ai_platform -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16
docker run -d --name qihang-redis -p 6379:6379 redis:7

# 2. 设置API Key
export DEEPSEEK_API_KEY=sk-your-deepseek-api-key

# 3. 启动后端
mvn clean install -DskipTests
mvn spring-boot:run -pl ai-api -Dspring-boot.run.profiles=dev

# 4. 新终端 - 启动前端
cd ai-frontend
npm install
npm run dev

# 5. 打开浏览器访问 http://localhost:3000
```

---

## 🧪 API 接口一览

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/ai/ping` | 健康检查 |
| POST | `/api/ai/chat` | AI对话接口 |
| GET | `/api-docs` | OpenAPI文档（SpringDoc） |
| GET | `/swagger-ui.html` | Swagger UI界面 |

---

## 🔗 与启航电商ERP的关系

本平台与老ERP（qihang-erp-open）是**平级协作**关系：

| 场景 | 关系说明 |
|------|---------|
| 独立使用 | ✅ 本平台完全独立，自带所有数据能力 |
| 电商对接 | 🔌 可选通过OpenAPI同步老ERP的电商订单数据 |
| 数据导入 | 📥 支持从老ERP一次性导入历史数据（Excel/CSV） |

**本平台不依赖老ERP**，老ERP只是众多数据源之一。

---

## 📄 设计文档

详见 [docs/AI-NATIVE-ERP-DESIGN.md](docs/AI-NATIVE-ERP-DESIGN.md)

## 📌 版本规划

| 版本 | 阶段 | 核心交付 |
|------|------|---------|
| v1.0 | Phase 1 | AI对话 + NL2SQL查询 + 基础业务模块 |
| v2.0 | Phase 2 | AI Agent体系 + RAG知识库 |
| v3.0 | Phase 3 | 智能供应链 + MCP协议 |
| v4.0 | Phase 4 | 全渠道销售 + AI工作流 |

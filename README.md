# Alshamel — 库存批次数量调整 Demo（后端）

基于 **Spring Boot 3.4 + MyBatis + Spring Security (JWT) + MySQL** 的库存批次数量调整后端 API。

## 业务说明

仓库中存放产品批次（batch），每个批次有一个当前数量。当实盘数量与系统数量不一致时，创建一条「库存调整」来修正批次数量。每次调整必须带一个**预定义原因**（如：实盘数量修正 / 破损 / 缺失 / 数据录入错误），可附加自由文本备注。

示例：批次系统数量 100，实盘 92 → 创建调整 100 → 92，记录旧值、新值、差值、原因与备注，并更新批次数量。

## 技术栈

| 层 | 选型 |
|---|---|
| 语言 / 构建 | Java 17 / Maven |
| Web | Spring Boot 3.4 (Spring MVC) |
| ORM | MyBatis（注解 + XML 混合） |
| 鉴权 | Spring Security + JWT（BCrypt 密码哈希） |
| 数据库 | MySQL 8 |
| 校验 | Bean Validation (jakarta.validation) |

## 目录结构

```
src/main/java/com/alshamel/mall/
├── config/        SecurityConfig
├── security/      JwtService, CustomUserDetailsService, JwtAuthenticationFilter
├── entity/        User, Warehouse, Product, Batch, AdjustmentReason, InventoryAdjustment
├── mapper/        MyBatis Mapper 接口
├── dto/           请求/响应 DTO
├── service/       AuthService, AdjustmentReasonService, InventoryAdjustmentService
├── controller/    AuthController, AdjustmentReasonController, InventoryAdjustmentController
├── common/        ApiResponse（统一响应）
└── exception/     BusinessException, GlobalExceptionHandler
src/main/resources/
├── db/schema.sql  建表语句
├── db/data.sql    演示数据（用户/仓库/产品/批次/原因）
└── mapper/*.xml   MyBatis XML
```

## 快速开始

1. **准备 MySQL 8**，确认账号密码（默认 `root`/`123456`，可在 `application.yml` 中修改）。
   - 启动时 `createDatabaseIfNotExist=true` 会自动创建 `alshamel` 库（需账号有建库权限）。

2. **建表与演示数据**
   - 应用启动时 `spring.sql.init.mode=always` 会自动执行 `db/schema.sql` 和 `db/data.sql`（幂等，可重复执行）。
   - 也可手动执行：
     ```bash
     mysql -uroot -p < src/main/resources/db/schema.sql
     mysql -uroot -p < src/main/resources/db/data.sql
     ```

3. **启动**
   ```bash
   ./mvnw spring-boot:run      # 或 mvn spring-boot:run
   ```

4. **验证（可选）**
   ```bash
   mvn test    # 无需数据库，校验种子密码哈希正确
   ```

## API 说明

所有接口（除登录）都需要在请求头携带 JWT：
`Authorization: Bearer <token>`

### 1. 登录

```
POST /api/auth/login
{ "username": "admin", "password": "admin123" }
```
响应：
```json
{ "code": 200, "message": "OK", "data": { "token": "...", "tokenType": "Bearer", "username": "admin", "role": "ADMIN", "expiresInMs": 86400000 } }
```

演示账号：`admin / admin123`（ADMIN）、`user / user123`（USER）

### 2. 调整原因（固定 4 个预定义集合，仅支持启用/禁用）

```
GET /api/adjustment-reasons               # 全部原因（管理页）
GET /api/adjustment-reasons/available     # 可用（启用 + INVENTORY，供调整下拉）
PUT /api/adjustment-reasons/{id}/active   # 启用/停用 { active: true|false }
```

### 3. 创建库存调整

```
POST /api/inventory-adjustments
{ "batchId": 1, "reasonId": 2, "newQuantity": 92, "note": "3 damaged on shelf" }
```
校验规则：批次存在、原因存在、原因激活、原因适用于库存调整；记录旧/新数量、差值、原因、备注；在事务中乐观锁更新批次数量。

### 3.1 批量库存调整

```
POST /api/inventory-adjustments/batch
{ "items": [ { "batchId": 1, "reasonId": 2, "newQuantity": 92, "note": "..." }, ... ] }
```
多条目同事务处理，逐条复用同一套校验（批次/原因存在+启用+适用于库存调整）；任一失败整体回滚；同请求内批次不可重复。

### 4. 查看库存调整（联表返回批次/产品/仓库/原因/操作人）

```
GET /api/inventory-adjustments/{id}
```

## 设计决策

- **MyBatis 而非 JPA**：按需求使用 MyBatis，SQL 显式可控；简单查询用注解，复杂联表用 XML `resultMap`。
- **密码安全**：`users` 表只存 BCrypt 哈希，登录用 `DaoAuthenticationProvider` 校验，绝不落库明文。
- **无状态 JWT 鉴权**：适合 Vue3 SPA；登录发 token，之后每次请求带 `Bearer`。`/api/auth/login` 放行，其余 `anyRequest().authenticated()`。
- **事务 + 乐观锁安全更新数量**：`create` 用 `@Transactional`，插入调整记录后用 `UPDATE ... WHERE quantity = oldQuantity` 乐观锁更新，避免并发覆盖，失败即回滚。
- **统一响应与异常**：`ApiResponse{code,message,data}` + `@RestControllerAdvice` 集中处理业务/校验/认证异常。
- **幂等种子数据**：`CREATE TABLE IF NOT EXISTS` + `INSERT IGNORE`，重启不重复。

## 前端（Vue3 + TS + Vite）

位于 `frontend/` 子目录，前后端分离开发。

```bash
cd frontend
npm install
npm run dev        # 开发：http://localhost:5173（/api 自动代理到 8080）
npm run build      # 生产构建（vue-tsc 类型检查 + vite 打包）
```

技术栈：Vue3 + TypeScript + Vite、Element Plus（UI）、Pinia（状态）、Vue Router、Axios。
页面：登录 → 产品列表 / 库存(带批次) / 调整原因 / 调整记录列表 / 新建调整（批次+原因下拉、备注）→ 调整详情。
鉴权：登录后 JWT 存 localStorage，Axios 拦截器自动附带 `Authorization`，401 自动跳回登录页。

## 完整启动（前后端）

1. 启动后端：`mvn spring-boot:run`（默认 8080，需 MySQL 8）
2. 启动前端：`cd frontend && npm run dev`（默认 5173）
3. 浏览器打开 http://localhost:5173 ，用 `admin / admin123` 登录

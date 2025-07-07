# 学生违纪处分管理系统（SDMS）

## 项目简介
学生违纪处分管理系统（SDMS）是一套面向高校的全栈管理平台，支持系统管理员和学院角色，实现违纪记录管理、成员管理、统计分析、操作日志、学院管理等功能。系统采用前后端分离架构，界面美观，操作便捷，支持多角色权限。

## 技术栈
- 前端：Vue3 + Element Plus + Pinia + Vue Router + Axios
- 后端：Spring Boot + MyBatis-Plus + MySQL
- 数据库字符集：utf8mb4，支持中文和表情

## 主要功能模块
- 用户/成员管理：支持多角色、多学院、状态管理，账号激活/冻结
- 学院管理：系统管理员可增删改查学院信息
- 违纪记录管理：支持全校/本院违纪记录的增删改查、筛选、分页
- 统计分析：按学院统计违纪数据
- 操作日志：自动记录关键操作，便于追溯
- 处分类型管理：支持自定义处分类型
- 登录与权限：支持系统管理员、学院角色分权登录

## 角色说明
- **系统管理员**：全局管理、统计、成员管理、学院管理、日志等
- **学院角色**：本院数据管理、日志查看、违纪记录维护

## 环境依赖
- Node.js >= 16.x
- npm >= 8.x
- JDK >= 17
- Maven >= 3.6
- MySQL >= 8.0

## 目录结构
```
SDMS/
├── sdms-backend/      # 后端Spring Boot项目
├── sdms-frontend/     # 前端Vue3项目
├── db_init.sql        # 数据库初始化脚本
├── README.md          # 项目说明
```

## 操作步骤
### 1. 数据库初始化
- 使用 `db_init.sql` 脚本自动创建数据库（utf8mb4）和所有表、初始数据。
- 脚本开头已包含：
  ```sql
  CREATE DATABASE IF NOT EXISTS sdms
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_zh_0900_as_cs;
  USE sdms;
  ```
- 直接在MySQL命令行或可视化工具运行 `db_init.sql` 即可，无需手动建库。
- 修改 `sdms-backend/src/main/resources/application.yml`，配置数据库连接（如用户名、密码、端口、库名）。

### 2. 启动后端

#### 方法一：命令行启动（推荐）
```bash
cd sdms-backend
mvn spring-boot:run
```
- 默认端口：8080
- 启动成功后访问 http://localhost:8080

#### 方法二：用 IDEA 启动
1. 用 IDEA 打开 `sdms-backend` 目录（或整个 SDMS 项目）。
2. 等待 IDEA 自动识别 Maven 项目并加载依赖。
3. 找到 `com.example.sdms.SdmsApplication` 类（`src/main/java/com/example/sdms/SdmsApplication.java`）。
4. 右键该类，选择"Run 'SdmsApplication.main()'" 或点击左侧绿色三角形按钮启动。
5. 控制台出现 `Started SdmsApplication` 字样，说明后端启动成功。

### 3. 启动前端

#### 方法一：命令行启动（推荐）
```bash
cd sdms-frontend
npm install
npm run dev
```
- 默认端口：5173
- 启动成功后访问 http://localhost:5173

#### 方法二：用 VSCode 启动
1. 用 VSCode 打开 `sdms-frontend` 目录。
2. 打开终端（Terminal）。
3. 输入如下命令：
   ```sh
   npm install
   npm run dev
   ```
4. 终端会显示本地访问地址（如 http://localhost:5173），用浏览器访问即可。

### 4. 登录与角色
- 初始管理员账号：用户名 `admin`，密码 `admin123`
- 学院账号示例：用户名 `college1`，密码 `college123`
- 登录后根据角色自动跳转到对应系统

### 5. 常见操作
- **添加成员**：管理员可在"成员管理"中添加老师账号，需填写用户名和密码，设置角色和学院。
- **学院管理**：管理员可在"学院管理"中增删改查学院信息。
- **违纪记录管理**：支持按条件筛选、分页、增删改查。
- **账号激活/冻结**：冻结成员无法登录系统。
- **操作日志**：所有关键操作自动记录，便于追溯。

### 6. 常见问题
- 数据库连接失败：请检查 application.yml 配置、MySQL 是否启动、端口是否正确。
- 新增学院ID异常大：请确保数据库表id为自增，实体类id注解为`@TableId(type = IdType.AUTO)`，前端新增时不传id。
- 账号无法登录：请确认账号已激活（status=active），用户名密码正确。
- 页面空白/接口404：请确认前后端均已启动，端口和代理配置无误。

## 其他说明
- 所有表、字段、数据均为 `utf8mb4` 字符集，支持中文和表情。
- 前后端接口通过 `/api` 路径对接。
- 可根据实际需求扩展更多功能。


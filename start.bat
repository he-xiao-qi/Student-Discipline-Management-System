@echo off
chcp 65001
REM 启动 SDMS 后端
cd sdms-backend
echo 正在启动后端...
start cmd /k "chcp 65001 && mvn spring-boot:run"
cd ..

REM 启动 SDMS 前端
cd sdms-frontend
echo 正在启动前端...
start cmd /k "chcp 65001 && npm run dev"
cd ..

echo ==========================
echo SDMS一键启动完成！
echo 后端端口: 8080
echo 前端端口: 5173
echo ==========================
pause
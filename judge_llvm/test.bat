@echo off
setlocal

:: 定义源文件和目标文件
set "SOURCE_FILE=..\llvm_ir.txt"
set "TARGET_FILE=main.ll"
set "c_file=..\judge_llvm\%TARGET_FILE%"

:: 检查源文件是否存在
if exist "%SOURCE_FILE%" (
    copy "%SOURCE_FILE%" "%c_file%" >nul
    echo 文件已复制并重命名为 %TARGET_FILE%
) else (
    echo 错误: 源文件 %SOURCE_FILE% 不存在。
)

llvm-link main.ll lib.ll -S -o out.ll
if errorlevel 1 (
    echo Error: Failed to link IR files
    exit /b 1
)

echo Running out.ll...
lli out.ll

:: 暂停，等待用户按下回车键退出
echo.
echo Press Enter to exit...
pause >nul

endlocal

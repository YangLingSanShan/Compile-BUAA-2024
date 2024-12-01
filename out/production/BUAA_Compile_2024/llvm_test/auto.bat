@echo off
setlocal

:: 设置原始文件路径和当前目录
set "original_file=D:\BUAA_Compile_2024\testfile.txt"
set "current_dir=%cd%"
set "c_file=%current_dir%\testfile.c"

:: 复制原始文件并重命名为 `.c`
copy "%original_file%" "%c_file%" >nul

:: 在 `testfile.c` 的第一行插入 `#include "libsysy.h"`
echo #include "libsysy.h" > temp_file.txt
type "%c_file%" >> temp_file.txt
move /y temp_file.txt "%c_file%" >nul

:: 编译 `libsysy.c` 为 IR 文件
clang -emit-llvm -S libsysy.c -o lib.ll
if errorlevel 1 (
    echo Error: Failed to compile libsysy.c
    exit /b 1
)

:: 编译 `testfile.c` 为 IR 文件
clang -emit-llvm -S testfile.c -o main.ll
if errorlevel 1 (
    echo Error: Failed to compile testfile.c
    exit /b 1
)

:: 链接 IR 文件生成 `out.ll`
llvm-link main.ll lib.ll -S -o out.ll
if errorlevel 1 (
    echo Error: Failed to link IR files
    exit /b 1
)

:: 运行 `out.ll` 并支持命令行输入
echo Running out.ll...
lli out.ll

:: 暂停，等待用户按下回车键退出
echo.
echo Press Enter to exit...
pause >nul

endlocal


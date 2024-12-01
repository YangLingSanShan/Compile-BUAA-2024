@echo off
REM 检查外层目录是否存在 mips.txt 文件
if exist "..\mips.txt" (
    REM 将 mips.txt 复制并重命名为 mips.asm，保存到当前目录
    copy "..\mips.txt" "mips.asm"
    echo 文件已成功重命名并保存到当前目录。
) else (
    echo 错误：未找到外层目录中的 mips.txt 文件。
)
